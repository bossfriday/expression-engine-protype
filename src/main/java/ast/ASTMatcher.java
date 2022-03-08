package ast;

import token.Token;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ASTMatcher {
    int tokenCount = 0;
    public LinkedList<Object> source = new LinkedList<>();
    boolean isSuccess = false;
    public String name = null;
    int level = 0;
    boolean hasLeftRecursion = false;
    boolean hasRightRecursion = false;

    /**
     * add
     */
    public void add(Object obj) {
        if (obj instanceof Token) {
            tokenCount++;
        }

        if (obj instanceof ASTMatcher) {
            ASTMatcher matcher = (ASTMatcher) obj;
            tokenCount += matcher.tokenCount;
        }

        source.add(obj);
        isSuccess = true;
    }

    /**
     * getMatcher
     */
    public ASTMatcher getMatcher(int index) throws Exception {
        Object object = source.get(index);
        if (object instanceof ASTMatcher) {
            return (ASTMatcher) object;
        }

        throw new Exception("");
    }

    /**
     * getToken
     */
    public Token getToken(int index) throws Exception {
        Object object = source.get(index);
        if (object instanceof Token) {
            return (Token) object;
        }

        throw new Exception("");
    }

    /**
     * addAll
     */
    public void addAll(ASTMatcher matcher) {
        tokenCount += matcher.tokenCount;
        source.addAll(matcher.source);
        isSuccess = true;
    }

    int size() {
        return this.tokenCount;
    }

    /**
     * toList
     */
    public List<Token> toList() {
        List<Token> result = new ArrayList<>();
        for (Object object : source) {
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
        return "'" + name + "':" + source;
    }
}
