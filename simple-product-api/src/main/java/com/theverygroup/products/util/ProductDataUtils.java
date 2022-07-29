package com.theverygroup.products.util;

import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.theverygroup.products.dto.Product;

import lombok.SneakyThrows;

/**
 * 
 * @author Mrunalini
 * 
 * Utility class for read and write operations on Product.
 *
 */
@Component
@ComponentScan({ "com.theverygroup.products.util" })
public final class ProductDataUtils {

	private static final String RESOURCE_LOCATION = "src/main/resources/data/products.json";

	/**
	 * Gets all the product information.
	 * 
	 * @return
	 * 		list of {@link Product}
	 */
	@SneakyThrows
	public static List<Product> loadAll() {
		String contents = FileUtils.readFileToString(ResourceUtils.getFile(RESOURCE_LOCATION), StandardCharsets.UTF_8);
		return new ObjectMapper().readValue(contents, new TypeReference<>() {
		});
	}

	 /**
     * write the product data in the file.
     * 
     * @param master
     * 		  list of {@link Product}
     */
	@SneakyThrows
	public static void  writeValue(List<Product> master)  {

		FileOutputStream fout = new FileOutputStream(RESOURCE_LOCATION, false);
		JsonGenerator g = new ObjectMapper().getFactory().createGenerator(fout);
		g.useDefaultPrettyPrinter();
		new ObjectMapper().writeValue(g, master);

	}
	
}
