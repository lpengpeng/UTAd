package cn.utsoft.commons.ADView;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

/**
 * Create by 李俊鹏 on 2016/12/26 13:43
 * Function：
 * Desc：
 */

public class TrustAnyTrustManager implements X509TrustManager
{

    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException
    {
    }

    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException
    {
    }

    public X509Certificate[] getAcceptedIssuers()
    {
        return new X509Certificate[]{};
    }
}  
