package cn.bossfridy.protype.expression.statement.handle;

import cn.bossfridy.protype.expression.ast.ASTMatcher;
import cn.bossfridy.protype.expression.runtime.ScriptContext;
import cn.bossfridy.protype.expression.statement.AbstractStatementHandle;
import cn.bossfridy.protype.expression.statement.ElementName;
import cn.bossfridy.protype.expression.token.Token;

@ElementName({"root", "statementList", "statement"})
public class StatementHandle extends AbstractStatementHandle {
    @Override
    public Token apply(ASTMatcher astMatcher, ScriptContext context) throws Exception {
        for (Object obj : astMatcher.source) {
            if (obj instanceof ASTMatcher) {
                parse((ASTMatcher) obj, context);
            }
        }

        return null;
    }
}
