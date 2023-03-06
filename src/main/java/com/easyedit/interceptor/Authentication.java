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

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class Authentication implements HandlerInterceptor{

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
     * 校验domain
     *
     * @param httpServletRequest request
     * @return 是否可访问
     */
    private String verifyDomain(HttpServletRequest httpServletRequest) {
        String referer = httpServletRequest.getHeader("referer");
        String origin = httpServletRequest.getHeader("origin");
        String allowDomains = "osinfra.cn;8080";
        String[] domains = allowDomains.split(";");

        boolean checkReferer = isDomainLegal(domains, referer);
        boolean checkOrigin = isDomainLegal(domains, origin);

        if (!checkReferer && !checkOrigin) {
            return "unauthorized";
        }
        return "success";
    }

    private boolean isDomainLegal(String[] domains, String input) {
        if (StringUtils.isBlank(input)) return true;

        String substring = "";
        String[] start_path = input.split("//");
        String mid_path = "";
        if (start_path.length > 1){
            mid_path = start_path[1];
        }
         
        if (!StringUtils.isBlank(mid_path)) {
            String[] sub_path = mid_path.split("/");
            if (sub_path.length > 0){
                substring = sub_path[0];
            }
        }

        for (String domain : domains) {
            if (substring.endsWith(domain)) return true;
        }

        return false;
    }

    private void tokenError(HttpServletRequest httpServletRequest,
        HttpServletResponse httpServletResponse, String message) throws IOException {
        httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, message);
    }
}
