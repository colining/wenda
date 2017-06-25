package cn.colining.service;

import org.springframework.stereotype.Service;

/**
 * Created by colin on 2017/6/24.
 */

/**
 * 服务类 提供一些服务
 */
@Service
public class WendaService {
    public String getMessage(int userId) {
        return "Hello World :" + String.valueOf(userId);
    }
}
