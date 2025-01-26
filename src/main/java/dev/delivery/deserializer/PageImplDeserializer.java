package dev.delivery.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class PageImplDeserializer<T> extends JsonDeserializer<PageImpl<T>> {

    private final Class<T> contentClass;

    public PageImplDeserializer(Class<T> contentClass) {
        this.contentClass = contentClass;
    }

    @Override
    public PageImpl<T> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        ObjectMapper mapper = (ObjectMapper) p.getCodec();
        JsonNode node = mapper.readTree(p);
        JsonNode contentNode = node.get("content");
        List<T> content = new ArrayList<>();
        if (contentNode.isArray()) {
            for (JsonNode elementNode : contentNode) {
                T element = mapper.readValue(elementNode.toString(), contentClass);
                content.add(element);
            }
        }
        JsonNode pageableNode = node.get("pageable");
        Pageable pageable = PageRequest.of(
                pageableNode.get("pageNumber").asInt(),
                pageableNode.get("pageSize").asInt()
        );
        JsonNode totalElementsNode = node.get("totalElements");
        long totalElements = totalElementsNode.asLong();

        return new PageImpl<>(content, pageable, totalElements);
    }
}
