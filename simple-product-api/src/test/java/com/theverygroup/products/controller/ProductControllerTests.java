package com.theverygroup.products.controller;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.theverygroup.products.dto.Price;
import com.theverygroup.products.dto.Product;
import com.theverygroup.products.dto.Type;
import com.theverygroup.products.exception.InvalidInputException;
import com.theverygroup.products.exception.ResourceNotFoundException;
import com.theverygroup.products.repository.ProductRepository;
import com.theverygroup.products.service.ProductService;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ProductRepository productRepository;

	@MockBean
	private ProductService productServiceMock;

	@BeforeEach
	public void setUp() {
		HashSet<Product> productSet = new HashSet<>();
		HashSet<Product> productSetEmpty = new HashSet<>();

		Price price = Price.builder().value(new BigDecimal("18.99")).currency("GBP").build();
		Product product = Product.builder().id("CLN-CDE-BOOK").name("Clean Code")
				.description("Clean Code: A Handbook of Agile Software Craftsmanship (Robert C. Martin)").price(price)
				.type(Type.BOOKS).department("Books and Stationery").weight("220g").build();

		String id = "CLN-CDE-BOOK";
		String type = "Books";
		productSet.add(product);
	
		when(productRepository.findAll()).thenReturn(Arrays.asList(product));
		when(productServiceMock.getProducts(id, null)).thenReturn(productSet);
		when(productServiceMock.getProducts(id, type)).thenReturn(productSet);
		when(productServiceMock.getProducts(null, type)).thenReturn(productSet);
		when(productServiceMock.getProducts(null, null)).thenReturn(productSetEmpty);
		when(productServiceMock.getProducts(null, "Book1")).thenThrow(InvalidInputException.class);
		when(productServiceMock.getProducts("CLN", type)).thenReturn(productSetEmpty);
		when(productServiceMock.saveProduct(product)).thenReturn(product);
		
		
	}

	@Test
	public void testFindAll() throws Exception {
		String expected = "[{\"id\":\"CLN-CDE-BOOK\"," + "\"name\":\"Clean Code\","
				+ "\"description\":\"Clean Code: A Handbook of Agile Software Craftsmanship (Robert C. Martin)\","
				+ "\"price\":{\"value\":18.99,\"currency\":\"GBP\"}," + "\"type\":\"Book\","
				+ "\"department\":\"Books and Stationery\"," + "\"weight\":\"220g\"}]";

		mockMvc.perform(get("/products")).andDo(print()).andExpect(status().isOk()).andExpect(content().json(expected));
	}

	@Test
	public void getProductTestwhenTypeIsNull() throws Exception {

		String expected = "[{\"id\":\"CLN-CDE-BOOK\"," + "\"name\":\"Clean Code\","
				+ "\"description\":\"Clean Code: A Handbook of Agile Software Craftsmanship (Robert C. Martin)\","
				+ "\"price\":{\"value\":18.99,\"currency\":\"GBP\"}," + "\"type\":\"Book\","
				+ "\"department\":\"Books and Stationery\"," + "\"weight\":\"220g\"}]";

		mockMvc.perform(get("/products/search?id=CLN-CDE-BOOK")).andDo(print()).andExpect(status().isOk())
				.andExpect(content().json(expected));
	}

	@Test
	public void getProductTestwhenIdIsNull() throws Exception {

		String expected = "[{\"id\":\"CLN-CDE-BOOK\"," + "\"name\":\"Clean Code\","
				+ "\"description\":\"Clean Code: A Handbook of Agile Software Craftsmanship (Robert C. Martin)\","
				+ "\"price\":{\"value\":18.99,\"currency\":\"GBP\"}," + "\"type\":\"Book\","
				+ "\"department\":\"Books and Stationery\"," + "\"weight\":\"220g\"}]";

		mockMvc.perform(get("/products/search?type=Books")).andDo(print()).andExpect(status().isOk())
				.andExpect(content().json(expected));
	}

	@Test
	public void getProductTestwhenIdTypeBothNOTNull() throws Exception {

		String expected = "[{\"id\":\"CLN-CDE-BOOK\"," + "\"name\":\"Clean Code\","
				+ "\"description\":\"Clean Code: A Handbook of Agile Software Craftsmanship (Robert C. Martin)\","
				+ "\"price\":{\"value\":18.99,\"currency\":\"GBP\"}," + "\"type\":\"Book\","
				+ "\"department\":\"Books and Stationery\"," + "\"weight\":\"220g\"}]";

		mockMvc.perform(get("/products/search?type=Books&id=CLN-CDE-BOOK")).andDo(print()).andExpect(status().isOk())
				.andExpect(content().json(expected));
	}

	@Test
	public void getProductTestwhenIdTypeBothNull() throws Exception {
		
		String expected = "[]";
		mockMvc.perform(get("/products/search")).andDo(print()).andExpect(status().isOk()).andExpect(content().json(expected));
	}

	@Test
	public void getProductTestwhenTypeIsInvalid() throws Exception {
		mockMvc.perform(get("/products/search?type=Book1")).andDo(print()).andExpect(status().isInternalServerError());

	}
	
	@Test
	public void getProductTestwhenInvalidIdandValidType() throws Exception {
		
		String expected = "[]";
		mockMvc.perform(get("/products/search")).andDo(print()).andExpect(status().isOk()).andExpect(content().json(expected));
	}
	

	@Test
	public void SaveProductTestwhenInputIsValid() throws Exception {
		String requestBody = "{\"id\":\"CLN-CDE-BOOK\"," + "\"name\":\"Clean Code\","
				+ "\"description\":\"Clean Code: A Handbook of Agile Software Craftsmanship (Robert C. Martin)\","
				+ "\"price\":{\"value\":18.99,\"currency\":\"GBP\"}," + "\"type\":\"Book\","
				+ "\"department\":\"Books and Stationery\"," + "\"weight\":\"220g\"}";

		this.mockMvc.perform(post("/products").contentType(MediaType.APPLICATION_JSON).content(requestBody))
				.andExpect(status().isCreated());
	
   }
	
	
	@Test
	public void  saveProductTestwhenIdIsDuplicate() throws Exception {
    	Price price = Price.builder().value(new BigDecimal("18.99")).currency("GBP").build();
		Product product = Product.builder().id("CLN-CDE-BOOK").name("Clean Code")
				.description("Clean Code: A Handbook of Agile Software Craftsmanship (Robert C. Martin)").price(price)
				.type(Type.BOOKS).department("Books and Stationery").weight("220g").build();
		
		Product newproduct = Product.builder().id("CLN-CDE-BOOK").name("Clean Code")
				.description("Clean Code: A Handbook of Agile Software Craftsmanship (Robert C. Martin)").price(price)
				.type(Type.BOOKS).department("Books and Stationery").weight("220g").build();

		String requestBody = "{\"id\":\"CLN-CDE-BOOK\"," + "\"name\":\"Clean Code\","
				+ "\"description\":\"Clean Code: A Handbook of Agile Software Craftsmanship (Robert C. Martin)\","
				+ "\"price\":{\"value\":18.99,\"currency\":\"GBP\"}," + "\"type\":\"Book\","
				+ "\"department\":\"Books and Stationery\"," + "\"weight\":\"220g\"}";
		
		when(productRepository.findAll()).thenReturn(Arrays.asList(product));
		when(productServiceMock.saveProduct(newproduct)).thenThrow(InvalidInputException.class);
		this.mockMvc.perform(post("/products").contentType(MediaType.APPLICATION_JSON).content(requestBody))
		 	.andExpect(status().isInternalServerError());
	
	}
	
	@Test
	public void deleteProductTest() throws Exception{
	
		this.mockMvc.perform(MockMvcRequestBuilders
	            .delete("/products/{id}", "CLN-CDE-BOOK")
	            .contentType(MediaType.APPLICATION_JSON))
	            .andExpect(status().isOk());
	}
	
	@Test
	public void deleteProductTestwhenInvalidID() throws Exception{
		
		Price price = Price.builder().value(new BigDecimal("18.99")).currency("GBP").build();
		Product product = Product.builder().id("CLN-CDE-BOOK").name("Clean Code")
				.description("Clean Code: A Handbook of Agile Software Craftsmanship (Robert C. Martin)").price(price)
				.type(Type.BOOKS).department("Books and Stationery").weight("220g").build();
		
		when(productRepository.findAll()).thenReturn(Arrays.asList(product));
		
		doThrow(ResourceNotFoundException.class).when(productServiceMock).deleteProduct("CLN-CDE-BOOKrr");
		
		this.mockMvc.perform(MockMvcRequestBuilders .delete("/products/{id}",
		  "CLN-CDE-BOOKrr") .contentType(MediaType.APPLICATION_JSON))
		  .andExpect(status().isNotFound());
		 
	}
}