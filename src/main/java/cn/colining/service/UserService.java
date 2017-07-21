package cn.colining.service;

import cn.colining.dao.LoginTicketDao;
import cn.colining.dao.UserDAO;
import cn.colining.model.LoginTicket;
import cn.colining.model.User;
import cn.colining.util.WendaUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by colin on 2017/6/29.
 */
@Service
public class UserService {


    @Autowired
    UserDAO userDAO;


    @Autowired
    LoginTicketDao loginTicketDao;

    public User getUser(int id) {
        return userDAO.selectById(id);
    }

    /**
     * 判断注册是否合法的函数
     * 如果合法的话，就添加盐，盐+password通过md5算法增加保密性；
     * 将用户加入数据库，添加ticket（服务器），然后下发（客户端）
     * @param username
     * @param password
     * @return
     */
    public Map<String, String> register(String username, String password) {
        Map<String, String> map = new HashMap<>();
        if (StringUtils.isBlank(username)) {
            map.put("msg", "用户名不能为空");
        }
        if (StringUtils.isBlank(password)) {
            map.put("msg", "密码不能为空");
        }
        User user = userDAO.selectByName(username);
        if (user != null) {
            map.put("msg", "该用户已经被注册");
            return map;
        }
        user = new User();
        user.setName(username);
        user.setSalt(UUID.randomUUID().toString().substring(0, 5));
        user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        //md5 需要重看
        user.setPassword(WendaUtil.MD5(password + user.getSalt()));
        userDAO.addUser(user);

        String ticket = addLoginTicket(user.getId());
        map.put("ticket", ticket);
        return map;
    }

    /**
     * 验证的登陆是否合法，添加ticket，下发ticket
     * @param username
     * @param password
     * @return
     */
    public Map<String, String> login(String username, String password) {
        Map<String, String> map = new HashMap<>();
        if (StringUtils.isBlank(username)) {
            map.put("msg", "用户名不能为空");
        }
        if (StringUtils.isBlank(password)) {
            map.put("msg", "密码不能为空");
        }
        User user = userDAO.selectByName(username);
        if (user == null) {
            map.put("msg", "用户名不存在");
            return map;
        }
        if (!WendaUtil.MD5(password + user.getSalt()).equals(user.getPassword())) {
            map.put("msg", "用户密码错误");
            return map;
        }

        String ticket = addLoginTicket(user.getId());
        map.put("ticket", ticket);
        return map;
    }

    /**
     * 增加ticket，注意日期
     * @param userId
     * @return
     */
    public String addLoginTicket(int userId) {
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(userId);
        Date now = new Date();
        now.setTime(now.getTime() + 3600L * 24 * 1000 * 1000);
        loginTicket.setExpired(now);
        loginTicket.setStatus(0);
        loginTicket.setTicket(UUID.randomUUID().toString().replace("-", ""));
        loginTicketDao.addTicket(loginTicket);
        return loginTicket.getTicket();

    }


    /**
     * 将ticket的status置为1
     *
     * @param ticket
     */
    public void logout(String ticket) {
        loginTicketDao.updateStatus(ticket, 1);
    }

    public static void main(String[] args) {
        for (int i = 0; i < 20; i++) {
            System.out.println(UUID.randomUUID().toString());
        }
    }

    public User selectByName(String name) {
        return userDAO.selectByName(name);
    }
}
