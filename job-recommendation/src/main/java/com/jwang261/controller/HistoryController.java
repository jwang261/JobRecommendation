package com.jwang261.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jwang261.dao.HistoryDao;
import com.jwang261.pojo.Item;
import com.jwang261.service.SearchServiceImpl;
import com.jwang261.utils.RPCHelper;
import com.jwang261.utils.SessionStatus;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@Controller
@RequestMapping("/history")
public class HistoryController {

    @Autowired
    SearchServiceImpl searchServiceImpl;

    @ApiOperation("localhost:8080/history/setFavoriteItem")
    @PostMapping("/setFavoriteItem")
    public void setFavoriteItem( HttpServletRequest request,
                                 HttpServletResponse response) throws IOException {

        if (SessionStatus.isEmpty(request)) {
            response.setStatus(403);
            return;
        }
        String str = IOUtils.toString(request.getReader());
        JSONObject jsonObject = JSON.parseObject(str);
        String userId = jsonObject.getString("user_id");
        Item item = RPCHelper.parseFavoriteItem(jsonObject.getJSONObject("favorite"));

        searchServiceImpl.setFavoriteItems(userId, item);

        RPCHelper.writeJsonObject(response, new JSONObject().fluentPut("result", "SUCCESS"));

    }

    @DeleteMapping("/setFavoriteItem")
    public void unsetFavoriteItem(HttpServletRequest request, HttpServletResponse response) throws IOException {

        if (SessionStatus.isEmpty(request)) {
            response.setStatus(403);
            return;
        }
        String str = IOUtils.toString(request.getReader());
        JSONObject jsonObject = JSON.parseObject(str);
        String userId = jsonObject.getString("user_id");
        Item item = RPCHelper.parseFavoriteItem(jsonObject.getJSONObject("favorite"));
        searchServiceImpl.unsetFavoriteItems(userId, item.getItemId());
        RPCHelper.writeJsonObject(response, new JSONObject().fluentPut("result", "SUCCESS"));

    }

    @ApiOperation("localhost:8080/history/getFavoriteItem/1111")
    @GetMapping("/getFavoriteItem/{userId}")
    public void getFavoriteItem(@PathVariable("userId") String userId,
                                HttpServletResponse response,
                                HttpServletRequest request)
            throws IOException {

        if (SessionStatus.isEmpty(request)) {
            response.setStatus(403);
            return;
        }
        Set<Item> items = searchServiceImpl.getFavoriteItems(userId);
        JSONArray array = new JSONArray();
        for (Item item : items) {
            JSONObject obj = item.toJSONObject();
            obj.put("favorite", true);
            array.add(obj);
        }
        RPCHelper.writeJsonArray(response, array);

    }



}
