package com.wolfhouse.verifychain.impl;


import com.wolfhouse.verifychain.VerifyConstant;

/**
 * 枚举验证节点。
 * 验证 from 对象是否为 t 的类型
 *
 * @author Rylin Wolf
 */
public class EnumVerifyNode extends BaseVerifyNode<Class<? extends Enum<?>>> {
    private Object from;

    public EnumVerifyNode() {
        super();
    }

    public EnumVerifyNode(Class<Enum<?>> enumClass) {
        super(enumClass);
    }

    public EnumVerifyNode(Class<Enum<?>> enumClass, Boolean allowNull) {
        super(enumClass, allowNull);
    }

    public EnumVerifyNode from(Object from) {
        this.from = from;
        return this;
    }

    @Override
    public boolean verify() {
        if (t == null) {
            throw new RuntimeException(VerifyConstant.TARGET_LOST);
        }

        if (from == null) {
            return allowNull;
        }
        // 不是同一枚举类
        if (!t.isAssignableFrom(from.getClass())) {
            return false;
        }
        Enum<?> fromEnum = t.cast(from);
        Enum<?>[] values = t.getEnumConstants();
        for (Enum<?> value : values) {
            if (value.equals(fromEnum)) {
                return true;
            }
        }
        return false;
    }
}
