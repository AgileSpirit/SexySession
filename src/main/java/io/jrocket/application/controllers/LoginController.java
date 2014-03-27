package io.jrocket.application.controllers;

import java.util.Map;
import io.jrocket.application.model.LoginForm;
import io.jrocket.domain.entities.CookieConstants;
import io.jrocket.domain.entities.User;
import io.jrocket.domain.services.UserService;
import io.jrocket.infra.repository.UserRepository;
import io.jrocket.infra.util.ApplicationException;
import io.jrocket.infra.util.Features;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.inject.Inject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class LoginController {

    @Inject
    UserService userService;

    @Inject
    UserRepository userRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    @RequestMapping(value= "/login", method = RequestMethod.GET)
    public String viewLoginForm(Map<String, LoginForm> model) {
        LoginForm form = new LoginForm();
        model.put("loginForm", form);
        return "login";
    }

    @RequestMapping(value= "/login", method = RequestMethod.POST)
    public String signIn(HttpServletResponse response, @ModelAttribute("loginForm") LoginForm form, Map<String, Object> model) {
        String login = form.getLogin();
        String password = form.getPassword();

        try {
            String sessionToken = userService.signIn(login, password);
            response.addCookie(new Cookie(CookieConstants.SMART_SESSION_ID, sessionToken));

            if (Features.IS_ENABLED_DATA_IN_SESSION) {
                User user = userRepository.findByLogin(login);
                if (user.getFirstName() != null) response.addCookie(new Cookie(CookieConstants.USER_FIRST_NAME, user.getFirstName()));
                if (user.getLastName() != null) response.addCookie(new Cookie(CookieConstants.USER_LAST_NAME, user.getLastName()));
            }

        } catch (ApplicationException e) {
            LOGGER.error(e.getMessage());
            return "login";
        }

        return "redirect:/overview";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String signOut(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(CookieConstants.SMART_SESSION_ID)) {
                    String sessionToken = cookie.getValue();
                    try {
                        userService.signOut(sessionToken);
                        removeCookies(response);
                    } catch (ApplicationException e) {
                        LOGGER.error(e.getMessage());
                        return "redirect:/error";
                    }
                    return "redirect:/login";
                }
            }
        }
        return "redirect:/error";
    }

    private void removeCookies(HttpServletResponse response) {
        removeCookie(response, CookieConstants.SMART_SESSION_ID);
        removeCookie(response, CookieConstants.USER_FIRST_NAME);
        removeCookie(response, CookieConstants.USER_LAST_NAME);
    }

    private void removeCookie(HttpServletResponse response, String cookieName) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

}
