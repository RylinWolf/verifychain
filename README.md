# VerifyChain
在编写个人博客时，为了优化验证逻辑，自己设计了该工具类。

这是一个轻量级、可扩展的链式校验工具库。你可以通过组合多个验证节点（VerifyNode）来快速构建表单/参数等数据的校验流程，并按策略决定是仅返回结果还是抛出异常，支持统一错误信息、自定义异常、允许空值、条件校验等能力。

## 特性

- 链式构建验证流程，API 简洁直观
- 内置提供了多种内置验证节点（非空、长度、邮箱、手机号、日期、生日、枚举等）
- 三种校验策略：普通校验/抛异常/抛自定义异常
- 支持统一错误信息覆盖、允许空值、条件校验
- 失败信息收集，便于一次性展示所有错误

## 运行环境

- Java 21+
- 构建工具：Maven

## 安装与引入

本项目暂未发布到任何远程 Maven 仓库，但已提供可直接使用的 JAR 包 (见 release)：`verifychain-1.0-ALPHA.jar`。

你可以通过以下两种方式引入：

方式 A：安装到本地 Maven 仓库

```bash
mvn install:install-file \
  -Dfile=verifychain-1.0-ALPHA.jar \
  -DgroupId=com.wolfhouse \
  -DartifactId=verifychain \
  -Dversion=1.0-ALPHA \
  -Dpackaging=jar
```

安装成功后，在你的项目 `pom.xml` 中按常规坐标引入：

```xml
<dependency>
  <groupId>com.wolfhouse</groupId>
  <artifactId>verifychain</artifactId>
  <version>1.0-ALPHA</version>
</dependency>
```

方式 B：直接以本地 JAR 依赖

1) 将 `verifychain-1.0-ALPHA.jar` 放到你的项目目录（例如 `libs/`）

2) 在 `pom.xml` 中添加 system 范围依赖：

```xml
<dependency>
  <groupId>com.wolfhouse</groupId>
  <artifactId>verifychain</artifactId>
  <version>1.0-ALPHA</version>
  <scope>system</scope>
  <systemPath>${project.basedir}/libs/verifychain-1.0-ALPHA.jar</systemPath>
</dependency>
```
将 jar 放到项目的 `lib/` 目录，并与 `systemPath` 保持一致。

提示：`system` 依赖方式在 Maven 中并不推荐，优先使用“方式 A”。

另外，项目源码中使用了 Lombok（用于自定义抛出异常的信息掩盖）。Lombok 为可选依赖，使用该库时，你的业务项目可以不引入 Lombok；仅在你需要编译/修改本库源码时才需要本地安装或启用 Lombok 注解处理器。

## 项目结构

```
verifychain/
├── pom.xml
├── src/main/java/com/wolfhouse/verifychain/
│   ├── VerifyChain.java         # 验证链接口
│   ├── VerifyConstant.java      # 常量
│   ├── VerifyException.java     # 默认异常类型
│   ├── VerifyNode.java          # 验证节点接口
│   ├── VerifyStrategy.java      # 校验策略
│   └── VerifyTool.java          # 构建工具类
│
└── src/main/java/com/wolfhouse/verifychain/impl/
    ├── BaseVerifyChain.java     # 验证链默认实现
    ├── BaseVerifyNode.java      # 验证节点基类
    ├── EmptyVerifyNode.java     # 非空校验
    ├── StrLenVerifyNode.java    # 字符串长度校验
    ├── EmailVerifyNode.java     # 邮箱格式校验
    ├── PhoneVerifyNode.java     # 手机号格式校验
    ├── DateVerifyNode.java      # 日期格式校验
    ├── BirthVerifyNode.java     # 生日(年龄/日期)校验
    └── EnumVerifyNode.java      # 枚举值校验
```

## 快速开始

使用 `VerifyTool.of(...)` 组合多个验证节点后调用 `doVerify()`：

