package com.app.studentManagementSystem.util;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ReportAsSingleViolation;
import jakarta.validation.constraints.NotNull;

import java.lang.annotation.*;

@Constraint(validatedBy = EnumValidatorImpl.class)
@Documented
@Target({ElementType.FIELD,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
//@NotNull(message = "Role must not be null")
//@ReportAsSingleViolation
public @interface EnumValidator {
    //Class<? extends Enum<?>> enumClazz();
    String[] acceptedValues();
    String message() default "Role is not valid,it must be student or admin";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
