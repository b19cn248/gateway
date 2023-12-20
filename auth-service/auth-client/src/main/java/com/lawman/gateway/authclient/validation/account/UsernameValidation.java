package com.lawman.gateway.authclient.validation.account;

import com.lawman.gateway.authclient.constant.AuthClientConstants;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.*;
import java.util.Objects;

@Constraint(validatedBy = UsernameValidation.UsernameValidator.class)
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE, ElementType.FIELD})
public @interface UsernameValidation {

  String message() default AuthClientConstants.ValidationMessage.INVALID_USERNAME;

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  class UsernameValidator implements ConstraintValidator<UsernameValidation, String> {

    @Override
    public void initialize(UsernameValidation constraintAnnotation) {
      ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext constraintValidatorContext) {
      String usernameRegex = "^[a-zA-Z0-9_-]{3,20}$";
      if (Objects.isNull(username) || username.isEmpty()) return false;
      return username.matches(usernameRegex);
    }
  }
}
