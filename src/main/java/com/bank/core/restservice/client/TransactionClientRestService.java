package com.bank.core.restservice.client;

import com.bank.core.util.bankaccount.BankAccountReadConverter;
import com.bank.core.util.transaction.TransactionCreateUpdateConverter;
import com.bank.core.util.transaction.TransactionReadConverter;
import com.bank.core.util.transaction.TransactionReadToUpdateConverter;
import com.bank.core.validation.NotFoundException;
import com.bank.core.validation.ValidationException;
import com.bank.model.dto.bankaccount.BankAccountReadDTO;
import com.bank.model.dto.transaction.*;
import com.bank.model.entity.BankAccount;
import com.bank.model.entity.Transaction;
import com.bank.model.enums.TransactionStatus;
import com.bank.model.enums.TransactionType;
import com.bank.repository.BankAccountRepository;
import com.bank.repository.TransactionRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Data
@RequiredArgsConstructor
public class TransactionClientRestService {

    private final TransactionRepository transactionRepository;
    private final TransactionReadConverter transactionReadConverter;
    private final TransactionCreateUpdateConverter transactionCreateUpdateConverter;
    private final BankAccountClientRestService bankAccountClientRestService;
    private final BankAccountRepository bankAccountRepository;
    private final BankAccountReadConverter bankAccountReadConverter;
    private final TransactionReadToUpdateConverter transactionReadToUpdateConverter;


    public FindAllTransactionsResponse findAllByBankAccountId(Long bankAccountId){
        /**
         BankAccount bankAccount = bankAccountRepository.findById(bankAccountId)
         ---- bankAccountRepository --> service
         ---- BankAccount --> BankReadDTO

         */
        BankAccount bankAccount = bankAccountRepository.findById(bankAccountId)
                .orElseThrow(() -> new NotFoundException("Bank Account not found "));
        List<TransactionReadDTO> allTransactions;
        if (!transactionRepository.findAllByBankAccount(bankAccount).isEmpty()) {
            allTransactions = transactionRepository.findAll().stream()
                    .map(transactionReadConverter::convert)
                    .toList();
        } else {
            throw new ValidationException("Nothing found");
        }
        // if accessKey is CLIENT`s accessKey
        // throw new ValidationException("Admin rights required");
        return new FindAllTransactionsResponse(allTransactions, new ArrayList<>());

    }

    public Optional<TransactionReadDTO> findById(Long id) {
        return  transactionRepository.findById(id)// findById(ID id) return Optional<T>
                //<U> Optional<U> map(Function<? super T, ? extends U> mapper)
                // .map(transaction -> transactionReadConverter.convert(transaction))
                .map(transactionReadConverter::convert); // client -> clientReadDTO
    }

    public CreateUpdateTransactionResponse create(TransactionCreateUpdateDTO transactionCreateUpdateDTO) {
        transactionCreateUpdateDTO.setCreatedAt(LocalDateTime.now());
        String sourceAccount = bankAccountClientRestService.findById(transactionCreateUpdateDTO.getAccountId())
                .orElseThrow().getAccountNumber();
       transactionCreateUpdateDTO.setSourceAccount(sourceAccount);
        String senderFirstName = bankAccountClientRestService.findById(transactionCreateUpdateDTO.getAccountId())
                .orElseThrow().getClient().getFirstName();
        String senderLastName = bankAccountClientRestService.findById(transactionCreateUpdateDTO.getAccountId())
                .orElseThrow().getClient().getLastName();
        transactionCreateUpdateDTO.setSender(senderFirstName +" "+senderLastName);
        BigDecimal interestRate = bankAccountClientRestService.findById(transactionCreateUpdateDTO.getAccountId())
                .orElseThrow().getProduct().getInterestRate();
        transactionCreateUpdateDTO.setInterestRate(interestRate);
        transactionCreateUpdateDTO.setTransactionType(TransactionType.INTERNAL);
        transactionCreateUpdateDTO.setTransactionStatus(TransactionStatus.DRAFT_INVALID);

        TransactionReadDTO transactionReadDTO= Optional.of(transactionCreateUpdateDTO)
                .map(transactionCreateUpdateConverter::convert)//TransactionCreateConverter  TransactionCreateDTO ->Transaction
                .map(transactionRepository::save)   // .map(Transaction -> TransactionRepository.save(Transaction))
                .map(transactionReadConverter::convert) //TransactionReadConverter  Transaction -> TransactionReadDTO
                .orElseThrow();
        return new CreateUpdateTransactionResponse(transactionReadDTO, new ArrayList<>());
    }


