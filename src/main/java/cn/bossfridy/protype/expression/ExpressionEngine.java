package cn.bossfridy.protype.expression;

import cn.bossfridy.protype.expression.ast.ASTMatcher;
import cn.bossfridy.protype.expression.ast.ASTPattern;
import cn.bossfridy.protype.expression.runtime.MethodStack;
import cn.bossfridy.protype.expression.runtime.TupleExecutor;
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
        List<Token> tokens = tokenRegister.getTokens(script);   // 1.词法分析
        System.out.println("---------Tokens-----------");
        tokens.forEach(token -> {
            System.out.println(token.toFullString());
        });

        System.out.println("---------AST Result-----------");   // 2.语法分析
        ASTMatcher astResult = astPattern.match(tokens);
        System.out.println(astResult.toString());


        System.out.println("---------Tuples-----------");
        MethodStack methodStack = AbstractStatementHandle.parseMethodStack(astResult);  // 3.根据AST生成四元式
        Arrays.asList(methodStack.getTuples()).forEach(tuple -> {
            System.out.println(tuple.toString());
        });

        System.out.println("---------apply-----------");
        TupleExecutor executor = new TupleExecutor(methodStack);
        executor.apply(null);       // 4.四元式执行器
    }

    public static void main(String[] args) throws Exception {
        String script = "var a = 1 + 1; printl(a);";
        ExpressionEngine expEngine = new ExpressionEngine();
        expEngine.apply(script);
    }
}
