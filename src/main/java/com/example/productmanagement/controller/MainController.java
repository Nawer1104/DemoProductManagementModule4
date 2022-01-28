package com.example.productmanagement.controller;

import com.example.productmanagement.model.Category;
import com.example.productmanagement.model.Employee;
import com.example.productmanagement.model.Product;
import com.example.productmanagement.model.Role;
import com.example.productmanagement.repository.EmployeeRepository;
import com.example.productmanagement.service.CategoryService;
import com.example.productmanagement.service.CustomEmployeeDetails;
import com.example.productmanagement.service.EmployeeService;
import com.example.productmanagement.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
public class MainController {

    @Autowired
    EmployeeService employeeService;

    @Autowired
    ProductService productService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    EmployeeRepository employeeRepository;

    @GetMapping("")
    public String showHomePage() {
        return "views/index";
    }

    @GetMapping("/register")
    public String showSignUpForm(Model model) {
        model.addAttribute("employee", new Employee());
        return "views/signup_form";
    }

    @PostMapping("/process_register")
    public String processRegistration(@ModelAttribute(value = "employee") Employee employee,
                                      @RequestParam String pass,
                                      @RequestParam String confirmPass,
                                      Model model) throws Exception {
        if (pass.equals(confirmPass)) {
            employee.setPassword(pass);
            employee.setImg("defaultavatar.png");
            try {
                employeeService.saveUserWithDeafultRole(employee);
                model.addAttribute("mess", "Success!!");
                return "views/signup_form";
            } catch (Exception e) {
                model.addAttribute("messFail", "Employee's number already exists!!");
                return "views/signup_form";
            }
        } else {
            model.addAttribute("messFail", "Your password doesn't match each other!!");
            return "views/signup_form";
        }
    }

    @GetMapping("/products")
    public ModelAndView showList() {
        return listByPage(1, "id", "asc", "");
    }

    @GetMapping("/employees")
    public ModelAndView showEmployeeList( @AuthenticationPrincipal CustomEmployeeDetails loggedEmployee) {
        ModelAndView modelAndView = new ModelAndView("views/listAllEmployee");
        Employee employee = loggedEmployee.getLoggedEmployee();
        List<Employee> employeeList = employeeService.getEmployeeExceptItSelf(employee.getId());
        modelAndView.addObject("employeeList", employeeList);
        return modelAndView;
    }

    @GetMapping("/page/{pageNumber}")
    public ModelAndView listByPage(@PathVariable(name = "pageNumber") int currentPage,
                                   @RequestParam(value = "sortField") String sortField,
                                   @RequestParam(value = "sortDir") String sortDir,
                                   @RequestParam("keyword") String keyword
                                   ) {
        ModelAndView modelAndView = new ModelAndView("views/list");
        Page<Product> page;
        if (!keyword.equals("")) {
            page = productService.findAllByName(currentPage, sortField, sortDir, keyword);
        } else {
            page = productService.findAll(currentPage, sortField, sortDir);
        }
        long totalItems = page.getTotalElements();
        int totalPages = page.getTotalPages();
        List<Product> products = page.getContent();
        List<Category> categories = categoryService.findAll();
        modelAndView.addObject("products", products);
        modelAndView.addObject("currentPage", currentPage);
        modelAndView.addObject("categories", categories);
        modelAndView.addObject("totalItems", totalItems);
        modelAndView.addObject("totalPages", totalPages);
        modelAndView.addObject("sortField", sortField);
        modelAndView.addObject("sortDir", sortDir);
        modelAndView.addObject("keyword", keyword);

        String reverseSortDir = sortDir.equals("asc")? "desc" : "asc";
        modelAndView.addObject("reverseSortDir", reverseSortDir);
        return modelAndView;
    }

    @GetMapping("/add")
    public String showNewForm(Model model) {
        model.addAttribute("product", new Product());
        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        model.addAttribute("pageTitle", "Add New Product");
        return "views/add";
    }

    @GetMapping("/addCate")
    public String showCateForm(Model model) {
        model.addAttribute("category", new Category());
        model.addAttribute("pageTitle", "Add New Category");
        return "views/addCate";
    }

    @PostMapping("/saveCate")
    public String saveCategory(@ModelAttribute(name = "category") Category category, RedirectAttributes ra) {
        categoryService.save(category);
        ra.addFlashAttribute("mess", "Success");
        return "redirect:/products";
    }

