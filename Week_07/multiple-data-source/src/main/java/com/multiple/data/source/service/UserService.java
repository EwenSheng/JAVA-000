package com.multiple.data.source.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.multiple.data.source.dao.mapper.UserMapper;
import com.multiple.data.source.dao.model.User;
import com.multiple.data.source.entity.bo.UserBO;
import com.multiple.data.source.entity.transformer.UserTransformer;
import com.multiple.data.source.service.base.AbstractService;
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
    public List<UserBO> list() {
        QueryWrapper<User> wrapper = createEntityWrapper();

        List<User> models = getMapper().selectList(wrapper);

        return transformer.toBOs(models);
    }

    @PostMapping("/update")
    public void update(@RequestBody UserBO bo) {

        User model = new User();

        BeanUtils.copyProperties(bo, model);

        getMapper().updateById(model);
    }
}
