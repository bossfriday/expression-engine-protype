package cn.bossfridy.protype.expression.rval;

import cn.bossfridy.protype.expression.token.Token;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Objects;

public class BooleanRValue extends BaseRValue<Boolean> {

    private Boolean value;

    public BooleanRValue() {
        setType(BOOLEAN);
    }

    public BooleanRValue(Token token) {
        setType(BOOLEAN);
        if (token.getValue().equals("true")) {
            this.value = true;
        } else {
            this.value = false;
        }
    }

    public BooleanRValue(boolean value) {
        setType(BOOLEAN);
        this.value = value;
    }

    @Override
    public Boolean value() {
        return value;
    }

    @Override
    public RVariable operate(String operator, RVariable b) {
        RValue bValue = b.getValue();

        if (operator.equals("+") && bValue instanceof StringRValue) {
            return new RVariable(new StringRValue(this.value.toString() + bValue.value()));
        }

        if (operator.equals("==")) {
            if (bValue instanceof BooleanRValue) {
                return new RVariable(new BooleanRValue(this.value.equals(((BooleanRValue) bValue).value)));
            } else {
                return new RVariable(new BooleanRValue(false));
            }
        }
        if (operator.equals("!=")) {
            return new RVariable(new BooleanRValue(!this.value.equals(bValue.value())));
        }
        return null;
    }

    @Override
    public Object toObject() {
        return value;
    }

    @Override
    public void encode(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeInt(type());
        dataOutputStream.writeBoolean(value());
    }

    @Override
    public void decode(DataInputStream dataInputStream) throws IOException {
        this.value = dataInputStream.readBoolean();

    }


    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BooleanRValue that = (BooleanRValue) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
