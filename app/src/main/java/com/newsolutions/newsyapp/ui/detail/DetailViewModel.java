package com.newsolutions.newsyapp.ui.detail;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.newsolutions.newsyapp.data.room.post.PostsGet;

public class DetailViewModel extends ViewModel {
    public MutableLiveData<PostsGet> post=new MutableLiveData<>();
    public void setData(PostsGet postsGet) {
        post.setValue(postsGet);
    }
}
