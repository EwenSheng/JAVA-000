package com.multiple.data.source.entity.transformer;

import com.google.common.collect.Lists;
import com.multiple.data.source.dao.model.User;
import com.multiple.data.source.entity.bo.UserBO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author: Ewen
 * @program: JAVA-000
 * @date: 2020/12/5 20:48
 * @description:
 */
@Component
public class UserTransformer {

    public UserBO toBO(User model) {

        UserBO bo = new UserBO();

        BeanUtils.copyProperties(model, bo);

        return bo;
    }

    public List<UserBO> toBOs(List<User> models) {

        List<UserBO> bos = Lists.newArrayList();

        models.forEach(model -> bos.add(toBO(model)));

        return bos;
    }
}
