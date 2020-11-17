package com.realpage.tvmaze.repositories.memrepo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class ShowRepositoryInMemory {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final ArrayNode jArrayShows = mapper.createArrayNode();

    public ArrayNode findAll() {
        ArrayNode array = mapper.createArrayNode();
        jArrayShows.forEach(array::add);
        return jArrayShows;
    }

    public void save(JsonNode root) {
        if (root.isArray()) {
            for (final JsonNode objNode : root) {
                jArrayShows.add(objNode);
            }
        }
    }
}
