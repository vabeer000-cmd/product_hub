package com.producthub.product.common.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
//	@ExceptionHandler(MethodArgumentNotValidException.class)
//	public ResponseEntity<ErrorResponse> handleValidationException(
//			MethodArgumentNotValidException exception){
//		
//		Map<String, String> errors = new HashMap<String, String>();
//		
//		exception.getBindingResult()
//				.getFieldErrors()
//				.forEach(error -> 
//						errors.put(error.getField(),
//								   error.getDefaultMessage()
//								   )
//						);
//		
//		ErrorResponse response = new ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), "Validation Failed", 
//				"Request validation failed",
//				null, 
//				errors);
//		
//		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
//	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidationException(
	        MethodArgumentNotValidException exception,
	        HttpServletRequest request) {

	    Map<String, String> errors = new HashMap<>();

	    exception.getBindingResult()
	            .getFieldErrors()
	            .forEach(error ->
	                    errors.put(
	                            error.getField(),
	                            error.getDefaultMessage()
	                    )
	            );

	    ErrorResponse response = new ErrorResponse(
	            LocalDateTime.now(),
	            HttpStatus.BAD_REQUEST.value(),
	            "Validation Failed",
	            "Request validation failed",
	            request.getRequestURI(),
	            errors
	    );

	    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	            .body(response);
	}
	
//	  @ExceptionHandler(ProductNotFoundException.class)
//	public ResponseEntity<ErrorResponse> handleProductNotFoundException(ProductNotFoundException exception){
//		
//		ErrorResponse response = new ErrorResponse(LocalDateTime.now(),
//				HttpStatus.NOT_FOUND.value(),
//				"Product Not Found",
//				 exception.getMessage(),
//				 null, null);
//		
//		return ResponseEntity.status(HttpStatus.NOT_FOUND)
//				.body(response);
//	}
	
	@ExceptionHandler(ProductNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleProductNotFoundException(
	        ProductNotFoundException exception,
	        HttpServletRequest request) {

	    ErrorResponse response = new ErrorResponse(
	            LocalDateTime.now(),
	            HttpStatus.NOT_FOUND.value(),
	            "Product Not Found",
	            exception.getMessage(),
	            request.getRequestURI(),
	            null
	    );

	    return ResponseEntity.status(HttpStatus.NOT_FOUND)
	            .body(response);
	}

}
