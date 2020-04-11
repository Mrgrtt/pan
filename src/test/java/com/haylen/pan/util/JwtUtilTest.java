package com.haylen.pan.util;

import com.haylen.pan.util.JwtUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author haylen
 * @date 2020-0330
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class JwtUtilTest {
    @Autowired
    private JwtUtil jwtUtil;

    @Test
    public void test() {
        String username = "ffkfj88820";
        String token = jwtUtil.builtToken(username);
        Assert.assertEquals(username, jwtUtil.getUsernameByToken(token));
    }
}
