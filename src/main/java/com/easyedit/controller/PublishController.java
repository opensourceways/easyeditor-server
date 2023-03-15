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

package com.easyedit.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.easyedit.interceptor.OneidToken;
import com.easyedit.service.PublishService;
import com.easyedit.util.ResponseResult;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/publish")
public class PublishController {
    
    @Autowired
    private HttpServletResponse response;
    
    @Autowired
    private PublishService pService;

    // @OneidToken
    @PostMapping("")
    public ResponseResult publishAllPages(@RequestBody Map<String, String> items) {
        try {
            final ResponseResult result = pService.createPublish(items.get("path"));
            response.setStatus(result.getHttpStatusCode());
            return result;
        } catch(Exception e) {
            response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            return ResponseResult.errorResult(ResponseResult.AppHttpCodeEnum.ERROR);
        }
    }

    // @OneidToken
    @GetMapping("")
    public ResponseResult getAllPages(@RequestParam("path") String path, @RequestParam(value = "version", required = false) Integer version) {
        try {
            final ResponseResult result = pService.getPublish(path, version);
            response.setStatus(result.getHttpStatusCode());
            return result;
        } catch(Exception e) {
            response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            return ResponseResult.errorResult(ResponseResult.AppHttpCodeEnum.ERROR);
        }
    }

    // @OneidToken
    @GetMapping("/versions")
    public ResponseResult getAllPages(@RequestParam("path") String path) {
        try {
            final ResponseResult result = pService.getVersions(path);
            response.setStatus(result.getHttpStatusCode());
            return result;
        } catch(Exception e) {
            response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            return ResponseResult.errorResult(ResponseResult.AppHttpCodeEnum.ERROR);
        }
    }


}
