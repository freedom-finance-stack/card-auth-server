package org.freedomfinancestack.razorpay.cas.acs.context.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.freedomfinancestack.razorpay.cas.acs.context.RequestContext;
import org.freedomfinancestack.razorpay.cas.acs.context.RequestContextHolder;
import org.freedomfinancestack.razorpay.cas.acs.utils.Util;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component("requestInterceptor")
public class RequestInterceptor implements AsyncHandlerInterceptor {

    @Override
    public boolean preHandle(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            Object handler)
            throws Exception {

        RequestContext requestContext = new RequestContext(Util.generateUUID());
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
