package com.stubs.cool_extensions.start;

import org.springframework.stereotype.Component;

/**
 * Created by vikakumar on 7/30/16.
 */
@Component
public class PrintPortStartAction implements StartAction {

    @Override
    public void run() {
        System.out.println("Hi How are you");
    }
}
