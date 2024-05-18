package com.street.core.master_service.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WalletRequest {
    String name;

    String code;

    Boolean status;

    String address; 

    Long network;

    Long user; 
}
