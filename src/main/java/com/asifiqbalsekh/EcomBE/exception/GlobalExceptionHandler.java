package com.asifiqbalsekh.EcomBE.exception;

import com.asifiqbalsekh.EcomBE.dto.GlobalExceptionResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<GlobalExceptionResponseDTO> handleMultipartException(MultipartException ex) {
        var response= new GlobalExceptionResponseDTO("Request must be contain image param with a valid file.",
                getTimestamp(), ex.getStackTrace()[0].toString());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<GlobalExceptionResponseDTO> validationExceptionHandler(MethodArgumentNotValidException ex) {
        String message=ex.getBindingResult().getAllErrors().stream()
                .map(item->((FieldError)item).getField()+" "+item.getDefaultMessage())
                .toList().toString();
        var response=new GlobalExceptionResponseDTO(message,getTimestamp(),ex.getStackTrace()[0].toString());
        return ResponseEntity.status(ex.getStatusCode()).body(response);
    }
    @ExceptionHandler
    public ResponseEntity<GlobalExceptionResponseDTO> userNotFoundExceptionHandler(UsernameNotFoundException ex) {
        var response= new GlobalExceptionResponseDTO(ex.getMessage(),getTimestamp(),
                ex.getStackTrace()[0].toString());

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<GlobalExceptionResponseDTO> resourceNotFoundExceptionHandler(ResourceNotFoundException ex) {

        var response= new GlobalExceptionResponseDTO(ex.getMessage(),getTimestamp(),
                ex.getStackTrace()[0].toString());

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

    }
    @ExceptionHandler
    public ResponseEntity<GlobalExceptionResponseDTO> APIExceptionHandler(APIException ex) {

        var response= new GlobalExceptionResponseDTO(ex.getMessage(),getTimestamp(),
                ex.getStackTrace()[0].toString());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler
    public ResponseEntity<GlobalExceptionResponseDTO> generalExceptionHandler(Exception ex) {

        var response= new GlobalExceptionResponseDTO("Generic_Exception: "+ex.getMessage(),getTimestamp(),
                ex.getStackTrace()[0].toString());

        return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);

    }

    private String getTimestamp(){
        LocalDateTime systemTime = LocalDateTime.now();
        return systemTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy 'Time' HH:mm:ss"));
    }

}
