package com.kyle.interfaces;

import com.kyle.domain.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
@FeignClient(value = "USER-BOOK")
public interface UserClient {
        //远程调用client服务中的test01接口
        @GetMapping("/findByUserName")//标识远程调用的http的方法类型是什么
        public User findByUserName(@RequestBody User user);
}
