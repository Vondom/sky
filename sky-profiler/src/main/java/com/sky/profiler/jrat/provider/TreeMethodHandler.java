package com.sky.profiler.jrat.provider;

import org.shiftone.jrat.core.MethodKey;
import org.shiftone.jrat.core.spi.MethodHandler;
import org.shiftone.jrat.util.Assert;

/**
 * @author jeff@shiftone.org (Jeff Drost)
 */
public class TreeMethodHandler implements MethodHandler {

    private final TreeMethodHandlerFactory factory;
    private final MethodKey methodKey;

    public TreeMethodHandler(TreeMethodHandlerFactory factory, MethodKey methodKey) {
        Assert.assertNotNull("factory", factory);
        Assert.assertNotNull("methodKey", methodKey);

        this.factory = factory;
        this.methodKey = methodKey;
    }

    @Override
    public void onMethodStart() {
        Delegate delegate = factory.getDelegate();
        delegate.onMethodStart(methodKey);
    }

    @Override
    public void onMethodFinish(long durationNanos, Throwable throwable) {
        Delegate delegate = factory.getDelegate();
        delegate.onMethodFinish(methodKey, durationNanos, throwable == null);
    }
}
