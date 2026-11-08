package com.ricardo.financeiro.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.OverridesAttribute;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Pattern;

@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
@Pattern(regexp = "(\\d{13})?")
public @interface ISBN {
	
	@OverridesAttribute(constraint = Pattern.class, name = "message")
	String message() default "ISBN deve seguir o padrão XXXXXXXXXXXXX";
	
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};

}
