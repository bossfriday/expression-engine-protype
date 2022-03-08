package ast;

public class ASTRuler {
    //当前Token类型 0 none 1 字符串 2 分组 3 token类型 4 引用其他规则
    int rulerType;

    // 实体规则名
    String name = null;

    // 固定字符串匹配
    String strContent;

    // 引用规则名称匹配
    String referenceName;

    //子规则匹配
    ASTRuler rulerGroup;

    // token类型匹配
    String tokenType;

    // 重复次数 空:为必须1次 *:为0次或多次 +:为1次或多次 ?:为0次或1次
    String repeatType = "";

    // 规则的下一个部分
    ASTRuler nextAndNode;

    // 平级规则 当前规则不满足的走这里 “或”
    ASTRuler nextOrNode;

    // 平级规则优先级
    int level;

    //是否存在左递归
    boolean hasLeftRecursion;

    //是否存在右递归
    boolean hasRightRecursion;
}
