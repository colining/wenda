package cn.colining.controller;

import cn.colining.model.HostHolder;
import cn.colining.model.Question;
import cn.colining.service.QuestionService;
import cn.colining.service.UserService;
import cn.colining.util.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * Created by colin on 2017/7/12.
 */
@Controller
public class QuestionController {
    private static final Logger LOGGER = LoggerFactory.getLogger(QuestionController.class);

    @Autowired
    QuestionService questionService;

    @Autowired
    HostHolder hostHolder;
    @Autowired
    UserService userService;


    /**
     * 增加题目的函数；
     * 如果没登录就会加一个匿名，也可以返回code:999
     * 添加问题不成功的话，就会报错
     * @param title  问题标题
     * @param contetnt 问题内容
     * @return
     */
    @RequestMapping(value = "/question/add", method = {RequestMethod.POST})
    @ResponseBody
    public String addQuestion(@RequestParam("title") String title, @RequestParam("content") String contetnt) {
        try {
            Question question = new Question();
            question.setTitle(title);
            question.setCreateDate(new Date());
            question.setContent(contetnt);
            question.setCommentCount(0);
            if (hostHolder.getUser() == null) {
                question.setUserId(WendaUtil.ANOYNMOUS_USERID);
//                return WendaUtil.getJSONString(999);
            } else {
                question.setUserId(hostHolder.getUser().getId());
            }
            if (questionService.addQuestion(question) > 0) {
                return WendaUtil.getJSONString(0);
            }

        } catch (Exception e) {
            LOGGER.error("增加题目失败" + e.getMessage());
        }
        return WendaUtil.getJSONString(1, "失败");
    }

    /**
     * 问题详情页面
     * @param model model
     * @param qid   问题id，用来取出问题
     * @return
     */
    @RequestMapping(value = "/question/{qid}")
    public String questionDetail(Model model,@PathVariable("qid") int qid) {
        Question question = questionService.selectById(qid);
        model.addAttribute("question", question);
        model.addAttribute("user", userService.getUser(hostHolder.getUser().getId()));
        return "detail";
    }
}


