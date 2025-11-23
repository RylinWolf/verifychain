package com.wolfhouse.verifychain;


import com.wolfhouse.verifychain.impl.BaseVerifyChain;

/**
 * @author linexsong
 */
public class VerifyTool {
    /**
     * 根据 VerifyNode 数组构建验证链
     *
     * @param nodes 验证节点数组
     * @return 验证链
     */
    public static BaseVerifyChain of(VerifyNode<?>... nodes) {
        BaseVerifyChain chain = BaseVerifyChain.instance();
        chain.add(nodes);
        return chain;
    }

    /**
     * 根据 VerifyNode 数组构建验证链，并设置统一错误信息。
     * 该错误信息的优先级低于 VerifyNode 配置的信息。
     *
     * @param nodes 验证节点数组
     * @param msg   错误信息
     * @return 验证链
     */
    public static BaseVerifyChain ofAllMsg(String msg, VerifyNode<?>... nodes) {
        for (VerifyNode<?> node : nodes) {
            if (node.getException() == null) {
                node.exception(msg);
            }
        }
        return of(nodes);
    }
}
