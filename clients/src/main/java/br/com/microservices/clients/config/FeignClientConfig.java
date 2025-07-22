package br.com.microservices.clients.config;

import feign.Retryer;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
public class FeignClientConfig {

    @Bean
    public ErrorDecoder errorDecoder() {
        return new FeignErrorDecode();
    }

    @Bean
    public Retryer retryer() {
        Retryer.Default retryer = new Retryer.Default(100, TimeUnit.SECONDS.toSeconds(2), 3);
        return new FeignRetryable.AnnotationRetryable(retryer);

    }
}
