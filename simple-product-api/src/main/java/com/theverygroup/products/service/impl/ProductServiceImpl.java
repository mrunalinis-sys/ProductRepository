package com.theverygroup.products.service.impl;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.theverygroup.products.dto.Product;
import com.theverygroup.products.exception.InvalidInputException;
import com.theverygroup.products.exception.ResourceNotFoundException;
import com.theverygroup.products.repository.ProductRepository;
import com.theverygroup.products.service.ProductService;
import com.theverygroup.products.validator.ProductValidator;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

/**
 * 
 * @author Mrunalini
 * 
 * Service class to perform search,save and delete operations on Product.
 *
 */

@Service
@Log4j2
public class ProductServiceImpl implements ProductService{

	public ProductRepository productRepository;
	public ProductValidator productValidator;
	
	
	@Autowired
	public ProductServiceImpl(ProductRepository productRepository,ProductValidator productValidator) {
		super();
		this.productRepository = productRepository;
		this.productValidator = productValidator;
	}
	
	/**
	 * Method to save the product information.
	 * 
	 * @param product
	 * 		  Entity that wraps all the information related to new product which you want to save. 
	 * @return
	 * 		  Entity {@link Product}
	 */
	@Override
	@SneakyThrows
	public Product saveProduct(final Product product) {
		log.info("Method saveProduct in ProductServiceImpl");
		String id=product.getId().trim();
		List<Product> master = productRepository.findAll();//id null validation
		if (productValidator.validateId(id, master)) {
			throw new InvalidInputException("Id is already present please enter new ID ");
		}
		product.setId(id);
		master.add(product);
		productRepository.writeValue(master);
		return product;
	}

	/**
	 * Method to delete the product information.
	 * 
	 * @param id
	 * 		   unique product id. 
	 * 
	 * @throws ResourceNotFoundException
	 * 		  If Id is not present  then it throws above exception.
	 */
	@Override
	@SneakyThrows 
	public void deleteProduct(final String id) {
		log.info("Method deleteProduct in ProductServiceImpl"+ id);

		List<Product> master = productRepository.findAll();
		if (StringUtils.hasText(id)) {
			if (!productValidator.validateId(id, master)) {
				throw new ResourceNotFoundException("Product Id is Not Present");
			}
		}
		List<Product> productsSearch = master.stream().filter(p -> p.getId().equalsIgnoreCase(id)).collect(Collectors.toList());
		master.removeAll(productsSearch);
		productRepository.writeValue(master);
	}

	/**
	 * Method to search the product information based on id or type.
	 * 
	 * @param id
	 * 		   unique product id.
	 * @return
	 * 		  set of {@link Product}
	 * 
	 * @throws InvalidInputException
	 * 		  throw above exception when product id is already present in file. 
	 */
	@Override
	@SneakyThrows 
	public HashSet<Product> getProducts(final String id, final String type) {
		log.info("Method getProducts in ProductServiceImpl id is" + id);
		log.info("Method getProducts in ProductServiceImpl type is" + type);

		List<Product> products = productRepository.findAll();
		List<Product> productsSearch = new ArrayList<>();
		HashSet<Product> productSet = new HashSet<>();

		if (StringUtils.hasText(id)) {

			productsSearch = products.stream().filter(p -> p.getId().equalsIgnoreCase(id)).collect(Collectors.toList());

			productSet.addAll(productsSearch);
			products = productsSearch;
		}
		if (StringUtils.hasText(type)) {
			if (!productValidator.validateType(type)) {
								throw new InvalidInputException("Please Enter Valid Type");
			}
			productSet.clear();
			productSet.addAll(products.stream().filter(p -> p.getType().toString().equalsIgnoreCase(type))
					.collect(Collectors.toList()));

		}

		return productSet;

	}
	
	

}
