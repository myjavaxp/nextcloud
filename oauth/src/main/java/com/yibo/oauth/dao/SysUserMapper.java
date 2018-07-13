package com.yibo.oauth.dao;

import com.yibo.entity.oauth.Resource;
import com.yibo.entity.oauth.Role;
import com.yibo.entity.oauth.SysUser;

import java.util.List;

public interface SysUserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SysUser record);

    int insertSelective(SysUser record);

    SysUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysUser record);

    int updateByPrimaryKey(SysUser record);

    SysUser selectByUsername(String username);

    List<Role> selectRolesByUserId(Long userId);

    List<Resource> selectResourceListByRoleList(List<Role> roleList);
}