package cn.bossfridy.protype.expression.rval;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class UndefinedRValue extends BaseRValue<Void> {

    public UndefinedRValue() {
        setType(UNDEFINED);
    }

    @Override
    public Void value() {
        return null;
    }

    @Override
    public RVariable operate(String operator, RVariable b) {
        RValue variable = b.getValue();
        if (operator.equals("+") && variable instanceof StringRValue) {
            return new RVariable(new StringRValue("undefined" + variable.toString()));
        }
        if (operator.equals("==")) {
            if (variable instanceof UndefinedRValue) {
                return new RVariable(new BooleanRValue(true));
            } else {
                return new RVariable(new BooleanRValue(false));
            }
        }
        if (operator.equals("!=")) {
            if (variable instanceof UndefinedRValue) {
                return new RVariable(new BooleanRValue(false));
            } else {
                return new RVariable(new BooleanRValue(true));
            }
        }
        return null;
    }

    @Override
    public Object toObject() {
        return null;
    }

    @Override
    public void encode(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeInt(type());
    }

    @Override
    public void decode(DataInputStream dataInputStream) throws IOException {

    }


    @Override
    public String toString() {
        return "undefined";
    }

}
