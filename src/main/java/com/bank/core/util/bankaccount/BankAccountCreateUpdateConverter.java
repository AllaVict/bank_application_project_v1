package com.bank.core.util.bankaccount;

import com.bank.core.util.Converter;
import com.bank.model.dto.bankaccount.BankAccountCreateUpdateDTO;
import com.bank.model.entity.BankAccount;
import com.bank.model.entity.Client;
import com.bank.model.entity.Product;
import com.bank.repository.ClientRepository;
import com.bank.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
@RequiredArgsConstructor
public class BankAccountCreateUpdateConverter implements Converter<BankAccountCreateUpdateDTO, BankAccount> {

    private final ClientRepository clientRepository;
    private final ProductRepository productRepository;



    @Override
    public BankAccount convert(BankAccountCreateUpdateDTO fromBankAccountCreateUpdateDTO, BankAccount toClient) {
        copy(fromBankAccountCreateUpdateDTO, toClient);
        return toClient;
    }

    @Override
    public BankAccount convert(BankAccountCreateUpdateDTO bankAccountCreateUpdateDTO) {
        BankAccount bankAccount=new BankAccount();
        copy(bankAccountCreateUpdateDTO, bankAccount);
        return bankAccount;
    }
    private void copy(BankAccountCreateUpdateDTO object, BankAccount bankAccount) {
        bankAccount.setId(object.getId());
        bankAccount.setClient(getClient(object.getClientId()));
        bankAccount.setProduct(getProduct(object.getProductId()));
        bankAccount.setAccountName(object.getAccountName());
        bankAccount.setAccountNumber(object.getAccountNumber());
        bankAccount.setAccountType(object.getBankAccountType());
        bankAccount.setStatus(object.getStatus());
        bankAccount.setBalance(object.getBalance());
        bankAccount.setCurrencyCode(object.getCurrencyCode());
        bankAccount.setCreatedAt(object.getCreatedAt());
        bankAccount.setUpdatedAt(object.getUpdatedAt());
    }

    public Client getClient(Long clientId) {
        return Optional.ofNullable(clientId)
                .flatMap(clientRepository::findById)
                .orElse(null);
    }

    public Product getProduct(Long productId){
        return Optional.ofNullable(productId)
                .flatMap(productRepository::findById)
                .orElse(null);
    }
}
