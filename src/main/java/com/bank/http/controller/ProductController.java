package com.bank.http.controller;


import com.bank.core.service.ManagerService;
import com.bank.core.service.ProductService;
import com.bank.core.util.product.ProductReadToUpdateConverter;
import com.bank.model.dto.product.ProductCreateUpdateDTO;
import com.bank.model.enums.CurrencyCode;
import com.bank.model.enums.ProductStatus;
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
@RequestMapping("/product") // part of url adress
public class ProductController {

    private final ProductService productService;
    private final ManagerService managerService;
    private final ProductReadToUpdateConverter productReadToUpdateConverter;

    // ++++ works
    @GetMapping("/products")// /admin/products --  part of url adress
    public String findAllProducts(Model model) {
        model.addAttribute("products", productService.findAll());
        return "product/products";//folder name in templates
    }

    // ++++ works
    @GetMapping("/products/{id}")// product/products/{id} -- part of url adress
    public String findByIdProduct(@PathVariable("id") Long id, Model model) {
        return productService.findById(id)
                .map(product -> {
                    model.addAttribute("product", product);
                    model.addAttribute("manager",
                            product.getManager().getLast_name() + " " + product.getManager().getFirst_name());
                    return "product/product";//product/product - folder name in templates
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The product id doesn`t found"));
    }

    //products -- <a href="/admin/new_product">Add Manager</a>
    // @GetMapping("/new_product") return "admin/new_product";
    // ++++ works
    @GetMapping("/new_product")// /admin/new_product --  part of url adress
    public String formToCreateProduct(Model model,
                                      @ModelAttribute("product") ProductCreateUpdateDTO productCreateUpdateDTO) {
        model.addAttribute("product", productCreateUpdateDTO);
        model.addAttribute("statuses", ProductStatus.values());
        model.addAttribute("currency_codes", CurrencyCode.values());
        model.addAttribute("managers", managerService.findAll());
        return "product/new_product";//folder name in templates
    }

    //manager/update_product -- th:method="POST" th:action="@{/admin}" th:object="${manager}"
    //@PostMapping() return "redirect:/admin/managers";
    //++++ works
    @PostMapping()
    public String createProduct(@ModelAttribute("product") @Validated ProductCreateUpdateDTO productCreateUpdateDTO,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes,
                                Model model) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("product", productCreateUpdateDTO);
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            model.addAttribute("statuses", ProductStatus.values());
            model.addAttribute("currency_codes", CurrencyCode.values());
            return "redirect:/product/new_product";
        }
//        ProductReadDTO productReadDTO = productService.create(productCreateDTO);
//        return "redirect:/product/products/" + productReadDTO.getId();
        return "redirect:/product/products/" + productService.create(productCreateUpdateDTO).getId();
    }

    //  <a th:href="@{/product/update/{id}(id=${product.id})}">Update</a>
    //@GetMapping("/{id}/edit")  return "admin/update_product";
    //++++ works
    @GetMapping("update/{id}")
    public String formToUpdateProduct(Model model, @PathVariable("id") Long id) {
        model.addAttribute("product",
        productReadToUpdateConverter.convert(productService.findById(id).orElseThrow()));
        //findById(id) return Optional ProductReadDTO convert to productUpdateDTO
        model.addAttribute("statuses", ProductStatus.values());
        model.addAttribute("currency_codes", CurrencyCode.values());
        model.addAttribute("managers", managerService.findAll());
        return "product/update_product";
    }

    //th:method="PATCH" th:action="@{/product/{id}(id=${product.getId()})}"
    // @PatchMapping("/{id}") return "redirect:/product/products";
    @PatchMapping("/{id}") // /admin/{id} --  part of url adress
    public String updateProduct(@ModelAttribute("product") @Validated ProductCreateUpdateDTO productCreateUpdateDTO,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes,
                                @PathVariable("id") Long id, Model model) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("product", productCreateUpdateDTO);
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            model.addAttribute("statuses", ProductStatus.values());
            model.addAttribute("currency_codes", CurrencyCode.values());
            model.addAttribute("managers", managerService.findAll());
            return "redirect:/product/update/{id}";

        }
        return productService.update(id, productCreateUpdateDTO)
                .map(url -> "redirect:/product/products")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
    }
    //<a th:href="@{/product/delete/{id}(id=${product.id})}" class="btn btn-danger">Delete</a>
    //@GetMapping("/delete/{id}") @PathVariable("id") return "redirect:/admin/managers";
    @GetMapping("/delete/{id}")// /delete/{id} --  part of url adress
    public String deleteProduct(@PathVariable("id") Long id) {
        if (!productService.delete(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Can not delete, product not found");
        }
        productService.delete(id);
        return "redirect:/product/products";//url adress
    }


}

