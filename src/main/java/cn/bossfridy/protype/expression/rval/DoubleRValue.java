package cn.bossfridy.protype.expression.rval;

import cn.bossfridy.protype.expression.token.Token;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Objects;

public class DoubleRValue extends BaseRValue<Double> {

    private Double value;

    public DoubleRValue() {
        setType(DOUBLE);
    }

    public DoubleRValue(Token token) {
        setType(DOUBLE);
        this.value = Double.valueOf(token.getValue());
    }

    public DoubleRValue(double value) {
        setType(DOUBLE);
        this.value = value;
    }

    @Override
    public Double value() {
        return value;
    }

    @Override
    public RVariable operate(String operator, RVariable b) {
        RValue rVariable = b.getValue();
        switch (operator) {
            case "*":
                if (rVariable instanceof LongRValue) {
                    return new RVariable(new DoubleRValue(this.value * ((LongRValue) rVariable).value()));
                } else if (rVariable instanceof DoubleRValue) {
                    return new RVariable(new DoubleRValue(this.value * ((DoubleRValue) rVariable).value()));
                }
                break;
            case "/":
                if (rVariable instanceof LongRValue) {
                    return new RVariable(new DoubleRValue(this.value / ((LongRValue) rVariable).value()));
                } else if (rVariable instanceof DoubleRValue) {
                    return new RVariable(new DoubleRValue(this.value / ((DoubleRValue) rVariable).value()));
                }
                break;
            case "%":
                if (rVariable instanceof LongRValue) {
                    return new RVariable(new DoubleRValue(this.value % ((LongRValue) rVariable).value()));
                } else if (rVariable instanceof DoubleRValue) {
                    return new RVariable(new DoubleRValue(this.value % ((DoubleRValue) rVariable).value()));
                }
                break;
            case "+":
                if (rVariable instanceof LongRValue) {
                    return new RVariable(new DoubleRValue(this.value + ((LongRValue) rVariable).value()));
                } else if (rVariable instanceof DoubleRValue) {
                    return new RVariable(new DoubleRValue(this.value + ((DoubleRValue) rVariable).value()));
                } else if (rVariable instanceof StringRValue) {
                    return new RVariable(new StringRValue(this.value.toString() + ((StringRValue) rVariable).value()));
                }
                break;
            case "-":
                if (rVariable instanceof LongRValue) {
                    return new RVariable(new DoubleRValue(this.value - ((LongRValue) rVariable).value()));
                } else if (rVariable instanceof DoubleRValue) {
                    return new RVariable(new DoubleRValue(this.value - ((DoubleRValue) rVariable).value()));
                }
                break;
            case ">":
                if (rVariable instanceof LongRValue) {
                    return new RVariable(new BooleanRValue(this.value > ((LongRValue) rVariable).value()));
                } else if (rVariable instanceof DoubleRValue) {
                    return new RVariable(new BooleanRValue(this.value > ((DoubleRValue) rVariable).value()));
                }
                break;
            case "<":
                if (rVariable instanceof LongRValue) {
                    return new RVariable(new BooleanRValue(this.value < ((LongRValue) rVariable).value()));
                } else if (rVariable instanceof DoubleRValue) {
                    return new RVariable(new BooleanRValue(this.value < ((DoubleRValue) rVariable).value()));
                }
                break;
            case "==":
                if (rVariable instanceof LongRValue) {
                    return new RVariable(new BooleanRValue(this.value - ((DoubleRValue) rVariable).value() == 0));
                } else if (rVariable instanceof DoubleRValue) {
                    return new RVariable(new BooleanRValue(this.value == ((DoubleRValue) rVariable).value()));
                } else {
                    return new RVariable(new BooleanRValue(false));
                }
            case "!=":
                if (rVariable instanceof LongRValue) {
                    return new RVariable(new BooleanRValue(this.value - ((DoubleRValue) rVariable).value() != 0));
                } else if (rVariable instanceof DoubleRValue) {
                    return new RVariable(new BooleanRValue(this.value != ((DoubleRValue) rVariable).value()));
                } else {
                    return new RVariable(new BooleanRValue(true));
                }
            case ">=":
                if (rVariable instanceof LongRValue) {
                    return new RVariable(new BooleanRValue(this.value >= ((LongRValue) rVariable).value()));
                } else if (rVariable instanceof DoubleRValue) {
                    return new RVariable(new BooleanRValue(this.value >= ((DoubleRValue) rVariable).value()));
                }
                break;
            case "<=":
                if (rVariable instanceof LongRValue) {
                    return new RVariable(new BooleanRValue(this.value <= ((LongRValue) rVariable).value()));
                } else if (rVariable instanceof DoubleRValue) {
                    return new RVariable(new BooleanRValue(this.value <= ((DoubleRValue) rVariable).value()));
                }
                break;
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
        dataOutputStream.writeDouble(value());
    }

    @Override
    public void decode(DataInputStream dataInputStream) throws IOException {
        this.value = dataInputStream.readDouble();
    }


    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DoubleRValue that = (DoubleRValue) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
