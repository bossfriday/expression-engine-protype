package cn.bossfridy.protype.expression.test.token;

import lombok.Getter;

@Getter
public class TokenProcessorResult {
    private final int nextOffset;
    private final String content;

    public TokenProcessorResult(int nextOffset, String content) {
        this.nextOffset = nextOffset;
        this.content = content;
    }
}
