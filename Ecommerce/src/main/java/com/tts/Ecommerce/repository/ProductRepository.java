package com.tts.Ecommerce.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.tts.Ecommerce.model.Product;
@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {
	List<Product> findAll();
	Product findById(long id);
	List<Product> findByBrand(String brand);
	List<Product> findByCategory(String category);
	List<Product> findByBrandAndCategory(String brand, String category);
	
	@Query("SELECT DISTINCT p.brand from Product p")
	List<String> findByDistinctBrands();
	
	@Query("SELECT DISTINCT p.category from Product p")
	List<String> findByDistinctCategories();
}