package com.supporthub.dataprovider;

import java.io.File;
import java.io.IOException;

import io.qameta.allure.internal.shadowed.jackson.databind.JsonNode;
import io.qameta.allure.internal.shadowed.jackson.databind.ObjectMapper;

public class JSONDataReader {

	private static final String JSON_FILE_PATH = "src/test/resources/testdata/loginData.json";

	public static String getData(String workspaceKey, String fieldName) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode rootNode = objectMapper.readTree(new File(JSON_FILE_PATH));
			return rootNode.path(workspaceKey).path(fieldName).asText();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to read loginData.json file: " + e.getMessage());
		}
	}
}