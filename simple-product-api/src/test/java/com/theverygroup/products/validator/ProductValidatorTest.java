package com.theverygroup.products.validator;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.theverygroup.products.dto.Price;
import com.theverygroup.products.dto.Product;
import com.theverygroup.products.dto.Type;

@SpringBootTest
public class ProductValidatorTest {

	@InjectMocks
	public ProductValidator productValidatorMock;
	
	List<Product> products = new ArrayList<Product>();

	@Mock
	List<Product> productsMock;
	
	@BeforeEach
	public void setUp() {
		Price price = Price.builder().value(new BigDecimal("18.99")).currency("GBP").build();
		Product product = Product.builder().id("CLN-CDE-BOOK12").name("Clean Code")
				.description("Clean Code: A Handbook of Agile Software Craftsmanship (Robert C. Martin)").price(price)
				.type(Type.BOOKS).department("Books and Stationery").weight("220g").build();
	
		products.add(product);
		}
	
	@Test
	public void validateTest() {

	    when(productsMock.stream().anyMatch(i -> i.getId().equalsIgnoreCase(products.get(0).getId()))).thenAnswer(invo -> productsMock.stream());

		assertTrue(productValidatorMock.validateId("CLN-CDE-BOOK12", products));
		assertFalse(productValidatorMock.validateId("CLN-CDE-BOOK12ff", products));
	}
	
	@Test 
	public void validateTypeTestWhenValidType() {
		assertTrue(productValidatorMock.validateType("Books"));
	}
	
	@Test 
	public void validateTypeTestWhenInvallidType() {
		assertFalse(productValidatorMock.validateType("Books1"));
	}
}
