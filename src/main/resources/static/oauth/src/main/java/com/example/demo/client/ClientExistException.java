package com.example.demo.client;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.CONFLICT;

/**
 * @author wangbin
 */
@ResponseStatus(CONFLICT)
public class ClientExistException extends RuntimeException {
}
