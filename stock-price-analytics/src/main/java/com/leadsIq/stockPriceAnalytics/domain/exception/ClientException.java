package com.leadsIq.stockPriceAnalytics.domain.exception;

import lombok.Getter;

@Getter
public class ClientException extends RuntimeException {
    private final String message;

    public ClientException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
    }
}
