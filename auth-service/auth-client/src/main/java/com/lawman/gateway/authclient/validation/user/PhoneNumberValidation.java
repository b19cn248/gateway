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
@Constraint(validatedBy = PhoneNumberValidation.PhoneNumberValidator.class)
public @interface PhoneNumberValidation {
  String message() default AuthClientConstants.ValidationMessage.INVALID_PHONE_NUMBER;

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};


  class PhoneNumberValidator implements ConstraintValidator<PhoneNumberValidation, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
      if (Objects.isNull(value) || value.isEmpty()) return true;
      return value.matches("(84|0[3|5|7|8|9])+([0-9]{8})\\b");
    }
  }

}

