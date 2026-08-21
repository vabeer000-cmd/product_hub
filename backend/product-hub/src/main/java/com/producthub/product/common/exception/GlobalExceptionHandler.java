package com.producthub.product.common.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidationException(
			MethodArgumentNotValidException exception){
		
		Map<String, String> errors = new HashMap<String, String>();
		
		exception.getBindingResult()
				.getFieldErrors()
				.forEach(error -> 
						errors.put(error.getField(),
								   error.getDefaultMessage()
								   )
						);
		
		ErrorResponse response = new ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), "Validation Failed", 
				"Request validation failed", null, errors);
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}
	

}
