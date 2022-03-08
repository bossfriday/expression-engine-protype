package cn.bossfridy.protype.expression;

import cn.bossfridy.protype.expression.ast.ASTMatcher;
import cn.bossfridy.protype.expression.ast.ASTPattern;
import cn.bossfridy.protype.expression.token.ScriptTokenRegister;
import cn.bossfridy.protype.expression.token.Token;

import java.util.List;

public class ExpressionEngine {
    private ASTPattern astPattern;
    private ScriptTokenRegister tokenRegister;

    public ExpressionEngine() throws Exception {
        tokenRegister = new ScriptTokenRegister();
        astPattern = ASTPattern.compileWithFile("AstParser.conf");
    }

    /**
     * apply
     */
    public void apply(String script) throws Exception {
        List<Token> tokens = this.getTokens(script);
        System.out.println("---------Tokens-----------");
        tokens.forEach(token -> {
            System.out.println(token.toString());
        });

        System.out.println("---------AST-----------");
        ASTMatcher ast = this.syntacticAnalysis(tokens);
        System.out.println(ast.toString());
    }

    /**
     * 词法分析
     */
    private List<Token> getTokens(String script) throws Exception {
        return tokenRegister.getTokens(script);   // 词法分析
    }

    /**
     * 语法分析
     */
    private ASTMatcher syntacticAnalysis(List<Token> tokens) throws Exception {
        return astPattern.match(tokens);
    }

    public static void main(String[] args) throws Exception {
        String script = "var a = 1 + 1; printl(a);";
        ExpressionEngine expEngine = new ExpressionEngine();
        expEngine.apply(script);
    }
}
