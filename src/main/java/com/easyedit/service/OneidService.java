package com.easyedit.service;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhongjun
 * @since 2023-03-01
 */
@Repository
public class OneidService {

    @Value("${spring.auth.host}")
    String g_authHost;

    @Value("${spring.general.community}")
    String g_community;

    public List<String> getUserPermission(String token, String cookie) {
        String community = g_community;
        String oneIdHost = g_authHost;
        String url = String.format("%s/oneid/user/permissions?community=%s", oneIdHost, community);

        try {
            HttpResponse<JsonNode> response = Unirest.get(url)
                    .header("token", token)
                    .header("Cookie", "_Y_G_=" + cookie)
                    .asJson();
            JSONArray jsonArray = response.getBody().getObject().getJSONObject("data")
                                    .getJSONArray("permissions");

            List<String> list = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                list.add(jsonArray.getString(i));
            }
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
}
