package com.jwang261.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jwang261.pojo.Item;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class RPCHelper {
    public static void writeJsonArray(HttpServletResponse response, JSONArray array) throws IOException {
        response.setContentType("application/json");
        response.getWriter().print(array);
    }

    // Writes a JSONObject to http response.
    public static void writeJsonObject(HttpServletResponse response, JSONObject obj) throws IOException, IOException {
        response.setContentType("application/json");
        response.getWriter().print(obj);
    }
    // Convert a JSON Object to Item Object
    public static Item parseFavoriteItem(JSONObject favoriteItem) {
        Set<String> keywords = new HashSet<>();
        JSONArray array = favoriteItem.getJSONArray("keywords");
        for (int i = 0; i < array.size(); ++i) {
            keywords.add(array.getString(i));
        }
        return Item.builder()
                .itemId(favoriteItem.getString("item_id"))
                .name(favoriteItem.getString("name"))
                .address(favoriteItem.getString("address"))
                .url(favoriteItem.getString("url"))
                .imageUrl(favoriteItem.getString("image_url"))
                .keywords(keywords)
                .build();
    }


}
