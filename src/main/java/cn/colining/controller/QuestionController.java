package cn.colining.controller;

import cn.colining.model.*;
import cn.colining.service.CommentService;
import cn.colining.service.QuestionService;
import cn.colining.service.UserService;
import cn.colining.util.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    @Autowired
    CommentService commentService;


    /**
     * 增加题目的函数；
     * 如果没登录就会加一个匿名，也可以返回code:999
     * 添加问题不成功的话，就会报错
     * @param title  问题标题
     * @param content 问题内容
     * @return
     */
    @RequestMapping(value = "/question/add", method = {RequestMethod.POST})
    @ResponseBody
    public String addQuestion(@RequestParam("title") String title, @RequestParam("content") String content) {
        try {
            Question question = new Question();
            question.setTitle(title);
            question.setCreateDate(new Date());
            question.setContent(content);
            question.setCommentCount(0);
            if (hostHolder.getUser() == null) {
                question.setUserId(WendaUtil.ANONYMITY_USERID);
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
     * 问题详情页
     * 新添加了评论的部分
     * 取出评论集合然后通过viewObject 放进页面中区
     * @param model model
     * @param qid   问题id，用来取出问题
     * @return
     */
    @RequestMapping(value = "/question/{qid}")
    public String questionDetail(Model model,@PathVariable("qid") int qid) {
        Question question = questionService.selectById(qid);
        model.addAttribute("question", question);
//        model.addAttribute("user", userService.getUser(hostHolder.getUser().getId()));
        List<Comment> commentsList = commentService.getCommentByEntity(qid, EntityType.ENTITY_QUESTION);
        List<ViewObject> comments = new ArrayList<>();
        for (Comment comment : commentsList) {
            ViewObject vo = new ViewObject();
            vo.set("comment", comment);
            vo.set("user", userService.getUser(comment.getUserId()));
            comments.add(vo);
        }
        model.addAttribute("comments", comments);
        return "detail";
    }
}


