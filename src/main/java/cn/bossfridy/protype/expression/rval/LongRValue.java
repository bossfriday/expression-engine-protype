package cn.bossfridy.protype.expression.rval;

import cn.bossfridy.protype.expression.token.Token;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Objects;

public class LongRValue extends BaseRValue<Long> {

    private Long value;

    public LongRValue() {
        setType(LONG);
    }

    public LongRValue(Token token) {
        setType(LONG);
        if (token.getValue().indexOf("0x") == 0) {
            this.value = Long.valueOf(token.getValue().substring(2), 16);
        } else {
            this.value = Long.valueOf(token.getValue());
        }
    }

    public LongRValue(long value) {
        setType(LONG);
        this.value = value;
    }


    @Override
    public Long value() {
        return value;
    }

    @Override
    public RVariable operate(String operator, RVariable b) {
        RValue variable = b.getValue();
        switch (operator) {
            case "*":
                if (variable instanceof LongRValue) {
                    return new RVariable(new LongRValue(this.value * ((LongRValue) variable).value()));
                } else if (variable instanceof DoubleRValue) {
                    return new RVariable(new DoubleRValue(this.value * ((DoubleRValue) variable).value()));
                }
                break;
            case "/":
                if (variable instanceof LongRValue) {
                    return new RVariable(new LongRValue(this.value / ((LongRValue) variable).value()));
                } else if (variable instanceof DoubleRValue) {
                    return new RVariable(new DoubleRValue(this.value / ((DoubleRValue) variable).value()));
                }
                break;
            case "%":
                if (variable instanceof LongRValue) {
                    return new RVariable(new LongRValue(this.value % ((LongRValue) variable).value()));
                } else if (variable instanceof DoubleRValue) {
                    return new RVariable(new DoubleRValue(this.value % ((DoubleRValue) variable).value()));
                }
                break;
            case "+":
                if (variable instanceof LongRValue) {
                    return new RVariable(new LongRValue(this.value + ((LongRValue) variable).value()));
                } else if (variable instanceof DoubleRValue) {
                    return new RVariable(new DoubleRValue(this.value + ((DoubleRValue) variable).value()));
                } else if (variable instanceof StringRValue) {
                    return new RVariable(new StringRValue(this.value.toString() + ((StringRValue) variable).value()));
                }
                break;
            case "-":
                if (variable instanceof LongRValue) {
                    return new RVariable(new LongRValue(this.value - ((LongRValue) variable).value()));
                } else if (variable instanceof DoubleRValue) {
                    return new RVariable(new DoubleRValue(this.value - ((DoubleRValue) variable).value()));
                }
                break;
            case ">>":
                if (variable instanceof LongRValue) {
                    return new RVariable(new LongRValue(this.value >> ((LongRValue) variable).value()));
                }
                break;
            case "<<":
                if (variable instanceof LongRValue) {
                    return new RVariable(new LongRValue(this.value << ((LongRValue) variable).value()));
                }
                break;
            case "&":
                if (variable instanceof LongRValue) {
                    return new RVariable(new LongRValue(this.value & ((LongRValue) variable).value()));
                }
                break;
            case "^":
                if (variable instanceof LongRValue) {
                    return new RVariable(new LongRValue(this.value ^ ((LongRValue) variable).value()));
                }
                break;
            case "|":
                if (variable instanceof LongRValue) {
                    return new RVariable(new LongRValue(this.value | ((LongRValue) variable).value()));
                }
                break;
            case "~":
                if (variable instanceof LongRValue) {
                    return new RVariable(new LongRValue(~this.value));
                }
                break;
            case ">":
                if (variable instanceof LongRValue) {
                    return new RVariable(new BooleanRValue(this.value > ((LongRValue) variable).value()));
                } else if (variable instanceof DoubleRValue) {
                    return new RVariable(new BooleanRValue(this.value > ((DoubleRValue) variable).value()));
                }
                break;
            case "<":
                if (variable instanceof LongRValue) {
                    return new RVariable(new BooleanRValue(this.value < ((LongRValue) variable).value()));
                } else if (variable instanceof DoubleRValue) {
                    return new RVariable(new BooleanRValue(this.value < ((DoubleRValue) variable).value()));
                }
                break;
            case "==":
                if (variable instanceof LongRValue) {
                    return new RVariable(new BooleanRValue(this.value == ((LongRValue) variable).value()));
                } else if (variable instanceof DoubleRValue) {
                    return new RVariable(new BooleanRValue(this.value - ((DoubleRValue) variable).value() == 0));
                } else {
                    return new RVariable(new BooleanRValue(false));
                }
            case "!=":
                if (variable instanceof LongRValue) {
                    return new RVariable(new BooleanRValue(this.value != ((LongRValue) variable).value()));
                } else if (variable instanceof DoubleRValue) {
                    return new RVariable(new BooleanRValue(this.value - ((DoubleRValue) variable).value() != 0));
                } else {
                    return new RVariable(new BooleanRValue(true));
                }
            case ">=":
                if (variable instanceof LongRValue) {
                    return new RVariable(new BooleanRValue(this.value >= ((LongRValue) variable).value()));
                } else if (variable instanceof DoubleRValue) {
                    return new RVariable(new BooleanRValue(this.value >= ((DoubleRValue) variable).value()));
                }
                break;
            case "<=":
                if (variable instanceof LongRValue) {
                    return new RVariable(new BooleanRValue(this.value <= ((LongRValue) variable).value()));
                } else if (variable instanceof DoubleRValue) {
                    return new RVariable(new BooleanRValue(this.value <= ((DoubleRValue) variable).value()));
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
        dataOutputStream.writeLong(value());
    }

    @Override
    public void decode(DataInputStream dataInputStream) throws IOException {
        this.value = dataInputStream.readLong();
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LongRValue that = (LongRValue) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
