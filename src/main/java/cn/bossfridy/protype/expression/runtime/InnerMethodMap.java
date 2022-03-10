package cn.bossfridy.protype.expression.runtime;

import cn.bossfridy.protype.expression.rval.FunctionRValue;
import cn.bossfridy.protype.expression.rval.NullRValue;
import cn.bossfridy.protype.expression.rval.RVariable;

import java.util.HashMap;

public class InnerMethodMap {
    private static HashMap<String, RVariable> functionMap = new HashMap<>();

    static {
        putFunction("printl", args -> {
            StringBuilder builder = new StringBuilder();

            for (int i = 0; i < args.length; i++) {
                builder.append(args[i].toString());
            }

            System.err.println(builder.toString());

            return new RVariable(new NullRValue());
        });
    }

    public static RVariable getFunction(String name) {
        return functionMap.get(name);
    }

    static boolean containsFunction(String name) {
        return functionMap.containsKey(name);
    }

    static void putFunction(String name, BiFunction function) {
        functionMap.put(name, new RVariable(new FunctionRValue(function)));
    }
}
