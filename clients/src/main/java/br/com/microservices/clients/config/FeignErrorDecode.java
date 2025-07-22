package br.com.microservices.clients.config;

import feign.FeignException;
import feign.Response;
import feign.RetryableException;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FeignErrorDecode implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        log.error("FeignErrorDecode error occurred: status={}, reason={}", response.status(), response.reason());

        if (response.status() >= 500) {
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
