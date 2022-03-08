package cn.bossfridy.protype.expression.test.token;

import java.util.List;

/**
 * 脚本词法分析器
 */
public class ScriptTokenRegister extends TokenRegister {
    /**
     * 这里hardCode定义词法分析规则（也可以把分词规则做成配置文件）
     */
    public ScriptTokenRegister() throws Exception {
        // 多行注释
        put("MultiLineComment", "/\\*[\\S\\s]*?\\*/", null);

        // 单行注释
        put("SingleLineComment", "//[\\S\\s]*?$", null);

        // 空白字符串
        put("Blank", "\\s+", null);

        // 字符串
        put("String", "[\"']", STRING_PROCESSOR);

        // 多行字符串
        put("MultiLineString", "```[\\s\\S]+?```");

        // Float类型
        put("Float", "(?:(?<![a-zA-Z0-9_$)\\]])-)?\\d+\\.\\d+[Ff]?(?=[^a-zA-Z0-9_$]|$)");

        // Integer类型
        put("Integer", "(?:(?<![a-zA-Z0-9_$)\\]])-)?(?:0[xX][0-9a-fA-F]+|0[0-7]*|[1-9]\\d*)[lL]?(?=[^a-zA-Z0-9_$]|$)");

        // 布尔类型
        put("Boolean", "\\b(?:true|false)\\b");

        // Null
        put("Null", "\\bnull\\b");

        // This
        put("This", "\\bthis\\b");

        // 多符号
        put("CompoundSymbol", "<<|>>|\\|\\||&&|<=|>=|!=|==|\\+\\+|--|\\+=|-=|\\*=|/=|&=|^=|\\|=");

        // 单符号
        put("SingleSymbol", "[+\\-*/%;=,.!|^&<>(){}\\[\\]?:]");

        // 关键字
        put("Keyword", "\\b(?:import|return|break|continue|do|for|while|if|else|var|go|in|typeof|function|new|class)\\b");

        // 标识符
        put("Identifier", "[a-zA-Z_$][a-zA-Z0-9_$]*");
    }

    /**
     * 词法分析
     * 1、扫描源代码文本，从左到右扫描文本，把文本拆成一些单词。
     * 2、分析出拆出来的单词是什么：关键字、标识符、符号、，注释……，其结果产物为Token。
     *
     * @param script
     * @return
     */
    public List<Token> getTokens(String script) throws Exception {
        return this.lexicalAnalysis(script);
    }
}
