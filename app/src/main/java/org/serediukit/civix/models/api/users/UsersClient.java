package org.serediukit.civix.models.api.users;

import android.content.Context;
import android.util.Log;

import com.squareup.moshi.Moshi;

import org.serediukit.civix.models.api.BaseHTTPClient;
import org.serediukit.civix.models.api.auth.RefreshAuthenticator;
import org.serediukit.civix.models.api.interceptors.AuthInterceptor;
import org.serediukit.civix.models.api.users.response.GetUserResponse;
import org.serediukit.civix.models.token.TokenManager;

import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class UsersClient implements BaseHTTPClient {
    private static UsersClient instance;
    private UsersService usersService;

    private UsersClient(Context context) {
        try {
            TokenManager tokenManager = TokenManager.getInstance(context);

            Moshi moshi = new Moshi.Builder().build();

            Retrofit baseRetrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(MoshiConverterFactory.create(moshi))
                    .build();

            AuthInterceptor authInterceptor = new AuthInterceptor(tokenManager);
            RefreshAuthenticator refreshAuthenticator = new RefreshAuthenticator(tokenManager, baseRetrofit);

            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(X509Certificate[] chain, String authType) {}

                        @Override
                        public void checkServerTrusted(X509Certificate[] chain, String authType) {}

                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[]{};
                        }
                    }
            };

            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustAllCerts[0])
                    .hostnameVerifier((hostname, session) -> true)
                    .addInterceptor(authInterceptor)
                    .authenticator(refreshAuthenticator)
                    .build();

            Retrofit retrofit = baseRetrofit.newBuilder()
                    .client(okHttpClient)
                    .build();

            usersService = retrofit.create(UsersService.class);
        } catch (Exception e) {
            Log.e("USERS CLIENT", e.toString());
        }
    }

    public static UsersClient getInstance(Context context) {
        if (instance == null) {
            instance = new UsersClient(context);
        }
        return instance;
    }

    /**
     * Get the current user's profile information.
     * @return A Retrofit Call object for the get current user request.
     */
    public Call<GetUserResponse> getCurrentUser() {
        return usersService.getCurrentUser();
    }
}
