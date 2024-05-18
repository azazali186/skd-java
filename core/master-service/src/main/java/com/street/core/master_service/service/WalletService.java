package com.street.core.master_service.service;

import com.street.core.master_service.entity.WalletEntity;
import com.street.core.master_service.entity.NetworkEntity;
import com.street.core.master_service.entity.response.UserEntityResponse;
import com.street.core.master_service.repository.WalletRepo;
import com.street.core.master_service.repository.NetworkRepo;
import com.street.core.master_service.repository.UserRepo;
import com.street.core.master_service.request.WalletRequest;
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
public class WalletService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WalletService.class);

    @Autowired
    private WalletRepo walletRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private NetworkRepo networkRepo;

    public ApiResponse getAll(String name, Integer pageSize, Integer pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "id"));
        Page data = walletRepo.findAllWallet(name, pageable);
        return ApiResponse.successPage(data.getContent(), data.getTotalElements());
    }

    public ApiResponse getById(Long id) {
        Optional<WalletEntity> wallet = walletRepo.findById(id);
        if (wallet.isEmpty()) {
            return ApiResponse.failNotFound("INVALID_CRYPTO_ID");
        }
        return ApiResponse.success(wallet.get());
    }

    public ApiResponse create(WalletRequest req) {

        if (req.getCode() == null || req.getName() == null || req.getAddress() == null
                || req.getNetwork() == null || req.getUser() == null) {
            return ApiResponse.error(400, "BAD_REQUEST_ALL_DATA_NEEDED");
        }

        Optional<WalletEntity> walletOptional = walletRepo.findByAddress(req.getAddress());
        if (walletOptional.isPresent()) {
            return ApiResponse.failNotFound("INVALID_DATA_CRYPTO_ADDRESS_ALREADY_EXIST");
        }

        Optional<NetworkEntity> networkOptional = networkRepo.findById(req.getNetwork());

        if (networkOptional.isEmpty()) {
            return ApiResponse.failNotFound("INVALID_NETWORK_ID");
        }

        Optional<UserEntityResponse> optionalUser = userRepo.findById(req.getUser());

        if (optionalUser.isEmpty()) {
            return ApiResponse.failNotFound("INVALID_USER_ID");
        }

        WalletEntity wallet = new WalletEntity();
        wallet.setName(req.getName());
        wallet.setCode(req.getCode());
        wallet.setAddress(req.getAddress());
        wallet.setNetwork(networkOptional.get());
        wallet.setCreatedAt(new Date());
        wallet.setUser(optionalUser.get());
        wallet.setStatus(true);
        walletRepo.save(wallet);
        return ApiResponse.success();
    }

    public ApiResponse update(Long id, WalletRequest req) {
        Optional<WalletEntity> wallet = walletRepo.findById(id);
        if (wallet.isEmpty()) {
            return ApiResponse.failNotFound("INVALID_CRYPTO_ID");
        }

        if (req.getCode() != null)
            wallet.get().setCode(req.getCode());

        if (req.getName() != null)
            wallet.get().setName(req.getName());

        if (req.getAddress() != null)
            wallet.get().setAddress(req.getAddress());

        if (req.getStatus() != null)
            wallet.get().setStatus(req.getStatus());

        if (req.getNetwork() != null) {
            Optional<NetworkEntity> networkOptional = networkRepo.findById(req.getNetwork());

            if (networkOptional.isEmpty()) {
                return ApiResponse.failNotFound("INVALID_NETWORK_ID");
            }
            wallet.get().setNetwork(networkOptional.get());
        }

        if (req.getUser() != null) {
            Optional<UserEntityResponse> optionalUser = userRepo.findById(req.getUser());

            if (optionalUser.isEmpty()) {
                return ApiResponse.failNotFound("INVALID_USER_ID");
            }
            wallet.get().setUser(optionalUser.get());
        }

        wallet.get().setUpdatedAt(new Date());
        walletRepo.save(wallet.get());

        return ApiResponse.success();
    }

}
