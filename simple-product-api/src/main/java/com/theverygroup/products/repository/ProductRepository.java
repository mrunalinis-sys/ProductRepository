package com.theverygroup.products.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.theverygroup.products.dto.Product;
import com.theverygroup.products.util.ProductDataUtils;

import lombok.extern.log4j.Log4j2;

/**
 * 
 * @author Mrunalini
 * 
 * Repository class for all Data layer operations.
 *
 */
@Repository
@Log4j2
public class ProductRepository {
	
    @Autowired
    public ProductRepository() {
    	List<Product> products = ProductDataUtils.loadAll();
    }

	/**
	 * Loads all the product information.
	 * 
	 * @return
	 * 		list of {@link Product}
	 */
    public List<Product> findAll() {
    	log.info("Method findAll in ProductRepository");
    	
        return ProductDataUtils.loadAll();
    }
    
    /**
     * write the product data in the file.
     * 
     * @param master
     * 		  list of {@link Product}
     */
    public void writeValue(List<Product> master) {
    	log.info("Method writeValue in ProductRepository");
         ProductDataUtils.writeValue(master);
    }

}