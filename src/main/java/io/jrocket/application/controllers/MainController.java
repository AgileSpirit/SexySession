package io.jrocket.application.controllers;

import io.jrocket.domain.services.UserService;
import io.jrocket.infra.util.ApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class MainController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainController.class);
    @Inject
    UserService userService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String displayIndexPage(@CookieValue(value = "token", required = false) String token) {
        return "index";
    }

    @RequestMapping(value = "/error", method = RequestMethod.GET)
    public String displayErrorPage() {
        return "error";
    }

}
