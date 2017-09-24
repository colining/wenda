package cn.colining.controller;

import cn.colining.async.EventModel;
import cn.colining.async.EventProducer;
import cn.colining.async.EventType;
import cn.colining.model.*;
import cn.colining.service.*;
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

    @Autowired
    LikeService likeService;

    @Autowired
    FollowService followService;

    @Autowired
    EventProducer eventProducer;

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
                eventProducer.fireEvent(new EventModel(EventType.ADD_QUESTION)
                        .setActorId(question.getUserId()).setEntityId(question.getId())
                        .setExt("title", question.getTitle()).setExt("content", question.getContent()));
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
     * 增加了传递赞和踩的信息；
     * 并且传递了赞的数量
     * @param model model
     * @param qid   问题id，用来取出问题
     * @return
     */
    @RequestMapping(value = "/question/{qid}")
    public String questionDetail(Model model,@PathVariable("qid") int qid) {
        Question question = questionService.getById(qid);
        model.addAttribute("question", question);
//        model.addAttribute("user", userService.getUser(hostHolder.getUser().getId()));
        List<Comment> commentsList = commentService.getCommentByEntity(qid, EntityType.ENTITY_QUESTION);
        List<ViewObject> comments = new ArrayList<>();
        for (Comment comment : commentsList) {
            ViewObject vo = new ViewObject();
            vo.set("comment", comment);
            if (hostHolder.getUser() == null) {
                vo.set("liked", 0);
            }else {
                vo.set("liked", likeService.getLikeStatus(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT, comment.getId()));
            }
            vo.set("likeCount",likeService.getLikeCount(EntityType.ENTITY_COMMENT,comment.getId()));
            vo.set("user", userService.getUser(comment.getUserId()));
            comments.add(vo);
        }
        model.addAttribute("comments", comments);
        List<ViewObject> followUsers = new ArrayList<ViewObject>();
        // 获取关注的用户信息
        List<Integer> users = followService.getFollowers(EntityType.ENTITY_QUESTION, qid, 20);
        for (Integer userId : users) {
            ViewObject vo = new ViewObject();
            User u = userService.getUser(userId);
            if (u == null) {
                continue;
            }
            vo.set("name", u.getName());
            vo.set("headUrl", u.getHeadUrl());
            vo.set("id", u.getId());
            followUsers.add(vo);
        }
        model.addAttribute("followUsers", followUsers);
        if (hostHolder.getUser() != null) {
            model.addAttribute("followed", followService.isFollower(hostHolder.getUser().getId(), EntityType.ENTITY_QUESTION, qid));
        } else {
            model.addAttribute("followed", false);
        }
        return "detail";
    }
}


