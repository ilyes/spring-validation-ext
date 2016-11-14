package org.ilyes.spring.validation;

import java.text.ParseException;
import java.util.Collection;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.ilyes.spring.validation.validator.EqualsValidator;
import org.ilyes.spring.validation.validator.LengthValidator;
import org.ilyes.spring.validation.validator.PatternValidator;
import org.ilyes.spring.validation.validator.RangeValidator;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;


public abstract class ValidationUtils {

    public static final String ERROR_PREFIX              = "error";
    public static final String REQUIRED_SUFFIX           = "required";
    public static final String INVALID_SUFFIX            = "invalid";
    public static final String DOT                       = ".";
    public static final String DEFAULT_PREFIX            = "label";
    public static final String TYPE_MISMATCH             = "typeMismatch";
    private static final String DEFAULT_DATE_FORMAT		 = "dd.MM.YYYY";


    public static <T extends Comparable<T>> void  rejectIfGreaterThan(T maxValue, Errors errors, String fieldName, String errorCode, Object... errorArgs) {  
        Assert.notNull(errors, "Errors object must not be null");
        RangeValidator<T> fieldValidator = new RangeValidator<T>();
        fieldValidator.setMaxValue(maxValue);
        fieldValidator.validate(errors, fieldName, errorCode, errorArgs);
    }

    public static <T extends Comparable<T>> void  rejectIfLessThan(T minValue, Errors errors,  String fieldName, String errorCode, Object... errorArgs) {  
        Assert.notNull(errors, "Errors object must not be null");
        RangeValidator<T> fieldValidator = new RangeValidator<T>();
        fieldValidator.setMinValue(minValue);
        fieldValidator.validate(errors, fieldName, errorCode, errorArgs);
    }

    public static <T extends Comparable<T>> void  rejectIfNotInRange(T minValue, T maxValue, Errors errors,  String fieldName, String errorCode, Object... errorArgs) { 
        Assert.notNull(errors, "Errors object must not be null");
        RangeValidator<T> fieldValidator = new RangeValidator<T>(minValue, maxValue);
        fieldValidator.validate(errors, fieldName, errorCode, errorArgs);
    }

    public static void rejectIfFieldNotConvertableToInteger(Errors errors, String fieldName, String errorCode, Object... errorArgs) {
        Object fieldValue = errors.getFieldValue(fieldName);
        Integer integer = Integer.valueOf(fieldValue.toString());
        if (integer == null) {
            errors.rejectValue(fieldName, errorCode, errorArgs, "");
        }
    }

