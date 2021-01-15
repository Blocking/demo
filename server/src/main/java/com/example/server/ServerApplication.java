package com.example.server;

import com.example.server.netty.StartNettyServerEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author zhangxiaoyu
 */
@Slf4j
@SpringBootApplication
@ConfigurationPropertiesScan("com.example.server.config")
public class ServerApplication {

    public static void main(String[] args) throws UnknownHostException {
        final ConfigurableApplicationContext context = SpringApplication.run(ServerApplication.class, args);
        final Environment env = context.getEnvironment();
        log.info("\n----------------------------------------------------------\n\t" +
                        "Application '{}' is running! Access URLs:\n\t" +
                        "Local: \t\thttp://127.0.0.1:{}\n\t" +
                        "External: \thttp://{}:{}\n----------------------------------------------------------",
                env.getProperty("spring.application.name"),
                env.getProperty("server.port"),
                InetAddress.getLocalHost().getHostAddress(),
                env.getProperty("server.port"));
        context.publishEvent(new StartNettyServerEvent(context));
    }

}
