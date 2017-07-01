package cn.colining.controller;

import cn.colining.aspect.LogAspect;
import cn.colining.model.User;
import cn.colining.service.WendaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * Created by colin on 2017/6/24.
 */
@Controller
public class IndexController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogAspect.class);

    @Autowired
    WendaService wendaService;              //自动装配

    /**
     * 通过path，可以指定多个路径到一个页面，method可以指定该页面的HTTP方法<br>
     * ResponseBody 表示该方法的返回结果直接写入HTTP response body<br>
     * 不调用静态模板，不会加载jsp啥的
     *
     * @return 返回结果直接写入response body
     */
//    @RequestMapping(path = {"/", "/index"}, method = {RequestMethod.POST, RequestMethod.GET})
//    @ResponseBody
//    public String index(HttpSession httpSession) {
//        LOGGER.info("visit" + new Date());
//        return "Hello World" + httpSession.getAttribute("msg");
//    }

    /**
     * PathVariable    是指从路径得到的参数 <br>
     * RequestParam    是指从请求得到的参数
     *
     * @param userId  用户Id
     * @param groupId 用户组
     * @param type    类型
     * @param key     key
     * @return 返回结果直接写入response body
     */
    @RequestMapping(path = {"/profile/{groupId}/{userId}"})
    @ResponseBody
    public String profile(@PathVariable("userId") int userId,
                          @PathVariable("groupId") String groupId,
                          @RequestParam(value = "type", defaultValue = "1", required = true) int type,
                          @RequestParam(value = "key", defaultValue = "zz", required = false) String key) {

        return String.format("Profile Page of %s %d,<br> t: %d k: %s", groupId, userId, type, key);
    }

    /**
     * 通过model可以将后台的参数传向页面
     * @param model model
     * @return 页面
     */
    @RequestMapping(path = {"/fm"})
    public String template(Model model) {
        model.addAttribute("value1", "v1");
        List<String> colors = Arrays.asList("red", "blue", "green");
        model.addAttribute("colors", colors);
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < 4; i++) {
            map.put(String.valueOf(i), String.valueOf(i * i));
        }
        model.addAttribute("map", map);
        model.addAttribute("user", new User("lee"));
        return "fm";
    }

    /**
     * 打印了从cookie获取数据<br>
     * 打印了请求头<br>
     * response 添加了一个Header，添加了一个cookie
     *
     * @param response  响应
     * @param request   请求
     * @param sessionId 从cookie获取的id
     * @return String
     */
    @RequestMapping(path = {"/request"}, method = {RequestMethod.GET})
    @ResponseBody
    public String template(HttpServletResponse response,
                           HttpServletRequest request,
                           @CookieValue("JSESSIONID") String sessionId) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("COOKIEVALUE" + " " + sessionId + "<br>");

        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            stringBuilder.append(name + ":" + request.getHeader(name) + "<br>");
        }

        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                stringBuilder.append("Cookie:" + cookie.getName() + "value" + cookie.getValue());
            }
        }
        stringBuilder.append(request.getMethod() + "<br>");
        stringBuilder.append(request.getQueryString() + "<br>");
        stringBuilder.append(request.getPathInfo() + "<br>");
        stringBuilder.append(request.getRequestURI() + "<br>");

        response.addHeader("colin", "hello");
        response.addCookie(new Cookie("username", "colin"));

//        try {
//            response.sendRedirect("/index");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return stringBuilder.toString();
    }

    /**
     * session设置参数，在跳转后的页面可以读出<br>
     * 状态码301 永久重定向，302，临时
     *
     * @param code        网址传过去的状态码
     * @param httpSession session
     * @return redirectView
     */
    @RequestMapping(path = {"/redirect/{code}"}, method = RequestMethod.GET)
    public RedirectView redirect(@PathVariable("code") int code,
                                 HttpSession httpSession) {
        httpSession.setAttribute("msg", "jump from redirect");
        RedirectView redirectView = new RedirectView("/", true);
        if (code == 301) {
            redirectView.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        }
        return redirectView;
    }

    @RequestMapping(path = {"/admin"}, method = {RequestMethod.GET})
    @ResponseBody
    public String admin(@RequestParam("key") String key) {
        if ("admin".equals(key))
            return "hello admin";
        throw new IllegalArgumentException("参数不对");

    }

    /**
     * 通过ExceptionHandler 设置公共的异常处理
     */
    @ExceptionHandler()
    @ResponseBody
    public String error(Exception e) {
        return "error:" + e.getMessage();
    }
}