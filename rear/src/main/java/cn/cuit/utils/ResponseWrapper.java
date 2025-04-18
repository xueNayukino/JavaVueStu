package cn.cuit.utils;

public class ResponseWrapper<T> {
    private boolean success;
    private T data;
    private String message; // 添加消息字段

    public ResponseWrapper(boolean success, T data) {
        this.success = success;
        this.data = data;
    }

    // 新增构造器，包含消息字段
    public ResponseWrapper(boolean success, T data, String message) {
        this.success = success;
        this.data = data;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message; // 添加 getter 方法
    }

    public void setMessage(String message) {
        this.message = message; // 添加 setter 方法
    }
}
