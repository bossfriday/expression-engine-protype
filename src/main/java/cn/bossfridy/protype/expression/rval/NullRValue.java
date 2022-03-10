package cn.bossfridy.protype.expression.rval;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class NullRValue extends BaseRValue<Void> {
    public static final int DEFAULT = 0;
    public static final int NEW_ARRAY = 1;
    public static final int NEW_MAP = 2;
    public static final int NEW_FUNCTION = 3;
    private int valueType = DEFAULT;

    public NullRValue() {
        setType(NULL);
    }

    @Override
    public Void value() {
        return null;
    }

    @Override
    public RVariable operate(String operator, RVariable b) {
        RValue variable = b.getValue();
        if (operator.equals("+") && variable instanceof StringRValue) {
            return new RVariable(new StringRValue("null" + variable.toString()));
        }
        if (operator.equals("==")) {
            if (variable instanceof NullRValue) {
                return new RVariable(new BooleanRValue(true));
            } else {
                return new RVariable(new BooleanRValue(false));
            }
        }
        if (operator.equals("!=")) {
            if (variable instanceof NullRValue) {
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
        dataOutputStream.writeInt(valueType);
    }

    @Override
    public void decode(DataInputStream dataInputStream) throws IOException {

    }


    @Override
    public String toString() {
        return "null";
    }

    public static void main(String[] args) {
        System.out.println(new NullRValue().type());
    }

}
