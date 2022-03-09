package cn.bossfridy.protype.expression.runtime;

import cn.bossfridy.protype.expression.tuple.Tuple;
import lombok.Getter;
import lombok.Setter;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class MethodStack {
    @Getter
    @Setter
    private Tuple[] tuples;

    @Setter
    private Map<String, MethodStack> methods = new HashMap<>();

    private final MethodStack parent;  // 为支持作用域的自内向外查找留口子

    public MethodStack(MethodStack parent) {
        this.parent = parent;
    }

    /**
     * getMethod
     */
    public MethodStack getMethod(String name) {
        MethodStack methodStack = this;
        do {
            if (methodStack.methods.containsKey(name)) {
                return methodStack.methods.get(name);
            } else {
                methodStack = methodStack.parent;
            }
        } while (methodStack != null);

        return null;
    }

    /**
     * setMethodStackBytes
     */
    public void setMethodStackBytes(byte[] data) throws Exception {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        DataInputStream dataInputStream = new DataInputStream(inputStream);
        this.decode(dataInputStream);
    }

    /**
     * getMethodStackBytes
     */
    public byte[] getMethodStackBytes() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        this.encode(dataOutputStream);

        return byteArrayOutputStream.toByteArray();
    }

    /**
     * TODO：方法栈编码（元组编码为build产物）
     * @param dataOutputStream
     * @throws IOException
     */
    public void encode(DataOutputStream dataOutputStream) throws IOException {

    }

    /**
     * TODO：方法栈解码（解码元祖后执行使用）
     * @param dataInputStream
     * @throws Exception
     */
    public void decode(DataInputStream dataInputStream) throws Exception {

    }
}
