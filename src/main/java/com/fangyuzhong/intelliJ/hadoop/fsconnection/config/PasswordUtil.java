package com.fangyuzhong.intelliJ.hadoop.fsconnection.config;

import com.fangyuzhong.intelliJ.hadoop.core.LoggerFactory;
import com.fangyuzhong.intelliJ.hadoop.core.util.StringUtil;
import com.intellij.openapi.diagnostic.Logger;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Created by fangyuzhong on 17-7-21.
 * Update by YUDOUFU123 on 2020/5/16
 */
public class PasswordUtil
{
    public static final Logger LOGGER = LoggerFactory.createLogger();

    public static String encodePassword(String password)
    {
        try
        {
            password = StringUtil.isEmpty(password) ? "" : Base64.getEncoder().encodeToString(nvl(password).getBytes(StandardCharsets.UTF_8));
        } catch (Exception e)
        {
            LOGGER.error("Error encoding password", e);
        }
        return password;
    }

    public static String decodePassword(String password)
    {
        try
        {
            password = StringUtil.isEmpty(password) ? "" : new String(Base64.getDecoder().decode(password.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
        } catch (Exception ignored)
        {
        }
        return password;
    }

    private static String nvl(String value)
    {
        return value == null ? "" : value;
    }
}
