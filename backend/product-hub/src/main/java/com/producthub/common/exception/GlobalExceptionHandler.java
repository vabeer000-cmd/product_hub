package com.producthub.common.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.persistence.OptimisticLockException;
import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	private static final Logger log =
	        LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
	
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

	    log.warn(
	            "Validation failed for request: {}. Errors: {}",
	            request.getRequestURI(),
	            errors
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
	

	
	@ExceptionHandler(ProductNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleProductNotFoundException(
	        ProductNotFoundException exception,
	        HttpServletRequest request) {

		log.warn(
		        "Product not found. Message: {}, Path: {}",
		        exception.getMessage(),
		        request.getRequestURI()
		);
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
	
	@ExceptionHandler(OptimisticLockException.class)
	public ResponseEntity<ErrorResponse> handleOptimisticLockException(
	        OptimisticLockException exception,
	        HttpServletRequest request) {
		
		log.warn(
		        "Optimistic lock conflict. Path: {}, Message: {}",
		        request.getRequestURI(),
		        exception.getMessage()
		);

	    ErrorResponse response = new ErrorResponse(
	            LocalDateTime.now(),
	            HttpStatus.CONFLICT.value(),
	            "Optimistic Lock Conflict",
	            exception.getMessage(),
	            request.getRequestURI(),
	            null
	    );

	    return ResponseEntity.status(HttpStatus.CONFLICT)
	            .body(response);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGenericException(
	        Exception exception,
	        HttpServletRequest request) {

		 log.error(
		            "Unexpected error occurred while processing request: {}",
		            request.getRequestURI(),
		            exception
		    );

		 
	    ErrorResponse response = new ErrorResponse(
	            LocalDateTime.now(),
	            HttpStatus.INTERNAL_SERVER_ERROR.value(),
	            "Internal Server Error",
	            "An unexpected error occurred",
	            request.getRequestURI(),
	            null
	    );

	    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	            .body(response);
	}
	
	@ExceptionHandler(InvalidCursorException.class)
	public ResponseEntity<ErrorResponse> handleInvalidCursor(
				InvalidCursorException ex,
				HttpServletRequest request){
		
		log.warn("Invalid cursor. path: {}",request.getRequestURI());
		
		ErrorResponse error = new ErrorResponse(
				LocalDateTime.now(), 
				HttpStatus.BAD_REQUEST.value(),
				"Invalid Cursor", 
				ex.getMessage(), 
				request.getRequestURI(),
				null);
		
		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(error);
	}

	@ExceptionHandler(InvalidCredentialsException.class)
	public ResponseEntity<ErrorResponse> handleInvalidCredentials(
	        InvalidCredentialsException ex,
	        HttpServletRequest request) {

	    log.warn(
	        "Invalid login attempt for path: {}",
	        request.getRequestURI()
	    );

	    ErrorResponse response = new ErrorResponse(
	        LocalDateTime.now(),
	        HttpStatus.UNAUTHORIZED.value(),
	        "Unauthorized",
	        ex.getMessage(),
	        request.getRequestURI(),
	        null
	    );

	    return ResponseEntity
	            .status(HttpStatus.UNAUTHORIZED)
	            .body(response);
	}
	
	@ExceptionHandler(AuthorizationDeniedException.class)
	public ResponseEntity<ErrorResponse> handleAuthorizationDeniedException(
	        AuthorizationDeniedException exception,
	        HttpServletRequest request) {

	    log.warn(
	            "Access denied. Path: {}, Message: {}",
	            request.getRequestURI(),
	            exception.getMessage()
	    );

	    ErrorResponse response = new ErrorResponse(
	            LocalDateTime.now(),
	            HttpStatus.FORBIDDEN.value(),
	            "Forbidden",
	            "You do not have permission to access this resource",
	            request.getRequestURI(),
	            null
	    );

	    return ResponseEntity
	            .status(HttpStatus.FORBIDDEN)
	            .body(response);
	}
	
	@ExceptionHandler(InvalidRefreshTokenException.class)
	public ResponseEntity<ErrorResponse> handleInvalidRefreshTokenException(
	        InvalidRefreshTokenException exception,
	        HttpServletRequest request) {

	    ErrorResponse errorResponse = new ErrorResponse(
	            LocalDateTime.now(),
	            HttpStatus.UNAUTHORIZED.value(),
	            "Unauthorized",
	            "Invalid or expired refresh token",
	            request.getRequestURI(),
	            null
	    );

	    return ResponseEntity
	            .status(HttpStatus.UNAUTHORIZED)
	            .body(errorResponse);
	}
	
	@ExceptionHandler(UsernameAlreadyExistsException.class)
	public ResponseEntity<ErrorResponse> handleUsernameAlreadyExists(
	        UsernameAlreadyExistsException ex,
	        HttpServletRequest request) {

	    log.warn(
	        "Registration failed: username already exists, path: {}",
	        request.getRequestURI()
	    );

	    ErrorResponse response = new ErrorResponse(
	        LocalDateTime.now(),
	        HttpStatus.CONFLICT.value(),
	        "Conflict",
	        ex.getMessage(),
	        request.getRequestURI(),
	        null
	    );

	    return ResponseEntity
	            .status(HttpStatus.CONFLICT)
	            .body(response);
	}
}
