package org.ilyes.spring.validation.validator;

import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;

public class EqualsValidator<T> extends AbstractFieldValidator {

	private T compareToObject;

	public EqualsValidator(T otherObject) {
		this.compareToObject = otherObject;
	}

	private boolean negate;
	private boolean ignoreCase;

	public void setNegate(boolean negate) {
		this.negate = negate;
	}

	public void setIgnoreCase(boolean ignoreCase) {
		this.ignoreCase = ignoreCase;
	}

	@SuppressWarnings("unchecked")
	public void validate(Errors errors, String fieldName, String errorCode, Object... errorArgs) {
		Class<T> type = (compareToObject != null)? (Class<T>)compareToObject.getClass() : null;
		if (type == null){
			errors.rejectValue(fieldName, TYPE_MISMATCH);
			return;
		}
		Object objValue = BindingResult.class.isInstance(errors)? ((BindingResult)errors).getRawFieldValue(fieldName) : errors.getFieldValue(fieldName);
		T fieldValue = convert(errors, fieldName, type, objValue);
		boolean valid;
		if (fieldValue == null){
			valid =  compareToObject == null;
		} else {
			valid = (ignoreCase && String.class.isInstance(fieldValue))? fieldValue.toString().equalsIgnoreCase(compareToObject != null? compareToObject.toString() : null) : fieldValue.equals(compareToObject);
		}
		if (negate) {
        	valid = !valid;
        }
		if (!valid){
			errors.rejectValue(fieldName, errorCode, errorArgs, EMPTY_STRING);
		}
	}

}
