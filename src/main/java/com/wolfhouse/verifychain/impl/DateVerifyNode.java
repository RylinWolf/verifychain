package com.wolfhouse.verifychain.impl;

import java.time.LocalDate;

/**
 * 日期验证节点
 *
 * @author Rylin Wolf
 */
public class DateVerifyNode extends BaseVerifyNode<LocalDate> {
    private final LocalDate t;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean allowNull;

    public DateVerifyNode(LocalDate localDate, Boolean allowNull) {
        this.t = localDate;
        this.allowNull = allowNull;
    }

    public DateVerifyNode(LocalDate localDate) {
        this.t = localDate;
    }

    public DateVerifyNode startDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public DateVerifyNode endDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    @Override
    public boolean verify() {
        if (t == null) {
            // 是否允许为空
            return allowNull;
        }
        return t.isAfter(startDate) && t.isBefore(endDate);
    }
}
