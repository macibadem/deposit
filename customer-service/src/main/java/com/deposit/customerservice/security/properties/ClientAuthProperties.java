package com.deposit.customerservice.security.properties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@NoArgsConstructor
@ConfigurationProperties(prefix = "client.auth", ignoreUnknownFields = false)
public class ClientAuthProperties {

    @NotBlank
    private String key;

    @NotEmpty
    private Map<String, String> properties;
}

