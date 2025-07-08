package br.com.wmw;

import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class LogicaDeNegocio implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        Map<String, String> body = exchange.getIn().getBody(Map.class);

        body.get("");
    }
    
}
