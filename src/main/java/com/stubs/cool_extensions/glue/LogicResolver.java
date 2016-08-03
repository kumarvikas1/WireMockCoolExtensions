package com.stubs.cool_extensions.glue;

import com.github.tomakehurst.wiremock.http.Request;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import org.json.JSONObject;
import org.json.XML;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.List;
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


    @Logic(exp = "(.*)given ([^\"]*) equals ([^\"]*) then ([^\"]*) else ([^\"]*)\\$}(.*)")
    public String given_then_else(String exp) {
        Matcher match = Pattern.compile(exp).matcher(Body);
        match.find();

        return request.queryParameter(match.group(2)).values().get(0).equals(match.group(3)) ?
                updateBody(Body, match.group(4), "if") :
                updateBody(Body, match.group(5), "if");
    }

    @Logic(exp = "(.*)key ([^\"]*)\\$}(.*)")
    public String key(String exp) {
        Matcher match = Pattern.compile(exp).matcher(Body);
        match.find();
        return request.getBodyAsString().startsWith("<") ? updateBody(Body, getXMLValue(match.group(2)), "key")
                :
                updateBody(Body, request.queryParameter(match.group(2)).values().get(0), "key");
    }

    private String getXMLValue(String value) {
        try {
            JSONObject xmlJSONObj = XML.toJSONObject(request.getBodyAsString());
            List<String> test = Splitter.on(".").splitToList(value);
            Object ob = xmlJSONObj;
            for (String s : test) {
                ob = ((JSONObject) ob).get(s);
            }
            return ob.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    @Logic(exp = "(.*)given ([^\"]*) equals ([^\"]*) then show(.*)\\$}")
    public String show_only_when(String exp) {
        Matcher match = Pattern.compile(exp, Pattern.DOTALL).matcher(Body);
        match.find();
        return request.queryParameter(match.group(2)).values().get(0).equals(match.group(3)) ?
                updateBody(Body, match.group(4), "show") :
                updateBody(Body, "", "show");
    }

    @Logic(exp = "(.*)<script>\\$\\{(.*)\\$\\}<script>")
    public String java_script(String exp) {
        Matcher match = Pattern.compile(exp).matcher(Body);
        match.find();
        return updateBody(Body, executeScript(match.group(2)), "<script>");
    }


    private String updateBody(String test, String replace, String text) {
        return Pattern.compile(Joiner.on("").join(text, "\\$(.*)\\$\\}", text), Pattern.DOTALL).matcher(test).replaceAll(replace);
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
