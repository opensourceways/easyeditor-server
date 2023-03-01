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

package com.easyedit.Controller;

import java.io.IOException;

import javax.naming.spi.ResolveResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.easyedit.entity.Page;
import com.easyedit.entity.User;
import com.easyedit.service.PageService;
import com.easyedit.service.UserService;
import com.easyedit.util.ResponseResult;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/page")
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
    @PostMapping(value = "")
    public ResponseResult CreateDynamicActive(@RequestBody Page pageBody) {
        try {
            final ResponseResult result = pageService.createPage(pageBody);
            response.setStatus(result.getHttpStatusCode());
            return result;
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            return ResponseResult.errorResult(ResponseResult.AppHttpCodeEnum.ERROR);
        }
    }

    @GetMapping(value = "/{id}")
    public ResponseResult GetDynamicActive(@PathVariable String id) throws IOException {
        try {
            ResponseResult result = pageService.getPage(id);
            response.setStatus(result.getHttpStatusCode());
            return result;
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            return ResponseResult.errorResult(ResponseResult.AppHttpCodeEnum.ERROR);
        }
    }

    @PutMapping(value = "/{id}")
    public ResolveResult UpdateDynamicActive(@PathVariable String id, @RequestBody String items) {
        return null;
    }

    @DeleteMapping(value = "/{id}")
    public ResolveResult DeleteDynamicActive(@PathVariable String id) {
        return null;
    }
}

