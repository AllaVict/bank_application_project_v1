package com.bank.http.controller;

import com.bank.core.service.ManagerService;
import com.bank.model.dto.manager.ManagerCreateUpdateDTO;
import com.bank.model.enums.ManagerStatus;
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
@RequestMapping("/manager") // part of url adress
public class ManagerController {
    private final ManagerService managerService;


    @GetMapping("/managers") // /manager/managers --  part of url adress
    public String findAllManagers(Model model) {
        model.addAttribute("managers", managerService.findAll());
        return "manager/managers"; //folder name in templates
    }


    @GetMapping("/managers/{id}")// manager/managers/{id} -- part of url adress
    public String findByIdManager(@PathVariable("id") Long id, Model model) {
        return managerService.findById(id)
                .map(manager -> {
                    model.addAttribute("manager", manager);
                    return "manager/manager";//admin/manager - folder name in templates
                })
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The manager id  doesn`t found"));
    }

    @GetMapping("/new_manager")// /manager/new_manager --  part of url adress
    public String formToCreateManager(Model model,
                                      @ModelAttribute("manager") ManagerCreateUpdateDTO managerCreateUpdateDTO) {
        model.addAttribute("manager", managerCreateUpdateDTO);
        model.addAttribute("statuses", ManagerStatus.values());
        return "manager/new_manager";//folder name in templates
    }


    @PostMapping()
    public String createManager(@ModelAttribute("manager") ManagerCreateUpdateDTO managerCreateUpdateDTO,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes,
                                Model model) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("manager", managerCreateUpdateDTO);
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            model.addAttribute("statuses", ManagerStatus.values());
            return "redirect:/manager/new_manager";
        }

        return "redirect:/manager/managers/" + managerService.create(managerCreateUpdateDTO).getId();
    }


    @GetMapping("update/{id}")
    public String updateManagerForm(Model model, @PathVariable("id") Long id) {
        model.addAttribute("manager",
                managerService.findById(id).orElseThrow()); //findById(id) return Optional
        model.addAttribute("statuses", ManagerStatus.values());
        return "manager/update_manager";
    }


    @PatchMapping("/{id}") // /admin/{id} --  part of url adress
    public String updateManager(@ModelAttribute("manager") ManagerCreateUpdateDTO managerCreateUpdateDTO,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes,
                                @PathVariable("id") Long id, Model model) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("manager", managerCreateUpdateDTO);
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            model.addAttribute("statuses", ManagerStatus.values());
            return "redirect:/manager/update/{id}";
        }
        return managerService.update(id, managerCreateUpdateDTO)
                .map(url -> "redirect:/manager/managers")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Manager not found"));

    }


    @GetMapping("/delete/{id}")// /manager/delete/{id} --  part of url adress
    public String deleteManager(@PathVariable("id") Long id) {
        if (!managerService.delete(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Can not delete, manager not found");
        }
        managerService.delete(id);
        return "redirect:/manager/managers";//url adress
    }

}
