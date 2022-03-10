package cn.bossfridy.protype.expression.statement.handle;

import cn.bossfridy.protype.expression.ast.ASTMatcher;
import cn.bossfridy.protype.expression.runtime.ScriptContext;
import cn.bossfridy.protype.expression.statement.AbstractStatementHandle;
import cn.bossfridy.protype.expression.statement.ElementName;
import cn.bossfridy.protype.expression.token.Token;
import cn.bossfridy.protype.expression.tuple.QuaternionTuple;

@ElementName({"singleExpression#MultiplicativeExpression",
        "singleExpression#AdditiveExpression",
        "singleExpression#BitShiftExpression",
        "singleExpression#RelationalExpression",
        "singleExpression#EqualityExpression",
        "singleExpression#BitAndExpression",
        "singleExpression#BitXOrExpression",
        "singleExpression#BitOrExpression"
})
public class SingleExpressionOperatorExpressionHandle extends AbstractStatementHandle {
    @Override
    public Token apply(ASTMatcher astMatcher, ScriptContext context) throws Exception {
        Object p1Object = astMatcher.source.getFirst();
        Object opObject = astMatcher.source.get(1);
        Object p2Object = astMatcher.source.get(2);
        Token p1 = getToken(p1Object, context);
        Token p2 = getToken(p2Object, context);
        Token op = getToken(opObject, context);

        Token result = context.createTempVariableToken(op.getLineNo());
        QuaternionTuple tuple = new QuaternionTuple(op.getValue());
        tuple.setP1(p1);
        tuple.setP2(p2);
        tuple.setResult(result);

        context.addTuple(tuple);

        return result;
    }
}
