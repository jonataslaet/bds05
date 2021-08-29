package com.devsuperior.movieflix.controllers.exceptions;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.devsuperior.movieflix.services.exceptions.DatabaseException;
import com.devsuperior.movieflix.services.exceptions.ForbiddenException;
import com.devsuperior.movieflix.services.exceptions.UnauthorizedException;

@ControllerAdvice
public class ResourceExceptionHandler {

	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<StandardError> entityNotFound(EntityNotFoundException e, HttpServletRequest req){
		StandardError error = new StandardError();
		error.setError("Entity not found");
		error.setMessage(e.getLocalizedMessage());
		error.setPath(req.getServletPath());
		error.setStatus(HttpStatus.NOT_FOUND.value());
		return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(error);
	}
	
	@ExceptionHandler(DatabaseException.class)
	public ResponseEntity<StandardError> databaseException(DatabaseException e, HttpServletRequest req){
		StandardError error = new StandardError();
		error.setError("Data restriction is being violated");
		error.setMessage(e.getLocalizedMessage());
		error.setPath(req.getServletPath());
		error.setStatus(HttpStatus.BAD_REQUEST.value());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(error);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ValidationError> validationException(MethodArgumentNotValidException e, HttpServletRequest req){
		ValidationError error = new ValidationError();
		error.setError("Field validation");
		error.setMessage(e.getLocalizedMessage());
		error.setPath(req.getServletPath());
		error.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
		
		for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
			error.addError(fieldError.getField(), fieldError.getDefaultMessage());
		}
		
		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY.value()).body(error);
	}
	
	@ExceptionHandler(ForbiddenException.class)
	public ResponseEntity<OAuthCustomError> forbidden(ForbiddenException e, HttpServletRequest req){
		OAuthCustomError error = new OAuthCustomError("Forbidden", e.getLocalizedMessage());
		return ResponseEntity.status(HttpStatus.FORBIDDEN.value()).body(error);
	}
	
	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<OAuthCustomError> unauthorized(UnauthorizedException e, HttpServletRequest req){
		OAuthCustomError error = new OAuthCustomError("Unauthorized", e.getLocalizedMessage());
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value()).body(error);
	}
}
