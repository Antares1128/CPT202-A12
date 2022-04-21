package com.cy.store.service;

import com.cy.store.entity.User;
import com.cy.store.mapper.UserMapper;
import com.cy.store.service.ex.ServiceException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
// @RunWith(SpringRunner.class)注解是一个测试启动器，可以加载Springboot测试注解

@SpringBootTest
@RunWith(SpringRunner.class)

public class UserServiceTests {
    @Autowired
    private IUserService userService;

    @Test
    public void reg() {
        try {
            User user = new User();
            user.setUsername("BaoXingYu03");
            user.setPassword("123");

            userService.reg(user);
            System.out.println("OK");
        } catch (ServiceException e) {
            //获取类的对象，在获取类的名称
            System.out.println(e.getClass().getSimpleName());
            //获取异常的具体描述信息
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void login() {
        User user = userService.login("123","123456");
        System.out.println(user);
    }



    @Test
    public void changePassword() {
        try {
        Integer uid = 24;
        String username = "lower";
        String oldPassword = "888888";
        String newPassword = "888888";
        userService.changePassword(uid, username, oldPassword, newPassword);
        System.out.println("密码修改成功！");
        } catch (ServiceException e) {
        System.out.println("密码修改失败！" + e.getClass().getSimpleName());
        System.out.println(e.getMessage());
        }
    }

    @Test
    public void getByUid() {
        System.err.println(userService.getByUid(13));

    }

    @Test
    public void changeInfo() {
        try {
            Integer uid = 13;
            String username = "数据管理员";
            User user = new User();
            user.setPhone("15512328888");
            user.setEmail("admin03@cy.cn");
            user.setGender(2);
            userService.changeInfo(uid, username, user);
            System.out.println("OK.");
        } catch (ServiceException e) {
                System.out.println(e.getClass().getSimpleName());
                System.out.println(e.getMessage());
        }

    }

    @Test
    public void changeAvatar(){
        userService.changeAvatar(31, "/upload/test.png","小米");
    }

}
