package com.shwlong.qsn.interceptor;

import com.mysql.cj.xdevapi.Client;
import com.shwlong.qsn.exception.QsnException;
import com.shwlong.qsn.util.Constant;
import com.shwlong.qsn.util.JWTUtils;
import com.shwlong.qsn.util.QsnEnum;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class TokenInterceptor implements HandlerInterceptor {

    @Autowired
    private JWTUtils jwt;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 获取用户凭证
        String token = request.getHeader(jwt.getHeader());
        if(StringUtils.isBlank(token)) {
            throw new QsnException(QsnEnum.TOKEN_IS_EMPTY.getMsg(), HttpStatus.UNAUTHORIZED.value());
        }
        // 检查token是否过期
        Claims claim = jwt.getClaimByToken(token);
        if(claim == null || jwt.isTokenExpired(claim.getExpiration()))
            throw new QsnException(QsnEnum.TOKEN_IS_Expired.getMsg(), HttpStatus.UNAUTHORIZED.value());

        request.setAttribute(Constant.USER_ID, claim.get(Constant.USER_ID));
        request.setAttribute(Constant.USER_NAME, claim.get(Constant.USER_NAME));

        return true;
    }
}
