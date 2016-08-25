package com.stubs.cool_extensions.freemaker;

import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.Response;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import java.io.IOException;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by vikakumar on 8/24/16.
 */
public class ResponseMaker {

    private Response response;
    private Request request;


    ResponseMaker(Response response, Request request) {
        this.response = response;
        this.request = request;
    }


    public Response evaluateLogic() {
        try {
            Map<String, String> queryParams = getParams();
            Template template = new Template("WireMock", response.getBodyAsString(), new Configuration(Configuration.VERSION_2_3_23));
            Map<String, Object> data = new HashMap<>();
            data.put("request", queryParams);
            StringWriter writer = new StringWriter();
            template.process(data, writer);
            return response.response().body(writer.toString()).build();
        } catch (URISyntaxException | IOException | TemplateException e) {
            return Response.notConfigured();
        }
    }

    private Map<String, String> getParams() throws URISyntaxException {
        List<NameValuePair> params = URLEncodedUtils.parse(new URI(request.getAbsoluteUrl()), "UTF-8");
        return params.stream().collect(Collectors.toMap(f -> f.getName(), f -> f.getValue(), (f1, f2) -> f1));
    }


    public static ResponseMaker of(Response response, Request request) {
        return new ResponseMaker(response, request);
    }

}
