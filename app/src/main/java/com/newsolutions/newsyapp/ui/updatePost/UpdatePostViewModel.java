package com.newsolutions.newsyapp.ui.updatePost;
import android.app.Activity;
import android.view.View;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.newsolutions.newsyapp.data.room.DataBase;
import com.newsolutions.newsyapp.data.room.post.PostsGet;

public class UpdatePostViewModel extends ViewModel {
    public MutableLiveData<PostsGet> post=new MutableLiveData<>();
    public void setData(PostsGet postsGet) {
        post.setValue(postsGet);
    }
    public MutableLiveData<PostsGet> getPost() {
//        if (post == null) {
//            post = new MutableLiveData<>();
//        }
        return post;
    }

    public void onClickUpdate(Activity activity, View view) {
        DataBase.getInstance(view.getContext()).postDao().updatePost(post.getValue());

    }

    public void update(Activity activity, View view, PostsGet post) {
        DataBase.getInstance(view.getContext()).postDao().updatePost(post);
      //  activity.finish();
    }
}
