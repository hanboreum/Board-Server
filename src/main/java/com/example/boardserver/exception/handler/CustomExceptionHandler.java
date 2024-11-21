package com.example.boardserver.exception.handler;

import com.example.boardserver.dto.response.CommonResponse;
import com.example.boardserver.exception.BoardServerException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice // 공통적으로 예외처리를 하기 위한 어노테이션
public class CustomExceptionHandler {

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleException(Exception e) {
        CommonResponse commonResponse = new CommonResponse(HttpStatus.OK, "Exception",
                e.getMessage(), e.getMessage());
        return new ResponseEntity<>(commonResponse, commonResponse.getStatus());
    }

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<Object> handleRuntimeException(RuntimeException e) {
        CommonResponse commonResponse = new CommonResponse(HttpStatus.OK, "Run time Exception",
                e.getMessage(), e.getMessage());
        return new ResponseEntity<>(commonResponse, new HttpHeaders(), commonResponse.getStatus());
    }

    @ExceptionHandler({BoardServerException.class})
    public ResponseEntity<Object> handleBoardServerException(BoardServerException e) {
        CommonResponse response = new CommonResponse(HttpStatus.OK, "BoardServerException",
                e.getMessage(), e.getMessage());
        return new ResponseEntity<>(response, new HttpHeaders(), response.getStatus());
    }
}
