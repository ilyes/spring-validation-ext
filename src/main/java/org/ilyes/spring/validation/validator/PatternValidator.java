package org.ilyes.spring.validation.validator;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import org.springframework.validation.Errors;

public class PatternValidator extends AbstractFieldValidator {

	private static final String ERROR_PATTERN_INVALID = "error.pattern.invalid";

	private static final Map<String, Pattern> COMPILED_REG_EXP_CACHE = new ConcurrentHashMap<String, Pattern>();

	private String 	regExp;
    private boolean negate;

    public PatternValidator(){}

    public PatternValidator(String 	regExp){
    	setRegExp(regExp);
    }

	public void validate(Errors errors, String fieldName, String errorCode, Object... errorArgs) {
		Object fieldValue = errors.getFieldValue(fieldName);
		String errCode = (errorCode != null && errorCode.length() > 0) ? errorCode : ERROR_PATTERN_INVALID;
		if (fieldValue == null) {
			errors.rejectValue(fieldName, errCode, errorArgs, EMPTY_STRING);
        	return;
        }
        Pattern pattern = compileRegExp();
        boolean valid = pattern != null && pattern.matcher(fieldValue.toString()).matches();
        if (negate) {
        	valid = !valid;
        }
        if (!valid) {
        	errors.rejectValue(fieldName, errCode, errorArgs, EMPTY_STRING);
        }
	}

	public boolean isNegate() {
		return negate;
	}

	public void setNegate(boolean negate) {
		this.negate = negate;
	}

	public String getRegExp() {
		return regExp;
	}

	public void setRegExp(String regExp) {
		this.regExp = regExp;
	}

	private Pattern compileRegExp() {
		Pattern pattern = COMPILED_REG_EXP_CACHE.get(regExp);
	    if (pattern == null) {
	        pattern = Pattern.compile(regExp);
	        COMPILED_REG_EXP_CACHE.put(getRegExp(), pattern);
	    }
	    return pattern;
    }
}
