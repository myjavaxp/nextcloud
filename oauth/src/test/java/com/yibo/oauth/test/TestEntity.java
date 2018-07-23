package com.yibo.oauth.test;

import com.yibo.entity.common.ResponseEntity;
import org.junit.Test;

public class TestEntity {
    @Test
    public void test01() {
        ResponseEntity<Void> responseEntity = new ResponseEntity<>();
        responseEntity.setMessage("test message");
        System.out.println(responseEntity);
    }
}