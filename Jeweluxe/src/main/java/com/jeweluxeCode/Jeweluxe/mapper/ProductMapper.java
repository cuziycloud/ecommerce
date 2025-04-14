package com.jeweluxeCode.Jeweluxe.mapper;

import com.jeweluxeCode.Jeweluxe.dto.ProductDTO;
import com.jeweluxeCode.Jeweluxe.dto.ProductResourceDto;
import com.jeweluxeCode.Jeweluxe.dto.ProductVariantDto;
import com.jeweluxeCode.Jeweluxe.entities.*;
import com.jeweluxeCode.Jeweluxe.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class ProductMapper {

    @Autowired
    private CategoryService categoryService;

    public Product mapToProductEntity(ProductDTO ProductDTO){
        Product product = new Product();
        if(null != ProductDTO.getId()){
            product.setId(ProductDTO.getId());
        }
        product.setName(ProductDTO.getName());
        product.setDescription(ProductDTO.getDescription());
        product.setBrand(ProductDTO.getBrand());
        product.setNewArrival(ProductDTO.isNewArrival());
        product.setPrice(ProductDTO.getPrice());
        product.setRating(ProductDTO.getRating());
        product.setSlug(ProductDTO.getSlug());

        Category category = categoryService.getCategory(ProductDTO.getCategoryId());
        if(null != category){
            product.setCategory(category);
            Long categoryTypeId = ProductDTO.getCategoryTypeId();

            CategoryType categoryType = category.getCategoryTypes().stream().filter(categoryType1 -> categoryType1.getId().equals(categoryTypeId)).findFirst().orElse(null);
            product.setCategoryType(categoryType);
        }

        if(null != ProductDTO.getVariants()){
            product.setProductVariants(mapToProductVariant(ProductDTO.getVariants(),product));
        }

        if(null != ProductDTO.getProductResources()){
            product.setResources(mapToProductResources(ProductDTO.getProductResources(),product));
        }



        return product;
    }

    private List<Resources> mapToProductResources(List<ProductResourceDto> productResources, Product product) {

        return productResources.stream().map(productResourceDto -> {
            Resources resources= new Resources();
            if(null != productResourceDto.getId()){
                resources.setId(productResourceDto.getId());
            }
            resources.setName(productResourceDto.getName());
            resources.setType(productResourceDto.getType());
            resources.setUrl(productResourceDto.getUrl());
            resources.setIsPrimary(productResourceDto.getIsPrimary());
            resources.setProduct(product);
            return resources;
        }).collect(Collectors.toList());
    }

    private List<ProductVariant> mapToProductVariant(List<ProductVariantDto> productVariantDtos, Product product){
        return productVariantDtos.stream().map(productVariantDto -> {
            ProductVariant productVariant = new ProductVariant();
            if(null != productVariantDto.getId()){
                productVariant.setId(productVariantDto.getId());
            }
            productVariant.setColor(productVariantDto.getColor());
            productVariant.setSize(productVariantDto.getSize());
            productVariant.setStockQuantity(productVariantDto.getStockQuantity());
            productVariant.setProduct(product);
            return productVariant;
        }).collect(Collectors.toList());
    }

    public List<ProductDTO> getProductDTOs(List<Product> products) {
        return products.stream().map(this::mapProductToDto).toList();
    }

    public ProductDTO mapProductToDto(Product product) {

        return ProductDTO.builder()
                .id(product.getId())
                .brand(product.getBrand())
                .name(product.getName())
                .price(product.getPrice())
                .isNewArrival(product.isNewArrival())
                .rating(product.getRating())
                .description(product.getDescription())
                .slug(product.getSlug())
                .thumbnail(getProductThumbnail(product.getResources())).build();
    }

    private String getProductThumbnail(List<Resources> resources) {
        return resources.stream().filter(Resources::getIsPrimary).findFirst().orElse(null).getUrl();
    }

    public List<ProductVariantDto> mapProductVariantListToDto(List<ProductVariant> productVariants) {
        return productVariants.stream().map(this::mapProductVariantDto).toList();
    }

    private ProductVariantDto mapProductVariantDto(ProductVariant productVariant) {
        return ProductVariantDto.builder()
                .color(productVariant.getColor())
                .id(productVariant.getId())
                .size(productVariant.getSize())
                .stockQuantity(productVariant.getStockQuantity())
                .build();
    }

    public List<ProductResourceDto> mapProductResourcesListDto(List<Resources> resources) {
        return resources.stream().map(this::mapResourceToDto).toList();
    }

    private ProductResourceDto mapResourceToDto(Resources resources) {
        return ProductResourceDto.builder()
                .id(resources.getId())
                .url(resources.getUrl())
                .name(resources.getName())
                .isPrimary(resources.getIsPrimary())
                .type(resources.getType())
                .build();
    }
}
