/**
 * 
 */
package org.ilyes.spring.validation.validator;

import org.springframework.validation.Errors;


public interface FieldValidator {
	void validate(Errors errors, String fieldName);
	void validate(Errors errors, String fieldName, String errorCode, Object... errorArgs);
}
