package cn.bossfridy.protype.expression.statement;

import cn.bossfridy.protype.expression.ast.ASTMatcher;
import cn.bossfridy.protype.expression.runtime.MethodStack;
import cn.bossfridy.protype.expression.runtime.ScriptContext;
import cn.bossfridy.protype.expression.token.Token;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class AbstractStatementHandle {
    private static final Map<String, Class<AbstractStatementHandle>> eachMap = new HashMap<>();
    private static int tempResultNo = 0;

    static {
        Reflections reflections = new Reflections("cn.bossfridy.protype.expression.statement.handle");
        Set<Class<? extends AbstractStatementHandle>> classes = reflections.getSubTypesOf(AbstractStatementHandle.class);
        Class clazz = AbstractStatementHandle.class;
        classes.forEach(item -> {
            if (!clazz.isAssignableFrom(item)) {
                return;
            }
            ElementName name = item.getAnnotation(ElementName.class);
            if (name == null) {
                return;
            }

            String[] names = name.value();
            Class<AbstractStatementHandle> handle = (Class<AbstractStatementHandle>) item;
            for (String s : names) {
                eachMap.put(s, handle);
            }
        });
    }

    /**
     * apply
     */
    public abstract Token apply(ASTMatcher astMatcher, ScriptContext context) throws Exception;

    /**
     * getToken
     */
    public Token getToken(Object obj, ScriptContext context) throws Exception {
        if (obj instanceof Token) {
            return (Token) obj;
        }

        return parse((ASTMatcher) obj, context);
    }

    /**
     * parse
     */
    public static Token parse(ASTMatcher matcher, ScriptContext context) throws Exception {
        if (matcher == null)
            throw new Exception("ASTMatcher result is null");

        if (!eachMap.containsKey(matcher.name))
            throw new Exception("Unimplemented statement handle!(" + matcher.name + ")");

        return eachMap.get(matcher.name).newInstance().apply(matcher, context);
    }

    /**
     * parseMethodStack
     */
    public static MethodStack parseMethodStack(ASTMatcher result) throws Exception {
        return parseContext(result).getMethodStack();
    }

    private static ScriptContext parseContext(ASTMatcher result) throws Exception {
        ScriptContext context = new ScriptContext();
        parse(result, context);

        return context;
    }
}
