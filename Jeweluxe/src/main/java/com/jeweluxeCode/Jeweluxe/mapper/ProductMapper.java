package com.jeweluxeCode.Jeweluxe.mapper;

import com.jeweluxeCode.Jeweluxe.dto.ProductDTO;
import com.jeweluxeCode.Jeweluxe.dto.ProductResourceDto;
import com.jeweluxeCode.Jeweluxe.dto.ProductVariantDto;
import com.jeweluxeCode.Jeweluxe.entities.*;
import com.jeweluxeCode.Jeweluxe.services.CategoryService; // Đảm bảo CategoryService dùng Long ID
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
// import java.util.UUID; // Bỏ import UUID
import java.util.stream.Collectors;

@Component
public class ProductMapper {

    @Autowired
    private CategoryService categoryService; // Đảm bảo CategoryService dùng Long ID

    public Product mapToProductEntity(ProductDTO ProductDTO) {
        Product product = new Product();
        // Không set ID khi tạo mới, nó sẽ tự tạo
        // if (ProductDTO.getId() != null) {
        //     product.setId(ProductDTO.getId());
        // }
        product.setName(ProductDTO.getName());
        product.setDescription(ProductDTO.getDescription());
        product.setBrand(ProductDTO.getBrand());
        product.setNewArrival(ProductDTO.isNewArrival());
        product.setPrice(ProductDTO.getPrice());
        product.setRating(ProductDTO.getRating());
        product.setSlug(ProductDTO.getSlug());

        if (ProductDTO.getCategoryId() != null) {
            // Giả sử CategoryService có phương thức getCategoryById trả về Optional<Category> hoặc ném exception
            Category category = categoryService.getCategoryById(ProductDTO.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found for ID: " + ProductDTO.getCategoryId()));
            product.setCategory(category);

            if (ProductDTO.getCategoryTypeId() != null) {
                // Tìm CategoryType trong danh sách của Category đã load
                CategoryType categoryType = category.getCategoryTypes().stream()
                        .filter(ct -> Objects.equals(ct.getId(), ProductDTO.getCategoryTypeId())) // So sánh Long
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("CategoryType not found for ID: " + ProductDTO.getCategoryTypeId() + " in Category: " + category.getName()));
                product.setCategoryType(categoryType);
            }
        } else {
            throw new IllegalArgumentException("Category ID cannot be null when creating/updating a product");
        }


        if (ProductDTO.getVariants() != null && !ProductDTO.getVariants().isEmpty()) {
            product.setProductVariants(mapToProductVariantList(ProductDTO.getVariants(), product));
        } else {
            product.setProductVariants(Collections.emptyList());
        }

        if (ProductDTO.getProductResources() != null && !ProductDTO.getProductResources().isEmpty()) {
            product.setResources(mapToProductResourceList(ProductDTO.getProductResources(), product));
        } else {
            product.setResources(Collections.emptyList());
        }

        return product;
    }

    // Đổi tên phương thức cho nhất quán
    private List<Resources> mapToProductResourceList(List<ProductResourceDto> resourceDtos, Product product) {
        if (resourceDtos == null) return Collections.emptyList();
        return resourceDtos.stream().map(dto -> {
            Resources resource = new Resources();
            // Không set ID khi map từ DTO để tạo mới
            resource.setName(dto.getName());
            resource.setType(dto.getType());
            resource.setUrl(dto.getUrl());
            resource.setIsPrimary(dto.getIsPrimary());
            resource.setProduct(product); // Thiết lập quan hệ ngược lại
            return resource;
        }).collect(Collectors.toList());
    }

    // Đổi tên phương thức cho nhất quán
    private List<ProductVariant> mapToProductVariantList(List<ProductVariantDto> variantDtos, Product product) {
        if (variantDtos == null) return Collections.emptyList();
        return variantDtos.stream().map(dto -> {
            ProductVariant variant = new ProductVariant();
            // Không set ID khi map từ DTO để tạo mới
            variant.setColor(dto.getColor());
            variant.setSize(dto.getSize());
            variant.setStockQuantity(dto.getStockQuantity());
            variant.setProduct(product); // Thiết lập quan hệ ngược lại
            return variant;
        }).collect(Collectors.toList());
    }

    public List<ProductDTO> getProductDTOs(List<Product> products) {
        if (products == null) return Collections.emptyList();
        return products.stream()
                .map(this::mapProductToDto)
                .collect(Collectors.toList()); // Dùng collect(Collectors.toList()) thay vì toList() để tương thích rộng hơn
    }

    public ProductDTO mapProductToDto(Product product) {
        if (product == null) return null;
        return ProductDTO.builder()
                .id(product.getId()) // ID giờ là Long
                .brand(product.getBrand())
                .name(product.getName())
                .price(product.getPrice())
                .isNewArrival(product.isNewArrival())
                .rating(product.getRating())
                .description(product.getDescription())
                .slug(product.getSlug())
                .thumbnail(getProductThumbnail(product.getResources()))
                // Thêm categoryId và categoryTypeId nếu DTO có các trường này
                .categoryId(product.getCategory() != null ? product.getCategory().getId() : null)
                .categoryTypeId(product.getCategoryType() != null ? product.getCategoryType().getId() : null)
                // Có thể thêm variants và resources nếu ProductDTO có
                // .variants(mapProductVariantListToDto(product.getProductVariants()))
                // .productResources(mapProductResourcesListDto(product.getResources()))
                .build();
    }

    private String getProductThumbnail(List<Resources> resources) {
        if (resources == null || resources.isEmpty()) {
            return null; // Hoặc trả về URL placeholder
        }
        return resources.stream()
                .filter(r -> r != null && Boolean.TRUE.equals(r.getIsPrimary())) // Kiểm tra isPrimary là true
                .map(Resources::getUrl)
                .findFirst()
                .orElseGet(() -> resources.get(0).getUrl()); // Lấy ảnh đầu tiên nếu không có ảnh primary
    }

    public List<ProductVariantDto> mapProductVariantListToDto(List<ProductVariant> productVariants) {
        if (productVariants == null) return Collections.emptyList();
        return productVariants.stream()
                .map(this::mapProductVariantToDto) // Sửa tên phương thức
                .collect(Collectors.toList());
    }

    // Đổi tên phương thức cho nhất quán
    private ProductVariantDto mapProductVariantToDto(ProductVariant productVariant) {
        if (productVariant == null) return null;
        return ProductVariantDto.builder()
                .id(productVariant.getId()) // ID giờ là Long
                .color(productVariant.getColor())
                .size(productVariant.getSize())
                .stockQuantity(productVariant.getStockQuantity())
                // Thường không cần productId trong Variant DTO
                .build();
    }

    public List<ProductResourceDto> mapProductResourcesListDto(List<Resources> resources) {
        if (resources == null) return Collections.emptyList();
        return resources.stream()
                .map(this::mapResourceToDto)
                .collect(Collectors.toList());
    }

    private ProductResourceDto mapResourceToDto(Resources resource) { // Đổi tên tham số
        if (resource == null) return null;
        return ProductResourceDto.builder()
                .id(resource.getId()) // ID giờ là Long
                .url(resource.getUrl())
                .name(resource.getName())
                .isPrimary(resource.getIsPrimary())
                .type(resource.getType())
                // Thường không cần productId trong Resource DTO
                .build();
    }
}