    public CreateUpdateTransactionResponse update(Long id, TransactionCreateUpdateDTO transactionCreateUpdateDTO) {
        Optional<Transaction> transactionForUpdate = Optional.ofNullable(transactionRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Transaction not found")));

        transactionCreateUpdateDTO.setSourceAccount(transactionForUpdate.get().getSourceAccount());
        transactionCreateUpdateDTO.setSender(transactionForUpdate.get().getSender());
        transactionCreateUpdateDTO.setInterestRate(transactionForUpdate.get().getInterestRate());
        transactionCreateUpdateDTO.setTransactionType(transactionForUpdate.get().getTransactionType());
        transactionCreateUpdateDTO.setTransactionStatus(transactionForUpdate.get().getTransactionStatus());
        transactionCreateUpdateDTO.setCreatedAt(transactionForUpdate.get().getCreatedAt());
        transactionCreateUpdateDTO.setUpdatedAt(LocalDateTime.now());

        return new CreateUpdateTransactionResponse(
                transactionForUpdate.map(transaction -> transactionCreateUpdateConverter.convert(transactionCreateUpdateDTO, transaction))
                        .map(transactionRepository::saveAndFlush) // save transactionCreateUpdateDTO
                        .map(transactionReadConverter::convert).orElseThrow()  // transaction -> transactionReadDTO
                , new ArrayList<>());
    }

    public AuthorizeTransactionResponse authorize(Long transactionId){
        TransactionCreateUpdateDTO transactionForAuthorize =transactionRepository.findById(transactionId)
                .map(transactionReadConverter::convert)
                .map(transactionReadToUpdateConverter::convert)
                .orElseThrow(() -> new ValidationException("Transaction not found"));

        //validating account balances
        validateBalance(transactionForAuthorize.getSourceAccount(), transactionForAuthorize.getTransactionAmount());
        // execute transaction
        TransactionReadDTO transactionReadDTO=

        executeInternalTransaction(transactionForAuthorize,
                 transactionForAuthorize.getSourceAccount(),
                transactionForAuthorize.getDestinationAccount(),
                transactionForAuthorize.getTransactionAmount());

        return new AuthorizeTransactionResponse(
                transactionReadDTO,
                "Transaction successfully completed",
                new ArrayList<>());
    }

    public TransactionReadDTO executeInternalTransaction(TransactionCreateUpdateDTO transactionForAuthorize,
                                                         String fromAccountNumber, String toAccountNumber, BigDecimal amount){
        BankAccount fromBankAccount =bankAccountRepository.findByAccountNumber(fromAccountNumber)
                .orElseThrow(() -> new NotFoundException("Bank Account not found "));
        BankAccount toBankAccount =bankAccountRepository.findByAccountNumber(toAccountNumber)
                .orElseThrow(() -> new NotFoundException("Bank Account not found "));

        transactionForAuthorize.setTransactionDate(LocalDateTime.now());
        transactionForAuthorize.setEffectiveDate(LocalDateTime.now());
        transactionForAuthorize.setTransactionStatus(TransactionStatus.EXECUTED);
        String transactionCode = UUID.randomUUID().toString();
        transactionForAuthorize.setTransactionCode(transactionCode);

        //  execute transaction fromBankAccount == sourceAccount  .amount(amount.negate())
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!! BEFOR fromBankAccount"+fromBankAccount.getBalance());
        fromBankAccount.setBalance(fromBankAccount.getBalance().subtract(amount));
        bankAccountRepository.save(fromBankAccount);
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!! AFTER fromBankAccount"+fromBankAccount.getBalance());

        TransactionReadDTO transactionReadDTO = Optional.of(transactionForAuthorize)
                .map(transactionCreateUpdateConverter::convert)
                .map(transactionRepository::saveAndFlush)
                .map(transactionReadConverter::convert).orElseThrow();


       //  execute transaction toBankAccount == sourceAccount  .amount(amount)
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!! BEFOR toBankAccount"+toBankAccount.getBalance());
        toBankAccount.setBalance(toBankAccount.getBalance().add(amount));
        bankAccountRepository.save(toBankAccount);
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!! AFTER toBankAccount"+toBankAccount.getBalance());
        TransactionCreateUpdateDTO transactionToAccount = Optional.of(transactionReadDTO)
        .map(transactionReadToUpdateConverter::convert)
        .orElseThrow();
        this.createTransactionToAccount(transactionToAccount);


        return transactionReadDTO;

    }
    public CreateUpdateTransactionResponse createTransactionToAccount(
            TransactionCreateUpdateDTO transactionCreateUpdateDTO) {

        Long toAccountId = bankAccountClientRestService.findByAccountNumber
                  (transactionCreateUpdateDTO.getDestinationAccount()).orElseThrow().getId();
       BankAccount toAccount = bankAccountRepository.findById(toAccountId).orElseThrow();
       BigDecimal interestRate = toAccount.getProduct().getInterestRate();

       Transaction transactionToAccount = new Transaction();
       transactionToAccount.setBankAccount(toAccount);
       transactionToAccount.setSender(transactionCreateUpdateDTO.getBeneficiary() );
       transactionToAccount.setSourceAccount(transactionCreateUpdateDTO.getDestinationAccount());
        transactionToAccount.setBeneficiary(transactionCreateUpdateDTO.getSender());
        transactionToAccount.setDestinationAccount(transactionCreateUpdateDTO.getSourceAccount() );
        transactionToAccount.setTransactionAmount(transactionCreateUpdateDTO.getTransactionAmount());
        transactionToAccount.setDescription(transactionCreateUpdateDTO.getDescription());
        transactionToAccount.setInterestRate(interestRate);
        transactionToAccount.setTransactionType(TransactionType.INTERNAL);
        transactionToAccount.setTransactionStatus(TransactionStatus.EXECUTED);
        transactionToAccount.setTransactionCode(transactionCreateUpdateDTO.getTransactionCode());
        transactionToAccount.setCreatedAt(LocalDateTime.now());
        transactionToAccount.setUpdatedAt(LocalDateTime.now());
        transactionToAccount.setTransactionDate(LocalDateTime.now());
        transactionToAccount.setEffectiveDate(LocalDateTime.now());

        TransactionReadDTO transactionReadDTO= Optional.of(transactionToAccount)
                 .map(transactionRepository::save)   // .map(Transaction -> TransactionRepository.save(Transaction))
                .map(transactionReadConverter::convert) //TransactionReadConverter  Transaction -> TransactionReadDTO
                .orElseThrow();
        return new CreateUpdateTransactionResponse(transactionReadDTO, new ArrayList<>());
    }


    private void validateBalance(String accountNumber, BigDecimal amount) {

        BankAccountReadDTO bankAccount= bankAccountClientRestService.findByAccountNumber(accountNumber)
                .orElseThrow();
        if (bankAccount.getBalance().compareTo(BigDecimal.ZERO) < 0
                || bankAccount.getBalance().compareTo(amount) < 0) {
                throw new RuntimeException("not enough money ");
        }
    }



    public DeleteTransactionResponse delete(Long request){
        Transaction clientForDelete =  transactionRepository.findById(request)
                .orElseThrow(() -> new ValidationException("Transaction not found"));
        transactionRepository.delete(clientForDelete);
        return new DeleteTransactionResponse(transactionReadConverter.convert(clientForDelete), new ArrayList<>());

    }

    private TransactionStatus getTransactionStatus(Long id){
        return transactionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Can not found the transaction "))
                .getTransactionStatus();
    }

}