package cn.bossfridy.protype.expression.rval;

public abstract class BaseRValue<T> implements RValue<T> {
    public final static int NULL = 0;
    public final static int LONG = 1;
    public final static int DOUBLE = 2;
    public final static int BOOLEAN = 3;
    public final static int STRING = 4;
    public final static int ARRAY = 5;
    public final static int MAP = 6;
    public final static int SET = 7;
    public final static int CONTEXT = 8;
    public final static int FUNCTION = 9;
    public final static int UNDEFINED =10;
    public final static int POINTER = 11;

    private int type;

    @Override
    public int type() {
        return type;
    }

    void setType(int type) {
        this.type = type;
    }
}
