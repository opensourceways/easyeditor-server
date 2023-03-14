/* This project is licensed under the Mulan PSL v2.
 You can use this software according to the terms and conditions of the Mulan PSL v2.
 You may obtain a copy of Mulan PSL v2 at:
     http://license.coscl.org.cn/MulanPSL2
 THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT, MERCHANTABILITY OR FIT FOR A PARTICULAR
 PURPOSE.
 See the Mulan PSL v2 for more details.
 Create: 2023
*/

package com.easyedit.interceptor;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.easyedit.service.OneidService;
import com.easyedit.util.Common;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class Authentication implements HandlerInterceptor{

    @Value("${spring.auth.domain}")
    String g_domainName;

    @Autowired
    private OneidService oneidService;

    @Autowired
    private Common utilCommon;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) throws Exception {
        // 如果不是映射到方法直接通过
        if (!(object instanceof HandlerMethod)) {
            return true;
        }

        // 检查有没有需要用户权限的注解，仅拦截OneidToken
        HandlerMethod handlerMethod = (HandlerMethod) object;
        Method method = handlerMethod.getMethod();
        if (method.isAnnotationPresent(OneidToken.class)) {
            // 从请求头中取出 token
            String headerToken = httpServletRequest.getHeader("token");
            if (StringUtils.isBlank(headerToken)) {
                return false;
            }
        }

        // 校验domain
        String verifyDomainMsg = verifyDomain(httpServletRequest);
        if (!verifyDomainMsg.equals("success")) {
            tokenError(httpServletRequest, httpServletResponse, verifyDomainMsg);
            return false;
        }

        // 校验cookie
        Cookie tokenCookie = verifyCookie(httpServletRequest);
        if (tokenCookie == null) {
            tokenError(httpServletRequest, httpServletResponse, "unauthorized");
            return false;
        }

        // 校验SIG权限
        SigToken sigToken = method.getAnnotation(SigToken.class);
        String verifyUserMsg = isUserHaveSigAccess(sigToken, httpServletRequest, tokenCookie);
        if (verifyUserMsg.equals("has no permission")) {
            tokenError(httpServletRequest, httpServletResponse, verifyUserMsg);
            return false;
        }
        
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest,
                           HttpServletResponse httpServletResponse,
                           Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest,
                                HttpServletResponse httpServletResponse,
                                Object o, Exception e) throws Exception {
    }

    /**
     * 校验用户sig操作权限
     */
    private String isUserHaveSigAccess(SigToken sigToken, HttpServletRequest httpServletRequest, Cookie tokenCookie) {
        try {
            if (sigToken != null && sigToken.required()) {
                String token = httpServletRequest.getHeader("token");
                String cookie = tokenCookie.getValue();
                List<String> pers = oneidService.getUserPermission(token, cookie);
                for (String per : pers) {
                    System.out.printf("The right is...%s ", per);
                    return "success";
                }
            } else{
                return "skip";
            }
        } catch (Exception e) {
            return "has no permission";
        }
        return "has no permission";
    }

    /**gongzhua
     * 获取包含存token的cookie
     *
     * @param httpServletRequest request
     * @return cookie
     */
    private Cookie verifyCookie(HttpServletRequest httpServletRequest) {
        Cookie[] cookies = httpServletRequest.getCookies();
        Cookie cookie = null;
        if (cookies != null) {
            // 获取cookie中的token
            Optional<Cookie> first = Arrays.stream(cookies).
                    filter(c -> "_Y_G_".equals(c.getName())).findFirst();
            if (first.isPresent()) cookie = first.get();
        }
        return cookie;
    }

    /**
     * 校验domain
     *
     * @param httpServletRequest request
     * @return 是否可访问
     */
    private String verifyDomain(HttpServletRequest httpServletRequest) {
        String referer = httpServletRequest.getHeader("referer");
        String origin = httpServletRequest.getHeader("origin");
        String allowDomains = g_domainName;
        String[] domains = allowDomains.split(";");

        boolean checkReferer = utilCommon.isDomainLegal(domains, referer);
        boolean checkOrigin = utilCommon.isDomainLegal(domains, origin);

        if (!checkReferer && !checkOrigin) {
            return "unauthorized";
        }
        return "success";
    }

    private void tokenError(HttpServletRequest httpServletRequest,
        HttpServletResponse httpServletResponse, String message) throws IOException {
        httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, message);
    }
}
