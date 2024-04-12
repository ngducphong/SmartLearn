package com.example.elearning.dto.validation;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AtLeastOneNotBlankValidator implements ConstraintValidator<AtLeastOneNotBlank, Object> {
    private String[] fields;

    @Override
    public void initialize(AtLeastOneNotBlank constraintAnnotation) {
        fields = constraintAnnotation.fields();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        for (String field : fields) {
            Object fieldValue = ValidatorUtil.getFieldValue(value, field);
            if (fieldValue != null && !fieldValue.toString().trim().isEmpty()) {
                return true;
            }
        }
        return false;
    }
}
