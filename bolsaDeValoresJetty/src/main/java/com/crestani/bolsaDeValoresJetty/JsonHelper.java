package com.crestani.bolsaDeValoresJetty;

import com.google.gson.Gson;

/**
 * Encapsula as funções de conversão json
 * 
 * @author fabio
 *
 */
public class JsonHelper {
	private static Gson gson = new Gson();

	public static <T> Object fromJson(String json, Class<T> classOfT) {
		Object obj = null;
		try {
			obj = gson.fromJson(json, classOfT);
		} catch (Exception e) {
			return null;
		}
		return obj;
	}

	public static String toJson(Object obj) {
		return gson.toJson(obj);
	}
}
