package com.lawman.gateway.authclient.validation.user;

import com.lawman.gateway.authclient.constant.AuthClientConstants;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Objects;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmailValidation.EmailValidator.class)
public @interface EmailValidation {

  String message() default AuthClientConstants.ValidationMessage.INVALID_EMAIL;

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};


  class EmailValidator implements ConstraintValidator<EmailValidation, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
      if (Objects.isNull(value) || value.isEmpty()) return true;
      return value.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    }
  }
}

