package com.example.server.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zhangxiaoyu
 * @date 2021/1/14
 */
@Data
@ConfigurationProperties(prefix = "netty.server")
public class NettyServerProperties {
    private String address;
    private int port;
}
