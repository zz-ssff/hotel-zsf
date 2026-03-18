package com.hotel.pms.controller;

import com.hotel.pms.entity.User;
import com.hotel.pms.service.UserService;
import com.hotel.pms.service.RoleService;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private RoleService roleService;

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> params) {
        Map<String, Object> result = new HashMap<>();
        String username = params.get("username");
        String password = params.get("password");
        User user = userService.login(username, password);
        if (user != null) {
            result.put("success", true);
            result.put("message", "登录成功");
            result.put("user", user);
        } else {
            result.put("success", false);
            result.put("message", "用户名或密码错误");
        }
        return result;
    }

    @GetMapping("/list")
    public Map<String, Object> list() {
        Map<String, Object> result = new HashMap<>();
        List<User> users = userService.listAll();
        result.put("success", true);
        result.put("data", users);
        return result;
    }

    @GetMapping("/roles")
    public Map<String, Object> roles() {
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("data", roleService.listAll());
        return result;
    }

    @PostMapping("/add")
    public Map<String, Object> add(@RequestBody User user) {
        Map<String, Object> result = new HashMap<>();
        boolean success = userService.add(user);
        result.put("success", success);
        result.put("message", success ? "添加成功" : "添加失败");
        return result;
    }

    @PostMapping("/update")
    public Map<String, Object> update(@RequestBody User user) {
        Map<String, Object> result = new HashMap<>();
        boolean success = userService.update(user);
        result.put("success", success);
        result.put("message", success ? "更新成功" : "更新失败");
        return result;
    }

    @PostMapping("/delete")
    public Map<String, Object> delete(@RequestBody Map<String, Long> params) {
        Map<String, Object> result = new HashMap<>();
        Long id = params.get("id");
        boolean success = userService.delete(id);
        result.put("success", success);
        result.put("message", success ? "删除成功" : "删除失败");
        return result;
    }
}