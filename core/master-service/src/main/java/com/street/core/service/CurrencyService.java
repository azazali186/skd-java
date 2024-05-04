package com.street.core.service;


import com.street.core.entity.CurrencyEntity;
import com.street.core.entity.response.UserEntityResponse;
import com.street.core.enums.JwtTokenUser;
import com.street.core.enums.LoginUserUtils;
import com.street.core.repository.CurrencyRepo;
import com.street.core.repository.UserRepo;
import com.street.core.request.CurrencyRequest;
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
public class CurrencyService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CurrencyService.class);

    @Autowired
    private CurrencyRepo currencyRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private LoginUserUtils loginUserUtils;

    public ApiResponse getAll(String name, Integer pageSize, Integer pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "id"));
        Page data = currencyRepo.findAllCurrency(name, pageable);
        return ApiResponse.successPage(data.getContent(), data.getTotalElements());
    }

    public ApiResponse getById(Long id) {
        Optional<CurrencyEntity> currency = currencyRepo.findById(id);
        if (currency.isEmpty()) {
            return ApiResponse.failNotFound("INVALID_CURRENCY_ID");
        }
        return ApiResponse.success(currency.get());
    }

    public ApiResponse create(CurrencyRequest req) {


        CurrencyEntity currency = new CurrencyEntity();
        currency.setName(req.getName());
        currency.setCode(req.getCode());
        currency.setCreatedAt(new Date());
        currency.setStatus(true);
        currencyRepo.save(currency);
        return ApiResponse.success();
    }

    public ApiResponse update(Long id, CurrencyRequest req) {
        Optional<CurrencyEntity> currency = currencyRepo.findById(id);
        if (currency.isEmpty()) {
            return ApiResponse.failNotFound("INVALID_CURRENCY_ID");
        }

        currency.get().setCode(req.getCode());
        currency.get().setName(req.getName());
        currency.get().setUpdatedAt(new Date());
        currencyRepo.save(currency.get());

        return ApiResponse.success();
    }

}
