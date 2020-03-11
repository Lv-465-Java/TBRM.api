package com.softserve.rms.exceptions.handler;

import com.softserve.rms.controller.ResourceTemplateController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;

public class CustomAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(CustomAsyncExceptionHandler.class);

    @Override
    public void handleUncaughtException(Throwable ex, Method method, Object... params) {
        LOG.info("Exception message: " + ex.getMessage());
        LOG.info("Method name: " + method.getName());
        for (Object param: params) {
            LOG.info("Parameter value: " + param);
        }
    }
}
