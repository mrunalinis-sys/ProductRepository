package com.theverygroup.products.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.theverygroup.products.dto.Price;
import com.theverygroup.products.dto.Product;
import com.theverygroup.products.dto.Type;
import com.theverygroup.products.exception.InvalidInputException;
import com.theverygroup.products.exception.ResourceNotFoundException;
import com.theverygroup.products.repository.ProductRepository;
import com.theverygroup.products.service.impl.ProductServiceImpl;
import com.theverygroup.products.validator.ProductValidator;

@SpringBootTest
public class ProductServiceTest {
	
	@InjectMocks
	private ProductServiceImpl productService;
	    
	@Mock
	public ProductRepository productRepositoryMock;
	
	@Mock
	public ProductValidator productValidatorMock;
	
	@Mock
	public FileOutputStream fileOutputStreamMock;
	
	@Mock
	public ObjectMapper objectMapper;

	@Mock
	public JsonFactory jsonFactory;
	
	@Mock
	public JsonGenerator jsonGenerator;
	
	@Mock
	List<Product> productsMock;

	List<Product> products = new ArrayList<Product>();
	HashSet<Product> productSet = new HashSet<>();
	
	
	@BeforeEach
	public void setUp() {
		Price price = Price.builder().value(new BigDecimal("18.99")).currency("GBP").build();
		Product product = Product.builder().id("CLN-CDE-BOOK12").name("Clean Code")
				.description("Clean Code: A Handbook of Agile Software Craftsmanship (Robert C. Martin)").price(price)
				.type(Type.BOOKS).department("Books and Stationery").weight("220g").build();
		products.add(product);
		}
	
	
		
	@Test
	public void saveProductTestWhenValidData(){
		Product product = products.get(0);
		
		when(productRepositoryMock.findAll()).thenReturn(products);
		when(productValidatorMock.validateId(product.getId(), products)).thenReturn(false);
		doNothing().when(productRepositoryMock).writeValue(products);
		
		Product newProduct = productService.saveProduct(product);
		Mockito.verify(productRepositoryMock, times(1)).writeValue(products);
		assertEquals("CLN-CDE-BOOK12", newProduct.getId());
	}
	
	
	@Test
	public void saveProductTestWhenIdIsInValid(){
		Product product = products.get(0);
		
		when(productRepositoryMock.findAll()).thenReturn(products);
		when(productValidatorMock.validateId(product.getId(), products)).thenReturn(true);
		
		assertThrows(InvalidInputException.class, ()->productService.saveProduct(product));
		Mockito.verify(productRepositoryMock, times(0)).writeValue(products);
 	}
	
	
	@Test  
	public void deleteProductTestWhenValidData(){
		Product product = products.get(0);
		
		when(productRepositoryMock.findAll()).thenReturn(products);
		when(productValidatorMock.validateId(product.getId(), products)).thenReturn(true);
		doNothing().when(productRepositoryMock).writeValue(products);
		
		productService.deleteProduct(product.getId());
		Mockito.verify(productRepositoryMock, times(1)).writeValue(products);

   }
	
	@Test  
	public void deleteProductTestWhenInValidData(){
		Product product = products.get(0);
		
		when(productRepositoryMock.findAll()).thenReturn(products);
		when(productValidatorMock.validateId(product.getId(), products)).thenReturn(false);
		
		assertThrows(ResourceNotFoundException.class, ()->productService.deleteProduct(product.getId()));
		Mockito.verify(productRepositoryMock, times(0)).writeValue(products);

   }
	
	@Test  
	public void getProductTestWhenValidId(){
		Product product = products.get(0);
		productSet.add(product);
		
		when(productRepositoryMock.findAll()).thenReturn(products);
		when(productsMock.stream().filter(p -> p.getId().equalsIgnoreCase(product.getId())).collect(Collectors.toList())).thenAnswer(invo -> productsMock.stream());
		when(productValidatorMock.validateId(product.getId(), products)).thenReturn(true);
		
		productSet=productService.getProducts("CLN-CDE-BOOK12", null);
		String newproductID = productSet.iterator().next().getId();
		assertEquals(product.getId(),newproductID);
	}
	
	@Test  
	public void getProductTestWhenValidType(){
		Product product = products.get(0);
		productSet.add(product);
		
		when(productRepositoryMock.findAll()).thenReturn(products);
		when(productsMock.stream().filter(p -> p.getId().equalsIgnoreCase(product.getId())).collect(Collectors.toList())).thenAnswer(invo -> productsMock.stream());
		when(productValidatorMock.validateType(product.getType().toString())).thenReturn(true);
		
		productSet=productService.getProducts(null, "BOOKS");
		Type newproductType = productSet.iterator().next().getType();
		assertEquals(product.getType(),newproductType);
	}
	
	@Test  
	public void getProductTestWhenValidTypeAndID(){
		Product product = products.get(0);
		productSet.add(product);
		
		when(productRepositoryMock.findAll()).thenReturn(products);
		when(productsMock.stream().filter(p -> p.getId().equalsIgnoreCase(product.getId())).collect(Collectors.toList())).thenAnswer(invo -> productsMock.stream());
		when(productValidatorMock.validateType(product.getType().toString())).thenReturn(true);
		when(productValidatorMock.validateId(product.getId(), products)).thenReturn(true);

		productSet=productService.getProducts("CLN-CDE-BOOK12", "BOOKS");
		Type newproductType = productSet.iterator().next().getType();
		String newproductID = productSet.iterator().next().getId();
	
		assertEquals(product.getId(),newproductID);
		assertEquals(product.getType(),newproductType);
	}
	
	@Test  
	public void getProductTestWhenInValidType(){
		Product product = products.get(0);
		productSet.add(product);
		
		when(productRepositoryMock.findAll()).thenReturn(products);
		when(productsMock.stream().filter(p -> p.getId().equalsIgnoreCase(product.getId())).collect(Collectors.toList())).thenAnswer(invo -> productsMock.stream());
		when(productValidatorMock.validateType(product.getType().toString())).thenReturn(false);
		
		assertThrows(InvalidInputException.class, ()->productService.getProducts(product.getId(), product.getType().toString()));
	
	}
	
	@Test  
	public void getProductTestWhenInValidId(){
		Product product = products.get(0);
		productSet.add(product);
		
		when(productRepositoryMock.findAll()).thenReturn(products);
		when(productsMock.stream().filter(p -> p.getId().equalsIgnoreCase(product.getId())).collect(Collectors.toList())).thenAnswer(invo -> productsMock.stream());
		when(productValidatorMock.validateType(product.getType().toString())).thenReturn(false);
		when(productValidatorMock.validateId(product.getId(),products)).thenReturn(false);
		
		assertThrows(InvalidInputException.class, ()->productService.getProducts(product.getId(), product.getType().toString()));
	
	}
	
	
	
}











