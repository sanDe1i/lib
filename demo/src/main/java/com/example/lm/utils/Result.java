package com.example.lm.utils;
import java.util.HashMap;
import java.util.Map;

public class Result {
    private boolean success;
    private Integer code;
    private String message;
    private Map<String, String> data = new HashMap<>();

   private Result(){}

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(ResultCode code) {
        this.code = code.getCode();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }

    public void addData(String key, String value) {
        this.data.put(key, value);
    }

    @Override
    public String toString() {
        return "Result{" +
                "success=" + success +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    public static Result ok(){
       Result r = new Result();
       r.setSuccess(true);
       r.setCode(ResultCode.SUCCESS);
       r.setMessage("success");
       return r;
    }

    public static Result error(){
       Result r = new Result();
       r.setSuccess(false);
       r.setCode(ResultCode.ERROR);
       r.setMessage("fail");
       return r;
    }

    public Result success(Boolean success){
       this.setSuccess(success);
       return this;
    }

//    public Result code(Integer code){
//        this.setCode(code);
//        return this;
//    }

    public Result message(String message){
        this.setMessage(message);
        return this;
    }

    public Result data(String key, String value){
        this.data.put(key, value);
        return this;
    }

    public Result data(Map<String,String> map){
        this.setData(map);
        return this;
    }

    public static void main(String[] args) {
        String token = "123123213321";
        System.out.println(Result.ok().data("token",token));
    }
}