```java
import com.wolfhouse.verifychain.VerifyTool;
import com.wolfhouse.verifychain.VerifyStrategy;
import com.wolfhouse.verifychain.impl.*;

boolean ok = VerifyTool.of(
        new EmptyVerifyNode<String>().target("Alice").exception("姓名不能为空"),
        new StrLenVerifyNode("Alice", 2, 20).exception("姓名长度需在2~20之间"),
        new EmailVerifyNode("alice@example.com").exception("邮箱格式不正确")
).doVerify();

// ok 为 true 表示全部校验通过；否则可通过 failed() 获取失败明细
```

## 使用示例

1) 普通校验（不抛异常）

```java
var chain = VerifyTool.of(
        new EmptyVerifyNode<>("hello"),
        new StrLenVerifyNode("hello", 1, 10)
);
boolean ok = chain.doVerify();
if (!ok) {
    System.out.println(chain.failed()); // 收集失败的节点描述
}
```

2) 失败时抛出默认异常

将节点策略设置为 `WITH_EXCEPTION`：

```java
var node = new EmailVerifyNode("not-an-email").setStrategy(VerifyStrategy.WITH_EXCEPTION);
VerifyTool.of(node).doVerify(); // 校验失败将抛出 VerifyException
```

3) 失败时抛出自定义异常信息

```java
var node = new PhoneVerifyNode("13800138000")
        .exception("手机号格式不正确")
        .setStrategy(VerifyStrategy.WITH_CUSTOM_EXCEPTION);
VerifyTool.of(node).doVerify();
```

4) 统一错误信息 ofAllMsg

`VerifyTool.ofAllMsg(msg, nodes...)` 会为未设置异常信息的节点统一设置错误文案（优先级低于节点自带的异常设置）：

```java
var ok = VerifyTool.ofAllMsg("参数不合法",
        new EmptyVerifyNode<>(null),                    // 将使用统一文案
        new EmailVerifyNode("bad").exception("邮箱错")  // 节点自定义优先
).doVerify();
```

5) 条件校验与允许空值

```java
// 仅当值非空时才校验长度；并允许空值通过
var node = new StrLenVerifyNode('abc')
        .min(3)
        .max(8)
        .allowNull(true)
        .exception("长度不符合要求");

boolean ok = VerifyTool.of(node).doVerify(); // null 且允许空 => 通过
```

6) 使用函数式接口快速添加临时校验

`VerifyChain#add(T t, Predicate<T> p)` 可快速添加一次性的校验：

```java
import java.util.regex.Pattern;

var chain = VerifyTool.of();
chain.add("AB-123", v -> Pattern.matches("[A-Z]{2}-\\d{3}", v));
boolean ok = chain.doVerify();
```

7) 自定义节点

继承 `BaseVerifyNode<T>`，在使用时仅需设置 `target` 与 `predicate` 或重用基类的 `verify()`：

```java
public class PositiveIntNode extends BaseVerifyNode<Integer> {
    public PositiveIntNode(Integer t) { this.t = t; }
    public PositiveIntNode() {}
    {
        // 也可在构造中设置：predicate = v -> v != null && v > 0;
    }
}

var node = new PositiveIntNode().target(10).predicate(v -> v != null && v > 0);
var ok = VerifyTool.of(node).doVerify();
```

## 进阶：策略与失败收集

- 策略 `VerifyStrategy`：
  - `NORMAL`：仅返回 true/false
  - `WITH_EXCEPTION`：失败抛出默认 `VerifyException`
  - `WITH_CUSTOM_EXCEPTION`：失败抛出节点上设置的自定义异常；若未设置则退化为默认异常

- 失败信息收集：

```java
var chain = VerifyTool.of(
        new EmailVerifyNode("bad"),
        new EmptyVerifyNode<>(null)
);
boolean ok = chain.doVerify();
if (!ok) {
    for (String f : chain.failed()) {
        System.out.println(f); // 打印失败节点描述
    }
}
```

## 开发与构建

```bash
mvn -v        # 确认 Maven 与 JDK 版本
mvn clean package
```

产物：`target/verifychain-1.0-ALPHA.jar`


## 致谢

感谢 `commons-validator` 提供的基础校验能力支持。
