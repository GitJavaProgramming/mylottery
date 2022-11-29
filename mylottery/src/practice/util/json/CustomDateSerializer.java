package practice.util.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: 45554
 * Date: 16-5-8
 * Time: 上午9:51
 * To change this template use File | Settings | File Templates.
 */
public class CustomDateSerializer extends JsonSerializer<Date> {

    /**
     *  日期格式转换
     *  eg.
     *  @JsonSerialize(using = CustomDateSerializer.class)
     *  public Date getCreateAt() {
     *      return createAt;
     *  }
     * @param o
     * @param jsonGenerator
     * @param serializerProvider
     * @throws IOException
     * @throws com.fasterxml.jackson.core.JsonProcessingException
     */
    @Override
    public void serialize(Date o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = formatter.format(o);
        jsonGenerator.writeString(formattedDate);
    }

}
