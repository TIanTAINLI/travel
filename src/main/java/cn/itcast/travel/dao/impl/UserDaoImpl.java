package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.print.DocFlavor;

public class UserDaoImpl implements UserDao {

    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    @Override
    public User findByUsername(String username) {
        //1. 定义SQL
        String sql = "select * from tab_user where username=?";
        //2. 执行SQL
        User user = template.queryForObject(sql, new BeanPropertyRowMapper<>(User.class), username);
        return user;
    }

    @Override
    public void save(User user) {
        //1. 定义SQL
        String sql = "insert into tab_user(username,password,name,birthday,sex,telephone,email) values(?,?,?,?,?,?,?)";
        //2.执行SQL
        template.update(sql, user.getUsername(),
                user.getPassword(),
                user.getName(),
                user.getBirthday(),
                user.getSex(),
                user.getTelephone(),
                user.getEmail());
    }
}
