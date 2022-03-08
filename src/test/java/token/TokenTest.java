package token;

import java.util.List;

/**
 * @ClassName: TokenTest
 * @Auther: chenx
 * @Description:
 */
public class TokenTest {
    public static void main(String[] args) throws Exception {
        String strScript = "var a = 1 + 1; // Comment";
        ScriptTokenRegister tokenRegister = new ScriptTokenRegister();
        List<Token> tokens = tokenRegister.getTokens(strScript);
        tokens.forEach(token->{
            System.out.println(token.toString());
        });
    }
}
