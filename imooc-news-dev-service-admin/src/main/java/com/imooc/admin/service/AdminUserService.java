package com.imooc.admin.service;

import com.imooc.pojo.AdminUser;
import com.imooc.pojo.bo.NewAdminBO;
import com.imooc.utils.PagedGridResult;

public interface AdminUserService {
    /**
     * 获取管理员用户信息
     * @param username
     * @return
     */
    AdminUser findByUserName(String username);

//    PagedGridResult queryAdminList(Integer page, Integer pageSize);


    /**
     * 新增管理员
     * @param newAdminBO
     */
    void createAdminUser(NewAdminBO newAdminBO);

    PagedGridResult queryAdminList(Integer page, Integer pageSize);

}
