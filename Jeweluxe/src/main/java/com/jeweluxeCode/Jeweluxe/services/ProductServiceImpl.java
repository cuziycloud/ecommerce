package com.jeweluxeCode.Jeweluxe.services;

import com.jeweluxeCode.Jeweluxe.dto.ProductDTO;
import com.jeweluxeCode.Jeweluxe.entities.Product;
import com.jeweluxeCode.Jeweluxe.exceptions.ResourceNotFoundEx;
import com.jeweluxeCode.Jeweluxe.mapper.ProductMapper;
import com.jeweluxeCode.Jeweluxe.repositories.ProductRepository;
import com.jeweluxeCode.Jeweluxe.specification.ProductSpecification;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    @Override
    @Transactional
    public Product addProduct(ProductDTO ProductDTO) {
        // Ánh xạ từ DTO sang Entity
        Product product = productMapper.mapToProductEntity(ProductDTO);
        return productRepository.save(product);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDTO> getAllProducts(Long categoryId, Long typeId) {
        Specification<Product> productSpecification = Specification.where(null);

        if (categoryId != null) { // ktra null
            productSpecification = productSpecification.and(ProductSpecification.hasCategoryId(categoryId));
        }
        if (typeId != null) {
            productSpecification = productSpecification.and(ProductSpecification.hasCategoryTypeId(typeId)); // Truyền Long vào Specification
        }

        List<Product> products = productRepository.findAll(productSpecification);
        return productMapper.getProductDTOs(products);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDTO getProductBySlug(String slug) {
        Product product = productRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundEx("Product Not Found with slug: " + slug));

        // Ánh xạ sang DTO
        ProductDTO ProductDTO = productMapper.mapProductToDto(product);
        ProductDTO.setCategoryId(product.getCategory() != null ? product.getCategory().getId() : null);
        ProductDTO.setCategoryTypeId(product.getCategoryType() != null ? product.getCategoryType().getId() : null);
        ProductDTO.setVariants(productMapper.mapProductVariantListToDto(product.getProductVariants()));
        ProductDTO.setProductResources(productMapper.mapProductResourcesListDto(product.getResources()));
        return ProductDTO;
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundEx("Product Not Found with ID: " + id)); // Thêm ID vào exception

        ProductDTO ProductDTO = productMapper.mapProductToDto(product);
        ProductDTO.setCategoryId(product.getCategory() != null ? product.getCategory().getId() : null);
        ProductDTO.setCategoryTypeId(product.getCategoryType() != null ? product.getCategoryType().getId() : null);
        ProductDTO.setVariants(productMapper.mapProductVariantListToDto(product.getProductVariants()));
        ProductDTO.setProductResources(productMapper.mapProductResourcesListDto(product.getResources()));
        return ProductDTO;
    }

    @Override
    @Transactional
    public Product updateProduct(ProductDTO ProductDTO, Long id) {
        // Tìm Product entity hiện có
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundEx("Product Not Found with ID: " + id));

        existingProduct.setName(ProductDTO.getName());
        existingProduct.setDescription(ProductDTO.getDescription());
        existingProduct.setPrice(ProductDTO.getPrice());
        existingProduct.setBrand(ProductDTO.getBrand());
        existingProduct.setRating(ProductDTO.getRating());
        existingProduct.setNewArrival(ProductDTO.isNewArrival());
        existingProduct.setSlug(ProductDTO.getSlug());

        return productRepository.save(existingProduct);
    }

    @Override
    @Transactional(readOnly = true)
    public Product fetchProductById(Long id) throws BadRequestException {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundEx("Product entity Not Found with ID: " + id));
    }
}
