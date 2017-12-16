package rest;

import com.kc.unsplash.api.HeaderInterceptor;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by PolaDo on 11/18/2017.
 */

public class ApiClient {
    public static String clientID = "51e5db65183a9b48b04f0bea91ec5f7fc84248245f96629fd9639e4e30828023";

    public static final String BASE_URL = "https://api.unsplash.com/";

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new HeaderInterceptor(clientID)).build();

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
