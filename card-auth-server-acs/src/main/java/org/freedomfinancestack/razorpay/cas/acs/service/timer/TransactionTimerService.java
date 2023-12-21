package org.freedomfinancestack.razorpay.cas.acs.service.timer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.freedomfinancestack.razorpay.cas.dao.enums.TransactionStatus;

public interface TransactionTimerService {
    void scheduleTask(
            String transactionId, TransactionStatus transactionStatus, String decoupledTimeOut);

    void cancelTask(String transactionId);

    void performTask(String timerTaskId);

    default String getIdFromTaskIdentifier(String key, String input) {
        String pattern = key + "\\[(.*?)\\]";
        Pattern regexPattern = Pattern.compile(pattern);
        Matcher matcher = regexPattern.matcher(input);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            // If no match is found, return null or an empty string, depending on your preference
            return null;
        }
    }
}
