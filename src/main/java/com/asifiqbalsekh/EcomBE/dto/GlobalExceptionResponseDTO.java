package com.asifiqbalsekh.EcomBE.dto;

public record GlobalExceptionResponseDTO(
        String message,
        String timestamp,
        String path
) {


}
