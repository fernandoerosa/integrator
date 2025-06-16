package br.com.wmw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;

import io.quarkus.logging.Log;

public class Integration extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        
        from("timer:tick?period=60000")
            .setHeader(Exchange.HTTP_METHOD, constant(org.apache.camel.component.http.HttpMethods.POST))
            .setHeader("appkey", constant("9b483254-a617-40a6-aee6-dbc9bd81058d"))
            .setHeader("password", constant("Wmwskw@!"))
            .setHeader("token", constant("6156c0ee-8a26-4514-a450-743a4096aced"))
            .setHeader("username", constant("sankhyaom@wmw.com.br"))
            .to("https://api.sandbox.sankhya.com.br/login")
            .unmarshal().json()
            .process(exchange -> {
                Map<String, Object> body = exchange.getIn().getBody(Map.class);

                Log.info(body.get("bearerToken"));

                exchange.getIn().setHeader("Authorization", "Bearer " + body.get("bearerToken"));
            })
            .setHeader(Exchange.HTTP_METHOD, constant(org.apache.camel.component.http.HttpMethods.POST))
            .setHeader("Content-Type", constant("application/json"))
            .setBody(simple("""
                    {
                        "serviceName": "CRUDServiceProvider.loadRecords",
                        "requestBody": {
                        "dataSet": {
                            "rootEntity": "Cidade",
                            "includePresentationFields": "S",
                            "offsetPage": "0",
                            "criteria": {
                            "expression": {

                            },
                            "parameter":[
                                {
                                    "type":"I",
                                    "value":"490"
                                }
                            ]
                            },
                            "entity": {
                            "fieldset": {
                                    "list": "CODCID,NOMECID,UF"
                                    }
                                }
                            }
                        }
                    }
                    """))
            .to("https://api.sandbox.sankhya.com.br/gateway/v1/mge/service.sbr?serviceName=CRUDServiceProvider.loadRecords&outputType=json")
            .to("mongodb:dataSource?database=wforce&collection=user&operation=findAll");

        from("direct:parse-doc")
            .process(exchange -> {
               
            })
            .process(exchange -> {
 
            });
    }
}
