package com.imooc.user.service.impl;

import com.imooc.enums.Sex;
import com.imooc.enums.UserStatus;
import com.imooc.exception.GraceException;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.pojo.AppUser;
import com.imooc.pojo.bo.UpdateUserInfoBO;
import com.imooc.user.mapper.AppUserMapper;
import com.imooc.user.service.UserService;
import com.imooc.utils.DateUtil;
import com.imooc.utils.DesensitizationUtil;
import com.imooc.utils.JsonUtils;
import com.imooc.utils.RedisOperator;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;

@Service
public class UserServiceImpl implements UserService {
    private static final String USER_FACE0 = "http://122.152.205.72:88/group1/M00/00/05/CpoxxFw_8_qAIlFXAAAcIhVPdSg994.png";
    private static final String USER_FACE1 = "http://122.152.205.72:88/group1/M00/00/05/CpoxxF6ZUySASMbOAABBAXhjY0Y649.png";
    private static final String USER_FACE2 = "http://122.152.205.72:88/group1/M00/00/05/CpoxxF6ZUx6ANoEMAABTntpyjOo395.png";
    private static final String REDIS_USER_INFO = "redis:user:info";


    @Autowired
    private RedisOperator redisOperator;

    @Autowired
    private AppUserMapper appUserMapper;

    @Autowired
    private Sid sid;

    @Override
    public AppUser queryMobileIsExists(String mobile) {
        Example example = new Example(AppUser.class);
        Example.Criteria userCriteria = example.createCriteria();
        userCriteria.andEqualTo("mobile", mobile);
        AppUser appUser = appUserMapper.selectOneByExample(example);

        return appUser;
    }

    @Override
    public AppUser createUser(String mobile) {
        /**
         * 保证全局唯一id，使用雪花算法
         */
        String id = sid.nextShort();
        AppUser user = new AppUser();

        user.setId(id);
        user.setMobile(mobile);
        user.setNickname("用户" + DesensitizationUtil.commonDisplay(mobile));
        user.setFace(USER_FACE0);
        user.setBirthday(DateUtil.stringToDate("1990-01-01"));
        user.setSex(Sex.secret.type);
        user.setActiveStatus(UserStatus.INACTIVE.type);
        user.setTotalIncome(0);
        user.setCreatedTime(new Date());
        user.setUpdatedTime(new Date());

        appUserMapper.insert(user);

        return user;
    }

    @Override
    public AppUser getUser(String userId) {
        return appUserMapper.selectByPrimaryKey(userId);
    }

    @Override
    public void updateUserInfo(UpdateUserInfoBO updateUserInfoBO) {
        String userId = updateUserInfoBO.getId();
        // 为了保证双写的一致性，先删除reids终老的数据
        redisOperator.del(REDIS_USER_INFO + ":" + userId);

        AppUser userInfo = new AppUser();
        BeanUtils.copyProperties(updateUserInfoBO, userInfo);

        userInfo.setUpdatedTime(new Date());
        userInfo.setActiveStatus(UserStatus.ACTIVE.type);

        int affect = appUserMapper.updateByPrimaryKeySelective(userInfo);
        if (affect != 1) {
            GraceException.display(ResponseStatusEnum.USER_UPDATE_ERROR);
        }

        // 再次查询用户的最新信息，放入redis中
        AppUser user = getUser(userId);
        redisOperator.set(REDIS_USER_INFO + ":" + userId, JsonUtils.objectToJson(user));

        // 缓存双删
        try {
            Thread.sleep(100);
            redisOperator.del(REDIS_USER_INFO + ":" + userId);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
