package org.jl.learnaicode.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@Data
@ConfigurationProperties(prefix = "common.cors")
public class CorsProperties {

    /** 是否启用跨域配置，默认 true */
    private boolean enabled = true;

    /** 允许的来源，默认允许所有 */
    private List<String> allowedOrigins = new ArrayList<>(List.of("*"));

    /** 允许的请求头，默认允许所有 */
    private List<String> allowedHeaders = new ArrayList<>(List.of("*"));

    /** 允许的方法 */
    private List<String> allowedMethods = new ArrayList<>(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

    /** 是否允许携带凭证 */
    private boolean allowCredentials = false;

    /** 预检请求的缓存时间（秒） */
    private long maxAge = 3600;
}
