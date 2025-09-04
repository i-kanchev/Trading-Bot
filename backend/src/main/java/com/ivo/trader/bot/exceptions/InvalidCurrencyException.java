package com.ivo.trader.bot.exceptions;

public class InvalidCurrencyException extends RuntimeException {
    public InvalidCurrencyException(String message) {
        super(message);
    }

    public InvalidCurrencyException(String message, Throwable e) {
        super(message, e);
    }
}
