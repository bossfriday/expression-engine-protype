package cn.bossfridy.protype.expression.runtime;

import cn.bossfridy.protype.expression.rval.RVariable;

import java.io.IOException;

@FunctionalInterface
public interface BiFunction {
    /**
     * apply
     *
     * @param params
     * @return
     * @throws IOException
     */
    RVariable apply(RVariable[] params) throws Exception;
}
