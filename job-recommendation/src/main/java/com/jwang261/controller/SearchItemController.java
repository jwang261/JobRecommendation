package com.jwang261.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jwang261.external.GitHubClient;
import com.jwang261.pojo.Item;
import com.jwang261.service.SearchServiceImpl;
import com.jwang261.utils.RPCHelper;
import com.jwang261.utils.SessionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Set;

@Controller
public class SearchItemController {

    @Autowired
    GitHubClient gitHubClient;

    @Autowired
    SearchServiceImpl searchServiceImpl;

    @GetMapping("/search/{lan}/{lon}")
    @ResponseBody
    public JSON search(@PathVariable("lan") Double lan
            , @PathVariable("lon") Double lon){
        List<Item> items = gitHubClient.search(lan, lon, null);
        JSONArray jsonArray = new JSONArray();
        for (Item item : items) {
            jsonArray.add(item.toJSONObject());
        }

        return jsonArray;

    }

    @GetMapping("/search/{lan}/{lon}/{userId}")
    protected void searchByUserId(@PathVariable("lan") Double lan,
                         @PathVariable("lon") Double lon,
                         @PathVariable("userId") String userId,
                         HttpServletResponse response,
                         HttpServletRequest request)
            throws IOException {

//        if (SessionStatus.isEmpty(request)) {
//            response.setStatus(403);
//            return;
//        }

        List<Item> items = gitHubClient.search(lan, lon, null);
        Set<String> favoriteItemIds = searchServiceImpl.getFavoriteItemIds(userId);
        JSONArray array = new JSONArray();
        for (Item item : items) {
            JSONObject obj = item.toJSONObject();
            obj.put("favorite", favoriteItemIds.contains(item.getItemId()));
            array.add(obj);
        }
        RPCHelper.writeJsonArray(response, array);
    }


    @GetMapping("/search/recommendItem/{userId}/{lat}/{lon}")
    public void recommendItem(HttpServletRequest request,
                              HttpServletResponse response,
                              @PathVariable("userId") String userId,
                              @PathVariable("lat") double lat,
                              @PathVariable("lon") double lon ) throws IOException {
        if (SessionStatus.isEmpty(request)) {
            response.setStatus(403);
            return;
        }
        List<Item> items = searchServiceImpl.recommendItem(userId, lat, lon);
        JSONArray array = new JSONArray();
        for (Item item : items) {
            array.add(item.toJSONObject());
        }
        RPCHelper.writeJsonArray(response, array);


    }

}
