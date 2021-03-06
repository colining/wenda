package cn.colining;

import cn.colining.dao.QuestionDAO;
import cn.colining.dao.UserDAO;
import cn.colining.model.EntityType;
import cn.colining.model.Question;
import cn.colining.model.User;
import cn.colining.service.FollowService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;


import java.util.Date;
import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest
//@Sql("/init-schema.sql")
public class InitDataBaseTests {

    @Autowired
    UserDAO userDAO;

    @Autowired
    QuestionDAO questionDAO;

    @Autowired
    FollowService followService;

    @Test
    public void initDatabase() {
        Random random = new Random();
        for (int i = 0; i < 11; ++i) {
            User user = new User();
            user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
            user.setName(String.format("USER%d", i));
            user.setPassword("");
            user.setSalt("");
            userDAO.addUser(user);
            user.setPassword("hello");
            userDAO.updatePassword(user);
            for (int j = 0; j < i; j++) {
                followService.follow(j, EntityType.ENTITY_USER,i);
            }



            Question question = new Question();
            question.setCommentCount(i);
            Date date = new Date();
            date.setTime(date.getTime() + 1000 * 3600 * i);
            question.setCreateDate(date);
            question.setUserId(i + 1);
            question.setTitle(String.format("TITLE %d", i));
            question.setContent(String.format("Balalalala Content %d", i));

            questionDAO.addQuestion(question);
        }
    }

    @Test
    public void test2() {
//		Assert.assertEquals("hello", userDAO.getById(1).getPassword());
//		userDAO.deleteById(1);
//        Assert.assertNull(userDAO.getById(1));
//        Assert.assertNotNull(userDAO.getById(1));
        System.out.println(questionDAO.selectLatestQuestions(0, 0, 10));
    }

    @Test
    public void test3() {
        Assert.assertNotNull(userDAO.selectByName("123"));
    }
}
