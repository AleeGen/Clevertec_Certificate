package ru.clevertec.ecl.service.util.patch;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;
import org.springframework.util.ReflectionUtils;
import ru.clevertec.ecl.exception.UtilException;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.Objects;

/**
 * Utility class for generating {@link PatchResponse} by {@link PatchRequest}.
 */
@UtilityClass
public class Patch {

    /**
     * Method for generating {@link PatchResponse} by {@link PatchRequest} and {@link Class}
     *
     * @param patch incoming update {@link PatchResponse}
     * @param clazz updated class
     * @return {@link PatchRequest}
     * @throws UtilException if the specified field does not exist or the value does not match the json format
     */
    public static PatchResponse execute(PatchRequest patch, Class<?> clazz) {
        Field field = ReflectionUtils.findField(clazz, patch.field());
        if (Objects.isNull(field)) {
            throw new UtilException(String.format("Element '%s' not found. ", patch.field()));
        }
        String valueStr = patch.value().toString();
        ObjectMapper mapper = new ObjectMapper();
        try {
            Object value = Collection.class.isAssignableFrom(field.getType()) ?
                    mapper.readValue(valueStr, mapper.getTypeFactory().constructCollectionType(Collection.class,
                            (Class<?>) ((ParameterizedType) (field.getGenericType())).getActualTypeArguments()[0])) :
                    mapper.readValue(valueStr, field.getType());
            return new PatchResponse(field, value);
        } catch (JacksonException eJson) {
            throw new UtilException(String.format("Json fail: %s", eJson.getMessage()));
        }
    }

}