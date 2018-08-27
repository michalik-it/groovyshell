package com.stanusch.gscripts;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

public class RestInvoker {
    private static Logger LOG = LoggerFactory.getLogger(RestInvoker.class);

    /**
     * Used in grovvy inline scripts
     * @param url
     * @return
     */
    public JsonObject call(String url) {
        String json = new RestTemplate().getForObject(url, String.class);
        LOG.info("Calling: " + url +"... Json as string: " + json);
        return new Gson().fromJson(json, JsonObject.class);
    }
}
