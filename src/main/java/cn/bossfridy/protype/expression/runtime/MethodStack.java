package cn.bossfridy.protype.expression.runtime;

import cn.bossfridy.protype.expression.token.Token;
import cn.bossfridy.protype.expression.tuple.Tuple;
import cn.bossfridy.protype.expression.tuple.TupleType;
import lombok.Getter;
import lombok.Setter;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MethodStack {
    @Getter
    @Setter
    private Tuple[] tuples;

    @Setter
    private Map<String, MethodStack> methods = new HashMap<>();

    @Getter
    @Setter
    private List<Token> params = new LinkedList<>();

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
     * encode：方法栈编码（元组编码为build产物）
     * @param dataOutputStream
     * @throws IOException
     */
    public void encode(DataOutputStream dataOutputStream) throws IOException {
        // tuples
        dataOutputStream.writeInt(tuples.length);
        for (Tuple tuple : tuples) {
            tuple.encode(dataOutputStream);
        }

        // methods
        dataOutputStream.writeInt(methods.size());
        methods.forEach((k, v) -> {
            try {
                dataOutputStream.writeUTF(k);
                v.encode(dataOutputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // params
        dataOutputStream.writeInt(params.size());
        for (Token param : params) {
            param.encode(dataOutputStream);
        }
    }

    /**
     * decode：方法栈解码（解码元组后执行使用）
     * @param dataInputStream
     * @throws Exception
     */
    public void decode(DataInputStream dataInputStream) throws Exception {
        // tuples
        int tupleSize = dataInputStream.readInt();
        if (tupleSize > 0) {
            this.tuples = new Tuple[tupleSize];
            for (int i = 0; i < tupleSize && dataInputStream.available() > 0; i++) {
                int type = dataInputStream.readByte();
                Tuple tuple = TupleType.getTuple(type);
                tuple.decode(dataInputStream);
                this.tuples[i] = tuple;
            }
        }

        // methods
        int methodSize = dataInputStream.readInt();
        if (methodSize > 0) {
            this.methods = new HashMap<>();
            for (int i = 0; i < methodSize && dataInputStream.available() > 0; i++) {
                String key = dataInputStream.readUTF();
                MethodStack method = new MethodStack(this);
                method.decode(dataInputStream);
                methods.put(key, method);
            }
        }

        // params
        int paramsSize = dataInputStream.readInt();
        if (paramsSize > 0) {
            this.params = new LinkedList<>();
            for (int i = 0; i < paramsSize && dataInputStream.available() > 0; i++) {
                params.add(new Token(dataInputStream));
            }
        }
    }
}
