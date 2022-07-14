package com.js.order_api.exceptions;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionGlobalHandler {

	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<?> handleNotFoundException(NotFoundException e) {
		var detail = new ExceptionDetail();

		detail.setStatus(HttpStatus.NOT_FOUND.value());
		detail.setDevMessage(e.getMessage());
		detail.setMensagem("NOT FOUND");

		return new ResponseEntity<>(detail, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(DataNotInformedException.class)
	public ResponseEntity<?> handleDataNotInformedException(DataNotInformedException e) {
		var detail = new ExceptionDetail();

		detail.setStatus(HttpStatus.BAD_REQUEST.value());
		detail.setMensagem("Invalid Field");
		detail.setDevMessage(e.getMessage());

		return new ResponseEntity<>(detail, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ValidationException.class)
	public ResponseEntity<?> handleValidationException(ValidationException e) {
		var detail = new ExceptionDetail();

		detail.setStatus(HttpStatus.BAD_REQUEST.value());
		detail.setMensagem("Invalid Field");
		detail.setDevMessage(e.getMessage());
		detail.setTimestamp(LocalDateTime.now());

		return new ResponseEntity<>(detail, HttpStatus.BAD_REQUEST);
	}
}
