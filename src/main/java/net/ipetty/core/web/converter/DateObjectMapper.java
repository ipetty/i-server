package net.ipetty.core.web.converter;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.ser.CustomSerializerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DateObjectMapper
 * 
 * @author luocanfeng
 * @date 2014年6月19日
 */
public class DateObjectMapper extends ObjectMapper {

	private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	Logger logger = LoggerFactory.getLogger(getClass());

	public DateObjectMapper() {
		super.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

		CustomSerializerFactory factory = new CustomSerializerFactory();
		factory.addGenericMapping(Date.class, new JsonSerializer<Date>() {
			@Override
			public void serialize(Date value, JsonGenerator jsonGenerator, SerializerProvider provider)
					throws IOException, JsonProcessingException {
				jsonGenerator.writeString(dateFormat.format(value));
			}
		});
		this.setSerializerFactory(factory);
	}

}
