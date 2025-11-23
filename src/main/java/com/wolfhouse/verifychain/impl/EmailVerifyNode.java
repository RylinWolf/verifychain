package com.wolfhouse.verifychain.impl;

import org.apache.commons.validator.routines.EmailValidator;

/**
 * 邮箱验证节点，利用 apache commons validator 校验邮箱
 *
 * @author Rylin Wolf
 */
public class EmailVerifyNode extends BaseVerifyNode<String> {
    private boolean allowLocal = false;

    public EmailVerifyNode(String s) {
        super(s);
    }

    public EmailVerifyNode(String s, boolean allowLocal) {
        super(s);
        this.allowLocal = allowLocal;
    }

    public EmailVerifyNode(String s, Boolean allowNull, boolean allowLocal) {
        super(s, allowNull);
        this.allowLocal = allowLocal;
    }

    @Override
    public boolean verify() {
        if (this.t == null) {
            return allowNull;
        }

        return EmailValidator.getInstance(allowLocal)
                             .isValid(t);
    }
}
