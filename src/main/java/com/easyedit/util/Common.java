/* This project is licensed under the Mulan PSL v2.
 You can use this software according to the terms and conditions of the Mulan PSL v2.
 You may obtain a copy of Mulan PSL v2 at:
     http://license.coscl.org.cn/MulanPSL2
 THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT, MERCHANTABILITY OR FIT FOR A PARTICULAR
 PURPOSE.
 See the Mulan PSL v2 for more details.
 Create: 2022
*/

package com.easyedit.util;

import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;

@Repository
public class Common {

    public boolean isDomainLegal(String[] supportDomains, String inputUrl) {
        if (StringUtils.isBlank(inputUrl)) return true;

        String substring = "";
        String[] start_path = inputUrl.split("//");
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

        for (String domain : supportDomains) {
            if (substring.endsWith(domain)) return true;
        }

        return false;
    }
}
