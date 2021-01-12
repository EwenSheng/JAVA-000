package com.syw.kafka.demo.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author: Ewen-Sheng
 * @date: 2021/1/12 19:48
 * @description:
 */
@Data
public class Message {
    //id
    private Long id;

    //消息
    private String msg;

    //时间戳
    private Date sendTime;
}
