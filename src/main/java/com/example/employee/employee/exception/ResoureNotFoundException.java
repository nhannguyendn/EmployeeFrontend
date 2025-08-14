package com.example.employee.employee.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus( value = HttpStatus.NOT_FOUND)
public class ResoureNotFoundException extends RuntimeException {

    public ResoureNotFoundException(String messsage) {
        super(messsage);
    }

}
