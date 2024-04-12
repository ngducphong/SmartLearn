package com.example.elearning.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class ResponseError {
    private int status;
    private String message;
    private Date timestamp;
    private String url;
}
