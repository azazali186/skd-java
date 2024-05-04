package com.street.core.service;

import com.street.core.entity.AcceptedCurrenciesEntity;
import com.street.core.entity.CryptoEntity;
import com.street.core.entity.CurrencyEntity;
import com.street.core.entity.response.UserEntityResponse;
import com.street.core.enums.CurrencyTypeEnum;
import com.street.core.enums.JwtTokenUser;
import com.street.core.enums.LoginUserUtils;
import com.street.core.repository.AcceptedCurrenciesRepo;
import com.street.core.repository.CryptoRepo;
import com.street.core.repository.CurrencyRepo;
import com.street.core.repository.UserRepo;
import com.street.core.request.AcceptedCurrenciesRequest;
import com.street.core.response.AcceptedCurrenciesResponse;
import com.street.core.response.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class AcceptedCurrenciesService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AcceptedCurrenciesService.class);

    @Autowired
    private AcceptedCurrenciesRepo acceptedCurrenciesRepo;

    @Autowired
    private CurrencyRepo currencyRepo;

    @Autowired
    private CryptoRepo cryptoRepo;

    public ApiResponse getAll(String type, Integer pageSize, Integer pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "id"));
        CurrencyTypeEnum cType = type != null && type != "" ?  CurrencyTypeEnum.valueOf(type) : null;
        Page data = acceptedCurrenciesRepo.findAllAcceptedCurrencies(cType, pageable);
        return ApiResponse.successPage(data.getContent(), data.getTotalElements());
    }

    public List<AcceptedCurrenciesResponse> getAllList(CurrencyTypeEnum type) {
        CurrencyTypeEnum cType = type != null ?  type : null;
        List<AcceptedCurrenciesResponse> data = acceptedCurrenciesRepo.getAllList(cType);
        return data;
    }

    public ApiResponse getById(Long id) {
        Optional<AcceptedCurrenciesEntity> acceptedCurrencies = acceptedCurrenciesRepo.findById(id);
        if (acceptedCurrencies.isEmpty()) {
            return ApiResponse.failNotFound("INVALID_CURRENCY_ID");
        }
        return ApiResponse.success(acceptedCurrencies.get());
    }

    public ApiResponse create(AcceptedCurrenciesRequest req) {

        AcceptedCurrenciesEntity acceptedCurrencies = new AcceptedCurrenciesEntity();
        acceptedCurrencies.setCurrencyType(req.getCurrencyType());
        acceptedCurrencies.setCurrencyTypeId(req.getCurrencyTypeId());
        acceptedCurrencies.setStatus(true);
        acceptedCurrencies.setCreatedAt(new Date());
        acceptedCurrenciesRepo.save(acceptedCurrencies);
        return ApiResponse.success();
    }

    public ApiResponse update(Long id, AcceptedCurrenciesRequest req) {
        Optional<AcceptedCurrenciesEntity> acceptedCurrencies = acceptedCurrenciesRepo.findById(id);
        if (acceptedCurrencies.isEmpty()) {
            return ApiResponse.failNotFound("INVALID_CURRENCY_ID");
        }

        if (req.getCurrencyType() != null && req.getCurrencyTypeId() != null) {
            validCurrency(req.getCurrencyType(), req.getCurrencyTypeId());
            acceptedCurrencies.get().setCurrencyType(req.getCurrencyType());
            acceptedCurrencies.get().setCurrencyTypeId(req.getCurrencyTypeId());
        }

        acceptedCurrencies.get().setUpdatedAt(new Date());
        acceptedCurrenciesRepo.save(acceptedCurrencies.get());

        return ApiResponse.success();
    }

    private Boolean validCurrency(CurrencyTypeEnum currencyType, Long currencyTypeId) {
        Boolean valid = false;
        switch (currencyType) {
            case CRYPTO:
                Optional<CryptoEntity> crypto = cryptoRepo.findById(currencyTypeId);
                if(crypto.isPresent()){
                    valid = true;
                }
                break;
            case FIAT:
            Optional<CurrencyEntity> currency = currencyRepo.findById(currencyTypeId);
            if(currency.isPresent()){
                valid = true;
            }
                break;
        }

        return valid;
    }

}
