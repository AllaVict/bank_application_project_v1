package com.bank.http.controller;

import com.bank.core.service.BankAccountService;
import com.bank.core.service.ClientService;
import com.bank.core.util.bankaccount.BankAccountReadToUpdateConverter;
import com.bank.model.dto.bankaccount.BankAccountCreateUpdateDTO;
import com.bank.model.enums.BankAccountStatus;
import com.bank.model.enums.BankAccountType;
import com.bank.model.enums.CurrencyCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequiredArgsConstructor
@RequestMapping("/bank_account")
public class BankAccountController {

    private final BankAccountService bankAccountService;
    private final ClientService clientService;
    private final BankAccountReadToUpdateConverter bankAccountReadToUpdateConverter;


    @GetMapping("/bank_accounts") // /account/accounts --  part of url adress
    public String findAllAccounts(Model model) {
        model.addAttribute("accounts", bankAccountService.findAll());
        return "bank_account/bank_accounts"; //folder name in templates
    }


    @GetMapping("/bank_accounts/{id}")// bank_account/bank_accounts/{id} -- part of url adress
    public String findByIdBankAccount(@PathVariable("id") Long id, Model model) {
        return bankAccountService.findById(id)
                .map(account -> {
                    model.addAttribute("account", account);
                    model.addAttribute("client",
                            account.getClient().getLast_name() + " " + account.getClient().getFirst_name());
                    return "bank_account/bank_account";//bank_account/bank_account - folder name in templates
                })
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The bank account id  doesn`t found"));
    }


    @GetMapping("/new_account")// bank_account/new_account --  part of url adress
    public String formToCreateBankAccount(Model model,
                                     @ModelAttribute("account") BankAccountCreateUpdateDTO bankAccountCreateUpdateDTO) {
        model.addAttribute("account", bankAccountCreateUpdateDTO);
        model.addAttribute("currency_codes", CurrencyCode.values());
        model.addAttribute("statuses", BankAccountStatus.values());
        model.addAttribute("types", BankAccountType.values());
        model.addAttribute("clients", clientService.findAll());
        return "bank_account/new_account";//folder name in templates
    }


    @PostMapping()
    public String createBankAccount(@ModelAttribute("account") BankAccountCreateUpdateDTO bankAccountCreateUpdateDTO,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes,
                               Model model) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("account", bankAccountCreateUpdateDTO);
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            model.addAttribute("currency_codes", CurrencyCode.values());
            model.addAttribute("statuses", BankAccountStatus.values());
            model.addAttribute("types", BankAccountType.values());
            model.addAttribute("clients", clientService.findAll());
            return "redirect:/bank_account/new_account";
        }
        return "redirect:/bank_account/bank_accounts/" + bankAccountService.create(bankAccountCreateUpdateDTO).getId();
    }


    @GetMapping("update/{id}")
    public String updateBankAccountForm(Model model, @PathVariable("id") Long id) {
        model.addAttribute("account",
                bankAccountReadToUpdateConverter.convert(bankAccountService.findById(id).orElseThrow()));
        model.addAttribute("currency_codes", CurrencyCode.values());
        model.addAttribute("statuses", BankAccountStatus.values());
        model.addAttribute("types", BankAccountType.values());
        model.addAttribute("clients", clientService.findAll());
        return "bank_account/update_account";
    }


    @PatchMapping("/{id}") // /bank_account/{id} --  part of url adress
    public String updateBankAccount(@ModelAttribute("client") BankAccountCreateUpdateDTO bankAccountCreateUpdateDTO,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes,
                               @PathVariable("id") Long id, Model model) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("account", bankAccountCreateUpdateDTO);
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            model.addAttribute("currency_codes", CurrencyCode.values());
            model.addAttribute("statuses", BankAccountStatus.values());
            model.addAttribute("types", BankAccountType.values());
            model.addAttribute("clients", clientService.findAll());
            return "redirect:/bank_account/update/{id}";
        }
        return bankAccountService.update(id, bankAccountCreateUpdateDTO)
                .map(url -> "redirect:/bank_account/bank_accounts")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Bank Account not found"));

    }

    @GetMapping("/delete/{id}")// /bank_account/delete/{id} --  part of url adress
    public String deleteBankAccount(@PathVariable("id") Long id) {
        if (!bankAccountService.delete(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Can not delete, Bank Account not found");
        }
        bankAccountService.delete(id);
        return "redirect:/bank_account/bank_accounts";//url adress
    }


}