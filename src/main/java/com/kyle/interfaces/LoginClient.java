package com.kyle.interfaces;

import com.kyle.domain.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
@FeignClient(value = "BOOKSTORELOGIN")
public interface LoginClient {
    @GetMapping("/login")
    public String login(@RequestBody User user,@RequestParam("session") HttpSession session);
}
