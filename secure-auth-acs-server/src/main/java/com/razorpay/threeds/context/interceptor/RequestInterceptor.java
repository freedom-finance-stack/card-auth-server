package com.razorpay.threeds.context.interceptor;

import com.razorpay.threeds.context.RequestContext;
import com.razorpay.threeds.context.RequestContextHolder;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component("requestInterceptor")
public class RequestInterceptor implements AsyncHandlerInterceptor {

    @Override
    public boolean preHandle(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            Object handler)
            throws Exception {

        // todo fix this unique guid generation
        RequestContext requestContext = new RequestContext("some-unique-guid-here");
        RequestContextHolder.set(requestContext);

        return AsyncHandlerInterceptor.super.preHandle(
                httpServletRequest, httpServletResponse, handler);
    }

    @Override
    public void afterConcurrentHandlingStarted(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            Object handler)
            throws Exception {

        RequestContextHolder.reset();
        AsyncHandlerInterceptor.super.afterConcurrentHandlingStarted(
                httpServletRequest, httpServletResponse, handler);
    }

    @Override
    public void postHandle(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            Object handler,
            ModelAndView modelAndView)
            throws Exception {

        RequestContextHolder.reset();
        AsyncHandlerInterceptor.super.postHandle(
                httpServletRequest, httpServletResponse, handler, modelAndView);
    }

    @Override
    public void afterCompletion(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            Object handler,
            Exception exception)
            throws Exception {

        RequestContextHolder.reset();
        AsyncHandlerInterceptor.super.afterCompletion(
                httpServletRequest, httpServletResponse, handler, exception);
    }
}
