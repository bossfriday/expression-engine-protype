package cn.bossfridy.protype.expression.statement.handle;

import cn.bossfridy.protype.expression.ast.ASTMatcher;
import cn.bossfridy.protype.expression.runtime.ScriptContext;
import cn.bossfridy.protype.expression.statement.AbstractStatementHandle;
import cn.bossfridy.protype.expression.statement.ElementName;
import cn.bossfridy.protype.expression.token.Token;

@ElementName("expressionSequence")
public class ExpressionSequenceHandle extends AbstractStatementHandle {

    @Override
    public Token apply(ASTMatcher astMatcher, ScriptContext context) throws Exception {
        for (int i = 0, len = astMatcher.source.size(); i < len; i++) {
            Object obj = astMatcher.source.get(i);
            if (obj instanceof Token) {
                Token token = (Token) obj;
                if (token.getValue().equals(",")) {
                    continue;
                }
            }
            parse((ASTMatcher) obj, context);
        }
        context.getTuples().get(context.size() - 1).end();
        return null;
    }
}
