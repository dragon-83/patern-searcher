package pl.dryja.patternsearcher;

import com.fasterxml.jackson.databind.ObjectMapper;

public class TestUtil {

    public static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T jsonBytesToObject(final byte[] bytes, Class<T> tClass) {

        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper. readValue(new String(bytes), tClass);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
