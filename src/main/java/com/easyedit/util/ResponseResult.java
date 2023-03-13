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

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

public class ResponseResult implements Serializable {

    @JsonIgnore
    private Integer httpStatusCode;

    private Integer statusCode;

    private String message;

    private Object data;

    public Integer getHttpStatusCode() {
        return this.httpStatusCode;
    }

    public void setHttpStatusCode(Integer httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    public Integer getStatusCode() {
        return this.statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String errorMessage) {
        this.message = errorMessage;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public ResponseResult() {
        this.statusCode = 200;
    }


    public ResponseResult(Integer statusCode, Object data) {
        this.statusCode = statusCode;
        this.data = data;
    }

    public ResponseResult(Integer statusCode, String message, Object data) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
    }

    public ResponseResult(Integer statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public static ResponseResult errorResult(Integer statusCode, String message) {
        ResponseResult result = new ResponseResult();
        return result.error(statusCode, message);
    }

    public static ResponseResult errorResult(Object data) {
        ResponseResult result = setAppHttpCodeEnum(AppHttpCodeEnum.DATA_ALREADY_EXIST, AppHttpCodeEnum.DATA_ALREADY_EXIST.getMessage());
        if(data!=null) {
            result.setData(data);
        }
        return result;
    }

    public static ResponseResult okResult(int statusCode, String message) {
        ResponseResult result = new ResponseResult();
        return result.ok(statusCode, null, message);
    }

    public static ResponseResult okResult(Object data) {
        ResponseResult result = setAppHttpCodeEnum(AppHttpCodeEnum.SUCCESS, AppHttpCodeEnum.SUCCESS.getMessage());
        if(data!=null) {
            result.setData(data);
        }
        return result;
    }

    public static ResponseResult errorResult(AppHttpCodeEnum enums){
        return setAppHttpCodeEnum(enums,enums.getMessage());
    }

    public static ResponseResult errorResult(AppHttpCodeEnum enums, String errorMessage){
        return setAppHttpCodeEnum(enums,errorMessage);
    }


    public static ResponseResult setAppHttpCodeEnum(AppHttpCodeEnum enums){
        return okResult(enums.getStatusCode(), enums.getMessage());
    }

    private static ResponseResult setAppHttpCodeEnum(AppHttpCodeEnum enums, String errorMessage){
        return okResult(enums.getStatusCode(),errorMessage);
    }

    public ResponseResult HttpCode(Integer code) {
        this.httpStatusCode = code;
        return this;
    }


    public ResponseResult error(Integer statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
        return this;
    }


    public ResponseResult ok(Integer statusCode, Object data) {
        this.statusCode = statusCode;
        this.data = data;
        return this;
    }

    public ResponseResult ok(Integer statusCode, Object data, String message) {
        this.statusCode = statusCode;
        this.data = data;
        this.message = message;
        return this;
    }

    public ResponseResult ok(Object data) {
        this.data = data;
        return this;
    }

    public enum AppHttpCodeEnum {


        // 成功段固定为200
        SUCCESS(200,"successful operation"),
        // 通用失败
        ERROR(5000, "error"),
        // 请求格式错误
        BAD_REQUEST(400, "bad request"),
        // 未查询到结果
        NO_RESULT_FOUND(404, "No result found"),

        NO_DATA_WAS_DELETED(503, "No data was deleted"),

        NO_DATA_WAS_UPDATE(503, "No data was update"),

        NO_DATA_WAS_INSERT(503, "No data was insert"),
        // 数据插入错误
        DATA_INSERTION_ERROR(501, "data insertion error"),

        DATA_ALREADY_EXIST(409, "exist data with the same name");

        int statusCode;

        String message;

        AppHttpCodeEnum(int code, String message){
            this.statusCode = code;
            this.message = message;
        }

        public int getStatusCode() {
            return this.statusCode;
        }

        public String getMessage() {
            return this.message;
        }

    }


}
