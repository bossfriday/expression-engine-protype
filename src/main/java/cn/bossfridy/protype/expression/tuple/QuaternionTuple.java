package cn.bossfridy.protype.expression.tuple;

import cn.bossfridy.protype.expression.runtime.TupleExecutor;
import cn.bossfridy.protype.expression.token.Token;
import lombok.Getter;
import lombok.Setter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * 四元组举例：1 + 1 = 2
 * p1: 1
 * p2: 1
 * operator: =
 * result: 2
 */
public class QuaternionTuple extends Tuple {
    @Getter
    @Setter
    private Token p1;

    @Getter
    @Setter
    private Token p2;

    @Getter
    @Setter
    private Token result;

    public QuaternionTuple(String operator) {
        super(operator);
        this.type = (byte) TupleType.QUATERNION_TUPLE.intValue();
    }

    @Override
    protected void write(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeUTF(getOperator());

        p1.encode(dataOutputStream);
        p2.encode(dataOutputStream);
        result.encode(dataOutputStream);
    }

    @Override
    protected void read(DataInputStream dataInputStream) throws IOException {
        setOperator(dataInputStream.readUTF());
        setP1(new Token(dataInputStream));
        setP2(new Token(dataInputStream));
        setResult(new Token(dataInputStream));
    }

    @Override
    protected void run(TupleExecutor tupleExecutor) throws Exception {

    }

    @Override
    public int getLineNo() {
        return p1.getLineNo();
    }

    @Override
    public String toString() {
        return "QuaternionTuple{" +
                "p1=" + p1 +
                ", p2=" + p2 +
                ", op=" + getOperator() +
                ", result=" + result +
                ", end=" + end +
                ", lineNo=" + p1.getLineNo() +
                "}";
    }
}
