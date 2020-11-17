package org.home.work.service;

import org.home.work.entity.BodyInfo;
import org.springframework.stereotype.Service;

/**
 * @author: Ewen
 * @program: JAVA-000
 * @date: 2020/11/15 20:07
 * @description:
 */
@Service
public class BodyInfoService {

    public String print(BodyInfo bodyInfo) {

        System.out.println(bodyInfo.toString());

        return bodyInfo.toString();
    }
}
