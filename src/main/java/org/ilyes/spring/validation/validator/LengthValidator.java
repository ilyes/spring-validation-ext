package org.ilyes.spring.validation.validator;

import org.springframework.validation.Errors;

public class LengthValidator extends AbstractFieldValidator {

	public static final String ERROR_LENGTH_OVER_MAX = "error.length.overMax";
	public static final String ERROR_LENGTH_UNDER_MIN = "error.length.underMin";

	private int min = 0;
    private int max = Integer.MAX_VALUE;

    public LengthValidator() {}

    public LengthValidator(int minValue, int maxValue) {
    	setMin(minValue);
		setMax(maxValue);
	}

    public void setMax(int max) {
    	if (max < min){
			throw new IllegalArgumentException("Max " + max + " must be greater than min " + min);
		}
        this.max = max;
    }

    public void setMin(int min) {
    	if (min < 0){
    		throw new IllegalArgumentException("min can not be negative");
    	}
        this.min = min;
    }

	public void validate(Errors errors, String fieldName, String errorCode, Object... errorArgs) {
		Object fieldValue = errors.getFieldValue(fieldName);
		String errCode = (errorCode != null && errorCode.length() > 0) ? errorCode : ERROR_LENGTH_UNDER_MIN;
		if (fieldValue == null) {
			errors.rejectValue(fieldName, errCode, errorArgs, EMPTY_STRING);
        	return ;
        }
        String value = fieldValue.toString();
        if (value.length() < min){
			errors.rejectValue(fieldName, errCode, errorArgs, EMPTY_STRING);
		} else if (value.length() > max) {
			errCode = (errorCode != null && errorCode.length() > 0) ? errorCode : ERROR_LENGTH_OVER_MAX;
			errors.rejectValue(fieldName, errCode, errorArgs, EMPTY_STRING);
		}
	}
}
