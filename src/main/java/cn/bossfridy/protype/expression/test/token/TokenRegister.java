package cn.bossfridy.protype.expression.test.token;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TokenRegister {
    public static final String LINE = "Line";

    private static final Pattern linePattern = Pattern.compile("\r\n|\r|\n", Pattern.MULTILINE);
    private static final HashMap<String, String> ESC_MAP = new HashMap<>();

    private Map<String, TokenProcessor> map = new HashMap<>();
    private StringJoiner joiner = new StringJoiner("|");
    private Pattern tokenPattern = null;

    static {
        ESC_MAP.put("r", "\r");
        ESC_MAP.put("b", "\b");
        ESC_MAP.put("f", "\f");
        ESC_MAP.put("n", "\n");
        ESC_MAP.put("t", "\t");
        ESC_MAP.put("\\", "\\");
        ESC_MAP.put("\"", "\"");
        ESC_MAP.put("'", "'");
    }

    public TokenRegister() {

    }

    /**
     * 词法分析
     *
     * @param content
     * @return
     * @throws Exception
     */
    public ArrayList<Token> lexicalAnalysis(String content) throws Exception {
        return lexicalAnalysis(content, false);
    }

    public ArrayList<Token> lexicalAnalysis(String content, boolean addLineHeader) throws Exception {
        TreeMap<Integer, Integer> lineNoMap = getLineNoMap(content);
        System.out.println();
        ArrayList<Token> result = new ArrayList<>();
        int next = 0, type = 0, offset = 0, prevLineNo = -1;
        Matcher matcher = getTokenPattern().matcher(content);

        while (matcher.find(next)) {
            String key = null;
            String captured = null;
            TokenProcessor processor = null;
            for (Map.Entry<String, TokenProcessor> entry : map.entrySet()) {
                key = entry.getKey();
                captured = matcher.group(key);

                if (captured != null) {
                    offset = matcher.start(type);
                    processor = entry.getValue();
                    break;
                }
            }

            if (processor != null) {
                TokenProcessorResult processResult = processor.process(content, captured, offset);
                int lineNo = lineNoMap.ceilingEntry(offset).getValue();
                if (addLineHeader && lineNo > prevLineNo) {
                    result.add(new Token(null, lineNo, offset, LINE));
                }
                prevLineNo = lineNo;
                Token token = new Token(processResult.getContent(), lineNo, offset, key);
                result.add(token);
                next = processResult.getNextOffset();
            } else {
                next = offset + captured.length();
            }
        }

        return result;
    }

    protected void put(String key, String patten) {
        put(key, patten, DEFAULT_PROCESSOR);
    }

    protected void put(String key, String patten, TokenProcessor processor) {
        if (map.containsKey(key)) {
            throw new RuntimeException("exist this '" + key + "' key");
        }

        map.put(key, processor);
        joiner.add("(?<" + key + ">" + patten + ")");
    }

    private Pattern getTokenPattern() {
        if (tokenPattern == null) {
            put("Error", "\\S+", ERROR_PROCESSOR);
            tokenPattern = Pattern.compile(joiner.toString(), Pattern.MULTILINE);
        }

        return tokenPattern;
    }

    private static TreeMap<Integer, Integer> getLineNoMap(String content) {
        TreeMap<Integer, Integer> lineNoMap = new TreeMap<>();
        Matcher lineNoMatcher = linePattern.matcher(content);
        int start = 0;

        while (lineNoMatcher.find()) {
            start = lineNoMatcher.start();
            lineNoMap.put(start, lineNoMap.size());
        }

        if (start < content.length() - 1) {
            lineNoMap.put(content.length() - 1, lineNoMap.size());
        }

        return lineNoMap;
    }

    /**
     * STRING_PROCESSOR
     */
    public static TokenProcessor STRING_PROCESSOR = (content, captured, offset) -> {
        int escape = 0;
        StringBuilder builder = new StringBuilder(captured);
        offset += captured.length();

        do {
            String c = content.substring(offset++, offset);
            if (c.equals("\n") || c.equals("\r") || c.equals("\t") || c.equals("\b") | c.equals("\f")) {
                throw new Exception("Illegal escape character in string literal");
            }
            if (escape % 2 == 1) {
                if (!ESC_MAP.containsKey(c)) {
                    throw new Exception("Illegal escape character in string literal");
                }
                builder.append(ESC_MAP.get(c));
            } else {
                if (!c.equals("\\")) {
                    builder.append(c);
                }

                if (captured.equals(c)) {
                    break;
                }
            }

            escape = c.equals("\\") ? escape + 1 : 0;
        } while (offset < content.length());

        String tempString = builder.toString();

        if (!tempString.endsWith(captured)) {
            throw new Exception("Illegal line end in string literal");
        }
        return new TokenProcessorResult(offset, tempString);
    };

    /**
     * ERROR_PROCESSOR
     */
    public static TokenProcessor ERROR_PROCESSOR = (content, captured, next) -> {
        throw new Exception("Cannot resolve symbol '" + captured + "' at " + next);
    };

    /**
     * DEFAULT_PROCESSOR
     */
    public static TokenProcessor DEFAULT_PROCESSOR = (content, captured, next) -> new TokenProcessorResult(next + captured.length(), captured);
}
