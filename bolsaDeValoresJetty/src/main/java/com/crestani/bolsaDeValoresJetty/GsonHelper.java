package com.crestani.bolsaDeValoresJetty;

import com.google.gson.Gson;

public class GsonHelper {
	
	public static String toJson(Object obj) {
		Gson gson = new Gson();
		return gson.toJson(obj);
	}

}
