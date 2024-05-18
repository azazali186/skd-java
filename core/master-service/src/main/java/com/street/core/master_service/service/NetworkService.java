package com.street.core.master_service.service;

import com.street.core.master_service.entity.NetworkEntity;
import com.street.core.master_service.entity.response.UserEntityResponse;
import com.street.core.master_service.enums.JwtTokenUser;
import com.street.core.master_service.enums.LoginUserUtils;
import com.street.core.master_service.repository.NetworkRepo;
import com.street.core.master_service.repository.UserRepo;
import com.street.core.master_service.request.NetworkRequest;
import com.street.common.utils.ApiResponse;
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
public class NetworkService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NetworkService.class);

    @Autowired
    private NetworkRepo networkRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private LoginUserUtils loginUserUtils;

    public ApiResponse getAll(String name, Integer pageSize, Integer pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "id"));
        Page data = networkRepo.findAllNetwork(name, pageable);
        return ApiResponse.successPage(data.getContent(), data.getTotalElements());
    }

    public ApiResponse getById(Long id) {
        Optional<NetworkEntity> network = networkRepo.findById(id);
        if (network.isEmpty()) {
            return ApiResponse.failNotFound("INVALID_CRYPTO_ID");
        }
        return ApiResponse.success(network.get());
    }

    public ApiResponse create(NetworkRequest req) {
        if ( req.getName() == null) {
            return ApiResponse.error(400, "BAD_REQUEST_ALL_DATA_NEEDED");
        }

        Optional<NetworkEntity> networkOptional = networkRepo.findByName(req.getName());
        if (networkOptional.isPresent()) {
            return ApiResponse.failNotFound("INVALID_DATA_NAME_ALREADY_EXIST");
        }
        NetworkEntity network = new NetworkEntity();
        network.setName(req.getName());
        network.setCreatedAt(new Date());
        network.setStatus(true);
        networkRepo.save(network);
        return ApiResponse.success();
    }

    public ApiResponse update(Long id, NetworkRequest req) {
        Optional<NetworkEntity> network = networkRepo.findById(id);
        if (network.isEmpty()) {
            return ApiResponse.failNotFound("INVALID_CRYPTO_ID");
        }

        if (req.getName() != null)
            network.get().setName(req.getName());

        if (req.getStatus() != null)
            network.get().setStatus(req.getStatus());

        network.get().setUpdatedAt(new Date());
        networkRepo.save(network.get());

        return ApiResponse.success();
    }

}
