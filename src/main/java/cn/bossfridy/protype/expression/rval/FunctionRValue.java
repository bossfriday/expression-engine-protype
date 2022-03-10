package cn.bossfridy.protype.expression.rval;

import cn.bossfridy.protype.expression.runtime.BiFunction;
import cn.bossfridy.protype.expression.runtime.TupleExecutor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class FunctionRValue extends BaseRValue<BiFunction> {

    private BiFunction value;

    public FunctionRValue() {
        setType(FUNCTION);
    }

    public FunctionRValue(BiFunction value) {
        setType(FUNCTION);
        this.value = value;
    }


    @Override
    public BiFunction value() {
        return this.value;
    }

    @Override
    public RVariable operate(String operator, RVariable b) {
        // todo
        return new RVariable(new NullRValue());
    }

    @Override
    public Object toObject() {
        return null; // todo
    }

    @Override
    public void encode(DataOutputStream dataOutputStream) throws IOException {

    }

    @Override
    public void decode(DataInputStream dataInputStream) throws IOException {

    }

    @Override
    public String toString() {
        return "FunctionRValue";
    }
}
