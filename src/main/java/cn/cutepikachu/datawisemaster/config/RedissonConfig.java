package cn.cutepikachu.datawisemaster.config;

import lombok.Data;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Redisson 配置
 *
 * @author 笨蛋皮卡丘
 * @version 1.0
 */
@Configuration
@Data
@ConfigurationProperties("spring.redis")
public class RedissonConfig {

    private String host;
    private String port;
    private String username;
    private String password;
    private int database;

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        String redisAddress = String.format("redis://%s:%s", host, port);
        config.useSingleServer()
                .setAddress(redisAddress)
                .setDatabase(database);
        return Redisson.create(config);

    }

}
