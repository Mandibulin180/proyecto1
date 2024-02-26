package com.inventary_service.models.dto;

public record BaseResponse(String[] errorMessages) {

    public Boolean hasErrores(){
        return errorMessages != null && errorMessages.length>0;
    }

}
