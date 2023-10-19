package com.bank.core.restservice.client;

import com.bank.core.restservice.LoginEntityRestService;
import com.bank.core.util.bankaccount.BankAccountReadConverter;
import com.bank.core.util.transaction.FindOneTransactionConverter;
import com.bank.core.util.transaction.TransactionCreateUpdateConverter;
import com.bank.core.util.transaction.TransactionReadConverter;
import com.bank.core.util.transaction.TransactionReadToUpdateConverter;
import com.bank.core.validation.CoreError;
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
import java.util.*;

@Service
@Data
@RequiredArgsConstructor
public class TransactionClientRestService {

    private final BankAccountRepository bankAccountRepository;
    private final BankAccountReadConverter bankAccountReadConverter;
    private final BankAccountClientRestService bankAccountClientRestService;

    private final TransactionRepository transactionRepository;
    private final TransactionReadConverter transactionReadConverter;
    private final TransactionCreateUpdateConverter transactionCreateUpdateConverter;
    private final TransactionReadToUpdateConverter transactionReadToUpdateConverter;
    private final FindOneTransactionConverter findOneTransactionConverter;
    private final LoginEntityRestService loginEntityRestService;
    private Long CLIENT_ID;

    public FindAllTransactionsForClientResponse findAllByBankAccountId(Long bankAccountId) {
        CLIENT_ID =loginEntityRestService.getClientId();
        Long clientId = bankAccountRepository.findById(bankAccountId).orElseThrow().getClient().getId();
        List<FindTransactionForClient> allTransactions;
        if (clientId == CLIENT_ID && !transactionRepository.findAllByBankAccountId(bankAccountId).isEmpty()) {
            allTransactions
                    = transactionRepository.findAllByBankAccountId(bankAccountId).stream()
                    .map(findOneTransactionConverter::convert)
                    .toList();
        } else {
            throw new ValidationException("Nothing found");
        }

        return new FindAllTransactionsForClientResponse(allTransactions, new ArrayList<>());
    }

//    public Optional<TransactionReadDTO> findById(Long id) {
//        return Optional.of(transactionRepository.findById(id)
//                .map(transactionReadConverter::convert).orElseThrow());
//    }

    public Optional<FindTransactionForClient> findByIdAndClientId(Long id) {
        CLIENT_ID =loginEntityRestService.getClientId();
        Long clientId = transactionRepository.findById(id).orElseThrow()
                .getBankAccount().getClient().getId();
        if (clientId == CLIENT_ID) {
            return transactionRepository.findById(id)
                    .map(findOneTransactionConverter::convert);
        } else {
            throw new ValidationException("Nothing found");
        }
    }

    public CreateUpdateTransactionForClientResponse create(TransactionCreateUpdateDTO transactionCreateUpdateDTO) {
        CLIENT_ID =loginEntityRestService.getClientId();
        Long clientId = bankAccountRepository
                .findById(transactionCreateUpdateDTO.getAccountId()).orElseThrow().getClient().getId();
        FindTransactionForClient createdTransaction;
        if (clientId == CLIENT_ID) {

            transactionCreateUpdateDTO.setCreatedAt(LocalDateTime.now());
            String sourceAccount = bankAccountClientRestService.findById(transactionCreateUpdateDTO.getAccountId())
                    .orElseThrow().getAccountNumber();
            transactionCreateUpdateDTO.setSourceAccount(sourceAccount);
            String senderFirstName = bankAccountClientRestService.findById(transactionCreateUpdateDTO.getAccountId())
                    .orElseThrow().getClient().getFirstName();
            String senderLastName = bankAccountClientRestService.findById(transactionCreateUpdateDTO.getAccountId())
                    .orElseThrow().getClient().getLastName();
            transactionCreateUpdateDTO.setSender(senderFirstName + " " + senderLastName);
            BigDecimal interestRate = bankAccountClientRestService.findById(transactionCreateUpdateDTO.getAccountId())
                    .orElseThrow().getProduct().getInterestRate();
            transactionCreateUpdateDTO.setInterestRate(interestRate);
            transactionCreateUpdateDTO.setTransactionType(TransactionType.INTERNAL);
            transactionCreateUpdateDTO.setTransactionStatus(TransactionStatus.DRAFT_INVALID);
            createdTransaction = Optional.of(transactionCreateUpdateDTO)
                    .map(transactionCreateUpdateConverter::convert)
                    .map(transactionRepository::save)
                    .map(findOneTransactionConverter::convert)
                    .orElseThrow();
            return new CreateUpdateTransactionForClientResponse(createdTransaction, new ArrayList<>());
        } else {
            createdTransaction = Optional.of(transactionCreateUpdateDTO)
                    .map(transactionCreateUpdateConverter::convert)
                    .map(findOneTransactionConverter::convert)
                    .orElseThrow();
            CoreError coreError = new CoreError("You can not create the transaction because account number is wrong");
            return new CreateUpdateTransactionForClientResponse(createdTransaction,
                    new ArrayList<>(Collections.singleton(coreError)));
        }

    }

