package cn.bossfridy.protype.expression.statement.handle;


import cn.bossfridy.protype.expression.ast.ASTMatcher;
import cn.bossfridy.protype.expression.runtime.ScriptContext;
import cn.bossfridy.protype.expression.statement.AbstractStatementHandle;
import cn.bossfridy.protype.expression.statement.ElementName;
import cn.bossfridy.protype.expression.token.Token;
import cn.bossfridy.protype.expression.tuple.FunctionTuple;

@ElementName("singleExpression#ArgumentsExpression")
public class SingleExpressionArgumentsExpressionHandle extends AbstractStatementHandle {
    @Override
    public Token apply(ASTMatcher astMatcher, ScriptContext context) throws Exception {
        ASTMatcher obj =  astMatcher.getMatcher(0);
        Token function = parse(obj, context);

        ASTMatcher arguments =  astMatcher.getMatcher(1);
        parse(arguments, context);
        FunctionTuple functionTuple = (FunctionTuple) context.getTuples().get(context.size() - 1);

        functionTuple.setFunction(function);
        Token result = context.createTempVariableToken(function.getLineNo());
        functionTuple.setResult(result);
        return result;
    }
}
