package com.orpatservice.app.data.remote;


import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orpatservice.app.BuildConfig;
import com.orpatservice.app.data.repository.SessionRepository;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.CertificatePinner;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Vikas Singh on 12/Dec/2021
 */

public class ApiClient {
    private static final int TIMEOUT_PERIOD = 120;

    private static ApiEndPoint apiEndpoint = null;

    public static ApiEndPoint getClient() {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        // add your other interceptors …
        // add logging as last interceptor
        httpClient.addInterceptor(logging);

        /*String BASE_URL = SharedPrefs.getInstance().getString(SharedPrefsConstant.BASE_URL, BuildConfig.DOMAIN);*/
        //String BASE_URL = BuildConfig.DOMAIN;
        String BASE_URL = "https://testingservice.orpatdistributors.com/api/";
        if (apiEndpoint == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();
            apiEndpoint = retrofit.create(ApiEndPoint.class);
        }
        return apiEndpoint;
    }

    public static ApiEndPoint getApi() {
        if (apiEndpoint == null) {
            Retrofit retrofit = getNormalRetrofitInstance(BuildConfig.ENABLE_SSL_PINNING);
            apiEndpoint = retrofit.create(ApiEndPoint.class);
        }
        return apiEndpoint;
    }


    /**
     * Create {@link Retrofit} instance using {@link OkHttpClient} and
     * IAM api base url for using {@link ApiEndPoint} webservice.
     * The retrofit object then create implementation  of API endpoint defined by {@link ApiEndPoint}.
     *
     * @return {@link ApiEndPoint}
     */
    public static ApiEndPoint getAuthApi() {
        if (apiEndpoint == null) {
            Retrofit retrofit = getHeaderRetrofitInstance(BuildConfig.ENABLE_SSL_PINNING);
            apiEndpoint = retrofit.create(ApiEndPoint.class);
        }
        return apiEndpoint;
    }

    @NonNull
    private static Retrofit getHeaderRetrofitInstance(boolean hasSSLPinning) {
        /* String baseUrl = SharedPrefs.getInstance().getString(SharedPrefsConstant.BASE_URL, BuildConfig.DOMAIN);*/
        String baseUrl = BuildConfig.DOMAIN;
       // String baseUrl = "https://testingservice.orpatdistributors.com/api/";

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(hasSSLPinning ? getSSLOkHttpClient(false) : getNormalOkHttpClient(true))
                .addConverterFactory(GsonConverterFactory.create(gson))
                /*for rxjava if not using then comment*/
                /*.addCallAdapterFactory(RxJava2CallAdapterFactory.create())*/
                .build();
    }

