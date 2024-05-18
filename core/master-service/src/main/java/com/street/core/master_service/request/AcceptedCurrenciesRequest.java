package com.street.core.master_service.request;

import com.street.core.master_service.enums.CurrencyTypeEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AcceptedCurrenciesRequest {
    private Long currencyTypeId;
    private CurrencyTypeEnum currencyType;
    private Boolean status;
}
