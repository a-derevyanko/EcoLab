package org.ekolab.server.model.content;

import java.util.Objects;

public interface FieldValidationResult {
    FieldValidationResult OK_RESULT = new FieldValidationResult() {
        @Override
        public String getErrorMessage() {
            return null;
        }

        @Override
        public boolean isError() {
            return false;
        }
    };

    class ErrorResult implements FieldValidationResult {

        private final String error;

        ErrorResult(String error) {
            this.error = error;
        }

        @Override
        public String getErrorMessage() {
            return error;
        }

        @Override
        public boolean isError() {
            return true;
        }

    }

    /**
     * Returns the result message.
     * <p>
     * Throws an {@link IllegalStateException} if the result represents success.
     *
     * @return the error message
     * @throws IllegalStateException
     *             if the result represents success
     */
    String getErrorMessage();

    /**
     * Checks if the result denotes an error.
     *
     * @return <code>true</code> if the result denotes an error,
     *         <code>false</code> otherwise
     */
    boolean isError();

    /**
     * Returns a successful result.
     *
     * @return the successful result
     */
    static FieldValidationResult ok() {
        return OK_RESULT;
    }

    /**
     * Creates the validation result which represent an error with the given
     * {@code errorMessage}.
     *
     * @param errorMessage
     *            error message, not {@code null}
     * @return validation result which represent an error with the given
     *         {@code errorMessage}
     * @throws NullPointerException
     *             if {@code errorMessage} is null
     */
    static FieldValidationResult error(String errorMessage) {
        Objects.requireNonNull(errorMessage);
        return new ErrorResult(errorMessage);
    }

    static FieldValidationResult of(boolean result) {
        return of (result, "validator.wrong-value");
    }

    static FieldValidationResult of(boolean result, String errorMsg) {
        return result ? ok() : error(errorMsg);
    }
}
