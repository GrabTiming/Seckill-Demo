package com.Ljt.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result<T> {

    private Integer code;
    private String message;
    private T result;

    public  Result(Integer code,String msg)
    {
        this.code = code;
        this.message = msg;
    }

    public static Result ok(String msg,Object data)
    {
        return new Result<>(200,msg,data);
    }


    public static Result ok(Object data)
    {
        return new Result<>(200,null,data);
    }

    public static Result error(Integer code,String msg)
    {
        return new Result<>(code,msg,null);
    }


}
