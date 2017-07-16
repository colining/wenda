package cn.colining.dao;

import cn.colining.model.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by colin on 2017/6/28.
 */
@Mapper
@Component
public interface QuestionDAO {
    String TABLE_NAME = " question ";
    String INSERT_FIELDS = " title, content, created_date, user_id, comment_count ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    /**
     * 通过mybatis 的注解书写的DAO，减少了许多代码<br>
     * 一定要注意string之间的空格，通过#{}的方式可以直接取得对象中的属性<br>
     * 一定要注意#{}内是类中字段名字,
     *
     * @param question 问题对象
     * @return int
     */
    @Insert({"insert into ", TABLE_NAME, "(", INSERT_FIELDS,
            ") values (#{title},#{content},#{createdDate},#{userId},#{commentCount})"})
    int addQuestion(Question question);

    /**
     * 这个不是通过注解的方式，而是通过xml来实现的<br>
     * Param注解 表示需要传过去的参数，真正的sql语句在xml中定义
     *
     * @param userId 用户Id
     * @param offset 开始位置
     * @param limit  限制
     * @return
     */
    List<Question> selectLatestQuestions(@Param("userId") int userId, @Param("offset") int offset,
                                         @Param("limit") int limit);
    @Select({"select" ,SELECT_FIELDS, " from ",TABLE_NAME,"where id = #{id}"})
    Question selectById(int id);
}


