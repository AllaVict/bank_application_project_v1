package com.bank.core.util.bankaccount;

import com.bank.core.util.Converter;
import com.bank.model.dto.bankaccount.BankAccountCreateUpdateDTO;
import com.bank.model.entity.BankAccount;
import com.bank.model.entity.Client;
import com.bank.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
@RequiredArgsConstructor
public class BankAccountCreateUpdateConverter implements Converter<BankAccountCreateUpdateDTO, BankAccount> {

    private final ClientRepository clientRepository;


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
        bankAccount.setClient( getClient(object.getClient_id())  );
        bankAccount.setAccount_name(object.getAccount_name());
        bankAccount.setAccount_number(object.getAccount_number());
        bankAccount.setAccount_type(object.getAccount_type());
        bankAccount.setStatus(object.getStatus());
        bankAccount.setBalance(object.getBalance());
        bankAccount.setCurrency_code(object.getCurrency_code());
        bankAccount.setCreated_at(object.getCreated_at());
        bankAccount.setUpdated_at(object.getUpdated_at());
    }

    public Client getClient(Long clientId) {
        return Optional.ofNullable(clientId)
                .flatMap(clientRepository::findById)
                .orElse(null);
    }
}
