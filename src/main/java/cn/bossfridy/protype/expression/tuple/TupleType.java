package cn.bossfridy.protype.expression.tuple;


public enum TupleType {
    JUMP_TUPLE(1),
    CONDITION_JUMP_TUPLE(2),
    QUATERNION_TUPLE(3),
    FUNCTION_TUPLE(4);
    // todo：根据需要扩展其他类型

    private final int intValue;

    TupleType(int intValue) {
        this.intValue = intValue;
    }

    public final int intValue() {
        return this.intValue;
    }

    /**
     * getTuple
     *
     * @param type
     * @return
     * @throws Exception
     */
    public static Tuple getTuple(int type) throws Exception {
        if (type == TupleType.QUATERNION_TUPLE.intValue())
            return new QuaternionTuple("");

        if (type == TupleType.FUNCTION_TUPLE.intValue())
            return new FunctionTuple();

        // todo:其他TupleType，例如：JUMP_TUPLE、CONDITION_JUMP_TUPLE ……

        throw new Exception("unsupported tupleType!");
    }
}
