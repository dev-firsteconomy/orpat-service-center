package com.orpatservice.app.ui.data.remote;

import android.util.Log;

import androidx.annotation.NonNull;

import com.orpatservice.app.BuildConfig;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Arrays;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by Vikas Singh on 12/Dec/2021
 */

public class RetrofitUtils {
    private static final String TAG = RetrofitUtils.class.getSimpleName();

    public static HttpLoggingInterceptor getLoggingLevel() {
        if (BuildConfig.DEBUG) {
            return new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
        }
        return new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE);
    }

    @NonNull
    public static X509TrustManager getCustomTrustManager(X509Certificate certificate) {
        TrustManager[] trustManagers = new TrustManager[1];
        try {
            // Create a KeyStore containing our trusted CAs
            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", certificate);

            // Use it to build an X509 trust manager.
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
                    TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            trustManagers = trustManagerFactory.getTrustManagers();
            if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
                throw new IllegalStateException("Unexpected default trust managers:"
                        + Arrays.toString(trustManagers));
            }
        } catch (NoSuchAlgorithmException | KeyStoreException | IOException e) {
            Log.d(TAG, "Error in getting certificate or algo or to get default keystore or to read the DemoData " + e.getMessage());
        } catch (java.security.cert.CertificateException e) {
            e.printStackTrace();
        }

        return (X509TrustManager) trustManagers[0];
    }

    @NonNull
    public static SSLSocketFactory getCustomSslSocketFactory(X509TrustManager x509TrustManager) {
        SSLSocketFactory sslSocketFactory;
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{x509TrustManager}, null);
            sslSocketFactory = sslContext.getSocketFactory();
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }

        return sslSocketFactory;
    }
}