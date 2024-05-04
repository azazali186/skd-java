package com.street.core.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CryptoRequest {
    String name;

    String code;

    Boolean status;

    String symbol; 

    String contract; 

    Long network; 
}
