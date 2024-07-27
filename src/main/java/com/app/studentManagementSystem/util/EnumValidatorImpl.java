package com.app.studentManagementSystem.util;

import com.app.studentManagementSystem.entity.Role;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.ArrayList;
import java.util.List;

public class EnumValidatorImpl implements ConstraintValidator<EnumValidator,Enum> {
    private List<String> valueList;
    @Override
    public void initialize(EnumValidator constraintAnnotation) {
        valueList = new ArrayList<>();
        for(String value : constraintAnnotation.acceptedValues()){
            valueList.add(value.toLowerCase());
        }
    }

    @Override
    public boolean isValid(Enum anEnum, ConstraintValidatorContext constraintValidatorContext) {
        return valueList.contains(anEnum.name());
//        if(anEnum == null){
//            return false;
//        }
//        try {
//            Role.valueOf(String.valueOf(anEnum));
//            return true;
//        }catch (IllegalArgumentException e){
//            return false;
//        }
    }


}
