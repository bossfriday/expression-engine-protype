package cn.bossfridy.protype.expression.statement.handle;

import cn.bossfridy.protype.expression.ast.ASTMatcher;
import cn.bossfridy.protype.expression.runtime.ScriptContext;
import cn.bossfridy.protype.expression.statement.AbstractStatementHandle;
import cn.bossfridy.protype.expression.statement.ElementName;
import cn.bossfridy.protype.expression.token.Token;
import cn.bossfridy.protype.expression.tuple.FunctionTuple;

@ElementName("arguments")
public class ArgumentsHandle extends AbstractStatementHandle {

    @Override
    public Token apply(ASTMatcher astMatcher, ScriptContext context) throws Exception {
        FunctionTuple functionTuple = new FunctionTuple();
        if (astMatcher.source.size() > 2) {
            for (int i = 1, len = astMatcher.source.size(); i < len - 1; i++) {
                Object obj = astMatcher.source.get(i);
                if (obj instanceof Token) {
                    Token token = (Token) obj;
                    if (token.getValue().equals(",")) {
                        continue;
                    }
                }
                Token token = obj instanceof Token ? (Token) obj : parse((ASTMatcher) obj, context);
                functionTuple.addArgument(token);
            }
        }
        context.addTuple(functionTuple);
        return null;
    }
}
