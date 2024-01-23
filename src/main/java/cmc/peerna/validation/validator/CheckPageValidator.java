package cmc.peerna.validation.validator;

import cmc.peerna.apiResponse.code.ResponseStatus;
import cmc.peerna.validation.annotation.CheckPage;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
@Component
@RequiredArgsConstructor
public class CheckPageValidator implements ConstraintValidator<CheckPage, Integer> {
    @Override
    public void initialize(CheckPage constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if(value < 1 || value == null) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(ResponseStatus.UNDER_PAGE_INDEX_ERROR.toString()).addConstraintViolation();
            return false;
        }
        return true;
    }
}
