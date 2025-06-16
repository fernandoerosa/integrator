package br.com.wmw;

import org.apache.camel.builder.RouteBuilder;

public class SqlIntegration extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("sql:select * from product")
            .log("${body}");
    }
    
}
