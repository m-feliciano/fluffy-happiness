package br.com.microservices.clients.config;

import feign.RetryableException;
import feign.Retryer;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface FeignRetryable {

    class AnnotationRetryable implements Retryer {
        private final Retryer delegate;

        public AnnotationRetryable(Retryer retryer) {
            this.delegate = retryer;
        }

        @Override
        public void continueOrPropagate(RetryableException e) {

            if (e.request().requestTemplate().methodMetadata().method().getAnnotation(FeignRetryable.class) != null) {
                delegate.continueOrPropagate(e);
            } else {
                throw e;
            }
        }

        @Override
        public Retryer clone() {
            return new AnnotationRetryable(delegate.clone());
        }
    }
}
