package br.com.microservices.clients.config.feign;

import feign.FeignException;
import feign.Response;
import feign.RetryableException;
import feign.codec.ErrorDecoder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

@Slf4j
public class FeignErrorDecode implements ErrorDecoder {

    @SneakyThrows
    @Override
    public Exception decode(String methodKey, Response response) {
        log.error("MethodKey: {}", methodKey);
        log.error("Method Http: {}", response.request().httpMethod());
        log.error("Method Url: {}", response.request().url());
        log.error("Method Headers: {}", response.request().headers());
        log.error("Status: {}", response.status());

        if (response.body() != null) {
            log.error("body: {}", new String(response.body().asInputStream().readAllBytes(), StandardCharsets.UTF_8));
        }

        if (response.status() >= 500) {
            log.debug("RetryableException activated");
            return new RetryableException(
                    response.status(),
                    "Feign error occurred",
                    response.request().httpMethod(),
                    null,
                    response.request()
            );
        }

        return FeignException.errorStatus(methodKey, response);
    }
}
