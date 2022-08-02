package com.demo.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Response {

    private String message;
    private Date date;
    private Object entity;

    public static Response buildResponse(String message,Object entity){

        Response r = new Response();
        r.setMessage(message);
        r.setDate(new Date());
        r.setEntity(entity);

        return r;
    }
}
