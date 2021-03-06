/**
 *
 */
package org.ilyes.spring.validation.validator;

import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.validation.Errors;


public abstract class AbstractFieldValidator implements FieldValidator {

	protected static final String EMPTY_STRING = "";
	protected static final String TYPE_MISMATCH = "typeMismatch";
	protected static final ConversionService CONVERSIONSTATE = new DefaultConversionService();

	@Override
    public void validate(Errors errors, String fieldName) {
		validate(errors, fieldName, null);
	}

	@SuppressWarnings("unchecked")
	protected <V> V convert(Errors errors, String fieldName, Class<V> type, Object objValue) {
		V value = null;
		if (objValue != null){
			if (type.isInstance(objValue)){
				value = (V)objValue;
			}
			else {
				try {
					value = CONVERSIONSTATE.convert(objValue, type);
				} catch (Exception e) {
					value = CONVERSIONSTATE.convert(errors.getFieldValue(fieldName), type);
				}
			}
		}
		return value;
	}
}
