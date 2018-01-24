package com.comcast.test.citf.common.cima.jsonObjs.ser;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.joda.time.Instant;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

/**
 * Class to deserialize time to unix seconds.
 * 
 * @author Valdas Sevelis
 */
public class InstantAsUnixSecondsDeserializer extends StdDeserializer<Instant> {

	protected InstantAsUnixSecondsDeserializer() {
		super(Instant.class);
	}

	/**
	 * Returns deserialized unix time.
	 */
	@Override
	public Instant deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
		return new Instant(
				TimeUnit.MILLISECONDS.convert(
						p.getNumberValue().longValue(),
						TimeUnit.SECONDS));
	}
}
