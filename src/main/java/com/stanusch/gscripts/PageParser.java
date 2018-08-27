package com.stanusch.gscripts;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class PageParser {
    private static Logger LOG = LoggerFactory.getLogger(PageParser.class);

    /**
     * Used in grovvy inline scripts
     * @param url
     * @return
     */
    public Document parse(String url) {
        try {
            LOG.info("Connecting: " + url);
            return Jsoup.connect(url).get();
        } catch (IOException e) {
            LOG.error("Error", e);
            return null;
        }
    }
}
