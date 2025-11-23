package com.wolfhouse.verifychain.impl;

/**
 * 手机号验证节点（支持全球格式）
 * <p>
 * 规则说明：
 * - 允许可选的国家区号前缀 '+'，若存在只能出现在首位且最多一个
 * - 允许常见分隔符：空格、短横线 '-'、点 '.'、括号 '()'
 * - 括号需成对且顺序正确
 * - 去除分隔符后，仅数字位数需在 7 到 15 位之间（E.164 最大 15 位）
 * - target 类型为 String
 * <p>
 * 注：该节点为通用校验，尽量宽松，不强校验各国本地格式细节
 *
 * @author Rylin Wolf
 */
public class PhoneVerifyNode extends StrLenVerifyNode {
    {
        // E.164: 1-15 位；考虑本地号码最小 7 位，这里默认取 [7, 15]
        min(7L).max(15L);
    }

    public PhoneVerifyNode() {
        super();
    }

    public PhoneVerifyNode(String s) {
        super(s);
    }

    public PhoneVerifyNode(String s, Boolean allowNull) {
        super(s, allowNull);
    }

    @Override
    public boolean verify() {
        // 先走父类可扩展/自定义谓词分支（与其他节点风格一致）
        super.verify();

        if (this.t == null) {
            return allowNull;
        }

        String s = this.t.trim();
        if (s.isEmpty()) {
            return false;
        }

        // '+' 只能在首位且最多一个
        int firstPlus = s.indexOf('+');
        if (firstPlus > 0) {
            return false;
        }
        if (firstPlus == 0 && s.indexOf('+', 1) != -1) {
            return false;
        }

        // 仅允许的字符集：数字、空格、-、.、(、)、可选的首位+
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (Character.isDigit(c)) {
                continue;
            }
            if (c == ' ' || c == '-' || c == '.' || c == '(' || c == ')') {
                continue;
            }
            if (c == '+' && i == 0) {
                continue;
            }
            return false;
        }

        // 括号匹配与顺序校验
        int balance = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '(') {balance++;} else if (c == ')') {
                balance--;
                if (balance < 0) {
                    // 出现了先 ')' 后 '('
                    return false;
                }
            }
        }
        if (balance != 0) {
            return false;
        }

        // 统计数字个数（去除所有非数字字符）
        int digitCount = 0;
        for (int i = 0; i < s.length(); i++) {
            if (Character.isDigit(s.charAt(i))) {
                digitCount++;
            }
        }

        return digitCount >= this.min && digitCount <= this.max;
    }
}