    public static <T> void rejectIfNullOrNotInValues(Collection<T> values, Errors errors, String fieldName, String errorCode, Object... errorArgs){
        rejectIfNull(errors, fieldName, errorCode, errorArgs);
        if (!errors.hasFieldErrors(fieldName)){
            rejectIfNotInValues(values, errors, fieldName, errorCode, errorArgs);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> void rejectIfNotInValues(Collection<T> values, Errors errors, String fieldName, String errorCode, Object... errorArgs){
        Assert.notNull(errors, "Errors object must not be null");
        Assert.notNull(values, "values must not be null");
        T fieldValue = BindingResult.class.isInstance(errors)? (T)((BindingResult)errors).getRawFieldValue(fieldName) : (T)errors.getFieldValue(fieldName);
        if (!values.contains(fieldValue)) {
            errors.rejectValue(fieldName, errorCode, errorArgs, "");
        }
    }

    public static <T> void rejectIfNotEqualsValue(T value, Errors errors, String fieldName, String errorCode, Object... errorArgs){
        Assert.notNull(errors, "Errors object must not be null");
        EqualsValidator<T> fieldValidator = new EqualsValidator<T>(value);
        fieldValidator.validate(errors, fieldName, errorCode, errorArgs);
    }

    public static void rejectIfEqualsIgnoreCase(String value, Errors errors, String fieldName, String errorCode, Object... errorArgs){
        Assert.notNull(errors, "Errors object must not be null");
        EqualsValidator<String> fieldValidator = new EqualsValidator<String>(value);
        fieldValidator.setIgnoreCase(true);
        fieldValidator.setNegate(true);
        fieldValidator.validate(errors, fieldName, errorCode, errorArgs);
    }

    public static void rejectIfNotEqualsIgnoreCase(String value, Errors errors, String fieldName, String errorCode, Object... errorArgs){
        Assert.notNull(errors, "Errors object must not be null");
        EqualsValidator<String> fieldValidator = new EqualsValidator<String>(value);
        fieldValidator.setIgnoreCase(true);
        fieldValidator.validate(errors, fieldName, errorCode, errorArgs);
    }

    public static <T> void rejectIfEqualsValue(T value, Errors errors, String fieldName, String errorCode, Object... errorArgs){
        Assert.notNull(errors, "Errors object must not be null");
        EqualsValidator<T> fieldValidator = new EqualsValidator<T>(value);
        fieldValidator.setNegate(true);
        fieldValidator.validate(errors, fieldName, errorCode, errorArgs);
    }

    public static <T> void rejectIfNullOrEqualsValue(T value, Errors errors, String fieldName, String errorCode, Object... errorArgs){
        rejectIfNull(errors, fieldName, errorCode, errorArgs);
        if (!errors.hasFieldErrors(fieldName)) {
            rejectIfEqualsValue(value, errors, fieldName, errorCode, errorArgs);
        }
    }

    public static <T> void rejectIfNullOrNotEqualsValue(T value, Errors errors, String fieldName, String errorCode, Object... errorArgs){
        rejectIfNull(errors, fieldName, errorCode, errorArgs);
        if (!errors.hasFieldErrors(fieldName)) {
            rejectIfNotEqualsValue(value, errors, fieldName, errorCode, errorArgs);
        }
    }

    public static <T> void rejectIfNullOrInValues(Collection<T> values, Errors errors, String fieldName, String errorCode, Object... errorArgs){
        rejectIfNull(errors, fieldName, errorCode, errorArgs);
        if (!errors.hasFieldErrors(fieldName)) {
            rejectIfInValues(values, errors, fieldName, errorCode, errorArgs);
        }
    }

    public static <T> void rejectIfInValues(Collection<T> values, Errors errors, String fieldName, String errorCode, Object... errorArgs){
        Assert.notNull(errors, "Errors object must not be null");
        for (T value : values) {
            rejectIfEqualsValue(value, errors, fieldName, errorCode, errorArgs);
            if (errors.hasFieldErrors(fieldName)){
                return;
            }
        }
    }

    public static void rejectIfLengthGreaterThan(int maxLength, Errors errors, String fieldName , String errorCode, Object... errorArgs) {
        Assert.notNull(errors, "Errors object must not be null");
        LengthValidator lengthValidator = new LengthValidator();
        lengthValidator.setMax(maxLength);
        lengthValidator.validate(errors, fieldName, errorCode, errorArgs);
    }

    public static void rejectIfLengthLessThan(int minLength, Errors errors, String fieldName, String errorCode, Object... errorArgs) {
        Assert.notNull(errors, "Errors object must not be null");
        LengthValidator lengthValidator = new LengthValidator();
        lengthValidator.setMin(minLength);
        lengthValidator.validate(errors, fieldName, errorCode, errorArgs);
    }

    public static void rejectIfLengthNotInRange(int minLength, int maxLength, Errors errors, String fieldName , String errorCode, Object... errorArgs) {
        Assert.notNull(errors, "Errors object must not be null");
        LengthValidator lengthValidator = new LengthValidator(minLength, maxLength);
        lengthValidator.validate(errors, fieldName, errorCode, errorArgs);
    }

    public static void rejectIfLengthNotEqual(int length, Errors errors, String fieldName , String errorCode, Object... errorArgs) {
        Assert.notNull(errors, "Errors object must not be null");
        LengthValidator lengthValidator = new LengthValidator(length, length);
        lengthValidator.validate(errors, fieldName, errorCode, errorArgs);
    }

    public static void rejectIfEmpty(Errors errors, String field, String errorCode, Object... errorArgs) {
        Assert.notNull(errors, "Errors object must not be null");
        Object value = errors.getFieldValue(field);
        if (value == null || value.toString().length() == 0) {
            errors.rejectValue(field, errorCode, errorArgs, "");
        }
    }

    public static void rejectIfNull(Errors errors, String field, String errorCode, Object... errorArgs) {
        Assert.notNull(errors, "Errors object must not be null");
        Object value = BindingResult.class.isInstance(errors)? ((BindingResult)errors).getRawFieldValue(field) : errors.getFieldValue(field);
        if (value == null) {
            errors.rejectValue(field, errorCode, errorArgs, "");
        }
    }
    public static void rejectIfEmptyOrWhitespace(Errors errors, String field, String errorCode, Object... errorArgs) {
        Assert.notNull(errors, "errors object must not be null");
        Object value = errors.getFieldValue(field);
        if (value == null || value.toString().trim().length() == 0) {
            errors.rejectValue(field, errorCode, errorArgs, "");
        }
    }
    public static void rejectIfEmptyOrWhitespaceOrPlsSel(Errors errors, String field, String errorCode, Object... errorArgs) {
        Assert.notNull(errors, "errors object must not be null");
        Object value = errors.getFieldValue(field);
        if (value == null || value.toString().trim().length() == 0 || value.toString().equals("PLEASE_SELECT")) {
            errors.rejectValue(field, errorCode, errorArgs, "");
        }
    }
    
    public static void rejectIfPatternValid(String pattern, Errors errors, String fieldName, String errorCode, Object... errorArgs) {
        rejectIfPattern(true, pattern, errors, fieldName, errorCode, errorArgs);
    }

    public static void rejectIfPatternNotValid(String pattern, Errors errors, String fieldName, String errorCode, Object... errorArgs) {
        rejectIfPattern(false, pattern, errors, fieldName, errorCode, errorArgs);
    }

    protected static void rejectIfPattern(boolean negate, String pattern, Errors errors, String fieldName, String errorCode, Object... errorArgs) {
        Assert.notNull(errors, "errors must not be null");
        Assert.notNull(pattern, "pattern must not be null");
        PatternValidator patternValidator = new PatternValidator(pattern);
        patternValidator.setNegate(negate);
        patternValidator.validate(errors, fieldName, errorCode, errorArgs);
    }

    public static void rejectIfFalse(boolean expression, Errors errors, String fieldName, String errorCode, Object... errorArgs) {
        rejectIfTrue(! expression, errors, fieldName, errorCode, errorArgs);
    }

    public static void rejectIfTrue(boolean expression, Errors errors, String fieldName, String errorCode, Object... errorArgs) {
        if(expression) {
            errors.rejectValue(fieldName, errorCode, errorArgs, "");
        }
    }

    public static void rejectIfNotValidInteger(Errors errors, String field, String errorCode, Object... errorArgs) {
        rejectIfEmptyOrWhitespace(errors, field, errorCode, errorArgs); 
        if (!errors.hasFieldErrors(field)) {
            try {
                RangeValidator<Integer> fieldValidator = new RangeValidator<Integer>();
                fieldValidator.setMinValue(Integer.MIN_VALUE);
                fieldValidator.setMaxValue(Integer.MAX_VALUE);
                fieldValidator.validate(errors, field, errorCode, errorArgs);
            }
            catch (Exception e) {
                errors.rejectValue(field, errorCode, errorArgs, "");
            }
        }
    }
    
    public static void rejectIfNotValidDate(Errors errors, String field, String errorCode, Object... errorArgs) {
    	rejectIfNotValidDate(errors, DEFAULT_DATE_FORMAT, field, errorCode, errorArgs);
    }
    
    public static void rejectIfNotValidDate(Errors errors, String datePattern, String field, String errorCode, Object... errorArgs) {
        rejectIfEmptyOrWhitespace(errors, field, errorCode, errorArgs);
        if (!errors.hasFieldErrors(field)) {
            Object fieldValue = errors.getFieldValue(field);
            Date parsedDate = parseDate(fieldValue, datePattern);
            if (parsedDate == null) {
                errors.rejectValue(field, errorCode, errorArgs, "");
            }
        }
    }

    public static void rejectIfDateBefore(Date date, Errors errors, String field, String errorCode, Object... errorArgs) {
    	rejectIfDateBefore(date, DEFAULT_DATE_FORMAT,  errors,  field, errorCode, errorArgs);
    }
    
    public static void rejectIfDateBefore(Date date, String datePattern, Errors errors, String field, String errorCode, Object... errorArgs) {
        rejectIfEmptyOrWhitespace(errors, field, errorCode, errorArgs);
        if (date == null) {
            errors.rejectValue(field, errorCode, errorArgs, "compare date is null");
        }
        if (!errors.hasFieldErrors(field)) {
            Object fieldValue = errors.getFieldValue(field);
            
            Date parsedDate = parseDate(fieldValue, datePattern);
            if (parsedDate == null || parsedDate.before(date)) {
                errors.rejectValue(field, errorCode, errorArgs, "");
            }
        }
    }
    
    public static void rejectIfDateNotBefore(Date date, Errors errors, String field, String errorCode, Object... errorArgs) {
    	rejectIfDateNotBefore(date, DEFAULT_DATE_FORMAT,  errors,  field, errorCode, errorArgs);
    }
    
    public static void rejectIfDateNotBefore(Date date, String datePattern, Errors errors, String field, String errorCode, Object... errorArgs) {
        rejectIfEmptyOrWhitespace(errors, field, errorCode, errorArgs);
        if (date == null) {
            errors.rejectValue(field, errorCode, errorArgs, "compare date is null");
        }
        if (!errors.hasFieldErrors(field)) {
            Object fieldValue = errors.getFieldValue(field);
            Date parsedDate = parseDate(fieldValue, datePattern);
            if (parsedDate == null || !parsedDate.before(date)) {
                errors.rejectValue(field, errorCode, errorArgs, "");
            }
        }
    }

	private static Date parseDate(Object fieldValue, String datePattern)  {
		Date parsedDate;
		try {
			parsedDate = DateUtils.parseDate(fieldValue.toString(), datePattern);
		} catch (ParseException e) {
			return null;
		}
		return parsedDate;
	}

	public static void rejectIfDateAfter(Date date, Errors errors, String field, String errorCode, Object... errorArgs) {
		rejectIfDateAfter(date, DEFAULT_DATE_FORMAT, errors,  field, errorCode, errorArgs);
	}
	
    public static void rejectIfDateAfter(Date date, String datePattern, Errors errors, String field, String errorCode, Object... errorArgs) {
        rejectIfEmptyOrWhitespace(errors, field, errorCode, errorArgs);
        if (date == null) {
            errors.rejectValue(field, errorCode, errorArgs, "compare date is null");
        }
        if (!errors.hasFieldErrors(field)) {
            Object fieldValue = errors.getFieldValue(field);
            Date parsedDate = parseDate(fieldValue, datePattern);
            if (parsedDate == null || parsedDate.after(date)) {
                errors.rejectValue(field, errorCode, errorArgs, "");
            }
        }
    }

    public static boolean skipFieldValidation(String checkField, String[] skipFieldValidation) {
        if (!StringUtils.isNotEmpty(checkField) || skipFieldValidation == null || skipFieldValidation.length == 0 ){
            return false;
        }
        for (String fieldName : skipFieldValidation) {
            if (StringUtils.isNotEmpty(fieldName) && fieldName.equals(checkField)) {
                return true;
            }
        }

        return false;
    }
}