package io.github.gateway.entity;

import lombok.Data;

/**
 * @author: Ewen
 * @program: JAVA-000
 * @date: 2020/11/2 20:13
 * @description:
 */
@Data
public class AddressInfo {

    private Integer port;
    private String host;
    private String ip;
}
