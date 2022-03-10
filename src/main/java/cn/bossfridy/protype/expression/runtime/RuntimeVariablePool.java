package cn.bossfridy.protype.expression.runtime;

import cn.bossfridy.protype.expression.rval.RValueFactory;
import cn.bossfridy.protype.expression.rval.RVariable;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class RuntimeVariablePool extends VariablePool<RVariable> {
    /**
     * encode
     *
     * @param dataOutputStream
     * @throws IOException
     */
    public void encode(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeInt(variableMap.size());
        Set<Map.Entry<String, RVariable>> entrySet = variableMap.entrySet();

        for (Map.Entry<String, RVariable> entry : entrySet) {
            dataOutputStream.writeUTF(entry.getKey());
            entry.getValue().encode(dataOutputStream);
        }
        if (parent != null) {
            RuntimeVariablePool runtimeVariablePool = (RuntimeVariablePool) parent;
            runtimeVariablePool.encode(dataOutputStream);
        }
    }

    /**
     * decode
     *
     * @param dataInputStream
     * @param level
     * @throws IOException
     */
    public void decode(DataInputStream dataInputStream, AtomicInteger level) throws IOException {
        level.decrementAndGet();
        int size = dataInputStream.readInt();

        for (int i = 0; i < size; i++) {
            String key = dataInputStream.readUTF();
            RVariable variable = new RVariable();
            variable.decode(dataInputStream);
            variableMap.put(key, variable);
        }

        if (dataInputStream.available() > 0 && level.get() > 0) {
            RuntimeVariablePool parent = new RuntimeVariablePool();
            parent.decode(dataInputStream, level);
            this.parent = parent;
        }
    }

    /**
     * setVariablePool
     *
     * @param variables
     */
    public void setVariablePool(Map<String, Object> variables) {
        if (variables != null) {
            variables.forEach((k, v) -> {
                if (k.equals("parent")) {
                    RuntimeVariablePool parent = new RuntimeVariablePool();
                    parent.setVariablePool((Map<String, Object>) v);
                    this.parent = parent;
                } else {
                    variableMap.put(k, RValueFactory.getRVariable(v));
                }
            });
        }
    }
}
