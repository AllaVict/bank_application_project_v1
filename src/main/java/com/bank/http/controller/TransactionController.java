package com.bank.http.controller;

import com.bank.core.service.BankAccountService;
import com.bank.core.service.TransactionService;
import com.bank.core.util.transaction.TransactionReadToUpdateConverter;
import com.bank.model.dto.transaction.TransactionCreateUpdateDTO;
import com.bank.model.enums.TransactionStatus;
import com.bank.model.enums.TransactionType;
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
@RequestMapping("/transaction") // part of url adress
public class TransactionController {

    private final TransactionService transactionService;
    private final BankAccountService bankAccountService;
    private final TransactionReadToUpdateConverter transactionReadToUpdateConverter;

    // ++++ works
    @GetMapping("/transactions") // /transaction/transactions --  part of url adress
    public String findAllTransactions(Model model) {
        model.addAttribute("transactions", transactionService.findAll());
        return "transaction/transactions";//folder name in templates
    }

    // ++++ works
    @GetMapping("/transactions/{id}")// transaction/transactions/{id} -- part of url adress
    public String findByIdTransaction(@PathVariable("id") Long id, Model model) {
        return transactionService.findById(id)
                .map(transaction -> {
                    model.addAttribute("transaction", transaction);
                    model.addAttribute("account", transaction.getBankAccount().getAccountNumber());
                  //  model.addAttribute("product", transaction.getProduct().getProductName());
                    return "transaction/transaction";//transaction/transaction - folder name in templates
                })
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The transaction id  doesn`t found"));
    }

    //transactions -- <a href="/transaction/new_transaction">Add transaction</a>
    // @GetMapping("/new_transaction") return "transaction/new_transaction";
    // ++++ works
    @GetMapping("/newTransaction")// /transaction/new_transaction --  part of url adress
    public String formToCreateTransaction(Model model,
                                     @ModelAttribute("transaction") TransactionCreateUpdateDTO transactionCreateUpdateDTO) {
        model.addAttribute("transaction", transactionCreateUpdateDTO);
        model.addAttribute("statuses", TransactionStatus.values());
        model.addAttribute("types", TransactionType.values());
        model.addAttribute("bankAccounts", bankAccountService.findAll());
        return "transaction/newTransaction";//folder name in templates
    }

    //client/new_client -- th:method="POST" th:action="@{/transaction}" th:object="${transaction}"
    //@PostMapping() return "redirect:/transaction/transactions";
    //++++ works
    @PostMapping()
    public String createTransaction(@ModelAttribute("transaction") @Validated TransactionCreateUpdateDTO transactionCreateUpdateDTO,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes,
                               Model model) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("transaction", transactionCreateUpdateDTO);
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            model.addAttribute("statuses", TransactionStatus.values());
            model.addAttribute("types", TransactionType.values());
            model.addAttribute("bankAccounts", bankAccountService.findAll());
            return "redirect:/transaction/newTransaction";
        }
        return "redirect:/transaction/transactions/" + transactionService.create(transactionCreateUpdateDTO).getId();
    }

    //client -- th:method="GET" th:action="@{/client/{id}/update(id=${client.getId()})}"
    //@GetMapping("/{id}/edit")  return "admin/update_client";
    //++++ works
    @GetMapping("update/{id}")
    public String updateTransactionForm(Model model, @PathVariable("id") Long id) {
        model.addAttribute("transaction",
                transactionReadToUpdateConverter.convert(transactionService.findById(id).orElseThrow()));
        model.addAttribute("statuses", TransactionStatus.values());
        model.addAttribute("types", TransactionType.values());
        model.addAttribute("bankAccounts", bankAccountService.findAll());
        return "transaction/updateTransaction";
    }

    //th:method="PATCH" th:action="@{/transaction/{id}(id=${transaction.getId()})}"
    // @PatchMapping("/{id}") return "redirect:/transaction/transactions";
    // ++++ works
    @PatchMapping("/{id}") // /transaction/{id} --  part of url adress
    public String updateTransaction(@ModelAttribute("client") @Validated TransactionCreateUpdateDTO transactionCreateUpdateDTO,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes,
                               @PathVariable("id") Long id, Model model) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("transaction", transactionCreateUpdateDTO);
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            model.addAttribute("statuses", TransactionStatus.values());
            model.addAttribute("types", TransactionType.values());
            model.addAttribute("bankAccounts", bankAccountService.findAll());
            return "redirect:/transaction/update/{id}";
        }
        return transactionService.update(id, transactionCreateUpdateDTO)
                .map(url -> "redirect:/transaction/transactions")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found"));
   }

    //th:method="DELETE" th:action="@{/transaction/{id}(id=${transaction.getId()})}">
    //@DeleteMapping("/{id}") @PathVariable("id") return "redirect:/transaction/transactions";
    //+++works
    @GetMapping("/delete/{id}")// /transaction/delete/{id} --  part of url adress
    public String deleteTransaction(@PathVariable("id") Long id) {
        if (!transactionService.delete(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Can not delete, Transaction not found");
        }
        transactionService.delete(id);
        return "redirect:/transaction/transactions";//url adress
    }



}