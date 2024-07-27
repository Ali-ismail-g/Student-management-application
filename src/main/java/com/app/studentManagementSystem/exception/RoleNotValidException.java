package com.app.studentManagementSystem.exception;

public class RoleNotValidException extends RuntimeException{
    public RoleNotValidException(String type){
        super(String.format("This role type '%s' is not supported!!",type));
    }
}
