package com.tcg.api.core.common;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class JsonUtil {

	private static ObjectMapper mapper = new ObjectMapper();
	
	static {
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}
	private JsonUtil() {}

	public static String toJson(Object obj) {
		if (obj == null) {
			return null;
		}
		try {
			return mapper.writeValueAsString(obj);
		} catch (IOException e) {
			throw new RuntimeException("Json serialization error.", e);
		}
	}

	public static <T> T fromJson(String json, Class<T> clazz) {
		if (StringUtil.isEmpty(json)) {
			return null;
		}
		try {
			return mapper.readValue(json, clazz);
		} catch (JsonMappingException e) {
			throw new RuntimeException("Json deserialization error.", e);
		} catch (IOException e) {
			throw new RuntimeException("Json io error.", e);
		}
	}

	public static <T> List<T> fromJsonToList(String json, Class<T> clazz) {
		if (StringUtil.isEmpty(json)) {
			return null;
		}
		try {
			return mapper.readValue(json, mapper.getTypeFactory()
					.constructCollectionType(List.class, clazz));
		} catch (JsonMappingException e) {
			throw new RuntimeException("Json deserialization error.", e);
		} catch (IOException e) {
			throw new RuntimeException("Json io error.", e);
		}
	}

	public static String map2Json(Map<String, Object> map) {
		return toJson(map);
	}
	
}
