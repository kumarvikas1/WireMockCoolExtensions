package com.stubs.cool_extensions.glue;

import com.github.tomakehurst.wiremock.http.Request;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

/**
 * Created by vikakumar on 8/7/16.
 */
public class JavaScriptHelper {

    public static String getJSONValue(Object object, String value) {
        return ((Request) object).queryParameter(value).values().get(0);
    }

    public static String getXMLValue(Object object, String value) {
        return getXMLValue(value, (Request) object);
    }

    private static String getXMLValue(String value, Request request) {
        String retval = "";
        try {
            JSONObject xmlJSONObj = XML.toJSONObject(request.getBodyAsString());
            Object document = Configuration.defaultConfiguration().jsonProvider().parse(xmlJSONObj.toString());
            retval = JsonPath.read(document, value).toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return retval;
    }
}
