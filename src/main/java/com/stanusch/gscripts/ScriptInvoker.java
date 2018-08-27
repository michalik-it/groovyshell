package com.stanusch.gscripts;

import groovy.lang.*;

import java.util.HashMap;
import java.util.Map;

public class ScriptInvoker {

    public Map call(String gscript) {
        return this.call(gscript, null);
    }

    public Map call(String gscript, Map<String, Object> inputVariables) {
        Binding binding = new Binding();

        if (inputVariables == null) {
            inputVariables = new HashMap<>();
        }
        inputVariables.putAll(getDefaultVariables());
        inputVariables.forEach(binding::setVariable);

        GroovyShell shell = new GroovyShell(binding);
        shell.evaluate(gscript);

        return binding.getVariables();
    }

    private Map<String, Object> getDefaultVariables() {
        RestInvoker restInvoker = new RestInvoker();
        Map<String, Object> inputVariables = new HashMap<>();
        inputVariables.put("invoker", restInvoker);
        return inputVariables;
    }
}
