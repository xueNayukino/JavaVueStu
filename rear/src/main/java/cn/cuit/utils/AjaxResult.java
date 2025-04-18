package cn.cuit.utils;

import lombok.Data;

@Data
public class AjaxResult {
    private Boolean success = true;
    private String message = "操作成功！！";
    private Object result;
    private String token;

    public static AjaxResult me() {
        return new AjaxResult();
    }

    public AjaxResult setSuccess(Boolean success) {
        this.success = success;
        return this;
    }

    public AjaxResult setMessage(String message) {
        this.message = message;
        return this;
    }

    public AjaxResult setResult(Object result) {
        this.result = result;
        return this;
    }

    public AjaxResult setToken(String token) {
        this.token = token;
        return this;
    }
}