package com.wolfhouse.verifychain;

/**
 * @author linexsong
 */
public class VerifyException extends RuntimeException {
    public VerifyException(String message) {
        super(message);
    }

    public VerifyException() {
        super();
    }

    public static VerifyException failed() {
        return new VerifyException(VerifyConstant.VERIFY_FAILED);
    }
}
