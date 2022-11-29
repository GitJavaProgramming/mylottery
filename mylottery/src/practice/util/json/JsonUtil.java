package practice.util.json;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.ArrayType;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 对象转换json(格式化)工具类
 */
public class JsonUtil {

    private static ObjectMapper mapper = new ObjectMapper();

    // note:http://blog.csdn.net/sdyy321/article/details/40298081
    // http://jackyrong.iteye.com/blog/2005323
    static {
        // 是否环绕根元素，默认false，如果为true，则默认以类名作为根元素，你也可以通过@JsonRootName来自定义根元素名称
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        // SerializationFeature.INDENT_OUTPUT：是否缩放排列输出，默认false，有些场合为了便于排版阅读则需要对输出做缩放排列
        mapper.configure(SerializationFeature.INDENT_OUTPUT, false);
        // SerializationFeature.WRITE_DATES_AS_TIMESTAMPS：序列化日期时以timestamps输出，默认true
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
//        mapper.configure(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS, true);
        mapper.configure(SerializationFeature.WRITE_ENUMS_USING_TO_STRING, false);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        mapper.setDateFormat(formatter);

//        SimpleModule module = new SimpleModule();
//        module.addSerializer(Date.class, new CustomDateSerializer());
////        module.addSerializer(Number.class, new CustomNumberSerializer());
//        mapper.registerModule(module);
    }

    /**
     * 序列化对象
     *
     * @param obj 要进行格式化的对象
     * @return 对象的json格式字符串
     */
    public static String serializable(Object obj) {
        StringWriter writer = new StringWriter();
        try {
            mapper.writeValue(writer, obj);
            return writer.toString();
        } catch (JsonGenerationException e) {
        } catch (JsonMappingException e) {
        } catch (IOException e) {
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
            }
        }
        return null;
    }

    /**
     * 将json格式字符串转换成java对象
     *
     * @param clazz 要转换成的实例类型（类）
     * @param str   json格式字符串
     * @param <T>   类型参数，编译时限定类型
     * @return 转换成的java对象
     */
    public static <T> T unSerializable(Class<T> clazz, String str) {
        try {
            if (str == null) return null;
            T object = mapper.readValue(str, clazz);
            return object;
        } catch (JsonMappingException e) {
        } catch (JsonGenerationException e) {
        } catch (IOException e) {
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * jackson1 用法
     * java类型标记，包含java类型信息
     *
     * @param collectionClass 集合类型
     * @param elementClasses  元素类型
     * @return
     */
    private static JavaType getReferenceType(Class<?> collectionClass, Class<?>... elementClasses) {
        return mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }

    private static ArrayType getArrayType(Class<?> elementType) {
        return mapper.getTypeFactory().constructArrayType(elementType);
    }

    private static CollectionType getCollectionType(Class<? extends Collection> collectionClass, Class<?> elementClasses) {
        return mapper.getTypeFactory().constructCollectionType(collectionClass, elementClasses);
    }

    private static MapType getMapType(Class<? extends Map> mapClass, Class<?> keyClass, Class<?> valueClass) {
        return mapper.getTypeFactory().constructMapType(mapClass, keyClass, valueClass);
    }

    /**
     * json字符串转换成数组
     *
     * @param t   数组元素类型
     * @param str json字符串
     * @param <T>
     * @return 元素类型为T的数组
     */
    public static <T> T[] unSerializableArray(Class<T> t, String str) {
        try {
            if (str == null || "".equals(str)) return null;
            JavaType javaType = getArrayType(t);
            T[] array = mapper.readValue(str, javaType);
            return array;
        } catch (JsonMappingException e) {
        } catch (JsonGenerationException e) {
        } catch (IOException e) {
        }
        return null;
    }

    public static <K, T> Map<K, T> unSerializableHashMap(Class<K> keyClazz, Class<T> valueClazz, String str) {
        try {
            if (str == null || "".equals(str)) return null;
            JavaType javaType = getMapType(HashMap.class, keyClazz, valueClazz);
            Map<K, T> object = mapper.readValue(str, javaType);
            return object;
        } catch (JsonMappingException e) {
        } catch (JsonGenerationException e) {
        } catch (IOException e) {
        }
        return null;
    }

    /**
     * json字符串转换成java.keno.util.List
     *
     * @param clazz 集合元素类型
     * @param str   json字符串
     * @param <T>   类型参数
     * @return List对象
     */
    public static <T> List<T> unSerializableList(Class<T> clazz, String str) {
        try {
            if (str == null) return null;
//            JavaType javaType = getReferenceType(ArrayList.class, clazz);
            CollectionType collectionType = getCollectionType(ArrayList.class, clazz);
            List<T> object = mapper.readValue(str, collectionType);
            return object;
        } catch (JsonMappingException e) {
        } catch (JsonGenerationException e) {
        } catch (IOException e) {
        }
        return null;
    }

    public static <T> Set<T> unSerializableSet(Class<T> clazz, String str) {
        try {
            if (str == null) return null;
            JavaType javaType = getReferenceType(HashSet.class, clazz);
            Set<T> object = mapper.readValue(str, javaType);
            return object;
        } catch (JsonMappingException e) {
        } catch (JsonGenerationException e) {
        } catch (IOException e) {
        }
        return null;
    }

    /**
     * json字符串转换成java.keno.util.Map
     *
     * @param keyClazz   键类型
     * @param valueClazz 值类型
     * @param str        json字符串
     * @param <K>
     * @param <T>
     * @return Map对象
     */
    public static <K, T> Map<K, T> unSerializableMap(Class<K> keyClazz, Class<T> valueClazz, String str) {
        try {
            if (str == null || "".equals(str)) return null;
            JavaType javaType = getReferenceType(Map.class, keyClazz, valueClazz);
            Map<K, T> object = mapper.readValue(str, javaType);
            return object;
        } catch (JsonMappingException e) {
        } catch (JsonGenerationException e) {
        } catch (IOException e) {
        }
        return null;
    }

    public static ObjectNode createObjectNode() {
        return mapper.createObjectNode();
    }

    public static JsonNode createJsonNode(String content) throws IOException {
        return mapper.readTree(content);
    }

    public static ArrayNode createArrayNode() {
        return mapper.createArrayNode();
    }

//    public static void bindBean(ObjectNode node, Object bean) {
//        if(node.isNull()) {
//            return;
//        }
//        Iterator<String> iterator = node.fieldNames();
//        for(;iterator.hasNext();) {
//            String fieldName = iterator.next();
//            GLog.logger.info("fieldName:{}", fieldName);
//        }
//    }

    class CustomNumberSerializer extends JsonSerializer<Number> {
        @Override
        public void serialize(Number value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
            if(value instanceof BigDecimal) {
                ((BigDecimal) value).setScale(2);
//                gen.writeString(value);
            }
        }
    }
}
