package io.jrocket.infra.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieHelper {

    public static Cookie get(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {
                    return cookie;
                }
            }
        }
        return null;
    }

    public static void delete(HttpServletResponse response, String cookieName) {
        Cookie eraser = new Cookie(cookieName, null);
        eraser.setMaxAge(0);
        response.addCookie(eraser);
    }
}