    public CreateUpdateTransactionForClientResponse update(Long id, TransactionCreateUpdateDTO transactionCreateUpdateDTO) {
        CLIENT_ID =loginEntityRestService.getClientId();
        Optional<Transaction> transactionForUpdate = Optional.ofNullable(transactionRepository.findById(id)
                .orElseThrow(() -> new ValidationException("Transaction not found")));
        TransactionStatus transactionStatus = transactionForUpdate.orElseThrow().getTransactionStatus();
        Long clientId = bankAccountRepository
                .findById(transactionCreateUpdateDTO.getAccountId()).orElseThrow().getClient().getId();

        if (clientId == CLIENT_ID) {

            if (transactionStatus == TransactionStatus.DRAFT_INVALID
                    || transactionStatus == TransactionStatus.DRAFT_VALID) {
                transactionCreateUpdateDTO.setCreatedAt(transactionForUpdate.get().getCreatedAt());
                transactionCreateUpdateDTO.setUpdatedAt(LocalDateTime.now());
                transactionCreateUpdateDTO.setTransactionType(transactionForUpdate.get().getTransactionType());
                transactionCreateUpdateDTO.setTransactionStatus(transactionForUpdate.get().getTransactionStatus());
                return new CreateUpdateTransactionForClientResponse(
                        transactionForUpdate.map(transaction -> transactionCreateUpdateConverter.convert(transactionCreateUpdateDTO, transaction))
                                .map(transactionRepository::saveAndFlush)
                                .map(findOneTransactionConverter::convert)
                                .orElseThrow(), new ArrayList<>());
            } else {
                CoreError coreError = new CoreError("You can not update the processing or executing transact");
                return new CreateUpdateTransactionForClientResponse(
                        transactionForUpdate.map(transaction -> transactionCreateUpdateConverter.convert(transactionCreateUpdateDTO, transaction))
                                .map(findOneTransactionConverter::convert)
                                .orElseThrow()
                        , new ArrayList<>(Collections.singleton(coreError)));
            }

        } else {
            CoreError coreError = new CoreError("You can not create the transaction because account number is wrong");
            return new CreateUpdateTransactionForClientResponse(
                    transactionForUpdate.map(transaction -> transactionCreateUpdateConverter.convert(transactionCreateUpdateDTO, transaction))
                            .map(findOneTransactionConverter::convert)
                            .orElseThrow()
                    , new ArrayList<>(Collections.singleton(coreError)));
        }

    }

