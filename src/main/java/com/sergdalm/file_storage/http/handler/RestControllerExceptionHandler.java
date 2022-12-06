package com.sergdalm.file_storage.http.handler;

import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice(basePackages = "com.sergdalm.file_storage.http.rest")
public class RestControllerExceptionHandler extends ResponseEntityExceptionHandler {
}
