package com.homethy.mls.listing.tools.utils;

import org.apache.commons.lang3.StringUtils;

public class StringUtil {

    /**
     * 判断字符串是否为空
     */
    public static boolean isEmpty(String str) {
        if (str != null) {
            str = str.trim();
        }
        return StringUtils.isEmpty(str);
    }
}
