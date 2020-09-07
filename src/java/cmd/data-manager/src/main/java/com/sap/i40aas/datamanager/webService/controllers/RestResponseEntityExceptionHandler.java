package com.sap.i40aas.datamanager.webService.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler
  extends ResponseEntityExceptionHandler {

// works at the @Controller level –
// define a method to handle exceptions, and annotate that with
// @ExceptionHandler:

  @ExceptionHandler(value
    = {java.util.NoSuchElementException.class})
  protected ResponseEntity<Object> handleNoResourceFound(
    RuntimeException ex, WebRequest request) {
    String bodyOfResponse = "Requested resource not found";
    return handleExceptionInternal(ex, bodyOfResponse,
      new HttpHeaders(), HttpStatus.NOT_FOUND, request);
  }

  @Override
  protected ResponseEntity<Object> handleMissingServletRequestParameter(
    MissingServletRequestParameterException ex, HttpHeaders headers,
    HttpStatus status, WebRequest request) {
    String name = ex.getParameterName();
    System.out.println(name + " parameter is missing");
    // Actual exception handling
    String bodyOfResponse = name + " parameter is missing";
    return handleExceptionInternal(ex, bodyOfResponse,
      new HttpHeaders(), HttpStatus.NOT_FOUND, request);
  }


//  @ExceptionHandler(value = MissingServletRequestParameterException.class)
//  protected ResponseEntity<Object> handleMissingParams(MissingServletRequestParameterException ex, WebRequest request) {
//    String name = ex.getParameterName();
//    System.out.println(name + " parameter is missing");
//    // Actual exception handling
//    String bodyOfResponse = name + " parameter is missing";
//    return handleExceptionInternal(ex, bodyOfResponse,
//      new HttpHeaders(), HttpStatus.NOT_FOUND, request);
//  }

  //  in case a PUT method tries to access existing resource
  @ExceptionHandler(value
    = {DuplicateResourceException.class})
  protected ResponseEntity<Object> handleDuplicateResourceFound(
    RuntimeException ex, WebRequest request) {
    String bodyOfResponse = "Requested resource already in Database";
    return handleExceptionInternal(ex, bodyOfResponse,
      new HttpHeaders(), HttpStatus.CONFLICT, request);
  }
}
