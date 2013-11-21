package be.crydust.tokenreplacer;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author kristof
 */
public class TokenReplacer {

    private final String begintoken;
    private final String endtoken;
    private final Map<String, String> replacetokens;
    
    public TokenReplacer(Map<String, String> replacetokens) {
        this("<", ">", replacetokens);
    }

    public TokenReplacer(String begintoken, String endtoken, Map<String, String> replacetokens) {
        if (begintoken == null || begintoken.isEmpty()) {
            throw new IllegalArgumentException("begintoken");
        }
        if (endtoken == null || endtoken.isEmpty()) {
            throw new IllegalArgumentException("endtoken");
        }
        if (replacetokens == null || replacetokens.isEmpty()) {
            throw new IllegalArgumentException("replacetokens");
        }
        for (String key : replacetokens.keySet()) {
            if (key == null || key.isEmpty()) {
                throw new IllegalArgumentException("replacetokens with empty key");
            }
        }
        this.begintoken = begintoken;
        this.endtoken = endtoken;
        this.replacetokens = new HashMap<>(replacetokens);
    }

    public Pattern getPattern() {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        sb.append(Pattern.quote(begintoken));
        sb.append("(");
        for (String key : replacetokens.keySet()) {
            sb.append(Pattern.quote(key));
            sb.append("|");
        }
        sb.setLength(sb.length() - 1);
        sb.append(")");
        sb.append(Pattern.quote(endtoken));
        sb.append(")");
        return Pattern.compile(sb.toString());
    }

    public String replace(String input) {
        Pattern pattern = getPattern();
        Matcher matcher = pattern.matcher(input);
        StringBuffer stringBuffer = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(stringBuffer, replacetokens.get(matcher.group(2)));
        }
        matcher.appendTail(stringBuffer);
        return stringBuffer.toString();
    }
}