    public AuthorizeTransactionResponse authorize(Long transactionId) {
        TransactionCreateUpdateDTO transactionForAuthorize = transactionRepository.findById(transactionId)
                .map(transactionReadConverter::convert)
                .map(transactionReadToUpdateConverter::convert)
                .orElseThrow(() -> new ValidationException("Transaction not found"));

        //validating account balances
        validateBalance(transactionForAuthorize.getSourceAccount(), transactionForAuthorize.getTransactionAmount());
        // execute transaction
        TransactionReadDTO transactionReadDTO =
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
                                                         String fromAccountNumber, String toAccountNumber, BigDecimal amount) {
        BankAccount fromBankAccount = bankAccountRepository.findByAccountNumber(fromAccountNumber)
                .orElseThrow(() -> new NotFoundException("Bank Account not found "));
        BankAccount toBankAccount = bankAccountRepository.findByAccountNumber(toAccountNumber)
                .orElseThrow(() -> new NotFoundException("Bank Account not found "));

        transactionForAuthorize.setTransactionDate(LocalDateTime.now());
        transactionForAuthorize.setEffectiveDate(LocalDateTime.now());
        transactionForAuthorize.setTransactionStatus(TransactionStatus.EXECUTED);
        String transactionCode = UUID.randomUUID().toString();
        transactionForAuthorize.setTransactionCode(transactionCode);

        //  execute transaction fromBankAccount == sourceAccount  .amount(amount.negate())
        fromBankAccount.setBalance(fromBankAccount.getBalance().subtract(amount));
        bankAccountRepository.save(fromBankAccount);
        TransactionReadDTO transactionReadDTO = Optional.of(transactionForAuthorize)
                .map(transactionCreateUpdateConverter::convert)
                .map(transactionRepository::saveAndFlush)
                .map(transactionReadConverter::convert).orElseThrow();

        //  execute transaction toBankAccount == sourceAccount  .amount(amount)
        toBankAccount.setBalance(toBankAccount.getBalance().add(amount));
        bankAccountRepository.save(toBankAccount);
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
        transactionToAccount.setSender(transactionCreateUpdateDTO.getBeneficiary());
        transactionToAccount.setSourceAccount(transactionCreateUpdateDTO.getDestinationAccount());
        transactionToAccount.setBeneficiary(transactionCreateUpdateDTO.getSender());
        transactionToAccount.setDestinationAccount(transactionCreateUpdateDTO.getSourceAccount());
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

        TransactionReadDTO transactionReadDTO = Optional.of(transactionToAccount)
                .map(transactionRepository::save)
                .map(transactionReadConverter::convert)
                .orElseThrow();
        return new CreateUpdateTransactionResponse(transactionReadDTO, new ArrayList<>());
    }


    private void validateBalance(String accountNumber, BigDecimal amount) {

        BankAccountReadDTO bankAccount = bankAccountClientRestService.findByAccountNumber(accountNumber)
                .orElseThrow();
        if (bankAccount.getBalance().compareTo(BigDecimal.ZERO) < 0
                || bankAccount.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("not enough money ");
        }
    }


    public DeleteTransactionForClientResponse delete(Long request) {
        CLIENT_ID =loginEntityRestService.getClientId();
        Long clientId = transactionRepository.findById(request).orElseThrow()
                .getBankAccount().getClient().getId();
        Transaction clientForDelete = transactionRepository.findById(request)
                .orElseThrow(() -> new ValidationException("Transaction not found"));
        TransactionStatus transactionStatus = transactionRepository.findById(request)
                .orElseThrow().getTransactionStatus();

        if (clientId == CLIENT_ID) {
            if (transactionStatus == TransactionStatus.DRAFT_INVALID
                    || transactionStatus == TransactionStatus.DRAFT_VALID) {
                transactionRepository.delete(clientForDelete);
                return new DeleteTransactionForClientResponse(findOneTransactionConverter.convert(clientForDelete), new ArrayList<>());
            } else {
                CoreError coreError = new CoreError("You can not delete the processing or executing transaction");
                return new DeleteTransactionForClientResponse(findOneTransactionConverter.convert(clientForDelete)
                        , new ArrayList<>(Collections.singleton(coreError)));
            }
        } else {
            throw new ValidationException("Nothing found");
        }

    }

}