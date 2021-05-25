/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mass.sica.utils;

import java.text.Normalizer;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Aristide MASSAGA
 */
public class UtilsJob {

    public static String buildUrl(String url) {
        return Normalizer
                .normalize(url.toLowerCase(), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "")
                .replaceAll(" ", "-").replaceAll(",", "").replaceAll("[^a-zA-Z0-9 \\-]", "");
    }

    public static String codeGenerator() {
        return String.valueOf((new Date()).getTime());
    }

    public static String codePubGenerator(Long id) {
        return "SICA-" + "00000000".substring(0, 8 - id.toString().length()) + id.toString();
    }

    public static String getFileName(String name) {
        if(!name.contains("SICA")) {
            return null;
        }
        return name.substring(14);
    }
    
    
    public static Map<String, String> convertParamsRequest(Map<String, String[]> params) {
        Map<String, String> result = new HashMap<>();

        if (params != null) {
            params.keySet().forEach(key -> {
                result.put(key, params.get(key)[0]);
            });
        }

        return result;
    }
    
}
