package cn.colining.controller;

import cn.colining.aspect.LogAspect;
import cn.colining.model.HostHolder;
import cn.colining.model.Question;
import cn.colining.model.ViewObject;
import cn.colining.service.QuestionService;
import cn.colining.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by colin on 2017/6/29.
 */
@Controller
public class HomeController {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogAspect.class);

    @Autowired
    UserService userService;
    @Autowired
    QuestionService questionService;

    @Autowired
    HostHolder hostHolder;

    /**
     * 通过model 将vos 传递给页面，进行首页展示
     *
     * @param model model
     * @return 页面
     */
    @RequestMapping(path = {"/", "/index"}, method = {RequestMethod.GET})
    public String index(Model model) {
        List<ViewObject> vos = getViewObjects(0, 0, 10);
        model.addAttribute("vos", vos);
        hostHolder.getUser();
        return "index";
    }

    private List<ViewObject> getViewObjects(int userId, int offset, int limit) {
        List<Question> questionList = questionService.getLatestQuestions(userId, offset, limit);
        List<ViewObject> vos = new ArrayList<ViewObject>();
        for (Question question : questionList) {
            ViewObject vo = new ViewObject();
            vo.set("question", question);
            vo.set("user", userService.getUser(question.getUserId()));
            vos.add(vo);
        }
        return vos;
    }

    @RequestMapping(path = {"/user/{userId}"}, method = {RequestMethod.GET})
    public String userIndex(Model model, @PathVariable("userId") int userId) {
        List<ViewObject> vos = getViewObjects(userId, 0, 10);
        model.addAttribute("vos", vos);
        return "index";
    }

}
