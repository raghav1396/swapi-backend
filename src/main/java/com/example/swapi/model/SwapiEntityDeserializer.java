package com.example.swapi.model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SwapiEntityDeserializer extends JsonDeserializer<SwapiEntity> {
  @Override
  public SwapiEntity deserialize(JsonParser parser, DeserializationContext context) throws IOException, JsonProcessingException {
    JsonNode node = parser.getCodec().readTree(parser);
    String url = node.get("url").asText();
    String name = node.get("name").asText();
    int id = -1;
    Pattern pattern = Pattern.compile(".*/(\\d+)/");
    Matcher matcher = pattern.matcher(url);
    if (matcher.find()) {
      id = Integer.parseInt(matcher.group(1));
    }

    List<String> films = new ArrayList<>();

    JsonNode filmsNode = node.get("films");
    if (filmsNode != null && filmsNode.isArray()) {
      filmsNode.forEach(n -> films.add(n.asText()));
    }

    List<Integer> filmIds = films.stream()
      .map(pattern::matcher)
      .filter(Matcher::find)
      .map(filmMatcher -> filmMatcher.group(1))
      .map(Integer::parseInt)
      .toList();
    return SwapiEntity.builder().name(name).id(id).films(filmIds).build();
  }
}
