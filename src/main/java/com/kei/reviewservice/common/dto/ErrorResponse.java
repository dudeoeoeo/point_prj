package com.kei.reviewservice.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    private String message;
    private int status;
    private List<CustomFieldError> errors;

    private ErrorResponse(int status, List<FieldError> errors) {
        this.status = status;
        this.errors = errors.stream().map(CustomFieldError::new).collect(Collectors.toList());
    }

    private ErrorResponse(int status, String exceptionMessage) {
        this.status = status;
        this.errors = List.of(new CustomFieldError("", "", exceptionMessage));
    }

    public static ErrorResponse of(int status) {
        return new ErrorResponse(status, Collections.EMPTY_LIST);
    }

    public static ErrorResponse of(int status, BindingResult bindingResult) {
        return new ErrorResponse(status, bindingResult.getFieldErrors());
    }

    public static ErrorResponse of(int status, String exceptionMessage) {
        return new ErrorResponse(status, exceptionMessage);
    }

    public static class CustomFieldError {
        private String field;
        private String value;
        private String reason;

        private CustomFieldError(String field, String value, String reason) {
            this.field = field;
            this.value = value;
            this.reason = reason;
        }

        private CustomFieldError(FieldError fieldError) {
            this.field = fieldError.getField();
            this.reason = fieldError.getDefaultMessage();
        }

        public String getField() {
            return this.field;
        }

        public String getValue() {
            return value;
        }

        public String getReason() {
            return reason;
        }
    }
}
