package cn.bossfridy.protype.expression.runtime;

import java.util.HashMap;
import java.util.Map;

public class VariablePool<T> {
    VariablePool<T> parent = null;
    Map<String, T> variableMap = new HashMap<>();

    public void setVariable(String key, T value) {
        Map<String, T> map = getVariableMap(key);
        if (map == null) {
            throw new RuntimeException();
        }

        map.put(key, value);
    }

    public void createVariable(String key, T value) {
        Map<String, T> map = getVariableMap(key);
        if (map == null) {
            variableMap.put(key, value);
            return;
        }

        map.put(key, value);
    }

    public boolean containsKey(String key) {
        return getVariableMap(key) != null;
    }

    public T get(String key) {
        Map<String, T> map = getVariableMap(key);

        if (map == null) {
            return null;
        }

        return map.get(key);
    }

    public int getLevel() {
        int result = 0;
        VariablePool temp = this;

        do {
            result++;
            temp = temp.parent;
        } while (temp != null);

        return result;
    }

    public boolean containsCurrentKey(String key) {
        return variableMap.containsKey(key);
    }

    private Map<String, T> getVariableMap(String key) {
        VariablePool<T> temp = this;

        do {
            if (temp.variableMap.containsKey(key)) {
                return temp.variableMap;
            } else {
                temp = temp.parent;
            }
        } while (temp != null);

        return null;
    }
}
