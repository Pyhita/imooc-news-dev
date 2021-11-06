package com.imooc.admin.service.impl;

import com.imooc.admin.mapper.AdminUserMapper;
import com.imooc.admin.service.AdminUserService;
import com.imooc.pojo.AdminUser;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

public class AdminUserServiceImpl implements AdminUserService {

    @Autowired
    private AdminUserMapper adminUserMapper;

    @Override
    public AdminUser findByUserName(String username) {
        Example adminExample = new Example(AdminUser.class);
        Example.Criteria criteria = adminExample.createCriteria();
        criteria.andEqualTo("username", username);

        AdminUser admin = adminUserMapper.selectOneByExample(adminExample);
        return admin;
    }



}
