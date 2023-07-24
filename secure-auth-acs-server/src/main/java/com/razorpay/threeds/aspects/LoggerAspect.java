package com.razorpay.threeds.aspects;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.logging.LogLevel;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Order(Ordered.HIGHEST_PRECEDENCE)
@Aspect
@Component
@NoArgsConstructor
@Slf4j
public class LoggerAspect {

    private static final String DEFAULT_MASKED_LOG_MSG = "-----LARGE SIZE------";

    @Value("${logger.sizeLimit:10000}")
    private int logSizeLimit;

    /***
     * This contains all the packages for which we need to generate logs at DEBUG level
     */
    private static final String WHITELISTED_PACKAGES_DEBUG =
            "(" + "execution(* com.razorpay.acs.dao.repository..*(..)) || "
                    + "execution(* com.razorpay.threeds.validator..*(..)) || "
                    + "execution(* com.razorpay.threeds.hsm..*(..))"
                    + ")";

    private static final String BLACKLISTED_PACKAGES_DEBUG =
            "!("
                    + "execution(* com.razorpay.acs.dao.enums..*(..))"
                    + ")"; // Added repository as example, need change it once we have use case
    /***
     * This contains all the packages which should
     * not be generated at INFO level
     */
    private static final String WHITELISTED_PACKAGES_INFO =
            "("
                    + "execution(* com.razorpay.threeds.service..*(..)) || "
                    + "execution(* com.razorpay.threeds.controller..*(..))"
                    + ")";

    private static final String BLACKLISTED_PACKAGES_INFO =
            "!(execution(* com.razorpay.threeds.service.authvalue..*(..)))";

    private static final String INFO_POINTCUTS =
            WHITELISTED_PACKAGES_INFO + " && " + BLACKLISTED_PACKAGES_INFO;
    private static final String DEBUG_POINTCUTS =
            WHITELISTED_PACKAGES_DEBUG + " && " + BLACKLISTED_PACKAGES_DEBUG;

    @Around(value = DEBUG_POINTCUTS)
    public Object logAroundInDebugMode(final ProceedingJoinPoint joinPoint) throws Throwable {
        return around(joinPoint, LogLevel.DEBUG);
    }

    @Around(value = INFO_POINTCUTS)
    public Object logAroundInInfoMode(final ProceedingJoinPoint joinPoint) throws Throwable {
        return around(joinPoint, LogLevel.INFO);
    }

    private Object around(final ProceedingJoinPoint joinPoint, LogLevel logLevel) throws Throwable {

        logMethodEntry(joinPoint, logLevel);

        final Instant enteringTime = Instant.now();
        final Object returnValue = joinPoint.proceed();

        logMethodExit(
                joinPoint,
                returnValue,
                Duration.between(enteringTime, Instant.now()).toMillis(),
                logLevel);

        return returnValue;
    }

    private void logMethodEntry(final JoinPoint joinPoint, final LogLevel level) {

        final Signature signature = joinPoint.getSignature();

        final String[] methodParameterNames = ((CodeSignature) signature).getParameterNames();
        Log(
                level,
                "ENTERED: METHOD_NAME={}; PARAMETER={};",
                getClassNameWithMethodName(signature),
                getParameterNamesWithTheirValues(methodParameterNames, joinPoint.getArgs()));
    }

    private String getParameterNamesWithTheirValues(
            final String[] methodParameterNames, final Object[] parameterValues) {

        if (methodParameterNames != null) {
            return IntStream.range(0, methodParameterNames.length)
                    .boxed()
                    .map(i -> methodParameterNames[i] + " = " + parameterValues[i])
                    .collect(Collectors.joining(", ", "[ ", " ]"));
        }
        return "[]";
    }

    private void logMethodExit(
            final JoinPoint joinPoint,
            final Object returnValue,
            final Long executionTime,
            final LogLevel level) {

        Log(
                level,
                "EXITED: METHOD_NAME={}; RETURN={}; TIME_TAKEN={}ms;",
                getClassNameWithMethodName(joinPoint.getSignature()),
                abbreviate(returnValue),
                executionTime);
    }

    private void Log(LogLevel level, String message, Object... vars) {
        switch (level) {
            case INFO:
                log.info(message, vars);
            case DEBUG:
                log.debug(message, vars);
        }
    }

    private String getClassNameWithMethodName(final Signature signature) {
        return signature.getDeclaringTypeName() + "." + signature.getName();
    }

    // Todo Add logic to remove PII data
    private Object abbreviate(Object returnValue) {

        if (Objects.nonNull(returnValue)) {
            String stringValue = returnValue.toString();
            if (stringValue.length() > logSizeLimit) {
                return DEFAULT_MASKED_LOG_MSG;
            }
            return stringValue;
        } else {
            return null;
        }
    }
}
