package com.yibo.entity.oauth;

import java.io.Serializable;
import java.util.List;

public class UserAuthorization implements Serializable {
    private static final long serialVersionUID = -7675744871854596795L;
    private Long userId;
    private String username;
    private List<Role> roleList;
    private List<Resource> resourceList;

    public UserAuthorization() {
    }

    public UserAuthorization(Long userId, String username, List<Role> roleList, List<Resource> resourceList) {
        this.userId = userId;
        this.username = username;
        this.roleList = roleList;
        this.resourceList = resourceList;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }

    public List<Resource> getResourceList() {
        return resourceList;
    }

    public void setResourceList(List<Resource> resourceList) {
        this.resourceList = resourceList;
    }

    @Override
    public String toString() {
        return "UserAuthorization{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", roleList=" + roleList +
                ", resourceList=" + resourceList +
                '}';
    }
}