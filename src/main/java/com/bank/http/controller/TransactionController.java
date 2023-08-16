package com.bank.http.controller;

import com.bank.core.service.BankAccountService;
import com.bank.core.service.ProductService;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequiredArgsConstructor
@RequestMapping("/transaction") // part of url adress
public class TransactionController {

    private final TransactionService transactionService;
    private final BankAccountService bankAccountService;
    private final ProductService productService;
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
                    model.addAttribute("account", transaction.getBankAccount().getAccount_number());
                    model.addAttribute("product", transaction.getProduct().getProduct_name());
                    return "transaction/transaction";//transaction/transaction - folder name in templates
                })
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The transaction id  doesn`t found"));
    }

    @GetMapping("/new_transaction")// /transaction/new_transaction --  part of url adress
    public String formToCreateTransaction(Model model,
                                     @ModelAttribute("transaction") TransactionCreateUpdateDTO transactionCreateUpdateDTO) {
        model.addAttribute("transaction", transactionCreateUpdateDTO);
        model.addAttribute("statuses", TransactionStatus.values());
        model.addAttribute("types", TransactionType.values());
        return "transaction/new_transaction";//folder name in templates
    }


    @PostMapping()
    public String createTransaction(@ModelAttribute("transaction") TransactionCreateUpdateDTO transactionCreateUpdateDTO,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes,
                               Model model) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("transaction", transactionCreateUpdateDTO);
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
           model.addAttribute("statuses", TransactionStatus.values());
            model.addAttribute("types", TransactionType.values());
            return "redirect:/transaction/new_transaction";
        }
        return "redirect:/transaction/transactions/" + transactionService.create(transactionCreateUpdateDTO).getId();
    }


    @GetMapping("update/{id}")
    public String updateTransactionForm(Model model, @PathVariable("id") Long id) {
        model.addAttribute("transaction",
                transactionReadToUpdateConverter.convert(transactionService.findById(id).orElseThrow()));
        model.addAttribute("statuses", TransactionStatus.values());
        model.addAttribute("types", TransactionType.values());

        return "transaction/update_transaction";
    }

    @PatchMapping("/{id}") // /transaction/{id} --  part of url adress
    public String updateTransaction(@ModelAttribute("client") TransactionCreateUpdateDTO transactionCreateUpdateDTO,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes,
                               @PathVariable("id") Long id, Model model) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("transaction", transactionCreateUpdateDTO);
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            model.addAttribute("statuses", TransactionStatus.values());
            model.addAttribute("types", TransactionType.values());
            return "redirect:/transaction/update/{id}";
        }
        return transactionService.update(id, transactionCreateUpdateDTO)
                .map(url -> "redirect:/transaction/transactions")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found"));
   }


    @GetMapping("/delete/{id}")// /transaction/delete/{id} --  part of url adress
    public String deleteTransaction(@PathVariable("id") Long id) {
        if (!transactionService.delete(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Can not delete, Transaction not found");
        }
        transactionService.delete(id);
        return "redirect:/transaction/transactions";//url adress
    }



}