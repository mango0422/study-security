package mango.security.authorizationServer.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import java.io.IOException;
import java.time.Instant;

public class CustomInstantModule extends SimpleModule {
    public CustomInstantModule() {
        // Instant를 문자열로 직렬화: Instant.toString()은 ISO-8601 형식의 문자열 반환
        addSerializer(Instant.class, new JsonSerializer<Instant>() {
            @Override
            public void serialize(Instant value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                gen.writeString(value.toString());
            }
        });
    }
}
