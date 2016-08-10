package com.stubs.cool_extensions.start;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by vikakumar on 8/10/16.
 */
@Component("startActionsExecutor")
public class StartActonExecutor {

    List<StartAction> startActionList;

    @Autowired
    StartActonExecutor(List<StartAction> startActionList) {
        this.startActionList = startActionList;
    }

    public List<StartAction> getStartActions() {
        return startActionList;
    }
}
