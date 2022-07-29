package com.theverygroup.products.dto;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Type {

    BOOKS("Book"),
    CASUAL("Casual"),
    CERAMICS("Ceramics"),
    ELECTRICAL("Electrical"),
    RUNNING("Running"),
    VOUCHERS("Voucher");

    @JsonValue
    private String name;

}