package com.lawman.gateway.authclient.validation.user;

import com.lawman.gateway.authclient.constant.AuthClientConstants;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.*;
import java.util.Objects;


@Constraint(validatedBy = DateformatValidation.DateformatValidator.class)
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE, ElementType.FIELD})
public @interface DateformatValidation {

  String message() default AuthClientConstants.ValidationMessage.INVALID_DATE_FORMAT;

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  class DateformatValidator implements ConstraintValidator<DateformatValidation, String> {

    @Override
    public void initialize(DateformatValidation constraintAnnotation) {
      ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String date, ConstraintValidatorContext constraintValidatorContext) {
      String dateRegex = "^(0[1-9]|1\\d|2\\d|3[01])/(0[1-9]|1[0-2])/\\d{4}$";
      if (Objects.isNull(date)) {
        return true;
      }
      return date.matches(dateRegex);
    }
  }
}
