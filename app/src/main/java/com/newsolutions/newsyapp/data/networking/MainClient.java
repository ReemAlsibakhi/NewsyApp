package com.newsolutions.newsyapp.data.networking;

import com.newsolutions.newsyapp.data.room.post.PostsGet;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainClient {
    private MainRetrofit mainRetrofit;
    private static MainClient mainClient;
    private OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    public MainClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConstant.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mainRetrofit = retrofit.create(MainRetrofit.class); }
    public static MainClient getInstance() {
        if (mainClient == null) {
            mainClient = new MainClient();
        } return mainClient;
    }

    public Call<List<PostsGet>> getPosts(){
        return mainRetrofit.getPosts();
    }
}
