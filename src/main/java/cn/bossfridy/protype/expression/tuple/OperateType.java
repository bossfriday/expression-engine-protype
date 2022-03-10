package cn.bossfridy.protype.expression.tuple;

import java.util.HashMap;

/**
 * 这里列出的操作符基本可以满足多数的需求（当然可以根据需求自定义扩展非标准操作符）
 */
public class OperateType {
    private static final HashMap<String, Byte> strToNumMap = new HashMap<>();
    private static final HashMap<Byte, String> numToStrMap = new HashMap<>();

    public static final byte ADDITION               = (byte) 0;     // +
    public static final byte SUBTRACTION            = (byte) 1;     // -
    public static final byte MULTIPLICATION         = (byte) 2;     // *
    public static final byte DIVISION               = (byte) 3;     // /
    public static final byte MODULO                 = (byte) 4;     // %
    public static final byte ASSIGNMENT             = (byte) 5;     // =
    public static final byte NOT                    = (byte) 6;     // !
    public static final byte BIT_OR                 = (byte) 7;     // |
    public static final byte BIT_XOR                = (byte) 8;     // ^
    public static final byte BIT_AND                = (byte) 9;     // &
    public static final byte BIT_NOT                = (byte) 10;    // ~
    public static final byte LESS_THEN              = (byte) 11;    // <
    public static final byte GREATER_THEN           = (byte) 12;    // >

    public static final byte LEFT_BIT_SHIFT         = (byte) 30;    // <<
    public static final byte RIGHT_BIT_SHIFT        = (byte) 31;    // >>
    public static final byte LOGIC_OR               = (byte) 32;    // ||
    public static final byte LOGIC_AND              = (byte) 33;    // &&
    public static final byte LOGIC_LESS_THEN        = (byte) 34;    // <=
    public static final byte LOGIC_GREATER_THEN     = (byte) 35;    // >=
    public static final byte LOGIC_NOT_EQUAL        = (byte) 36;    // !=
    public static final byte LOGIC_EQUAL            = (byte) 37;    // ==
    public static final byte POST_INCREMENT         = (byte) 38;    // ++
    public static final byte POST_DECREASE          = (byte) 39;    // --
    public static final byte ADD_ASSIGNMENT         = (byte) 40;    // +=
    public static final byte SUB_ASSIGNMENT         = (byte) 41;    // -=
    public static final byte MUL_ASSIGNMENT         = (byte) 42;    // *=
    public static final byte DIV_ASSIGNMENT         = (byte) 43;    // /=
    public static final byte BIT_AND_ASSIGNMENT     = (byte) 44;    // &=
    public static final byte BIT_XOR_ASSIGNMENT     = (byte) 45;    // ^=
    public static final byte BIT_OR_ASSIGNMENT      = (byte) 46;    // |=

    public static final byte CONST_DECLARATION      = (byte) 60;    // =:
    public static final byte VARIABLE_DECLARATION   = (byte) 61;    // :=
    public static final byte MEMBER_DOT             = (byte) 62;    // .

    /**
     * 本项目只是示例原型项目，这里不做所有操作符映射
     */
    static {
        strToNumMap.put("+", ADDITION);
        strToNumMap.put("-", SUBTRACTION);
        strToNumMap.put("*", MULTIPLICATION);
        strToNumMap.put("/", DIVISION);
        // todo: 其他操作符映射

        numToStrMap.put(ADDITION, "+");
        numToStrMap.put(SUBTRACTION, "-");
        numToStrMap.put(MULTIPLICATION, "*");
        numToStrMap.put(DIVISION, "/");
        // todo: 其他操作符映射

    }

    /**
     * getByteValue
     *
     * @param key
     * @return
     */
    public static byte getByteValue(String key) {
        if (!strToNumMap.containsKey(key))
            throw new RuntimeException("unsupported  operate!(op:" + key + ")");

        return strToNumMap.get(key);
    }

    /**
     * getStringValue
     *
     * @param key
     * @return
     */
    public static String getStringValue(byte key) {
        if (!numToStrMap.containsKey(key))
            throw new RuntimeException("unsupported  operate!(num:" + Byte.toUnsignedInt(key) + ")");

        return numToStrMap.get(key);
    }
}
