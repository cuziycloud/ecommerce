package com.jeweluxeCode.Jeweluxe.services;

import com.jeweluxeCode.Jeweluxe.dto.ProductDTO;
import com.jeweluxeCode.Jeweluxe.entities.Product;

import java.util.List;

public interface ProductService {

    public Product addProduct(ProductDTO product);
    public List<ProductDTO> getAllProducts(Long  categoryId, Long  typeId);

    ProductDTO getProductBySlug(String slug);

    ProductDTO getProductById(Long  id);

    Product updateProduct(ProductDTO productDto, Long  id);

    Product fetchProductById(Long  uuid) throws Exception;
}
