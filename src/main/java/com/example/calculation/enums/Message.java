package com.example.calculation.enums;

public enum Message {

    REFERENCE_DATA_DOES_NOT_EXIST("reference data does not exist"),
    UNEXPECTED_ERROR("Unexpected Error"),
    NO_DATA_MSG("Data does not exist"),
    CAN_NOT_PARSING_CALCULATION_EXPRESSION("Can Not Parsing Calculation Expression"),
    NO_CURRENT_DATA_MSG("Data does not exist"),
    CURRENT_VAULE_IS_NA("Current Value is (\"NA\")"),
    IS_NOT_SETTED_MSG(" is not setted in "),
    COMPARISON_IS_NOT_SETTED_MSG("Comparison is not setted in pam_tag_mappping"),
    THERE_ARE_NO_VALUES_WITHIN_THE_THRESHOLD("There are no values within the threshold"),
    FIRST_ACTION_MESSAGE("최초발생"),
    NOT_A_NUMBER("Not a number (\"NA\")"),
    REFERENCE_VAULE_IS_NA("Reference Value is (\"NA\")"),
    OUT_OF_THRESHOLD_MSG("Out Of Threshold");

    private String msg;

    Message(String msg) {
        this.msg = msg;
    }
    public String getMsg(){
        return msg;
    }
}
