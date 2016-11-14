package org.ilyes.spring.validation.validator;

import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;

public class RangeValidator<T extends Comparable<T>> extends AbstractFieldValidator {

	public static final String ERROR_RANGE = "error.range";
	public static final String ERROR_RANGE_OVER_MAX = "error.range.overMax";
	public static final String ERROR_RANGE_UNDER_MIN = "error.range.underMin";

	private T minValue;
	private T maxValue;

	public RangeValidator(){}

	public RangeValidator(T min, T max) {
		if (min == null && max == null){
			throw new IllegalStateException("min and max can not be both null");
		}
		setMinValue(min);
		setMaxValue(max);
		if (min != null && max != null &&  max.compareTo(min) < 0){
			throw new IllegalArgumentException("Max " + max + " must be greater than min " + min);
		}
	}

	public void setMinValue(T realMin) {
		this.minValue = realMin;
	}

	public void setMaxValue(T realMax) {
		this.maxValue = realMax;
	}

	@SuppressWarnings("unchecked")
	public void validate(Errors errors, String fieldName, String errorCode, Object... errorArgs) {
		Class<T> type = (minValue != null)?  (Class<T>)minValue.getClass() : maxValue != null? (Class<T>)maxValue.getClass() : null;
		if (type == null){
			errors.rejectValue(fieldName, TYPE_MISMATCH);
			return;
		}
		Object objValue =  BindingResult.class.isInstance(errors)? ((BindingResult)errors).getRawFieldValue(fieldName) : errors.getFieldValue(fieldName);
		T value = convert(errors, fieldName, type, objValue);
		if (minValue != null && maxValue != null && (!isValueGreaterThanMin(value) || !isValueLessThanMax(value))) {
			String errCode = (errorCode != null && errorCode.length() > 0) ? errorCode : ERROR_RANGE;
			errors.rejectValue(fieldName, errCode, errorArgs, EMPTY_STRING);
		} else if (!isValueGreaterThanMin(value)){
			String errCode = (errorCode != null && errorCode.length() > 0) ? errorCode : ERROR_RANGE_UNDER_MIN;
			errors.rejectValue(fieldName, errCode, errorArgs, EMPTY_STRING);
		} else if (!isValueLessThanMax(value)) {
			String errCode = (errorCode != null && errorCode.length() > 0) ? errorCode : ERROR_RANGE_OVER_MAX;
			errors.rejectValue(fieldName, errCode, errorArgs, EMPTY_STRING);
		}
	}

	protected boolean isValueGreaterThanMin(T value){
		if (minValue==null) {
			return true;
		}
		if (value == null){
			return false;
		}
		return value.compareTo(minValue) >= 0;
	}

	protected boolean isValueLessThanMax(T value){
		if (maxValue==null) {
			return true;
		}
		if (value == null){
			return true;
		}
		return value.compareTo(maxValue) <= 0;
	}
}