    @NonNull
    private static Retrofit getNormalRetrofitInstance(boolean hasSSLPinning) {
        /* String baseUrl = SharedPrefs.getInstance().getString(SharedPrefsConstant.BASE_URL, BuildConfig.DOMAIN);*/
        String baseUrl = BuildConfig.DOMAIN;
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(hasSSLPinning ? getSSLOkHttpClient(false) : getNormalOkHttpClient(true))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    public static OkHttpClient getSSLOkHttpClient(boolean addTokenInHeaders) {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder()
                .readTimeout(TIMEOUT_PERIOD, TimeUnit.SECONDS) //Sets the default read timeout for new connections
                .writeTimeout(TIMEOUT_PERIOD, TimeUnit.SECONDS) //set the default write timeout for new connections
                .connectTimeout(TIMEOUT_PERIOD, TimeUnit.SECONDS); //Sets the default connect timeout for new connections.

        /*get certificate from assets folder*/
        X509Certificate certificate = CryptographyUtils.getCertificate();

        /*make ssl connection trusted for self-signed certificate*/
        X509TrustManager x509TrustManager = RetrofitUtils.getCustomTrustManager(certificate);
        SSLSocketFactory sslSocketFactory = RetrofitUtils.getCustomSslSocketFactory(x509TrustManager);
        okHttpClientBuilder.sslSocketFactory(sslSocketFactory, x509TrustManager);

        /*verify host because we are using self-signed certificate*/
        okHttpClientBuilder.hostnameVerifier((hostname, session) -> BuildConfig.SSL_HOST.contains(hostname));

        /*set ssl pinning for network connection*/
        String certificateSha1 = CryptographyUtils.getCertificateSha1(certificate);
        okHttpClientBuilder.certificatePinner(
                new CertificatePinner.Builder().add(BuildConfig.SSL_HOST, certificateSha1).build());

        /*log network call*/
        okHttpClientBuilder.addInterceptor(RetrofitUtils.getLoggingLevel());

        /*setting authorization token to call*/
        if (addTokenInHeaders) {
            okHttpClientBuilder.addInterceptor(chain -> {
                Request original = chain.request();
                // Request customization: add request headers
                Request.Builder requestBuilder = original.newBuilder()
                        .header("Authorization", SessionRepository.Companion.getInstance().getHeaderToken())
                        .header("Accept", "application/json")
                         .header("Device-Id", SessionRepository.Companion.getInstance().getDeviceId())
                         .header("Device-Type", "ANDROID")
                        .header("Content-Type", "application/json");

                Request request = requestBuilder.build();
                return chain.proceed(request);
            });
        } else {
            okHttpClientBuilder.addInterceptor(chain -> {
                Request original = chain.request();
                // Request customization: add request headers
                Request.Builder requestBuilder = original.newBuilder()
                        .header("Authorization", SessionRepository.Companion.getInstance().getHeaderToken())
                        .header("Accept", "application/json")
                         .header("Device-Id", SessionRepository.Companion.getInstance().getDeviceId())
                         .header("Device-Type", "ANDROID")
                        .header("Content-Type", "application/json");

                Request request = requestBuilder.build();
                return chain.proceed(request);
            });
        }

        return okHttpClientBuilder.build();
    }

    public static OkHttpClient getNormalOkHttpClient(boolean addTokenInHeaders) {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder()
                .readTimeout(TIMEOUT_PERIOD, TimeUnit.SECONDS) //Sets the default read timeout for new connections
                .writeTimeout(TIMEOUT_PERIOD, TimeUnit.SECONDS) //set the default write timeout for new connections
                .connectTimeout(TIMEOUT_PERIOD, TimeUnit.SECONDS); //Sets the default connect timeout for new connections.

        /*log network call*/
        okHttpClientBuilder.addInterceptor(RetrofitUtils.getLoggingLevel());



        /*setting authorization token to call*/
        if (addTokenInHeaders) {

            okHttpClientBuilder.addInterceptor(chain -> {
                Request original = chain.request();
                // Request customization: add request headers
                Request.Builder requestBuilder = original.newBuilder()
                        .header("Authorization", SessionRepository.Companion.getInstance().getHeaderToken())
                        .header("Accept", "application/json")
                        /*.header("Cookie", "PHPSESSID=lgt43ahhkppsmea6r5jc6rt67l"+ SharedPrefs.getInstance().getString(Constant.COOKIES_ID,""))*/
                         .header("Device-Id", SessionRepository.Companion.getInstance().getDeviceId())
                         .header("Device-Type", "ANDROID")
                        .header("Content-Type", "application/json");

                Request request = requestBuilder.build();
                return chain.proceed(request);
            });
        } else {
            okHttpClientBuilder.addInterceptor(chain -> {
                Request original = chain.request();
                // Request customization: add request headers
                Request.Builder requestBuilder = original.newBuilder()
                        .header("Authorization", SessionRepository.Companion.getInstance().getHeaderToken())
                        .header("Accept", "application/json")
                         .header("Device-Id", SessionRepository.Companion.getInstance().getDeviceId())
                         .header("Device-Type", "ANDROID")
                        .header("Content-Type", "application/json");

                Request request = requestBuilder.build();
                return chain.proceed(request);
            });
        }

        //test
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            X509Certificate[] cArrr = new X509Certificate[0];
                            return cArrr;
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            OkHttpClient okHttpClient = builder.build();
            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}