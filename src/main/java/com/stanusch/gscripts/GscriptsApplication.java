package com.stanusch.gscripts;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.URISyntaxException;
import java.util.Map;

@SpringBootApplication
public class GscriptsApplication implements CommandLineRunner {

    private static final String SCRIPT_1_EXAMPLE = "def result = invoker.call('https://httpbin.org/get');header = result.getAsJsonObject(\"headers\").get(\"Accept\");";
    private static Logger LOG = LoggerFactory.getLogger(GscriptsApplication.class);

    public static void main(String[] args) {
        LOG.info("STARTING THE APPLICATION");
        SpringApplication.run(GscriptsApplication.class, args);
        LOG.info("APPLICATION FINISHED");
    }

    @Override
    public void run(String... args) throws URISyntaxException {
        LOG.info("EXECUTING : command line runner");

        for (int i = 0; i < args.length; ++i) {
            LOG.info("args[{}]: {}", i, args[i]);
        }


        testCall();

    }

    public void testCall() {
        ScriptInvoker scriptInvoker = new ScriptInvoker();

        Map<String, Object> outputVariables = scriptInvoker.call(SCRIPT_1_EXAMPLE, null);

        LOG.info("Header is: " + outputVariables.get("header"));
    }

}
