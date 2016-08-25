package com.stubs.cool_extensions.glue;

import com.github.tomakehurst.wiremock.http.Request;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by vikakumar on 6/27/16.
 */
public class LogicResolver implements AbstractLogicResolver {
    final String XML_PATTERN_STR = "<.*>";

    String Body;
    Request request;


    public LogicResolver(String Body, Request request) {
        this.Body = Body;
        this.request = request;
    }

    public void setBody(String body) {
        this.Body = body;
    }

    @Logic(exp = "if#given ([^\"]*) equals ([^\"]*) then ([^\"]*) else ([^\"]*)#if")
    public String given_then_else(String exp) {
        Matcher match = Pattern.compile(exp).matcher(Body);
        String retval = Body;
        while (match.find()) {
            String orig = match.group();
            String value = match.group(1);
            if (request.getBodyAsString().startsWith("<")) {
                retval = getXMLValue(value).equals(match.group(2)) ?
                        updateBody(retval, match.group(3), orig) :
                        updateBody(retval, match.group(4), orig);
            } else {
                retval = request.queryParameter(match.group(1)).values().get(0).equals(match.group(2)) ?
                        updateBody(retval, match.group(3), orig) :
                        updateBody(retval, match.group(4), orig);
            }
        }
        return retval;
    }

    @Logic(exp = "key#key (.*?)#key")
    public String key(String exp) {
        Matcher match = Pattern.compile(exp).matcher(Body);
        String retval = Body;
        while (match.find()) {
            String orig = match.group();
            String value = match.group(1);
            if (request.getBodyAsString().startsWith("<")) {
                retval = updateBody(retval, getXMLValue(value), orig);
            } else {
                retval = updateBody(retval, request.queryParameter(value).values().get(0), orig);
            }
        }
        return retval;
    }

    @Logic(exp = "show#given ([^\"]*) equals ([^\"]*) then show(.*?)#show")
    public String show_only_when(String exp) {
        Matcher match = Pattern.compile(exp, Pattern.DOTALL).matcher(Body);
        String retval = Body;
        while (match.find()) {
            String orig = match.group();
            String value = match.group(1);
            if (request.getBodyAsString().startsWith("<")) {
                retval = getXMLValue(value).equals(match.group(2)) ?
                        updateBody(retval, match.group(3), orig) :
                        updateBody(retval, "", orig);
            } else {
                retval = request.queryParameter(value).values().get(0).equals(match.group(2)) ?
                        updateBody(retval, match.group(3), orig) :
                        updateBody(retval, "", orig);
            }
        }
        return retval;
    }


    private String updateBody(String body, String value, String replaceText) {
        return body.replaceAll(Pattern.quote(replaceText), Matcher.quoteReplacement(value));
    }


    private String getXMLValue(String value) {
        return JavaScriptHelper.getXMLValue(request, value);
    }


    @Logic(exp = "#script(.*)#script")
    public String java_script(String exp) {
        Matcher match = Pattern.compile(exp).matcher(Body);
        match.find();
        String orig = match.group();
        return updateBody(Body, executeScript(match.group(1)), orig);
    }


    private String executeScript(String script) {
        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
        ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("nashorn");
        try {
            scriptEngine.eval(script);
            Invocable invocable = (Invocable) scriptEngine;
            Object result = invocable.invokeFunction("value", request);
            return result.toString();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

}
