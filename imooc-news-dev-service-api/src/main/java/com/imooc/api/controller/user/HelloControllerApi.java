package com.imooc.api.controller.user;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;

@Api(value = "controller的标题", tags = {"带有xx功能"})
public interface HelloControllerApi {

    /**
     * api的作用：其他的服务层都是实现
     * 所有的api接口同一在这里进行管理
     *
     * 所有的接口在这里暴露，实现在各自的微服务中
     * 微服务之间的调用都是基于接口
     * @return
     */
    @ApiOperation(value = "hello方法的接口", notes = "hello方法的借口", httpMethod = "GET")
    @GetMapping("/hello")
    public Object hello();

    @ApiOperation(value = "redis方法的接口", notes = "redis方法的借口", httpMethod = "GET")
    @GetMapping("/redis")
    public Object redis();


}
