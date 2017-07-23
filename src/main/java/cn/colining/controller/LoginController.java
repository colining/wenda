package cn.colining.controller;

import cn.colining.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


/**
 * Created by colin on 2017/7/5.
 */
@Controller
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    UserService userService;

    /**
     * regLogin 注册和登陆的处理；默认会跳转到登录页面
     * 该页面可以登陆也可以注册
     *
     * @param model 通过model传递到view，view再通过input传递next值
     * @param next  指之前浏览的页面，登陆注册完就可以跳转过去
     * @return
     */
    @RequestMapping(path = {"/reglogin"}, method = {RequestMethod.GET})
    public String regLogin(Model model,
                           @RequestParam(value = "next", defaultValue = "") String next) {
        model.addAttribute("next", next);
        return "login";
    }

    /**
     * reg register的简写；
     * 主要功能就是看看用户能不能进行注册；
     * 通过map得到的ticket跳转响应的页面
     *
     * @param model      model
     * @param username   用户名
     * @param password   密码
     * @param next       下一个跳转的页面
     * @param rememberme 是否记住
     * @param response   响应
     * @return
     */
    @RequestMapping(path = {"/reg"}, method = RequestMethod.POST)
    public String reg(Model model,
                      @RequestParam("username") String username,
                      @RequestParam("password") String password,
                      @RequestParam(value = "next", defaultValue = "") String next,
                      @RequestParam(value = "rememberme", defaultValue = "false") boolean rememberme,
                      HttpServletResponse response) {
        try {
            Map<String, String> map = userService.register(username, password);
            return checkTicket(model, next, rememberme, response, map);

        } catch (Exception e) {
            logger.error("注册异常" + e.getMessage());
            return "login";
        }
    }

    /**
     * 登陆主要就是需要查看用户是不是合法的；
     * 通过userService返回的map，来获取ticket，
     * 然后做出相应跳转
     *
     * @param model
     * @param username
     * @param password
     * @param rememberMe 是否记住，决定于是否下发cookie
     * @param next       登录后要跳转的页面
     * @param response   通过响应来下发cookie
     * @return
     */
    @RequestMapping(path = {"/login"}, method = RequestMethod.POST)
    public String login(Model model,
                        @RequestParam("username") String username,
                        @RequestParam("password") String password,
                        @RequestParam(value = "rememberMe", defaultValue = "false") boolean rememberMe,
                        @RequestParam(value = "next", defaultValue = "") String next,
                        HttpServletResponse response) {
        try {
            Map<String, String> map = userService.login(username, password);
            return checkTicket(model, next, rememberMe, response, map);
        } catch (Exception e) {
            logger.error("注册异常" + e.getMessage());
            return "login";
        }
    }

    /**
     * 将ticket的status置为0，也就是说不能用了，
     * 这样用户就需要重新登陆了，因为本地的cookie值 已经失效；
     * 因为服务器表示你这ticket不能用啊；
     *
     * @param ticket
     * @return
     */
    @RequestMapping(path = {"/logout"}, method = RequestMethod.GET)
    public String logout(@CookieValue("ticket") String ticket) {
        userService.logout(ticket);
        return "redirect:/";
    }

    /**
     * 检验ticket，通过map得到ticket，如果map没有ticket
     * 说明这注册或者登陆不合法，返回map中的msg给view，显示错误信息；
     * 拿到ticket就下发cookie，默认是session，如果点击记住我，时间就是
     * 5天，然后进行跳转，如果有next就跳转next
     * @param model
     * @param next
     * @param rememberMe
     * @param response
     * @param map
     * @return
     */
    private String checkTicket(Model model, @RequestParam(value = "next", defaultValue = "") String next, @RequestParam(value = "rememberMe", defaultValue = "false") boolean rememberMe, HttpServletResponse response, Map<String, String> map) {
        if (map.containsKey("ticket")) {
            Cookie cookie = new Cookie("ticket", map.get("ticket"));
            cookie.setPath("/");
            if (rememberMe) {
                cookie.setMaxAge(3600 * 24 * 5);
            }
            response.addCookie(cookie);
            if (StringUtils.isNotBlank(next)) {
                return "redirect:" + next;
            }
            return "redirect:/";
        } else {
            model.addAttribute("msg", map.get("msg"));
            return "login";
        }
    }
}

