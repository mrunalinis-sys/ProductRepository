package com.theverygroup.products.util;

import com.theverygroup.products.dto.Price;
import com.theverygroup.products.dto.Product;
import com.theverygroup.products.dto.Type;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductDataUtilsTests {

    @Test
    public void testLoadAll_containsElement() {
        // Given

        // When
        List<Product> products = ProductDataUtils.loadAll();

        // Then
        Price price = Price.builder()
                .value(new BigDecimal("18.99"))
                .currency("GBP")
                .build();
        Product expected = Product.builder()
                .id("CLN-CDE-BOOK")
                .name("Clean Code")
                .description("Clean Code: A Handbook of Agile Software Craftsmanship (Robert C. Martin)")
                .price(price)
                .type(Type.BOOKS)
                .department("Books and Stationery")
                .weight("220g")
                .build();
        assertThat(products).contains(expected);
    }
}