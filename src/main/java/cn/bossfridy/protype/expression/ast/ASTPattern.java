package cn.bossfridy.protype.expression.ast;

import cn.bossfridy.protype.expression.test.token.Token;
import cn.bossfridy.protype.expression.test.token.TokenRegister;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class ASTPattern {
    private static final int NONE = 0;
    private static final int STRING_RULER = 1;
    private static final int GROUP_RULER = 2;
    private static final int TOKEN_RULER = 3;
    private static final int REFERENCE_RULER = 4;

    private static final String STRING = "String";
    private static final String SYMBOL = "Symbol";
    private static final String VARIABLE = "Variable";
    private static final String TOKEN_TYPE = "TokenType";
    private static final String NODE_NAME = "NodeName";
    private static final String ROOT_NODE = "root";

    private static ASTTokenRegister register = new ASTTokenRegister();
    private HashMap<String, ASTRuler> ruleMap = new HashMap<>();

    public ASTPattern() {

    }

    /**
     * compile
     *
     * @param content
     * @return
     * @throws Exception
     */
    public static ASTPattern compile(String content) throws Exception {
        ASTPattern creator = new ASTPattern();
        creator.init(content);

        return creator;
    }

    public static ASTPattern compileWithFile(String file) throws Exception {
        return compile(readFileContent(file));
    }

    /**
     * match
     *
     * @param tokens
     * @return
     */
    public ASTMatcher match(List<Token> tokens) {
        ASTRuler ruler = ruleMap.get(ROOT_NODE);
        ErrorInfo errorInfo = new ErrorInfo();
        ASTMatcher result = match(ruler, tokens, 0, errorInfo);

        if (result.tokenCount < tokens.size()) {
            System.out.println(errorInfo.msg);
            result.isSuccess = false;
        }

        return result;
    }

    public ASTMatcher match(ASTRuler ruler, List<Token> tokens, int index, ErrorInfo errorInfo) {
        ASTMatcher result = new ASTMatcher();
        if (ruler == null) {
            return result;
        }
        do {
            result = matchNext(ruler, tokens, index, errorInfo);
            if (!result.isSuccess) {
                ruler = ruler.nextOrNode;
                continue;
            }
            break;
        } while (ruler != null && index < tokens.size());

        return result;
    }

    private void init(String content) throws Exception {
        ArrayList<Token> tokens = register.lexicalAnalysis(content, true);

        for (int i = 0; i < tokens.size(); i++) {
            String key = null;
            for (; i < tokens.size(); i++) {
                if (tokens.get(i).type.equals(VARIABLE)) {
                    key = tokens.get(i).value;
                } else if (tokens.get(i).type.equals(SYMBOL) && tokens.get(i).value.equals(":")) {
                    break;
                }
            }

            ASTRuler ruler = new ASTRuler();
            ruler.level = -1;
            ruler.name = key;
            ASTRuler lineRuler = ruler, headRuler = ruler;

            LinkedList<ASTRuler> rulerStack = new LinkedList<>();
            boolean isNewLine = false, isNewOther = false;
            int level = 0;

            while (i < tokens.size() - 1) {
                i++;
                Token token = tokens.get(i);
                if (token.type.equals(TokenRegister.LINE)) {
                    isNewLine = true;
                    continue;
                }

                if (token.value.equals(";")) {
                    break;
                }

                if (ruler.rulerType != NONE && !token.type.equals(NODE_NAME) && (!token.type.equals(SYMBOL) || token.value.equals("("))) {
                    ASTRuler newRuler = new ASTRuler();
                    if (isNewLine) {
                        lineRuler = newRuler;
                    }
                    ruler.nextAndNode = newRuler;
                    ruler = newRuler;
                }

                if (token.type.equals(NODE_NAME)) {
                    lineRuler.name = key + token.value.replaceAll("\\s+", "");
                    isNewOther = true;
                    continue;
                } else if (token.type.equals(STRING)) {
                    ruler.rulerType = STRING_RULER;
                    String str = token.value;
                    ruler.strContent = str.substring(1, str.length() - 1);
                } else if (token.type.equals(TOKEN_TYPE)) {
                    ruler.rulerType = TOKEN_RULER;
                    ruler.tokenType = token.value;
                } else if (token.type.equals(VARIABLE)) {
                    ruler.rulerType = REFERENCE_RULER;
                    ruler.referenceName = token.value;
                } else if (token.value.equals("(")) {
                    ruler.rulerType = GROUP_RULER;
                    ASTRuler groupRuler = new ASTRuler();
                    if (isNewLine) {
                        lineRuler = groupRuler;
                    }
                    ruler.rulerGroup = groupRuler;
                    rulerStack.addLast(ruler);
                    ruler = groupRuler;
                } else if (token.value.equals(")")) {
                    ruler = rulerStack.removeLast();
                } else if (token.value.equals("|")) {
                    ASTRuler newRuler = new ASTRuler();
                    newRuler.level = ++level;

                    if (isNewOther) {
                        lineRuler.nextOrNode = newRuler;
                    } else {
                        ruler.nextOrNode = newRuler;
                    }
                    if (isNewLine) {
                        lineRuler = newRuler;
                    }
                    ruler = newRuler;
                } else if (token.value.equals("+") || token.value.equals("?") || token.value.equals("*")) {
                    ruler.repeatType = token.value;
                }
                isNewOther = isNewLine = false;
            }
            ruler = headRuler;

            List<ASTRuler> oldRulers = new ArrayList<>();
            List<ASTRuler> newRulers = new ArrayList<>();
            // 消除直接左递归
            while (ruler != null) {
                if (ruler.rulerType == REFERENCE_RULER && ruler.referenceName.equals(key)) {
                    ASTRuler newRuler = ruler.nextAndNode;
                    if (newRuler != null) {
                        if (ruler.name != null) {
                            newRuler.name = ruler.name;
                        }
                        newRuler.hasLeftRecursion = true;
                        addTailRuler(newRuler, key);
                        newRuler.level = ruler.level;
                        newRulers.add(newRuler);
                    }
                } else if (newRulers.size() > 0) {
                    addTailRuler(ruler, key);
                    oldRulers.add(ruler);
                }
                ruler = ruler.nextOrNode;
            }
            if (newRulers.size() > 0) {
                ASTRuler oldHeadRuler = linkRulerNode(oldRulers);
                if (oldHeadRuler != null) {
                    ruleMap.put(key, oldHeadRuler);
                }

                ASTRuler newHeadRuler = linkRulerNode(newRulers);
                if (newHeadRuler != null) {
                    ruleMap.put("#" + key, newHeadRuler);
                }
            } else {
                ruleMap.put(key, headRuler);
            }
        }
    }

    private void addTailRuler(ASTRuler ruler, String key) {
        ASTRuler headRuler = ruler;

        while (ruler.nextAndNode != null) {
            ruler = ruler.nextAndNode;
        }

        if (ruler.rulerType != REFERENCE_RULER || !ruler.referenceName.equals(key)) {
            ASTRuler refRuler = new ASTRuler();
            refRuler.rulerType = REFERENCE_RULER;
            refRuler.referenceName = "#" + key;
            refRuler.repeatType = "?";
            ruler.nextAndNode = refRuler;
            headRuler.hasRightRecursion = true;
            headRuler.level = -1;
        } else {
            headRuler.hasRightRecursion = false;
            headRuler.hasLeftRecursion = true;
        }
    }

    private ASTRuler linkRulerNode(List<ASTRuler> rulers) {
        if (rulers.size() == 0) {
            return null;
        }
        ASTRuler head = rulers.get(0);
        ASTRuler ruler = head;
        ruler.nextOrNode = null;
        for (int i = 1; i < rulers.size(); i++) {
            ASTRuler tempRuler = rulers.get(i);
            ruler.nextOrNode = tempRuler;
            ruler = tempRuler;
        }
        ruler.nextOrNode = null;
        return head;
    }

    private static class ErrorInfo {
        int index;
        String msg;
    }

    private ASTMatcher matchNext(ASTRuler ruler, List<Token> tokens, int index, ErrorInfo errorInfo) {
        ASTMatcher result = new ASTMatcher();
        result.name = ruler.name;
        result.level = ruler.level;
        result.hasLeftRecursion = ruler.hasLeftRecursion;
        result.hasRightRecursion = ruler.hasRightRecursion;

        do {
            ASTMatcher nextResult = matchItem(ruler, tokens, index, errorInfo);
            if (!nextResult.isSuccess) {
                result.isSuccess = false;
                break;
            }

            if (nextResult.name != null) {
                result.add(nextResult);
            } else {
                result.addAll(nextResult);
            }

            index += nextResult.size();
            ruler = ruler.nextAndNode;
        } while (ruler != null && index < tokens.size());

        if (tokens.size() == index && ruler != null && (ruler.repeatType.equals("") || ruler.repeatType.equals("+"))) {
            result.isSuccess = false;
        }

        if (result.isSuccess && result.hasRightRecursion && result.source.size() > 1 && result.name != null && result.source.getLast() instanceof ASTMatcher) {
            String resultPrevName = result.name.split("#")[0];
            ASTMatcher astMatcher = (ASTMatcher) result.source.getLast();
            result.source.removeLast();
            astMatcher.tokenCount = result.tokenCount;
            ASTMatcher headMarcher = astMatcher;

            do {
                Object obj = headMarcher.source.getFirst();
                if (obj == null || !(obj instanceof ASTMatcher)) {
                    break;
                }
                ASTMatcher tempMatcher = (ASTMatcher) obj;
                if (tempMatcher.name == null) {
                    break;
                }
                String tempPrevName = tempMatcher.name.split("#")[0];
                if (!tempPrevName.equals(resultPrevName)) {
                    break;
                }


                headMarcher = tempMatcher;
            } while (true);

            headMarcher.source.addFirst(result);
            result = astMatcher;
        }

        if (result.isSuccess && result.hasLeftRecursion && result.source.size() > 1 && result.source.getLast() instanceof ASTMatcher) {
            ASTMatcher astMatcher = (ASTMatcher) result.source.getLast();
            if (astMatcher.level >= result.level) {
                String resultPrevName = result.name.split("#")[0];
                result.source.removeLast();
                astMatcher.tokenCount = result.tokenCount;
                ASTMatcher headMarcher = astMatcher;

                do {
                    Object obj = headMarcher.source.getFirst();
                    if (obj == null || !(obj instanceof ASTMatcher)) {
                        break;
                    }
                    ASTMatcher tempMatcher = (ASTMatcher) obj;
                    if (tempMatcher.name == null) {
                        break;
                    }
                    String tempPrevName = tempMatcher.name.split("#")[0];
                    if (!tempPrevName.equals(resultPrevName)) {
                        break;
                    }
                    if (tempMatcher.level < result.level) {
                        break;
                    }
                    headMarcher = tempMatcher;
                } while (true);

                result.source.addLast(headMarcher.source.removeFirst());
                headMarcher.source.addFirst(result);

                result = astMatcher;
            }
        }

        return result;
    }

    private ASTMatcher matchItem(ASTRuler ruler, List<Token> tokens, int index, ErrorInfo errorInfo) {
        ASTMatcher result = new ASTMatcher();

        while (index < tokens.size()) {
            if (ruler.rulerType == STRING_RULER) {
                if (!ruler.strContent.equals(tokens.get(index).value)) {
                    if (index >= errorInfo.index) {
                        errorInfo.msg = "grammar mistake , at line:" + tokens.get(index).lineNo + " offset:" + tokens.get(index).offset;
                        errorInfo.index = index;
                    }
                    break;
                }

                result.add(tokens.get(index++));
            } else if (ruler.rulerType == GROUP_RULER) {
                ASTMatcher groupResult = match(ruler.rulerGroup, tokens, index, errorInfo);
                if (!groupResult.isSuccess) {
                    break;
                }
                index += groupResult.size();
                result.addAll(groupResult);
            } else if (ruler.rulerType == TOKEN_RULER) {
                if (!ruler.tokenType.equals(tokens.get(index).type)) {
                    if (index >= errorInfo.index) {
                        errorInfo.msg = "grammar mistake, at line:" + tokens.get(index).lineNo + " offset:" + tokens.get(index).offset;
                        errorInfo.index = index;
                    }
                    break;
                }

                result.add(tokens.get(index++));
            } else if (ruler.rulerType == REFERENCE_RULER) {
                ASTRuler referenceRuler = ruleMap.get(ruler.referenceName);
                if (referenceRuler == null) {
                    throw new RuntimeException(ruler.referenceName);
                }

                ASTMatcher referenceResult = match(referenceRuler, tokens, index, errorInfo);
                if (!referenceResult.isSuccess) {
                    break;
                }

                index += referenceResult.size();
                if (referenceResult.name == null) {
                    referenceResult.name = ruler.referenceName;
                }

                if (referenceResult.name.indexOf("#") == 0) {
                    return referenceResult;
                } else {
                    result.add(referenceResult);
                }
            }

            if (ruler.repeatType.equals("") || ruler.repeatType.equals("?")) {
                break;
            }
        }

        if (ruler.repeatType.equals("*") || ruler.repeatType.equals("?")) {
            result.isSuccess = true;
        }

        return result;
    }

    private static String readFileContent(String file) throws IOException {
        StringBuilder result = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(ASTPattern.class.getClassLoader().getResourceAsStream(file)));
        String s = null;
        while ((s = br.readLine()) != null) {
            result.append(s + "\n");
        }
        br.close();

        return result.toString();
    }

    private static class ASTTokenRegister extends TokenRegister {
        ASTTokenRegister() {
            put("MultiLineComment", "/\\*[\\S\\s]*?\\*/", null);
            put("SingleLineComment", "//[\\S\\s]*?$", null);
            put("Blank", "\\s+", null);
            put(STRING, "'", STRING_PROCESSOR);
            put(SYMBOL, "[+*?|():;]");
            put(NODE_NAME, "#[ \\t]*\\w+(?=[ \\t]*[;\\n])");
            put(TOKEN_TYPE, "[A-Z]\\w*");
            put(VARIABLE, "[a-z]\\w*");
        }
    }
}
