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
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductMapper productMapper;

    @Override
    public Product addProduct(ProductDTO ProductDTO) {
        Product product = productMapper.mapToProductEntity(ProductDTO);
        return productRepository.save(product);
    }

    @Override
    public List<ProductDTO> getAllProducts(Long categoryId, Long typeId) {

        Specification<Product> productSpecification= Specification.where(null);

        if(null != categoryId){
            productSpecification = productSpecification.and(ProductSpecification.hasCategoryId(categoryId));
        }
        if(null != typeId){
            productSpecification = productSpecification.and(ProductSpecification.hasCategoryTypeId(typeId));
        }

        List<Product> products = productRepository.findAll(productSpecification);
        return productMapper.getProductDTOs(products);
    }

    @Override
    public ProductDTO getProductBySlug(String slug) {
        Product product= productRepository.findBySlug(slug);
        if(null == product){
            throw new ResourceNotFoundEx("Product Not Found!");
        }
        ProductDTO ProductDTO = productMapper.mapProductToDto(product);
        ProductDTO.setCategoryId(product.getCategory().getId());
        ProductDTO.setCategoryTypeId(product.getCategoryType().getId());
        ProductDTO.setVariants(productMapper.mapProductVariantListToDto(product.getProductVariants()));
        ProductDTO.setProductResources(productMapper.mapProductResourcesListDto(product.getResources()));
        return ProductDTO;
    }

    @Override
    public ProductDTO getProductById(Long id) {
        Product product= productRepository.findById(id).orElseThrow(()-> new ResourceNotFoundEx("Product Not Found!"));
        ProductDTO ProductDTO = productMapper.mapProductToDto(product);
        ProductDTO.setCategoryId(product.getCategory().getId());
        ProductDTO.setCategoryTypeId(product.getCategoryType().getId());
        ProductDTO.setVariants(productMapper.mapProductVariantListToDto(product.getProductVariants()));
        ProductDTO.setProductResources(productMapper.mapProductResourcesListDto(product.getResources()));
        return ProductDTO;
    }

    @Override
    public Product updateProduct(ProductDTO ProductDTO, Long id) {
        Product product= productRepository.findById(id).orElseThrow(()-> new ResourceNotFoundEx("Product Not Found!"));
        ProductDTO.setId(product.getId());
        return productRepository.save(productMapper.mapToProductEntity(ProductDTO));
    }

    @Override
    public Product fetchProductById(Long id) throws Exception {
        return productRepository.findById(id).orElseThrow(BadRequestException::new);
    }


}
