package com.ivo.trader.bot.exceptions;

public class InsufficientAmountException extends Exception {
    public InsufficientAmountException(String message) {
        super(message);
    }

    public InsufficientAmountException(String message, Throwable e) {
        super(message, e);
    }
}
