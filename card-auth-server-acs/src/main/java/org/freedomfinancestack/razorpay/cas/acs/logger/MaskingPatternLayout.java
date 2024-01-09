package org.freedomfinancestack.razorpay.cas.acs.logger;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import com.fasterxml.jackson.core.JsonStreamContext;

import net.logstash.logback.mask.ValueMasker;

public class MaskingPatternLayout implements ValueMasker {

    static String methodParamTemplate = "%s\\s*=\\s*(.*?)\\s*[,)}\\]]";

    private List<String> maskPatterns = new ArrayList<>();

    private Pattern multilinePattern;

    @Override
    public Object mask(JsonStreamContext jsonStreamContext, Object o) {

        if (o instanceof CharSequence) {
            return transform((String) o);
        }
        return o;
    }

    public void addMaskPattern(final String maskPattern) {
        maskPatterns.add(maskPattern);
        multilinePattern = Pattern.compile(String.join("|", maskPatterns), Pattern.MULTILINE);
    }

    public void addMaskKey(final String maskKey) {
        maskPatterns.add(String.format(methodParamTemplate, maskKey));
        multilinePattern = Pattern.compile(String.join("|", maskPatterns), Pattern.MULTILINE);
    }

    private String maskMessage(String message) {
        if (multilinePattern == null) {
            return message;
        }
        StringBuilder sb = new StringBuilder(message);
        Matcher matcher = multilinePattern.matcher(sb);
        while (matcher.find()) {
            IntStream.rangeClosed(1, matcher.groupCount())
                    .forEach(
                            group -> {
                                if (matcher.group(group) != null) {
                                    IntStream.range(matcher.start(group), matcher.end(group))
                                            .forEach(i -> sb.setCharAt(i, '*'));
                                }
                            });
        }
        return sb.toString();
    }

    private String transform(final String msg) {
        return maskMessage(msg);
    }
}
