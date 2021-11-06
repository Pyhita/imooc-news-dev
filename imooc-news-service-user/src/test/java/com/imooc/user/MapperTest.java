package com.imooc.user;

import com.imooc.pojo.AppUser;
import com.imooc.user.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = {Application.class})
@RunWith(SpringRunner.class)
public class MapperTest {

    @Autowired
    private UserService userService;

    @Test
    public void test() {
        String mobile = "13963118693";

        AppUser user = userService.queryMobileIsExists(mobile);
        System.out.println(user);
    }


}
