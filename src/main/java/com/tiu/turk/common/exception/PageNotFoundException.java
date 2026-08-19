package com.tiu.turk.common.exception;

public class PageNotFoundException
extends RuntimeException {
    public PageNotFoundException(String message) {
        super(message);
    }
}

