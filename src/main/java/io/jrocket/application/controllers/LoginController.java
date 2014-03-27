package io.jrocket.application.controllers;

import io.jrocket.application.model.LoginForm;
import io.jrocket.domain.entities.Session;
import io.jrocket.domain.services.UserService;
import io.jrocket.infra.util.ApplicationException;
import io.jrocket.infra.util.CookieHelper;
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
import java.util.Map;

@Controller
public class LoginController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    @Inject
    UserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String viewLoginForm(Map<String, LoginForm> model) {
        LoginForm form = new LoginForm();
        model.put("loginForm", form);
        return "login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String signIn(HttpServletResponse response, @ModelAttribute("loginForm") LoginForm form, Map<String, Object> model) {
        String login = form.getLogin();
        String password = form.getPassword();

        try {
            String sessionToken = userService.signIn(login, password);
            response.addCookie(new Cookie(Session.SMART_SESSION_ID, sessionToken));

        } catch (ApplicationException e) {
            LOGGER.error(e.getMessage());
            return "login";
        }

        return "redirect:/overview";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String signOut(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = CookieHelper.get(request, Session.SMART_SESSION_ID);
        if (cookie != null) {
            String sessionToken = cookie.getValue();
            try {
                // Call sign-out service
                userService.signOut(sessionToken);

                // Remove cookie
                CookieHelper.delete(response, Session.SMART_SESSION_ID);

            } catch (ApplicationException e) {
                return "redirect:/error";
            }
            return "redirect:/login";
        }
        return "redirect:/error";
    }

}
