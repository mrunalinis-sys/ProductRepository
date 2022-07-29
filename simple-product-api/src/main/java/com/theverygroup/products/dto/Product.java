package com.theverygroup.products.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
public class Product {

	@NotBlank(message = "Id should not be Blank")
	private String id;
	private String name;
    private String description;
    private Price price;
    
    @NotNull(message = "Type should not be null")
    private Type type;
    private String department;
    private String weight;
    
    
    

}