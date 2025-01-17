package com.street.core.master_service.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

import com.street.core.master_service.entity.NetworkEntity;
import com.street.core.master_service.enums.CurrencyTypeEnum;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AcceptedCurrenciesResponse {
    private Long id;

    private CurrencyTypeEnum currencyType;

    private Long currencyTypeId;

    private Boolean status;

    private Date createdAt;

    private Date updatedAt;

    private String name;

    private String code;

    private String symbol;

    private Integer decimal;

    private String contract;

    private NetworkEntity network;
}
