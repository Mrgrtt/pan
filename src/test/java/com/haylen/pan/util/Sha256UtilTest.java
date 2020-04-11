package com.haylen.pan.util;

import com.haylen.pan.util.Sha256Util;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * @author haylen
 * @date 2020-0330
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class Sha256UtilTest {
    @Test
    public void encode() {
        String[] inputs = new String[] {"ffii##4gg4343...j", "jjjf..;#9876@@jf"};
        String[] results = new String[] {
                "5f3985ccb30d0bce7c24bb7d4b3f299d34d9ff9b106c4735497470c818b6c057",
                "69e74f5e3f17a12d4de9155572569d8577e973879cfc3f9bbe280d1c41eb6eb4"
        };

        try {
            for (int i = 0; i < inputs.length; ++i) {
                String r = Sha256Util.encode(new ByteArrayInputStream(inputs[i].getBytes()));
                Assert.assertEquals(results[i], r);
            }
        } catch (Exception e) {
            Assert.fail();
        }
    }
}
