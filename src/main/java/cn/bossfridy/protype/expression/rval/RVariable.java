package cn.bossfridy.protype.expression.rval;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Objects;

public class RVariable {
    private RValue value;

    public RVariable() {

    }

    public RVariable(RValue value) {
        this.value = value;
    }

    public RVariable operate(String operator, RVariable b) {
        return value.operate(operator, b);
    }

    /**
     * encode
     *
     * @param dataOutputStream
     * @throws IOException
     */
    public void encode(DataOutputStream dataOutputStream) throws IOException {
        value.encode(dataOutputStream);
    }

    /**
     * decode
     *
     * @param dataInputStream
     * @throws IOException
     */
    public void decode(DataInputStream dataInputStream) throws IOException {
        RValue rValue = RValueFactory.getRValue(dataInputStream.readInt());
        rValue.decode(dataInputStream);
        this.value = rValue;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RVariable variable = (RVariable) o;
        return Objects.equals(value, variable.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    public Object toObject() {
        return value.toObject();
    }

    public void setRVariable(RVariable variable) {
        this.value = variable.getValue();
    }

    public RValue getValue() {
        return value;
    }
}
