package com.jeweluxeCode.Jeweluxe.repositories;

import com.jeweluxeCode.Jeweluxe.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    Product findBySlug(String slug);

}

