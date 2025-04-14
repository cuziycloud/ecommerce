package com.jeweluxeCode.Jeweluxe.controllers;


import com.jeweluxeCode.Jeweluxe.dto.ProductDTO;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    @GetMapping
    public List<ProductDTO> getAllProduct(){
        return Collections.EMPTY_LIST;
    }

    //create Product
    @PostMapping
    public ProductDTO createProduct(@RequestBody ProductDTO product){
        return null;
    }
}
