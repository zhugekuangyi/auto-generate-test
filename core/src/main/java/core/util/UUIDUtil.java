/*
 * uifuture.com
 * Copyright (C) 2013-2019 All Rights Reserved.
 */
package core.util;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import java.util.UUID;

/**
 * @author chuqi
 */
public class UUIDUtil {

    private static String[] strDigits = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
                                          "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "a", "b", "c", "d", "e", "f",
                                          "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z" };

    private static String str = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    /**
     * 生成10位UUID
     *
     * @return 生成10位UUID
     */
    public static String getID() {
        UUID uuid = UUID.randomUUID();
        // 改变uuid的生成规则
        return HashUtil.convertToHashStr(uuid.getMostSignificantBits(), 5) + HashUtil.convertToHashStr(uuid.getLeastSignificantBits(), 5);
    }

    /**
     * 获取随机字符串(字节)
     *
     * @return 返回随机字节
     */
    public static String getRandomChar() {
        int rand = RandomUtils.nextInt(0, strDigits.length - 1);
        // 改变uuid的生成规则
        return strDigits[rand];
    }

    /**
     * 获取随机字符串
     *
     * @param count 字符串长度
     * @return count长度的随机字符串
     */
    public static String getRandomString(int count) {
        return RandomStringUtils.random(count, str);
    }

}