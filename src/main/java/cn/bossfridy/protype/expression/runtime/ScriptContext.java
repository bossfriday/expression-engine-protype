package cn.bossfridy.protype.expression.runtime;

import cn.bossfridy.protype.expression.token.Token;
import cn.bossfridy.protype.expression.tuple.Tuple;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ScriptContext {
    private Context current;
    private int tempResultNo = 0;

    public ScriptContext() {
        current = new Context();
    }

    public void addTuple(Tuple tuple) {
        current.tuples.add(tuple);
    }

    public List<Tuple> getTuples() {
        return current.tuples;
    }

    public int size() {
        return current.tuples.size();
    }

    /**
     * getMethodStack
     */
    public MethodStack getMethodStack() {
        return getMethodStack(current, null);
    }

    private MethodStack getMethodStack(Context context, MethodStack parent) {
        MethodStack methodStack = new MethodStack(parent);

        Tuple[] tuples = new Tuple[context.tuples.size()];
        context.tuples.toArray(tuples);

        Map<String, MethodStack> methods = new HashMap<>();
        context.methods.forEach((k, v) -> methods.put(k, getMethodStack(v, methodStack)));
        methodStack.setMethods(methods);
        methodStack.setTuples(tuples);

        return methodStack;
    }

    /**
     * 创建临时变量
     */
    public Token createTempVariableToken(int lineNo) {
        return new Token("#" + (this.tempResultNo++), lineNo, 0, "Identifier");
    }

    private static class Context {
        List<Tuple> tuples = new LinkedList<>();
        Map<String, Context> methods = new HashMap<>();
        private Context parent;
    }
}
