package org.home.work.entity.base;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author: Ewen
 * @program: JAVA-000
 * @date: 2020/11/17 15:58
 * @description:
 */
@Data
public class BasicDO {

    private Integer isValid;

    private Integer isDeleted;

    private String createBy;

    private LocalDateTime createDt;

    private String updateBy;

    private LocalDateTime updateDt;

    private Integer version;
}
