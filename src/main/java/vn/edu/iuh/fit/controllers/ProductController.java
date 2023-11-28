package vn.edu.iuh.fit.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import vn.edu.iuh.fit.enums.ProductStatus;
import vn.edu.iuh.fit.models.Customer;
import vn.edu.iuh.fit.models.Product;
import vn.edu.iuh.fit.repositories.CustomerRepository;
import vn.edu.iuh.fit.repositories.ProductRepository;
import vn.edu.iuh.fit.services.ProductServices;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class ProductController {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductServices productServices;
    @GetMapping("/products")
    private  String  showCustomerList(
            HttpSession httpSession,
            Model model,
            @RequestParam("page")Optional<Integer> page,
            @RequestParam("size")Optional<Integer> size){
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(10);
        Page<Product> cadidatePage = productServices.findPaginate(currentPage-1,
                pageSize,"name","asc");

        model.addAttribute("productPage",cadidatePage);

        int totalPages = cadidatePage.getTotalPages();
        if (totalPages >0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1,totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers",pageNumbers);
        }
        //tra ve trang web
        return "product/list";
    }

    //show form add
    @GetMapping ("/products/show-add-form")
    public String showFormAdd(Model model) {
        Product product = new Product();
        model.addAttribute("productAdd",product);
        model.addAttribute("statuses", ProductStatus.values());
        return "product/add";
    }
    //add
    @PostMapping("/products/add")
    public String addProduct(
            @ModelAttribute("productAdd")    Product Product,
            BindingResult result , Model model) {
        productRepository.save(Product);
        return "redirect:/products";
    }
    //delete
    @GetMapping ("/products/delete/{id}")
    public String deleteProduct(@PathVariable("id") long id) {
        Product product = productRepository.findById(id).orElse(new Product());
        productRepository.delete(product);
        return "redirect:/products";
    }
    //show form edit
    @GetMapping("/products/show-edit-form/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
        Product Product = productRepository.findById(id).orElse(null);
        model.addAttribute("productUpdate", Product);
        model.addAttribute("statuses", ProductStatus.values());
        return "product/update"; // Trả về view hiển thị form cập nhật thông tin khách hàng
    }
    //update
    @PostMapping("/products/update/{id}")
    public String updateCustomer(@PathVariable("id") long id,
                                 @ModelAttribute("productUpdate")  Product updateProduct) {
        Product product = productRepository.findById(id).orElse(null);
        if (product != null) {
            product.setName(updateProduct.getName());
            product.setDescription(updateProduct.getDescription());
            product.setUnit(updateProduct.getUnit());
            product.setManufacturer(updateProduct.getManufacturer());
            product.setStatus(updateProduct.getStatus());


            productRepository.save(product);
        }
        return "redirect:/products";
    }

}
