package com.street.core.master_service.service;

import com.street.core.master_service.entity.CryptoEntity;
import com.street.core.master_service.entity.NetworkEntity;
import com.street.core.master_service.entity.response.UserEntityResponse;
import com.street.core.master_service.enums.JwtTokenUser;
import com.street.core.master_service.enums.LoginUserUtils;
import com.street.core.master_service.repository.CryptoRepo;
import com.street.core.master_service.repository.NetworkRepo;
import com.street.core.master_service.repository.UserRepo;
import com.street.core.master_service.request.CryptoRequest;
import com.street.core.auth_service.response.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class CryptoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CryptoService.class);

    @Autowired
    private CryptoRepo cryptoRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private NetworkRepo networkRepo;

    @Autowired
    private LoginUserUtils loginUserUtils;

    public ApiResponse getAll(String name, Integer pageSize, Integer pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "id"));
        Page data = cryptoRepo.findAllCrypto(name, pageable);
        return ApiResponse.successPage(data.getContent(), data.getTotalElements());
    }

    public ApiResponse getById(Long id) {
        Optional<CryptoEntity> crypto = cryptoRepo.findById(id);
        if (crypto.isEmpty()) {
            return ApiResponse.failNotFound("INVALID_CRYPTO_ID");
        }
        return ApiResponse.success(crypto.get());
    }

    public ApiResponse create(CryptoRequest req) {

        if (req.getCode() == null || req.getName() == null || req.getSymbol() == null || req.getContract() == null
                || req.getNetwork() == null) {
            return ApiResponse.error(400, "BAD_REQUEST_ALL_DATA_NEEDED");
        }

        Optional<CryptoEntity> cryptoOptional = cryptoRepo.findByContractAndSymbol(req.getContract(), req.getSymbol());
        if (cryptoOptional.isPresent()) {
            return ApiResponse.failNotFound("INVALID_DATA_CRYPTO_CONTRACT_AND_SYMBOL_ALREADY_EXIST");
        }

        Optional<NetworkEntity> networkOptional = networkRepo.findById(req.getNetwork());

        if (networkOptional.isEmpty()) {
            return ApiResponse.failNotFound("INVALID_NETWORK_ID");
        }

        CryptoEntity crypto = new CryptoEntity();
        crypto.setName(req.getName());
        crypto.setCode(req.getCode());
        crypto.setSymbol(req.getSymbol());
        crypto.setContract(req.getContract());
        crypto.setNetwork(networkOptional.get());
        crypto.setCreatedAt(new Date());
        crypto.setStatus(true);
        cryptoRepo.save(crypto);
        return ApiResponse.success();
    }

    public ApiResponse update(Long id, CryptoRequest req) {
        Optional<CryptoEntity> crypto = cryptoRepo.findById(id);
        if (crypto.isEmpty()) {
            return ApiResponse.failNotFound("INVALID_CRYPTO_ID");
        }

        if (req.getCode() != null)
            crypto.get().setCode(req.getCode());

        if (req.getName() != null)
            crypto.get().setName(req.getName());

        if (req.getSymbol() != null)
            crypto.get().setSymbol(req.getSymbol());

        if (req.getContract() != null)
            crypto.get().setContract(req.getContract());

        if (req.getStatus() != null)
            crypto.get().setStatus(req.getStatus());

        if (req.getNetwork() != null) {
            Optional<NetworkEntity> networkOptional = networkRepo.findById(req.getNetwork());

            if (networkOptional.isEmpty()) {
                return ApiResponse.failNotFound("INVALID_NETWORK_ID");
            }
            crypto.get().setNetwork(networkOptional.get());
        }

        crypto.get().setUpdatedAt(new Date());
        cryptoRepo.save(crypto.get());

        return ApiResponse.success();
    }

}
