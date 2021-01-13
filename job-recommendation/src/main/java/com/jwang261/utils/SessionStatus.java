package com.jwang261.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionStatus {
    public static boolean isEmpty(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        return session == null;
    }
}
