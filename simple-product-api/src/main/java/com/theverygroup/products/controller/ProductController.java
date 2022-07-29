package com.theverygroup.products.controller;

import com.theverygroup.products.dto.Product;
import com.theverygroup.products.repository.ProductRepository;
import com.theverygroup.products.service.ProductService;
import lombok.extern.log4j.Log4j2;
import com.theverygroup.products.exception.InvalidInputException;
import com.theverygroup.products.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashSet;
import java.util.List;

import javax.validation.Valid;


/**
 * 
 * @author Mrunalini
 * 
 * This class provides entry point for all the APIS to perform operations on product.
 *
 */

@RestController
@Log4j2
@RequestMapping("/products")
public class ProductController {
	
	private final ProductRepository productRepository;
	public ProductService productService ;

	@Autowired
	public ProductController(ProductRepository productRepository,ProductService productService) {
	
	    this.productRepository = productRepository;
		this.productService = productService;
	}

	/**
	 * Gets all the product information.
	 * 
	 * @return
	 * 		list of {@link Product}
	 */
	@GetMapping
	public List<Product> findAll() { 
		log.info("Method FindAll");
		return productRepository.findAll();
	}

	/**
	 * Search the product based on product id or type.
	 * 
	 * @param type
	 * 			type of product.
	 * @param id
	 * 		   unique product id.
	 * @return
	 * 		  set of {@link Product}
	 * 
	 * @throws InvalidInputException
	 * 		  throw above exception when product id is already present in file.
	 */
	
	@GetMapping("/search")
	public  HashSet<Product> getProducts(@RequestParam(name = "type", required = false) String type,
			@RequestParam(name = "id", required = false) String id) throws  InvalidInputException {
		log.info("Method GetProducts");
		return productService.getProducts(id, type);
	}

	/**
	 * Save the new product.
	 * 
	 * @param product
	 * 		  Entity that wraps all the information related to new product which you want to save.
	 * 
	 * @return
	 *       Entity {@link Product}
	 *       
	 * @throws InvalidInputException
	 * 		 Throw exception when wrong input is provided.
	 */
	@PostMapping
	public ResponseEntity<Product> saveProduct(@Valid @RequestBody Product product) throws InvalidInputException {
		log.info("Method SaveProduct");
		return new ResponseEntity<Product>(productService.saveProduct(product), HttpStatus.CREATED);
	}
	
	/**
	 * Delete the product record with respect to product id provided.
	 * 
	 * @param id
	 * 		  product id given in path.
	 * 
	 * @return 
	 * 		 Response message.
	 *       
	 * @throws ResourceNotFoundException
	 * 		  If Id is not present then it throws above exception.
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteProduct(@PathVariable("id") String id) throws ResourceNotFoundException{
	    log.info("Method DeleteProduct");		
		productService.deleteProduct(id);
		return new ResponseEntity<String>("Product deleted successfully!.", HttpStatus.OK);
	}
}