package com.mycompany.bulk_transfer_application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchParameters {

    private String bic;
    private String iban;
    private String name;
    
}
