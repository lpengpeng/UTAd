package cn.utsoft.commons.ADView;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

/**
 * Create by 李俊鹏 on 2016/12/26 17:37
 * Function：
 * Desc：
 */

public class TrustAnyHostnameVerifier implements HostnameVerifier
{
    public boolean verify(String hostname, SSLSession session)
    {
        return true;
    }
}  
