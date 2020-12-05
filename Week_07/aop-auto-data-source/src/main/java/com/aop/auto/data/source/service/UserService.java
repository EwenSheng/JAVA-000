package com.aop.auto.data.source.service;

import com.aop.auto.data.source.constant.DynamicDataSourceEnum;
import com.aop.auto.data.source.dao.mapper.UserMapper;
import com.aop.auto.data.source.dao.model.User;
import com.aop.auto.data.source.entity.bo.UserBO;
import com.aop.auto.data.source.entity.transformer.UserTransformer;
import com.aop.auto.data.source.service.base.AbstractService;
import com.aop.auto.data.source.support.DataSourceSelector;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: Ewen
 * @program: JAVA-000
 * @date: 2020/12/5 20:37
 * @description:
 */
@Slf4j
@Service
@RestController
@RequestMapping("/user")
public class UserService extends AbstractService<User, UserMapper> {

    @Autowired
    private UserTransformer transformer;

    @GetMapping("/list")
    @DataSourceSelector(value = DynamicDataSourceEnum.SLAVE)
    public List<UserBO> list() {
        EntityWrapper<User> wrapper = createEntityWrapper();

        List<User> models = getMapper().selectList(wrapper);

        return transformer.toBOs(models);
    }

    @PostMapping("/update")
    @DataSourceSelector(value = DynamicDataSourceEnum.MASTER)
    public void update(@RequestBody UserBO bo) {

        User model = new User();

        BeanUtils.copyProperties(bo, model);

        getMapper().updateById(model);
    }
}
