package token;

import java.util.List;

/**
 * 脚本词法分析器
 */
public class ScriptTokenRegister extends TokenRegister {
    /**
     * 这里hardCode定义词法分析规则（也可以把分词规则做成配置文件）
     *
     * @throws Exception
     */
    public ScriptTokenRegister() throws Exception {
        put("MultiLineComment", "/\\*[\\S\\s]*?\\*/", null);
        put("SingleLineComment", "//[\\S\\s]*?$", null);
        put("Blank", "\\s+", null);
        put("String", "[\"']", STRING_PROCESSOR);
        put("MultiLineString", "```[\\s\\S]+?```");
        put("Float", "(?:(?<![a-zA-Z0-9_$)\\]])-)?\\d+\\.\\d+[Ff]?(?=[^a-zA-Z0-9_$]|$)");
        put("Integer", "(?:(?<![a-zA-Z0-9_$)\\]])-)?(?:0[xX][0-9a-fA-F]+|0[0-7]*|[1-9]\\d*)[lL]?(?=[^a-zA-Z0-9_$]|$)");
        put("Boolean", "\\b(?:true|false)\\b");
        put("Null", "\\bnull\\b");
        put("This", "\\bthis\\b");
        put("CompoundSymbol", "<<|>>|\\|\\||&&|<=|>=|!=|==|\\+\\+|--|\\+=|-=|\\*=|/=|&=|^=|\\|=");
        put("SingleSymbol", "[+\\-*/%;=,.!|^&<>(){}\\[\\]?:]");
        put("Keyword", "\\b(?:import|return|break|continue|do|for|while|if|else|var|go|in|typeof|function|new|class)\\b");
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

    public static void main(String[] args) throws Exception {
        String strScript = "var a = 1 + 1; // Comment";
        ScriptTokenRegister tokenRegister = new ScriptTokenRegister();
        List<Token> tokens = tokenRegister.getTokens(strScript);

        System.out.println(tokens);
    }
}
