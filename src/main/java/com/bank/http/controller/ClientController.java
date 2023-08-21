package com.bank.http.controller;

import com.bank.core.service.ClientService;
import com.bank.core.service.ManagerService;
import com.bank.core.util.client.ClientReadToUpdateConverter;
import com.bank.model.dto.client.ClientCreateUpdateDTO;
import com.bank.model.enums.ClientStatus;
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
@RequestMapping("/client")
public class ClientController {

    private final ClientService clientService;
    private final ManagerService managerService;
    private final ClientReadToUpdateConverter clientReadToUpdateConverter;

    // ++++ works
    @GetMapping("/clients")// /client/clients --  part of url adress
    public String findAllClients(Model model) {
        model.addAttribute("clients", clientService.findAll());
        return "client/clients";//folder name in templates
    }

    // ++++ works
    @GetMapping("/clients/{id}")// client/clients/{id} -- part of url adress
    public String findByIdClient(@PathVariable("id") Long id, Model model) {
        return clientService.findById(id)
                .map(client -> {
                    model.addAttribute("client", client);
                    model.addAttribute("manager",
                            client.getManager().getLastName() + " " + client.getManager().getFirstName());
                    return "client/client";//client/client - folder name in templates
                })
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The client id  doesn`t found"));
    }

    //clients -- <a href="/client/new_client">Add client</a>
    // @GetMapping("/new_client") return "client/new_client";
    // ++++ works
    @GetMapping("/newClient")// /client/new_client --  part of url adress
    public String formToCreateClient(Model model,
                                     @ModelAttribute("client") ClientCreateUpdateDTO clientCreateUpdateDTO) {
        model.addAttribute("client", clientCreateUpdateDTO);
        model.addAttribute("statuses", ClientStatus.values());
        model.addAttribute("managers", managerService.findAll());
        return "client/newClient";//folder name in templates
    }

    //client/new_client -- th:method="POST" th:action="@{/client}" th:object="${client}"
    //@PostMapping() return "redirect:/client/clients";
    //++++ works
    @PostMapping()
    public String createClient(@ModelAttribute("client")  @Validated ClientCreateUpdateDTO clientCreateUpdateDTO,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes,
                               Model model) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("client", clientCreateUpdateDTO);
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            model.addAttribute("statuses", ClientStatus.values());
            model.addAttribute("managers", managerService.findAll());
            return "redirect:/client/newClient";
        }
        return "redirect:/client/clients/" + clientService.create(clientCreateUpdateDTO).getId();
    }

    //client -- th:method="GET" th:action="@{/client/{id}/update(id=${client.getId()})}"
    //@GetMapping("/{id}/edit")  return "admin/update_client";
    //++++ works
    @GetMapping("update/{id}")
    public String updateClientForm(Model model, @PathVariable("id") Long id) {
        model.addAttribute("client",
                clientReadToUpdateConverter.convert(clientService.findById(id).orElseThrow()));
        model.addAttribute("statuses", ClientStatus.values());
        model.addAttribute("managers", managerService.findAll());
        return "client/updateClient";
    }

    //th:method="PATCH" th:action="@{/client/{id}(id=${client.getId()})}"
    // @PatchMapping("/{id}") return "redirect:/client/clients";
    // ++++ works
    @PatchMapping("/{id}") // /admin/{id} --  part of url adress
    public String updateClient(@ModelAttribute("client")  @Validated ClientCreateUpdateDTO clientCreateUpdateDTO,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes,
                               @PathVariable("id") Long id, Model model) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("client", clientCreateUpdateDTO);
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            model.addAttribute("statuses", ClientStatus.values());
            model.addAttribute("managers", managerService.findAll());
            return "redirect:/client/update/{id}";
        }
        return clientService.update(id, clientCreateUpdateDTO)
                .map(url -> "redirect:/client/clients")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found"));

    }

    //th:method="DELETE" th:action="@{/client/{id}(id=${client.getId()})}">
    //@DeleteMapping("/{id}") @PathVariable("id") return "redirect:/client/clients";
    //+++works
    @GetMapping("/delete/{id}")// /client/delete/{id} --  part of url adress
    public String deleteClient(@PathVariable("id") Long id) {
        if (!clientService.delete(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Can not delete, Client not found");
        }
        clientService.delete(id);
        return "redirect:/client/clients";//url adress
    }


}

