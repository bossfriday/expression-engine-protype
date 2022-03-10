package cn.bossfridy.protype.expression.statement.handle;

import cn.bossfridy.protype.expression.ast.ASTMatcher;
import cn.bossfridy.protype.expression.runtime.ScriptContext;
import cn.bossfridy.protype.expression.statement.AbstractStatementHandle;
import cn.bossfridy.protype.expression.statement.ElementName;
import cn.bossfridy.protype.expression.token.Token;

@ElementName({"singleExpression#LiteralExpression",
        "singleExpression#IdentifierExpression",
        "singleExpression#ArrayLiteralExpression",
        "singleExpression#AnonymousFunctionExpression",
        "singleExpression#ThisExpression",
        "singleExpression#ObjectLiteralExpression",
        "assignmentOperator",
        "literal"})
public class SingleExpressionLiteralExpression extends AbstractStatementHandle {
    @Override
    public Token apply(ASTMatcher astMatcher, ScriptContext context) throws Exception {
        Object obj = astMatcher.source.getFirst();
        return getToken(obj, context);
    }
}
