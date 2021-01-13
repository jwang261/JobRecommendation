package com.jwang261.external;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jwang261.pojo.Item;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Component
public class GitHubClient {
    public static final String URL_TEMPLATE = "https://jobs.github.com/positions.json?description=%s&lat=%s&long=%s";
    public static final String DEFAULT_KEYWORD = "developer";


    private String getStringFieldOrEmpty(JSONObject obj, String field) {
//        System.out.println(obj.containsKey(field));
//        System.out.println(obj.getString(field));
        return obj.containsKey(field) ? obj.getString(field) : "";
    }


    //JSONArray -> Collections<Item>
    private List<Item> getItemList(JSONArray array) {
        //System.out.println(array);
        List<Item> itemList = new ArrayList<>();
        List<String> descriptionList = new ArrayList<>();


        for (int i = 0; i < array.size(); i++) {
            JSONObject object = array.getJSONObject(i);
            String description = getStringFieldOrEmpty(object, "description");
            if(description.equals("") || description.equals("\n")){
                descriptionList.add(getStringFieldOrEmpty(object, "title"));
            }else{
                descriptionList.add(description);
            }
            List<List<String>> keywords = MonkeyLearnClient
                    .extractKeywords(descriptionList.
                            toArray(new String[descriptionList.size()]));
            //System.out.println(object.toJSONString());
            Item item = Item.builder()
                    .itemId(getStringFieldOrEmpty(object, "id"))
                    .name(getStringFieldOrEmpty(object, "title"))
                    .address(getStringFieldOrEmpty(object, "location"))
                    .url(getStringFieldOrEmpty(object, "url"))
                    .imageUrl(getStringFieldOrEmpty(object, "company_logo"))
                    .keywords(new HashSet<String>(keywords.get(i)))
                    .build();
            itemList.add(item);
        }

        return itemList;
    }



    public List<Item> search(double lat, double lon, String keyword) {
        if (keyword == null) {
            keyword = DEFAULT_KEYWORD;
        }
        try {
            keyword = URLEncoder.encode(keyword, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url = String.format(URL_TEMPLATE, keyword, lat, lon);

        CloseableHttpClient httpclient = HttpClients.createDefault();

        // Create a custom response handler
        ResponseHandler<List<Item>> responseHandler = new ResponseHandler<List<Item>>() {

            @Override
            public List<Item> handleResponse(
                    final HttpResponse response) throws IOException {
                if (response.getStatusLine().getStatusCode() != 200) {
                    return new ArrayList<>();
                }
                HttpEntity entity = response.getEntity();
                if (entity == null) {
                    return new ArrayList<>();
                }
                String responseBody = EntityUtils.toString(entity);

                return getItemList(JSON.parseArray(responseBody));
            }
        };

        try {
            return httpclient.execute( new HttpGet(url), responseHandler);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }


}


