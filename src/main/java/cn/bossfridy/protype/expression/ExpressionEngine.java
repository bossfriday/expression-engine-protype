package cn.bossfridy.protype.expression;

import cn.bossfridy.protype.expression.ast.ASTMatcher;
import cn.bossfridy.protype.expression.ast.ASTPattern;
import cn.bossfridy.protype.expression.runtime.MethodStack;
import cn.bossfridy.protype.expression.statement.AbstractStatementHandle;
import cn.bossfridy.protype.expression.token.ScriptTokenRegister;
import cn.bossfridy.protype.expression.token.Token;

import java.util.Arrays;
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
            System.out.println(token.toFullString());
        });

        System.out.println("---------AST Result-----------");
        ASTMatcher astResult = this.syntacticAnalysis(tokens);
        System.out.println(astResult.toString());

        MethodStack methodStack = AbstractStatementHandle.parseMethodStack(astResult);
        System.out.println("---------Tuples-----------");
        Arrays.asList(methodStack.getTuples()).forEach(tuple -> {
            System.out.println(tuple.toString());
        });
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
