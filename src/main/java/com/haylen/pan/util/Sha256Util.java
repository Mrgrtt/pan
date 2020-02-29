package com.haylen.pan.util;

import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * @author haylen
 * @date 2020-02-29
 */
public class Sha256Util {
   public static String encode(InputStream input) throws Exception {
        MessageDigest messageDigest = MessageDigest.getInstance("sha-256");
        byte[] buffer = new byte[1024];
        int length = 0;
        while ((length = input.read(buffer)) != -1) {
            messageDigest.update(buffer, 0, length);
        }
        input.close();
        byte[] sha256Bytes = messageDigest.digest();
        String code = new BigInteger(1, sha256Bytes).toString(16);
        return code;
    }
}
