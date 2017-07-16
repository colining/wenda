package cn.colining.service;

import cn.colining.dao.QuestionDAO;
import cn.colining.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * Created by colin on 2017/6/29.
 */
@Service
public class QuestionService {
    @Autowired
    QuestionDAO questionDAO;
    @Autowired
    SensitiveService sensitiveService;

    /**
     * 一是要灭html标签，全部转义
     * 二是敏感词过滤
     * @param question
     * @return
     */
    public int addQuestion(Question question) {
        question.setContent(HtmlUtils.htmlEscape(question.getContent()));
        question.setTitle(HtmlUtils.htmlEscape(question.getTitle()));
        question.setTitle(sensitiveService.filter(question.getTitle()));
        question.setContent(sensitiveService.filter(question.getContent()));
        //todo 敏感词过滤
        return questionDAO.addQuestion(question) > 0 ? question.getId() : 0;
    }
    public List<Question> getLatestQuestions(int userId, int offset, int limit) {
        return questionDAO.selectLatestQuestions(userId, offset, limit);
    }

    public Question selectById(int i) {
        return questionDAO.selectById(i);
    }


}
