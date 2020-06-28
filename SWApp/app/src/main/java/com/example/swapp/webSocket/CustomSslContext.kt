package com.example.swapp.webSocket

import java.lang.RuntimeException
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.security.Provider
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class CustomSslContext {
    @Throws(NoSuchAlgorithmException::class)
    fun getInstance(protocol : String) : SSLContext{
        return init(SSLContext.getInstance(protocol))
    }

    @Throws(NoSuchAlgorithmException::class, NoSuchAlgorithmException::class)
    fun getInstance(protocol: String, provider: Provider) : SSLContext{
        return init(SSLContext.getInstance(protocol, provider))
    }

    fun init(context: SSLContext) : SSLContext{
        try {
            context.init(null, arrayOf<TrustManager>(CustomTrustManager()), null)
        } catch (e : KeyManagementException){
            throw RuntimeException("Failed to initialize an SSLContext.", e)
        }
        return context
    }

    inner class CustomTrustManager : X509TrustManager{
        override fun getAcceptedIssuers(): Array<X509Certificate>? {
            return null
        }
        override fun checkClientTrusted(certs: Array<X509Certificate>, authType: String) {}
        override fun checkServerTrusted(certs: Array<X509Certificate>, authType: String) {}
    }
}