package com.example.waikan.controllers;

import com.example.waikan.models.Product;
import com.example.waikan.models.User;
import com.example.waikan.services.ProductService;
import com.example.waikan.services.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@Controller
@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
public class ProductController {
    private final ProductService productService;
    private final UserService userService;

    public ProductController(ProductService productService,
                             UserService userService) {
        this.productService = productService;
        this.userService = userService;
    }

    @GetMapping("/")
    public String products(@RequestParam(required = false, defaultValue = "") String searchCity,
                           @RequestParam(required = false, defaultValue = "") String searchWord,
                           Principal principal, Model model) {
        model.addAttribute("towns", ControllerUtils.getAllTowns());
        model.addAttribute("searchWord", searchWord);
        model.addAttribute("searchCity", searchCity);
        model.addAttribute("user", userService.getUserByPrincipal(principal));
        model.addAttribute("products", productService.getAllProducts(searchWord, searchCity));
        return "products";
    }

    @PostMapping("/product/create")
    public String saveProduct(@AuthenticationPrincipal User user,
                              @RequestParam("file1")MultipartFile file1,
                              @RequestParam("file2")MultipartFile file2,
                              @RequestParam("file3")MultipartFile file3,
                              Product product) throws IOException {
        productService.saveProduct(user, product, file1, file2, file3);
        return "redirect:/my/products";
    }

    @GetMapping("/product/{id}")
    public String product(@PathVariable("id") Product product, Principal principal,
                          Model model) {
        model.addAttribute("user", userService.getUserByPrincipal(principal));
        model.addAttribute("product", product);
        model.addAttribute("images", product.getImages());
        model.addAttribute("authorProduct", product.getUser());
        return "product-info";
    }

    @GetMapping("/my/products")
    public String userProducts(Principal principal, Model model) {
        User user = userService.getUserByPrincipal(principal);
        model.addAttribute("user", user);
        model.addAttribute("towns", ControllerUtils.getAllTowns());
        model.addAttribute("products", productService.getProductsByUserId(user.getId()));
        return "my-products";
    }


    @PostMapping("/product/delete/{id}")
    public String deleteProduct(@AuthenticationPrincipal User user, @PathVariable("id") Long id) {
        productService.deleteProduct(user, id);
        return "redirect:/my/products";
    }
}
