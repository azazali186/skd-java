package com.street.core.master_service.service;


import com.street.core.master_service.entity.WhiteListIpEntity;
import com.street.core.master_service.entity.response.UserEntityResponse;
import com.street.core.master_service.enums.JwtTokenUser;
import com.street.core.master_service.enums.LoginUserUtils;
import com.street.core.master_service.repository.UserRepo;
import com.street.core.master_service.repository.WhiteListIPRepo;
import com.street.core.master_service.request.WhiteListIpRequest;
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
import java.util.List;
import java.util.Optional;

@Service
public class WhiteListIpService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WhiteListIpService.class);

    @Autowired
    private WhiteListIPRepo whiteListRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private LoginUserUtils loginUserUtils;

    public ApiResponse getAll(String ipAddress, Integer pageSize, Integer pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "id"));
        Page data = whiteListRepo.findAllIpList(ipAddress, pageable);
        return ApiResponse.successPage(data.getContent(), data.getTotalElements());
    }

    public ApiResponse getById(Long id) {
        Optional<WhiteListIpEntity> whiteList = whiteListRepo.findById(id);
        if (whiteList.isEmpty()) {
            return ApiResponse.failNotFound("INVALID_WHITE_LIST_IP_ID");
        }
        return ApiResponse.success(whiteList.get());
    }

    public ApiResponse create(WhiteListIpRequest req) {

        JwtTokenUser tokenUser = loginUserUtils.getTokenUser();
        Optional<UserEntityResponse> user = userRepo.findById(tokenUser.getId());
        UserEntityResponse createdBy = user.orElse(null);

        WhiteListIpEntity whiteList = new WhiteListIpEntity();
        whiteList.setIpAddress(req.getIpAddress());
        whiteList.setLocation(req.getLocation());
        whiteList.setCreatedAt(new Date());
        whiteList.setCreatedBy(createdBy);
        whiteList.setStatus(true);
        whiteListRepo.save(whiteList);
        return ApiResponse.success();
    }

    public ApiResponse update(Long id, WhiteListIpRequest req) {
        Optional<WhiteListIpEntity> whiteList = whiteListRepo.findById(id);
        if (whiteList.isEmpty()) {
            return ApiResponse.failNotFound("INVALID_WHITE_LIST_IP_ID");
        }
        JwtTokenUser tokenUser = loginUserUtils.getTokenUser();
        Optional<UserEntityResponse> user = userRepo.findById(tokenUser.getId());
        UserEntityResponse updatedBy = user.orElse(null);

        if(req.getIpAddress()!=null)
        whiteList.get().setIpAddress(req.getIpAddress());
        if(req.getLocation()!=null)
        whiteList.get().setLocation(req.getLocation());

        whiteList.get().setUpdatedAt(new Date());
        whiteList.get().setUpdatedBy(updatedBy);
        whiteListRepo.save(whiteList.get());

        return ApiResponse.success();
    }

}
