package com.hospital.Arogeva.advices;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDateTime;



@Data
public class ApiResponse<T> {


 //   @Pattern(regexp = "hh-mm-ss dd-mm-yy")
  //  @JsonFormat(pattern = "hh:mm:ss dd-MM-yy")
    private LocalDateTime timeStamp;

    private T data;

    private ApiError error;

    public ApiResponse() {
        this.timeStamp = LocalDateTime.now();
    }

    public ApiResponse(T data) {
        this();
        this.data = data;
    }

    public ApiResponse(ApiError error) {
        this();
        this.error = error;
    }

}


