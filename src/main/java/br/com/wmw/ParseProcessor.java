package br.com.wmw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class ParseProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        Map<String, Object> body = exchange.getIn().getBody(Map.class);

        List<String> fields = List.of("CODCID", "NOMECID", "UF", "UnidadeFederativa_UF");

        Map<String, Object> responseBody = (Map<String, Object>) body.get("responseBody");
        Map<String, Object> entityBody = (Map<String, Object>) responseBody.get("entities");

        List<Map<String, Map<String, Map>>> entitiesBody = (List) entityBody.get("entity");

        List<Map<String, Object>> entityListParsed = new ArrayList<>();

        entitiesBody.forEach(entity -> {
            Map<String, Object> entityData = new HashMap<>();
            entity.forEach((key, value) -> {
                int index = Integer.parseInt(key.replace("f", ""));

                entityData.put(fields.get(index), value.get("$"));
            });

            entityListParsed.add(entityData);
        });

        List<Map<String, Object>> newBody = new ArrayList<>();

        entityListParsed.forEach(item -> {
            Map<String, Object> newItem = new HashMap<>();
            newItem.put("CDCIDADE", item.get("CODCID"));
            newItem.put("NOME", item.get("NOMECID"));
            newItem.put("NOMEUF", item.get("UF"));
            newItem.put("UF_UF", item.get("UnidadeFederativa_UF"));

            newBody.add(newItem);
        });

        exchange.getIn().setBody(newBody);
    }

}
