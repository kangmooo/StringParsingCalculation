package com.example.calculation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class CalcResult {

    private final Status status;
    private final String message;
    private final Object result;

    public enum Status {
        SUCCESS, FAIL
    }
}
