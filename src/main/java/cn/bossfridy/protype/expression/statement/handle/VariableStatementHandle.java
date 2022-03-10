package cn.bossfridy.protype.expression.statement.handle;

import cn.bossfridy.protype.expression.ast.ASTMatcher;
import cn.bossfridy.protype.expression.runtime.ScriptContext;
import cn.bossfridy.protype.expression.statement.AbstractStatementHandle;
import cn.bossfridy.protype.expression.statement.ElementName;
import cn.bossfridy.protype.expression.token.Token;

@ElementName("variableStatement")
public class VariableStatementHandle extends AbstractStatementHandle {

    @Override
    public Token apply(ASTMatcher astMatcher, ScriptContext context) throws Exception {
        parse(astMatcher.getMatcher(0), context);
        return null;
    }
}
