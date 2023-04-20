package ru.clevertec.ecl.exception.response;

import lombok.Data;

@Data
public abstract class AbstractErrorResponse {

    private String message;
    private long timeStamp;

}