package com.imooc.admin.controller;

import com.imooc.admin.service.AdminUserService;
import com.imooc.api.BaseController;
import com.imooc.api.controller.admin.AdminMngControllerApi;
import com.imooc.exception.GraceException;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.pojo.AdminUser;
import com.imooc.pojo.bo.AdminLoginBO;
import com.imooc.pojo.bo.NewAdminBO;
import com.imooc.utils.PagedGridResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.UUID;

@RestController
public class AdminMngController extends BaseController
        implements AdminMngControllerApi {

    @Autowired
    private AdminUserService adminUserService;

    @Override
    public GraceJSONResult adminLogin(AdminLoginBO adminLoginBO,
                                      HttpServletRequest request,
                                      HttpServletResponse response) {
        //1、判断请求参数是否为空
        if (StringUtils.isBlank(adminLoginBO.getUsername()) ||
                StringUtils.isBlank(adminLoginBO.getPassword())) {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.ADMIN_NOT_EXIT_ERROR);
        }
        //2、通过用户名查询对应管理用户
        AdminUser adminUser = adminUserService.findByUserName(adminLoginBO.getUsername());
        //3、判断用户是否存在
        if (adminUser == null) {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.ADMIN_NOT_EXIT_ERROR);
        }
        //4、判断密码是否匹配
        boolean checkpw = BCrypt.checkpw(adminLoginBO.getPassword(), adminUser.getPassword());
        if (!checkpw) {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.ADMIN_PASSWORD_ERROR);
        }

        doLoginSettings(adminUser, request, response);
        return GraceJSONResult.ok();
    }



    @Override
    public GraceJSONResult adminIsExist(String username) {
        checkAdminExists(username);
        return GraceJSONResult.ok();
    }


    @Override
    public GraceJSONResult getAdminList(Integer page, Integer pageSize) {
        PagedGridResult result = adminUserService.queryAdminList(page, pageSize);
        return GraceJSONResult.ok(result);
    }

    @Override
    public GraceJSONResult addNewAdmin(@Valid NewAdminBO newAdminBO,
                                       BindingResult bindingResult,
                                       HttpServletRequest request,
                                       HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.ADMIN_PASSWORD_NULL_ERROR);
        }
        // 如果人脸入库信息为空，校验密码
        if (StringUtils.isBlank(newAdminBO.getImg64())) {
            if (StringUtils.isBlank(newAdminBO.getPassword()) ||
                    StringUtils.isBlank(newAdminBO.getConfirmPassword())
            ) {
                return GraceJSONResult.errorCustom(ResponseStatusEnum.ADMIN_PASSWORD_NULL_ERROR);
            }
        }
        //密码不为空，判断两次输入一致
        if (StringUtils.isNoneBlank(newAdminBO.getPassword())) {
            if (!newAdminBO.getPassword().equals(newAdminBO.getConfirmPassword())) {
                return GraceJSONResult.errorCustom(ResponseStatusEnum.ADMIN_PASSWORD_ERROR);
            }
        }
        //校验用户名唯一
        checkAdminExists(newAdminBO.getUsername());
        //调用service存入admin信息
        adminUserService.createAdminUser(newAdminBO);
        return GraceJSONResult.ok();
    }

    @Override
    public GraceJSONResult adminLogout(String adminId,
                                       HttpServletRequest request,
                                       HttpServletResponse response) {
         // 删除redis存储的session 和 cookie中的信息
        redisOperator.del(REDIS_ADMIN_TOKEN + ":" + adminId);
        deleteCookie(request, response, "atoken");
        deleteCookie(request, response, "aid");
        deleteCookie(request, response, "aname");

        return GraceJSONResult.ok();
    }

    @Override
    public GraceJSONResult adminFaceLogin(AdminLoginBO adminLoginBO,
                                          HttpServletRequest request,
                                          HttpServletResponse response) {
        return null;
    }


//    @Autowired
//    private FaceVerifyUtils faceVerifyUtils;



    private void checkAdminExists(String username) {
        AdminUser user = adminUserService.findByUserName(username);
        if (user != null) {
            GraceException.display(ResponseStatusEnum.ADMIN_USERNAME_EXIST_ERROR);
        }

    }

    private void doLoginSettings(AdminUser adminUser,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {
        //生成token，设置cookie
        String token = UUID.randomUUID().toString();
        redisOperator.set(REDIS_ADMIN_TOKEN + ":" + adminUser.getId(), token);
        setCookie(request, response, "atoken", token, COOKIE_MONTH);
        setCookie(request, response, "aid", adminUser.getId(), COOKIE_MONTH);
        setCookie(request, response, "aname", adminUser.getAdminName(), COOKIE_MONTH);

    }
}
