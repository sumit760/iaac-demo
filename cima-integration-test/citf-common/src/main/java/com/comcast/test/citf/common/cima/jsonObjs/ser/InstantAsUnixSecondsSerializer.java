package com.comcast.test.citf.common.cima.jsonObjs.ser;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.joda.time.Instant;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 * Class to serialize instant time.
 * 
 * @author Valdas Sevelis
 *
 */
public class InstantAsUnixSecondsSerializer extends StdSerializer<Instant> {

	public InstantAsUnixSecondsSerializer() {
		super(Instant.class);
	}

	/**
	 * Serializes instant time.
	 */
	@Override
	public void serialize(Instant value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		gen.writeNumber(
				TimeUnit.SECONDS.convert(
						value.getMillis(),
						TimeUnit.MILLISECONDS));
	}
}
