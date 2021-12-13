package com.orpatservice.app.ui.data.remote;


import static java.security.spec.MGF1ParameterSpec.SHA1;

import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.orpatservice.app.BuildConfig;
import com.orpatservice.app.OrpatApp;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

/**
 * Created by Vikas Singh on 12/Dec/2021
 */
public class CryptographyUtils {
    private static final String TAG = CryptographyUtils.class.getSimpleName();

    private static final String ALGO_SHA_1 = "SHA-1";

    @NonNull
    public static String getCertificateSha1(X509Certificate certificate) {
        byte[] encodedPublicKey = certificate.getPublicKey().getEncoded();
        return SHA1 + generateSHA1Hash(encodedPublicKey);
    }

    @NonNull
    public static String generateSHA1Hash(byte[] messageBytes) {
        String hash;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(ALGO_SHA_1);
            messageDigest.update(messageBytes);
            byte[] digest = messageDigest.digest();
            hash = Base64.encodeToString(digest, Base64.DEFAULT);
        } catch (NoSuchAlgorithmException e) {
            Log.d(TAG, "Error in getting " + e.getMessage());
            hash = "";
        }

        return hash;
    }

    @Nullable
    public static X509Certificate getCertificate() {
        String certFilename = BuildConfig.CERT_FILE_NAME;
        X509Certificate certificate = null;
        try {
            InputStream certInputStream = OrpatApp.getInstance().getAssets().open(certFilename);
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            certificate = (X509Certificate) certificateFactory.generateCertificate(certInputStream);
        } catch (CertificateException | IOException e) {
            e.printStackTrace();
        }

        return certificate;
    }
}