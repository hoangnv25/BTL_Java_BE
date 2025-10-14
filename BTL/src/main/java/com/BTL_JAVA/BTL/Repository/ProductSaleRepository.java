package com.BTL_JAVA.BTL.Repository;

import com.BTL_JAVA.BTL.Entity.Product.ProductSale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductSaleRepository extends JpaRepository<ProductSale, Integer> {
}