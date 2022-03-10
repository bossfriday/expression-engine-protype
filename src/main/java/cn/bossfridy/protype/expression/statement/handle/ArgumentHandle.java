package cn.bossfridy.protype.expression.statement.handle;

import cn.bossfridy.protype.expression.ast.ASTMatcher;
import cn.bossfridy.protype.expression.runtime.ScriptContext;
import cn.bossfridy.protype.expression.statement.AbstractStatementHandle;
import cn.bossfridy.protype.expression.statement.ElementName;
import cn.bossfridy.protype.expression.token.Token;

@ElementName("argument")
public class ArgumentHandle extends AbstractStatementHandle {
    @Override
    public Token apply(ASTMatcher astMatcher, ScriptContext context) throws Exception {
        Object obj = astMatcher.source.getFirst();

        if (obj instanceof Token) {
            return (Token) obj;
        }

        return parse((ASTMatcher) obj, context);
    }
}
