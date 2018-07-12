package com.yibo.oauth.service.impl;

import com.yibo.oauth.dao.SysUserMapper;
import com.yibo.oauth.entity.SysUser;
import com.yibo.oauth.service.TestService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service("testService")
public class TestServiceImpl implements TestService {
    @Resource
    private SysUserMapper sysUserMapper;
    @Override
    @Transactional(readOnly = true)
    public SysUser getUserById(Long id){
        return sysUserMapper.selectByPrimaryKey(id);
    }
}
