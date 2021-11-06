package com.imooc.user.service;

import com.imooc.pojo.AppUser;
import com.imooc.pojo.bo.UpdateUserInfoBO;

public interface UserService {

    /**
     * 判断用户是否存在，如果存在返回用户信息
     */
    AppUser queryMobileIsExists(String mobile);

    /**
     * 创建用户，新增用户到数据库
     */
    AppUser createUser(String mobile);

    /**
     * 根据userid查询用户信息
     */
    AppUser getUser(String userId);

    /**
     * 用户修改信息，完善资料，并且激活
     */
    void updateUserInfo(UpdateUserInfoBO updateUserInfoBO);
}
