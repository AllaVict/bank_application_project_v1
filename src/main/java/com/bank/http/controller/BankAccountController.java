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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequiredArgsConstructor
@RequestMapping("/bankAccount")
public class BankAccountController {

    private final BankAccountService bankAccountService;
    private final ClientService clientService;
    private final BankAccountReadToUpdateConverter bankAccountReadToUpdateConverter;

    // ++++ works
    @GetMapping("/bankAccounts") // /account/accounts --  part of url adress
    public String findAllAccounts(Model model) {
        model.addAttribute("accounts", bankAccountService.findAll());
        return "bank_account/bankAccounts"; //folder name in templates
    }

    // ++++ works
    @GetMapping("/bankAccounts/{id}")// bank_account/bank_accounts/{id} -- part of url adress
    public String findByIdBankAccount(@PathVariable("id") Long id, Model model) {
        return bankAccountService.findById(id)
                .map(account -> {
                    model.addAttribute("account", account);
                    model.addAttribute("client",
                            account.getClient().getLastName() + " " + account.getClient().getFirstName());
                    return "bank_account/bankAccount";//bank_account/bankAccount - folder name in templates
                })
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The bank account id  doesn`t found"));
    }

    //bank_accounts -- <a href="/bank_account/new_account">Add bank account</a>
    // @GetMapping("/new_account") return "bank_account/new_account";
    // ++++ works
    @GetMapping("/newAccount")// bank_account/new_account --  part of url adress
    public String formToCreateBankAccount(Model model,
                                     @ModelAttribute("account") BankAccountCreateUpdateDTO bankAccountCreateUpdateDTO) {
        model.addAttribute("account", bankAccountCreateUpdateDTO);
        model.addAttribute("currencyCodes", CurrencyCode.values());
        model.addAttribute("statuses", BankAccountStatus.values());
        model.addAttribute("types", BankAccountType.values());
        model.addAttribute("clients", clientService.findAll());
        return "bank_account/newAccount";//folder name in templates
    }

    //bank_account/new_account -- th:method="POST" th:action="@{/bank_account}" th:object="${account}"
    //@PostMapping() return "redirect:/bank_account/bank_accounts";
    //++++ works
    @PostMapping()
    public String createBankAccount(@ModelAttribute("account") @Validated BankAccountCreateUpdateDTO bankAccountCreateUpdateDTO,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes,
                               Model model) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("account", bankAccountCreateUpdateDTO);
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            model.addAttribute("currencyCodes", CurrencyCode.values());
            model.addAttribute("statuses", BankAccountStatus.values());
            model.addAttribute("types", BankAccountType.values());
            model.addAttribute("clients", clientService.findAll());
            return "redirect:/bankAccount/newAccount";
        }
        return "redirect:/bankAccount/bankAccounts/" + bankAccountService.create(bankAccountCreateUpdateDTO).getId();
    }

    //account -- th:method="GET" th:action="@{/bank_account/{id}/update(id=${account.getId()})}"
    //@GetMapping("update/{id}")  return "bank_account/update_account";
    //++++ works
    @GetMapping("update/{id}")
    public String updateBankAccountForm(Model model, @PathVariable("id") Long id) {
        model.addAttribute("account",
                bankAccountReadToUpdateConverter.convert(bankAccountService.findById(id).orElseThrow()));
        model.addAttribute("currencyCodes", CurrencyCode.values());
        model.addAttribute("statuses", BankAccountStatus.values());
        model.addAttribute("types", BankAccountType.values());
        model.addAttribute("clients", clientService.findAll());
        return "bank_account/updateAccount";
    }

    //th:method="PATCH" th:action="@{/bank_account/{id}(id=${account.getId()})}"
    // @PatchMapping("/{id}") return "redirect:/bank_account/bank_accounts";
    // ++++ works
    @PatchMapping("/{id}") // /bank_account/{id} --  part of url adress
    public String updateBankAccount(@ModelAttribute("client") @Validated BankAccountCreateUpdateDTO bankAccountCreateUpdateDTO,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes,
                               @PathVariable("id") Long id, Model model) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("account", bankAccountCreateUpdateDTO);
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            model.addAttribute("currencyCodes", CurrencyCode.values());
            model.addAttribute("statuses", BankAccountStatus.values());
            model.addAttribute("types", BankAccountType.values());
            model.addAttribute("clients", clientService.findAll());
            return "redirect:/bankAccount/update/{id}";
        }
        return bankAccountService.update(id, bankAccountCreateUpdateDTO)
                .map(url -> "redirect:/bankAccount/bankAccounts")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Bank Account not found"));

    }

    //th:method="DELETE" th:action="@{/bank_account/{id}(id=${account.getId()})}">
    //@DeleteMapping("/{id}") @PathVariable("id") return "redirect:/bank_account/bank_accounts";
    //+++works
    @GetMapping("/delete/{id}")// /bank_account/delete/{id} --  part of url adress
    public String deleteBankAccount(@PathVariable("id") Long id) {
        if (!bankAccountService.delete(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Can not delete, Bank Account not found");
        }
        bankAccountService.delete(id);
        return "redirect:/bankAccount/bankAccounts";//url adress
    }


}