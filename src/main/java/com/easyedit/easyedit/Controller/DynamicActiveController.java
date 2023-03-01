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

package com.easyedit.easyedit.Controller;

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

import com.easyedit.easyedit.entity.Pages;
import com.easyedit.easyedit.service.IPagesService;
import com.easyedit.easyedit.service.IUserService;
import com.easyedit.easyedit.util.ResponseResult;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/dynamicactive")
public class DynamicActiveController {

    @Autowired
    private HttpServletResponse response;

    @Autowired
    private IPagesService pagesService;

    @Autowired
    private IUserService userService;

    /**
     * @param pagesBody
     * @return
     * @throws IOException
     */
    @PostMapping(value = "")
    public ResponseResult CreateDynamicActive(@RequestBody Pages pagesBody) {
        try {
            final ResponseResult result = pagesService.createPages(pagesBody);
            response.setStatus(result.getHttpStatusCode());
            return result;
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            return ResponseResult.errorResult(ResponseResult.AppHttpCodeEnum.ERROR);
        }
    }

    @GetMapping(value = "/page/{id}")
    public ResponseResult GetDynamicActive(@PathVariable String id) throws IOException {
        try {
            ResponseResult result = userService.getUser(id);
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

    @DeleteMapping(value = "/page/{id}")
    public ResolveResult DeleteDynamicActive(@PathVariable String id) {
        return null;
    }
}

