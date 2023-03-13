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
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.easyedit.entity.Page;
import com.easyedit.interceptor.OneidToken;
import com.easyedit.service.PageService;
import com.easyedit.util.ResponseResult;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/page")
public class DynamicActiveController {

    @Autowired
    private HttpServletResponse response;

    @Autowired
    private PageService pageService;

    /**
     * @param pageBody
     * @return
     * @throws IOException
     */
    // @OneidToken
    @PostMapping(value = "")
    public ResponseResult CreateDynamicActive(@RequestBody Page pageBody) throws IOException {
        try {
            final ResponseResult result = pageService.createPage(pageBody);
            response.setStatus(result.getHttpStatusCode());
            return result;
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            return ResponseResult.errorResult(ResponseResult.AppHttpCodeEnum.ERROR);
        }
    }

    // @OneidToken
    @GetMapping(value = "/{id}")
    public ResponseResult GetDynamicActiveById(@PathVariable Integer id) throws IOException {
        try {
            ResponseResult result = pageService.getPage(id, "", "");
            response.setStatus(result.getHttpStatusCode());
            return result;
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            return ResponseResult.errorResult(ResponseResult.AppHttpCodeEnum.ERROR);
        }
    }

    // @OneidToken
    @GetMapping(value = "")
    public ResponseResult GetDynamicActiveByName(@RequestParam("path") String path, @RequestParam("name") String name) throws IOException {
        try {
            ResponseResult result = pageService.getPage(-1, path, name);
            response.setStatus(result.getHttpStatusCode());
            return result;
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            return ResponseResult.errorResult(ResponseResult.AppHttpCodeEnum.ERROR);
        }
    }

    // @OneidToken
    @PutMapping(value = "/{id}")
    public ResponseResult UpdateDynamicActiveById(@PathVariable Integer id, @RequestBody Page page) throws IOException {
        try {
            ResponseResult result = pageService.updatePage(id, "", "", page);
            response.setStatus(result.getHttpStatusCode());
            return result;
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            return ResponseResult.errorResult(ResponseResult.AppHttpCodeEnum.ERROR);
        }
    }

    // @OneidToken
    @PutMapping(value = "")
    public ResponseResult UpdateDynamicActiveByName(@RequestParam("path") String path, @RequestParam("name") String name, @RequestBody Page page) throws IOException {
        try {
            ResponseResult result = pageService.updatePage(-1, path, name, page);
            response.setStatus(result.getHttpStatusCode());
            return result;
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            return ResponseResult.errorResult(ResponseResult.AppHttpCodeEnum.ERROR);
        }
    }

    // @OneidToken
    @DeleteMapping(value = "/{id}")
    public ResponseResult DeleteDynamicActiveById(@PathVariable Integer id) throws IOException {
        try {
            ResponseResult result = pageService.deletePage(id, "", "");
            response.setStatus(result.getHttpStatusCode());
            return result;
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            return ResponseResult.errorResult(ResponseResult.AppHttpCodeEnum.ERROR);
        }
    }

    // @OneidToken
    @DeleteMapping(value = "")
    public ResponseResult DeleteDynamicActiveByName(@RequestParam("path") String path, @RequestParam("name") String name) throws IOException {
        try {
            ResponseResult result = pageService.deletePage(-1, path, name);
            response.setStatus(result.getHttpStatusCode());
            return result;
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            return ResponseResult.errorResult(ResponseResult.AppHttpCodeEnum.ERROR);
        }
    }
}

