package cn.colining.service;

import cn.colining.dao.UserDAO;
import cn.colining.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by colin on 2017/6/29.
 */
@Service
public class UserService {
    @Autowired
    UserDAO userDAO;
    public User getUser(int id) {
        return userDAO.selectById(id);
    }
}
