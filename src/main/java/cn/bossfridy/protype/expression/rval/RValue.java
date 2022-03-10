package cn.bossfridy.protype.expression.rval;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public interface RValue<T> {
    /**
     * type
     */
    int type();

    /**
     * value
     */
    T value();

    /**
     * operate
     */
    RVariable operate(String operator, RVariable b);

    /**
     * toObject
     * @return
     */
    Object toObject();

    /**
     * encode
     */
    void encode(DataOutputStream dataOutputStream) throws IOException ;

    /**
     * decode
     */
    void decode(DataInputStream dataInputStream) throws IOException ;
}
