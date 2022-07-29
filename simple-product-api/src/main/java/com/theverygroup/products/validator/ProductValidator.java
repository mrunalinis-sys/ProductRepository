package com.theverygroup.products.validator;

import java.util.List;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import com.theverygroup.products.dto.Product;
import com.theverygroup.products.dto.Type;



/**
 * 
 * @author Mrunalini
 * 
 * Validator class to validate the id and type of product.
 *  
 */
@Component
@ComponentScan({ "com.theverygroup.products.validator" })
public class ProductValidator {

	/**
	 * checks whether the type is valid or not.
	 * 
	 * @param type
	 * 		  {@link Type}
	 * @return
	 *        true in case of valid type false otherwise.
	 * 
	 */
	public boolean validateType(final String type) {

	    boolean flag = false;

		Type[] Types = Type.values();
		for (Type aType : Types)

			if (aType.name().equalsIgnoreCase(type)) {
				flag = true;
			}

		return flag;

	}

	/**
	 * Checks whether the id is present in data or not.
	 * 
	 * @param id
	 * 			Id of the Product
	 * @param master
	 * 			List of all the products.
	 * @return
	 		true in case of valid type false otherwise.
	 */
	public boolean validateId(final String id, final List<Product> master){
		
			return master.stream().anyMatch(i -> i.getId().equalsIgnoreCase(id));

		
	}

}
