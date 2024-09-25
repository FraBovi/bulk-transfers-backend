package com.mycompany.bulk_transfer_application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchParameters {

    @Pattern(regexp = "^[A-Z]{7,11}$", message = "Invalid BIC")
    private String bic;

    @Pattern(regexp = "^[A-Z]{2}[A-Z0-9]{5,25}$", message = "Invalid IBAN")
    private String iban;

    @Pattern(regexp = ".{2,}", message = "Name must have at least 2 characters")
    private String name;

    @Pattern(regexp = "^(AND|OR)$", message = "Value must be either 'AND' or 'OR'")
    private String type;

    public SearchParameters(String bic, String iban) {
        this.bic = bic;
        this.iban = iban;
    }

    public SearchParameters(String iban) {
        this.iban = iban;
    }
    
}
