package com.jwang261.pojo;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    private String itemId;
    private String name;
    private String address;
    private Set<String> keywords;
    private String imageUrl;
    private String url;

    public JSONObject toJSONObject() {
        JSONObject obj = new JSONObject();
        obj.put("item_id", itemId);
        obj.put("name", name);
        obj.put("address", address);
        obj.put("keywords", Arrays.asList(keywords));
        obj.put("image_url", imageUrl);
        obj.put("url", url);
        return obj;
    }


}
