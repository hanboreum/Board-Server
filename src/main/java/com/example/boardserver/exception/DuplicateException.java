package com.example.boardserver.exception;

public class DuplicateException extends RuntimeException {

    public DuplicateException(String msg){
        super(msg);
    }
}
