package cn.bossfridy.protype.expression.tuple;

import cn.bossfridy.protype.expression.runtime.TupleExecutor;
import lombok.Getter;
import lombok.Setter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public abstract class Tuple {
    @Getter
    @Setter
    private String operator;

    @Getter
    @Setter
    protected byte type;

    @Getter
    @Setter
    protected boolean end = false;

    public Tuple(String operator) {
        this.operator = operator;
    }

    public void end() {
        this.end = true;
    }

    /**
     * write
     */
    protected abstract void write(DataOutputStream dataOutputStream) throws IOException;

    /**
     * write
     */
    protected abstract void read(DataInputStream dataInputStream) throws IOException;

    /**
     * run
     */
    protected abstract void run(TupleExecutor tupleExecutor) throws Exception;

    /**
     * getLineNo
     */
    public abstract int getLineNo();

    /**
     * encode
     *
     * @param dataOutputStream
     * @throws IOException
     */
    public void encode(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeByte(type);
        dataOutputStream.writeBoolean(end);
        write(dataOutputStream);
    }

    /**
     * decode
     *
     * @param dataInputStream
     * @throws IOException
     */
    public void decode(DataInputStream dataInputStream) throws IOException {
        end = dataInputStream.readBoolean();
        read(dataInputStream);
    }
}
