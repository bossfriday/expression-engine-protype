package cn.bossfridy.protype.expression.ast;

import cn.bossfridy.protype.expression.token.Token;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ASTMatcher {

    public int tokenCount = 0;
    public LinkedList<Object> source = new LinkedList<>();
    public boolean isSuccess = false;
    public String name = null;
    public int level = 0;
    public boolean hasLeftRecursion = false;
    public boolean hasRightRecursion = false;

    /**
     * add
     */
    public void add(Object obj) {
        if (obj instanceof Token) {
            this.tokenCount++;
        }

        if (obj instanceof ASTMatcher) {
            ASTMatcher matcher = (ASTMatcher) obj;
            this.tokenCount += matcher.tokenCount;
        }

        this.source.add(obj);
        this.isSuccess = true;
    }

    /**
     * getMatcher
     */
    public ASTMatcher getMatcher(int index) throws Exception {
        Object object = this.source.get(index);
        if (object instanceof ASTMatcher) {
            return (ASTMatcher) object;
        }

        throw new Exception("");
    }

    /**
     * getToken
     */
    public Token getToken(int index) throws Exception {
        Object object = this.source.get(index);
        if (object instanceof Token) {
            return (Token) object;
        }

        throw new Exception("");
    }

    /**
     * addAll
     */
    public void addAll(ASTMatcher matcher) {
        this.tokenCount += matcher.tokenCount;
        this.source.addAll(matcher.source);
        this.isSuccess = true;
    }

    /**
     * size
     */
    public int size() {
        return this.tokenCount;
    }

    /**
     * toList
     */
    public List<Token> toList() {
        List<Token> result = new ArrayList<>();
        for (Object object : this.source) {
            if (object instanceof Token) {
                result.add((Token) object);
            } else if (object instanceof ASTMatcher) {
                ASTMatcher astMatcher = (ASTMatcher) object;

                result.addAll(astMatcher.toList());
            }
        }

        return result;
    }

    @Override
    public String toString() {
        return "'" + this.name + "':" + this.source;
    }
}
