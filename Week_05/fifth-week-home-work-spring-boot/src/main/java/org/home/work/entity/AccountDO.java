package org.home.work.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.home.work.entity.base.BasicDO;

/**
 * @author: Ewen
 * @program: JAVA-000
 * @date: 2020/11/17 15:58
 * @description:
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AccountDO extends BasicDO {

    private String accountName;

    private String password;

    private String idType;

    private String idNo;

    private String mobile;

    private String email;

    private String remark;

    private String lastLoginDt;

    private String lastLoginIp;

    private String sha;

    private String sso_token;
}
