package com.razorpay.ffs.cas.acs.context;

public class RequestContextHolder {
    private static final ThreadLocal<RequestContext> CTX = new ThreadLocal<>();

    public static RequestContext get() {
        return CTX.get();
    }

    public static void set(RequestContext ctx) {
        CTX.set(ctx);
    }

    public static void reset() {
        CTX.remove();
    }
}
