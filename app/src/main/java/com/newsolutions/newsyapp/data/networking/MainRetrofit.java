package com.newsolutions.newsyapp.data.networking;

import com.newsolutions.newsyapp.data.room.post.PostsGet;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface MainRetrofit {
    @GET("photos")
    Call<List<PostsGet>> getPosts();
}
