package ru.crm.system.validation;

import ru.crm.system.validation.impl.CheckSameDataAndTimeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = CheckSameDataAndTimeValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckSameDataAndTime {

    String message() default "{lesson.data_time_existing}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}