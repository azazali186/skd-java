package com.street.core.master_service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.street.core.master_service.entity.AcceptedCurrenciesEntity;
import com.street.core.master_service.enums.CurrencyTypeEnum;
import com.street.core.master_service.response.AcceptedCurrenciesResponse;

import java.util.List;

public interface AcceptedCurrenciesRepo extends JpaRepository<AcceptedCurrenciesEntity, Long> {

    @Query("SELECT new com.street.core.master_service.response.AcceptedCurrenciesResponse(" +
            "currency.id, " +
            "currency.currencyType, " +
            "currency.currencyTypeId, " +
            "currency.status, " +
            "currency.createdAt, " +
            "currency.updatedAt, " +
            "COALESCE(fiat.name, crypto.name), " +
            "COALESCE(fiat.code, crypto.code), " +
            "crypto.symbol, " +
            "crypto.decimal, " +
            "crypto.contract, " +
            "crypto.network) " +
            "FROM AcceptedCurrenciesEntity currency " +
            "LEFT JOIN CurrencyEntity fiat ON currency.currencyTypeId = fiat.id AND currency.currencyType = 'FIAT' " +
            "LEFT JOIN CryptoEntity crypto ON currency.currencyTypeId = crypto.id AND currency.currencyType = 'CRYPTO' "
            +
            "WHERE (:type IS NULL OR currency.currencyType = :type)")
    Page findAllAcceptedCurrencies(
            @Param("type") CurrencyTypeEnum type, Pageable pageable);

    @Query("SELECT new com.street.core.master_service.response.AcceptedCurrenciesResponse(" +
            "currency.id, " +
            "currency.currencyType, " +
            "currency.currencyTypeId, " +
            "currency.status, " +
            "currency.createdAt, " +
            "currency.updatedAt, " +
            "COALESCE(fiat.name, crypto.name), " +
            "COALESCE(fiat.code, crypto.code), " +
            "crypto.symbol, " +
            "crypto.decimal, " +
            "crypto.contract, " +
            "crypto.network) " +
            "FROM AcceptedCurrenciesEntity currency " +
            "LEFT JOIN CurrencyEntity fiat ON currency.currencyTypeId = fiat.id AND currency.currencyType = 'FIAT' " +
            "LEFT JOIN CryptoEntity crypto ON currency.currencyTypeId = crypto.id AND currency.currencyType = 'CRYPTO' "
            +
            "WHERE (:type IS NULL OR currency.currencyType = :type)")
    List<AcceptedCurrenciesResponse> getAllList(
            @Param("type") CurrencyTypeEnum type);

}