    @PostMapping("/save")
    public String saveProduct(@Valid @ModelAttribute(name = "product") Product product,
                              BindingResult result,
                              Model model,
                              RedirectAttributes ra,
                              @RequestParam("fileImage") MultipartFile multipartFile
    ) throws IOException {
        if (result.hasErrors()){
            List<Category> categories = categoryService.findAll();
            model.addAttribute("categories", categories);
            model.addAttribute("pageTitle", "Add New Product");
            return "views/add";
        }
        if (multipartFile.getSize() != 0) {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            product.setImage(fileName);
            productService.save(product);
            String uploadDir = "./images" ;
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            try (InputStream inputStream = multipartFile.getInputStream()) {
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(inputStream,filePath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new IOException("Could not save upload file: " + fileName);
            }
            ra.addFlashAttribute("mess", "Success");
            return "redirect:/products";
        }
        if (product.getId() != 0) {
            int id = Math.toIntExact(product.getId());
            Product oldProduct = productService.findById(id);
            product.setImage(oldProduct.getImage());
        } else {
            product.setImage("default-bike-big.jpg");
        }
        productService.save(product);
        ra.addFlashAttribute("mess", "Success");
        return "redirect:/products";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") int id, Model model, RedirectAttributes ra) {
        Product product = productService.findById(id);
        model.addAttribute("product", product);
        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        model.addAttribute("pageTitle", "Edit product (Id : " + id + " )");
        return "views/add";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") int id, RedirectAttributes ra) {
        productService.delete(id);
        ra.addFlashAttribute("mess", "Deleted Success");
        return "redirect:/products";
    }

    @GetMapping("/employee/edit/{id}")
    public String editEmployeeForm (@PathVariable(value = "id") int id, Model model) {
        Employee employee = employeeService.getEmployee(id);
        List<Role> roleList = employeeService.getRoles();
        model.addAttribute("employee", employee);
        model.addAttribute("roleList", roleList);
        return "views/edit_form";
    }

    @PostMapping("/employee/save")
    public String editEmployee(@Valid @ModelAttribute(name = "employee") Employee employee,
                              BindingResult result,
                              Model model,
                              @AuthenticationPrincipal CustomEmployeeDetails loggedEmployee,
                              RedirectAttributes ra,
                              @RequestParam("fileImage") MultipartFile multipartFile
    ) throws IOException {
        if (result.hasErrors()){
            List<Role> roleList = employeeService.getRoles();
            model.addAttribute("roleList", roleList);
            return "views/edit_form";
        }
        if (multipartFile.getSize() != 0) {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            employee.setImg(fileName);
            try {
                employeeService.save(employee);
                loggedEmployee.setFirstName(employee.getFirstName());
                loggedEmployee.setLastName(employee.getLastName());
                String uploadDir = "./images" ;
                Path uploadPath = Paths.get(uploadDir);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                try (InputStream inputStream = multipartFile.getInputStream()) {
                    Path filePath = uploadPath.resolve(fileName);
                    Files.copy(inputStream,filePath, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    throw new IOException("Could not save upload file: " + fileName);
                }
                ra.addFlashAttribute("mess", "Success");
                return "redirect:/employees";
            } catch (Exception e) {
                model.addAttribute("messFail", "Employee's number already exists!!");
                return editEmployeeForm(employee.getId(), model);
            }

        }
        if (employee.getId() != 0) {
            int id = Math.toIntExact(employee.getId());
            Employee oldEmployee = employeeService.getEmployee(id);
            employee.setImg(oldEmployee.getImg());
        } else {
            employee.setImg("defaultavatar.png");
        }
        try {
            employeeService.save(employee);
            loggedEmployee.setFirstName(employee.getFirstName());
            loggedEmployee.setLastName(employee.getLastName());

        }catch (Exception e) {
            model.addAttribute("messFail", "Employee's number already exists!!");
            return editEmployeeForm(employee.getId(), model);
        }
        ra.addFlashAttribute("mess", "Success");
        return "redirect:/employees";
    }

    @GetMapping("/employee/delete/{id}")
    public String deleteEmployee(@PathVariable("id") int id, RedirectAttributes ra) {
        employeeService.delete(id);
        ra.addFlashAttribute("mess", "Deleted Success");
        return "redirect:/employees";
    }

    @GetMapping("/403")
    public String error403() {
        return "views/403";
    }
}
