package cn.bossfridy.protype.expression.token;

@FunctionalInterface
public interface TokenProcessor {
    /**
     * process
     *
     * @param content
     * @param captured
     * @param next
     * @return
     * @throws Exception
     */
    TokenProcessorResult process(String content, String captured, int next) throws Exception;
}
