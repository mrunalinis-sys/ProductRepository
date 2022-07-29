package com.theverygroup.products.service;

import java.util.HashSet;

import com.theverygroup.products.dto.Product;
import com.theverygroup.products.exception.InvalidInputException;
import com.theverygroup.products.exception.ResourceNotFoundException;

public interface ProductService {
	
	
	Product saveProduct(Product product) throws InvalidInputException;

	void deleteProduct(String id) throws ResourceNotFoundException;

	HashSet<Product> getProducts(String id, String type)throws InvalidInputException;


}
