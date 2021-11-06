package com.imooc.admin.service;

import com.imooc.pojo.AdminUser;
import com.imooc.utils.PagedGridResult;

public interface AdminUserService {

    AdminUser findByUserName(String username);

//    PagedGridResult queryAdminList(Integer page, Integer pageSize);

//    void createAdminUser(NewAdminBO newAdminBO);


}
