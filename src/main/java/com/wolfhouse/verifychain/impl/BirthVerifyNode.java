package com.wolfhouse.verifychain.impl;


import java.time.LocalDate;

/**
 * 生日验证节点
 *
 * @author Rylin Wolf
 */
public class BirthVerifyNode extends BaseVerifyNode<LocalDate> {
    @Override
    public boolean verify() {
        super.verify();
        if (this.t == null) {
            return allowNull;
        }
        return !this.t.isAfter(LocalDate.now());
    }
}
