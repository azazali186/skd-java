package com.street.core.master_service.filters;


import com.alibaba.fastjson.JSON;
import com.amazonaws.util.StringUtils;
import com.street.core.master_service.entity.LoggerEntity;
import com.street.core.master_service.repository.LoggerRepo;
import com.street.core.master_service.utils.HelperUtil;
import com.street.core.master_service.utils.JwtUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
@Slf4j
public class LoggerFilter extends OncePerRequestFilter {

    @Resource
    private LoggerRepo loggerRepo;

    @Autowired
    private HelperUtil helperUtil;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;


    @Override
    protected void doFilterInternal(HttpServletRequest servletRequest, HttpServletResponse servletResponse, FilterChain filterChain) throws ServletException, IOException {
        ContentCachingRequestWrapper requestWrapper = requestWrapper(servletRequest);
        ContentCachingResponseWrapper responseWrapper = responseWrapper(servletResponse);

        filterChain.doFilter(requestWrapper, responseWrapper);


        String requestedBy = helperUtil.getUserIPAddress();
        String jwtTokenUser = jwtRequestFilter.getToken(requestWrapper);
        if (!StringUtils.isNullOrEmpty(jwtTokenUser)) {
            requestedBy = jwtUtil.extractUsername(jwtTokenUser);
        }

        LoggerEntity loggerEntity = new LoggerEntity();
        loggerEntity.setUrl(requestWrapper.getRequestURI());
        loggerEntity.setClientAddress(requestWrapper.getRemoteAddr());
        loggerEntity.setHostname(requestWrapper.getServerName());
        loggerEntity.setIpAddress(helperUtil.getUserIPAddress());
        loggerEntity.setMethod(requestWrapper.getMethod());
        loggerEntity.setUserAgent(requestWrapper.getHeader("User-Agent"));
        loggerEntity.setRequestBody(logRequest(requestWrapper));
        loggerEntity.setResponseBody(logResponse(responseWrapper));
        loggerEntity.setStatus(responseWrapper.getStatus());
        loggerEntity.setRequestedBy(requestedBy);
        loggerEntity.setCreatedAt(new Date());


        if (!requestWrapper.getRequestURI().contains("index.html") && !requestWrapper.getRequestURI().contains("swagger") &&
                !requestWrapper.getRequestURI().contains("api-docs")) {
            loggerRepo.save(loggerEntity);
        }
    }


    private String logRequest(ContentCachingRequestWrapper request) {
        StringBuilder builder = new StringBuilder();
        String requestBody = getRequestParameters(request);
        builder.append(requestBody).append("\n");
        log.info("Request Body: {}", builder);
        return builder.toString();
    }

    private String logResponse(ContentCachingResponseWrapper response) throws IOException {
        StringBuilder builder = new StringBuilder();
        builder.append(new String(response.getContentAsByteArray()));
        log.info("Response Body: {}", builder);
        response.copyBodyToResponse();
        return builder.toString();
    }

    public static String getRequestParameters(ContentCachingRequestWrapper request) {
        if ("POST".equalsIgnoreCase(request.getMethod()) || "PUT".equalsIgnoreCase(request.getMethod())
                || "PATCH".equalsIgnoreCase(request.getMethod())) {
            StringBuilder stringBuilder = new StringBuilder();
            Map<String, String[]> parameterMap = request.getParameterMap();

            for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
                String key = entry.getKey();
                String[] values = entry.getValue();
                if (!key.contains("password")) {
                    stringBuilder.append(key).append("=[");

                    for (int i = 0; i < values.length; i++) {
                        stringBuilder.append(values[i]);
                        if (i < values.length - 1) {
                            stringBuilder.append(", ");
                        }
                    }
                    stringBuilder.append("] ");
                }
            }

            String data = stringBuilder.toString();

            if (StringUtils.isNullOrEmpty(data)) {
                String requestBody = new String(request.getContentAsByteArray(), StandardCharsets.UTF_8);
                if (requestBody.startsWith("{") && requestBody.endsWith("}")) {
                    Map<String, Object> mapObj = HelperUtil.getInstance().objectToMap(JSON.parse(requestBody));
                    List<String> keysToRemove = List.of("password");
                    keysToRemove.forEach(mapObj::remove);
                    data = mapObj.toString();
                } else {
                    data = requestBody;
                }
            }

            return data;
        }

        return request.getQueryString() == null ? "" : request.getQueryString();
    }

    private ContentCachingRequestWrapper requestWrapper(ServletRequest request) {
        if (request instanceof HttpServletRequest) {
            return new ContentCachingRequestWrapper((HttpServletRequest) request);
        }
        return null;
    }

    private ContentCachingResponseWrapper responseWrapper(ServletResponse response) {
        if (response instanceof HttpServletResponse) {
            return new ContentCachingResponseWrapper((HttpServletResponse) response);
        }
        return null;
    }

    @Override
    public void destroy() {
    }
}
