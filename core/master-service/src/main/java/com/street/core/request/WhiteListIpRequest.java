package com.street.core.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WhiteListIpRequest {
    @NotEmpty(message = "IP_ADDRESS_IS_REQUIRED")
    @NotNull(message = "IP_ADDRESS_IS_REQUIRED")
    String ipAddress;

    @NotEmpty(message = "LOCATION_IS_REQUIRED")
    @NotNull(message = "LOCATION_IS_REQUIRED")
    String location;
}
