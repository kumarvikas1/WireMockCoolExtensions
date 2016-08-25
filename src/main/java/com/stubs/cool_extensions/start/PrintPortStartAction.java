package com.stubs.cool_extensions.start;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by vikakumar on 7/30/16.
 */
@Component
public class PrintPortStartAction implements StartAction {

    private static final Logger LOGGER = LoggerFactory.getLogger(PrintPortStartAction.class);

    @Override
    public void run() {
        LOGGER.info("Hi How are you");
    }
}
