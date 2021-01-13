package com.jwang261.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jwang261.pojo.User;
import com.jwang261.service.UserService;
import com.jwang261.service.UserServiceImpl;
import com.jwang261.utils.RPCHelper;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.text.html.HTML;
import java.io.IOException;

@Controller
public class LoginController {

    @Autowired
    UserServiceImpl userService;


    @PostMapping("/login")
    public void login(HttpServletRequest request,
                      HttpServletResponse response,
                      HttpSession session) throws IOException {
        JSONObject jsonObject = JSONObject.parseObject(IOUtils.toString(request.getReader()));
        String userId = jsonObject.getString("user_id");
        String password = jsonObject.getString("password");

        JSONObject res = new JSONObject();
        if (userService.verifyLogin(userId, password)) {
            session.setAttribute("user_id", userId);
            res.fluentPut("status", "OK").fluentPut("user_id", userId).fluentPut("name", userService.getFullName(userId));
        } else {
            res.put("status", "User Doesn't Exist");
            response.setStatus(401);
        }

        RPCHelper.writeJsonObject(response, res);

    }

    @ApiOperation("/loginBySession")
    @GetMapping("/login")
    public void loginBySession(HttpServletRequest request,
                               HttpServletResponse response
    ) throws IOException {

        HttpSession session = request.getSession(false);
        JSONObject jsonObject = new JSONObject();
        if (session != null) {
            String userId = session.getAttribute("user_id").toString();
            jsonObject.fluentPut("status", "OK").fluentPut("user_id", userId).fluentPut("name", userService.getFullName(userId));
        } else {
            jsonObject.put("status", "Invalid Session");
            response.setStatus(403);
        }
        RPCHelper.writeJsonObject(response, jsonObject);

    }


    @GetMapping("/logout")
    public String logout(HttpSession session){
        if (session != null) {
            session.invalidate();
        }

        return "redirect:/";

    }

    @PostMapping("/register")
    public void register(HttpServletRequest request,
                         HttpServletResponse response) throws IOException {
        String str = IOUtils.toString(request.getReader());
        JSONObject jsonObject = JSONObject.parseObject(str);
        User user = new User(jsonObject.getString("user_id"),
                jsonObject.getString("password"),
                jsonObject.getString("first_name"),
                jsonObject.getString("last_name"));
        JSONObject res = new JSONObject();

        if (userService.addUser(user)) {
            res.put("status", "OK");
        } else {
            res.put("status", "User Already Exists");
        }

        RPCHelper.writeJsonObject(response, res);

    }
}
