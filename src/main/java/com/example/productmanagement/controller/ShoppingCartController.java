package com.example.productmanagement.controller;

import com.example.productmanagement.model.CartItem;
import com.example.productmanagement.model.Employee;
import com.example.productmanagement.repository.EmployeeRepository;
import com.example.productmanagement.service.CustomEmployeeDetails;
import com.example.productmanagement.service.EmployeeService;
import com.example.productmanagement.service.ProductService;
import com.example.productmanagement.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class ShoppingCartController {
    @Autowired
    EmployeeService employeeService;

    @Autowired
    ProductService productService;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    ShoppingCartService shoppingCartService;

    @GetMapping("/cart")
    public String showShoppingCart(
            Model model,
            @AuthenticationPrincipal CustomEmployeeDetails loggedEmployee){
        Employee employee = loggedEmployee.getLoggedEmployee();
        List<CartItem> cartItemList = shoppingCartService.listCartItems(employee);
        model.addAttribute("cartItemList", cartItemList);
        return "views/shopping_cart";
    }

    @GetMapping("/cart/add/{pid}/{qty}")
    public String addProductToCart(
            @PathVariable("qty") double quantity,
            @PathVariable("pid") int productId,
            @AuthenticationPrincipal CustomEmployeeDetails loggedEmployee,
            RedirectAttributes ra
    ){
        Employee employee = loggedEmployee.getLoggedEmployee();
        shoppingCartService.addProduct(productId, quantity, employee);
        ra.addFlashAttribute("mess", "Success");
        return "redirect:/products";
    }

    @GetMapping("/cart/update/{pid}/{qty}")
    public String updateProductQuantity(
            @PathVariable("qty") double quantity,
            @PathVariable("pid") int productId,
            @AuthenticationPrincipal CustomEmployeeDetails loggedEmployee
    ){
        Employee employee = loggedEmployee.getLoggedEmployee();
        if (quantity > 0) {
            shoppingCartService.updateQuantity(quantity, productId, employee);
            return "redirect:/cart";
        } else {
            shoppingCartService.remove(employee, productId);
            return "redirect:/cart";
        }
    }

    @GetMapping("/cart/remove/{pid}")
    public String removeProductQuantity(
            @PathVariable("pid") int productId,
            @AuthenticationPrincipal CustomEmployeeDetails loggedEmployee
    ){
        Employee employee = loggedEmployee.getLoggedEmployee();
        shoppingCartService.remove(employee, productId);
        return "redirect:/cart";
    }
}
