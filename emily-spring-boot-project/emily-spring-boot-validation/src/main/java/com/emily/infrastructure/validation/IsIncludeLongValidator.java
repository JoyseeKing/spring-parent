package com.emily.infrastructure.validation;

import com.emily.infrastructure.validation.annotation.IsIncludeLong;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.Objects;

/**
 * 校验是否包含指定值
 *
 * @author :  Emily
 * @since :  2023/12/24 1:32 PM
 */
public class IsIncludeLongValidator implements ConstraintValidator<IsIncludeLong, Object> {
    private long[] values;

    @Override
    public void initialize(IsIncludeLong annotation) {
        values = annotation.values();
    }

    /**
     * 校验方法
     *
     * @param value   object to validate
     * @param context context in which the constraint is evaluated
     * @return true if the object is valid, false otherwise
     */
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (Objects.isNull(value)) {
            return true;
        }
        try {
            if (value instanceof Long) {
                return Arrays.stream(values).anyMatch(i -> i == (long) value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
