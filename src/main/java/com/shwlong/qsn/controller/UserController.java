package com.shwlong.qsn.controller;

import com.shwlong.qsn.entity.UserEntity;
import com.shwlong.qsn.entity.vo.UserLoginVo;
import com.shwlong.qsn.exception.QsnException;
import com.shwlong.qsn.service.UserService;
import com.shwlong.qsn.util.JWTUtils;
import com.shwlong.qsn.util.QsnEnum;
import com.shwlong.qsn.util.R;
import com.shwlong.qsn.entity.vo.UserRegisterVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JWTUtils jwt;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public R login(UserLoginVo userVo) {
        // 1.根据用户名、学号/工号查询用户是否存在
        UserEntity user = userService.getUserByUserNameOrNumber(userVo.getAccount());
        if (user == null)
            throw new QsnException(QsnEnum.LOGIN_USER_UNEXIST_OR_UNAUTHORIZED.getMsg());
        if (!user.getPassword().equals(userVo.getPassword()))
            throw new QsnException(QsnEnum.LOGIN_PASSWORD_ERROR.getMsg());

        // 2.生成token并返回
        String token = jwt.generateToken(user.getId(), user.getUsername());
        return R.ok(QsnEnum.LOGIN_SUCCESS.getMsg()).put("token", token).put("user", user.getUsername()).put("id", user.getId());
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public R register(UserRegisterVo userVo) {
        // 1.检查用户名的唯一性
        UserEntity used = userService.getUserByUserName(userVo.getUser());
        if (used != null)
            throw new QsnException(QsnEnum.REGISTER_USER_EXIST.getMsg());

        // 2.将用户信息注册到数据库
        UserEntity user = new UserEntity();
        user.setUsername(userVo.getUser());
        user.setPassword(userVo.getPwd());
        user.setCreateTime(new Date());
        int userId = userService.addOneUser(user);
        return R.ok(QsnEnum.REGISTER_SUCCESS.getMsg());
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public R list() {
        List<UserEntity> users = userService.getUsers();
        return R.ok().put("users", users);
    }

    @RequestMapping(value = "/count", method = RequestMethod.GET)
    public R getUserCount() {
        List<UserEntity> users = userService.getUsers();
        return R.ok().put("data", users.size());
    }
}
