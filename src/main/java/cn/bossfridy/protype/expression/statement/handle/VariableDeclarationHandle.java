package cn.bossfridy.protype.expression.statement.handle;

import cn.bossfridy.protype.expression.ast.ASTMatcher;
import cn.bossfridy.protype.expression.runtime.ScriptContext;
import cn.bossfridy.protype.expression.statement.AbstractStatementHandle;
import cn.bossfridy.protype.expression.statement.ElementName;
import cn.bossfridy.protype.expression.token.Token;
import cn.bossfridy.protype.expression.tuple.QuaternionTuple;

@ElementName("variableDeclaration")
public class VariableDeclarationHandle extends AbstractStatementHandle {

    @Override
    public Token apply(ASTMatcher astMatcher, ScriptContext context) throws Exception {
        Token p1 = astMatcher.getToken(0);
        Token p2;
        if (astMatcher.source.size() > 1) {
            Object p2Object = astMatcher.source.get(2);
            if (p2Object instanceof Token) {
                p2 = (Token) p2Object;
            } else {
                p2 = parse((ASTMatcher) p2Object, context);
            }
        } else {
            p2 = new Token("null", p1.getLineNo(), p1.getOffset(), "Null");
        }

        QuaternionTuple tuple = new QuaternionTuple(":=");
        tuple.setP1(p1);
        tuple.setP2(p2);
        context.addTuple(tuple);
        tuple.setResult(p1);

        return p1;
    }
}
