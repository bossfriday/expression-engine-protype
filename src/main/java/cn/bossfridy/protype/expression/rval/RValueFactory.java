package cn.bossfridy.protype.expression.rval;

import cn.bossfridy.protype.expression.token.Token;

public class RValueFactory {
    public static RValue getRValue(Token token) {
        RValue result;
        switch (token.getType()) {
            case "Integer":
                result = new LongRValue(token);
                break;
            case "Float":
                result = new DoubleRValue(token);
                break;
            case "Boolean":
                result = new BooleanRValue(token);
                break;
            case "String":
            case "MultiLineString":
                result = new StringRValue(token);
                break;
            default:
                result = new NullRValue();
        }

        return result;
    }


    public static RVariable getRVariable(Token token) {
        return new RVariable(getRValue(token));
    }

    public static RVariable getRVariable(Object object) {
        RValue result = null;
        if (object == null) {
            result = new NullRValue();
        }

        if (object instanceof RVariable) {
            RVariable variable = new RVariable(new NullRValue());
            variable.setRVariable((RVariable) object);
            return variable;
        }

        if (object instanceof String) {
            result = new StringRValue((String) object);
        }

        if (object instanceof Number) {
            if (object instanceof Double || object instanceof Float) {
                result = new DoubleRValue(((Number) object).doubleValue());
            } else {
                result = new LongRValue(((Number) object).longValue());
            }
        }

        if (object instanceof Boolean) {
            result = new BooleanRValue((Boolean) object);
        }

        return new RVariable(result);
    }

    public static RValue getRValue(int type) {
        RValue result = null;
        switch (type) {
            case BaseRValue.NULL :
                result = new NullRValue();
                break;
            case BaseRValue.LONG :
                result = new LongRValue();
                break;
            case BaseRValue.DOUBLE :
                result = new DoubleRValue();
                break;
            case BaseRValue.BOOLEAN :
                result = new BooleanRValue();
                break;
            case BaseRValue.STRING :
                result = new StringRValue();
                break;
            case BaseRValue.FUNCTION :
                result = new FunctionRValue();
                break;

            default:
                result = new UndefinedRValue();
        }

        return result;
    }
}
