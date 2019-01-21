package com.mathi.region.flags.exception;

/**
 *
 * @author zMathi
 */
public class InvalidFlagException extends RuntimeException {

    public InvalidFlagException(String message) {
        super(message);
    }

    public InvalidFlagException() {
        super();
    }
}
