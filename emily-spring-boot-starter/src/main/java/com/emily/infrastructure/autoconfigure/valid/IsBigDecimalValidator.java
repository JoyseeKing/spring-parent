package com.emily.infrastructure.autoconfigure.valid;

import com.emily.infrastructure.autoconfigure.valid.annotation.IsBigDecimal;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

/**
 * BigDecimal 校验
 *
 * @author :  Emily
 * @since :  2023/12/24 1:32 PM
 */
public class IsBigDecimalValidator implements ConstraintValidator<IsBigDecimal, String> {
    private boolean required;

    @Override
    public void initialize(IsBigDecimal annotation) {
        required = annotation.required();
    }

    /**
     * 校验方法
     *
     * @param value   object to validate
     * @param context context in which the constraint is evaluated
     * @return true if the object is valid, false otherwise
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // 必传校验
        if (required) {
            if (StringUtils.isBlank(value)) {
                return false;
            }
        } else {
            if (StringUtils.isBlank(value)) {
                return true;
            }
        }
        try {
            // 格式校验
            new BigDecimal(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
