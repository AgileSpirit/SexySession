package io.jrocket.application.controllers;

import io.jrocket.domain.entities.Session;
import io.jrocket.domain.services.SessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class OverviewController {

    private static final Logger LOGGER = LoggerFactory.getLogger(OverviewController.class);

    @Inject
    SessionService sessionService;

    @RequestMapping(value = "/overview", method = RequestMethod.GET)
    public String displayOverviewPage(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("SMART_SESSION_ID")) {
                    String sessionToken = cookie.getValue();
                    Session session = sessionService.getSession(sessionToken);
                    if (session != null) {
                        LOGGER.info("Session asked : " + sessionToken);
                        return "overview";
                    }
                }
            }
        }
        return "redirect:/error";
    }

}
