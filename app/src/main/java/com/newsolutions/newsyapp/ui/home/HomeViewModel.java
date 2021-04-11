package com.newsolutions.newsyapp.ui.home;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.newsolutions.newsyapp.R;
import com.newsolutions.newsyapp.adapter.PostsAdapter;
import com.newsolutions.newsyapp.data.networking.MainClient;
import com.newsolutions.newsyapp.data.room.DataBase;
import com.newsolutions.newsyapp.data.room.post.PostsGet;
import com.newsolutions.newsyapp.ui.detail.DetailActivity;

import java.util.ArrayList;
import java.util.List;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeViewModel extends ViewModel {
    private PostsAdapter adapter;
    private MutableLiveData<List<PostsGet>> posts;
    public MutableLiveData<PostsGet> selected;
    public MutableLiveData<String> pageTitle=new MutableLiveData<>();
    private PostsGet post;
    private int pos = -1;
    private List<PostsGet> postList;
    private List<PostsGet> postsAPI;
    private static final String TAG = "HomeViewModel";

    public void init() {
        post = new PostsGet();
        adapter = new PostsAdapter();
        posts = new MutableLiveData<>();
        selected = new MutableLiveData<>();
        postList = new ArrayList<>();
      //  pageTitle.setValue("HOME");
    }
    public void getFromRoom(View view) {
        DataBase.getInstance(view.getContext()).postDao().getAll()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<PostsGet>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.e(TAG, "onSubscribe: " + d.toString());
                    }

                    @Override
                    public void onNext(@NonNull List<PostsGet> list) {
                        Log.e(TAG, "getFromRoom: " + list);
                        posts.setValue(list);
                        postList.addAll(list);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(TAG, "onError: " + e.getMessage().toString());
                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "onComplete: ");
                    }
                });
    }

    public void fetchList() {
        Callback<List<PostsGet>> callback = new Callback<List<PostsGet>>() {
            @Override
            public void onResponse(Call<List<PostsGet>> call, Response<List<PostsGet>> response) {
                postsAPI = response.body();
                Log.e(TAG, "onResponse: "+postsAPI.size() );
                posts.setValue(response.body());
                Log.e(TAG, "onResponse: " + response.body());
                //postList.addAll(postsAPI);
                //BaseClass.getInstance().postDao().insertPost(postList);
            }

            @Override
            public void onFailure(Call<List<PostsGet>> call, Throwable t) {
                Log.e("failure", t.getMessage(), t);
            }
        };
        MainClient.getInstance().getPosts().enqueue(callback);

    }
    public List<PostsGet> getPost(int count){
        for (int i=count;i<10;i++){
            postList.add(postsAPI.get(i));
        }
        return postList;
    }
    public void clickDelete(int position) {
        Log.e(TAG, "clickDelete: " + postList.size() + "position" + position);
        PostsGet post = postList.get(position);
       // BaseClass.getInstance().postDao().deletePost(post);
    }
    public void onClickDetail(View view,int position) {
        Intent intent=new Intent(view.getContext(), DetailActivity.class);
        intent.putExtra("post",getPostAt(position));
        view.getContext().startActivity(intent);
        Log.e(TAG, "onClickDetail: "+getPostAt(position) );
    }

    public void clickUpdate(View view, int position) {
//        PostsGet post = postList.get(position);
//        NavDirections action = ViewPostsFragmentDirections.actionViewPostsFragmentToUpdateInfoFragment(post);
//        Navigation.findNavController(view).navigate(action);
    }
    public MutableLiveData<List<PostsGet>> getPosts() {
        return posts;
    }
    public PostsAdapter getAdapter() {
        return adapter;
    }
    public void setPostsInAdapter(List<PostsGet> posts) {
        this.adapter.setPosts(posts);
        this.adapter.notifyDataSetChanged();
    }
    public MutableLiveData<PostsGet> getSelected() {
        return selected;
    }
    public void onItemClick(Integer index) {
        pos = index;
        Log.e(TAG, "onItemClick: " + index + pos);
        PostsGet db = getPostAt(index);
        selected.setValue(db);
    }
    public PostsGet getPostAt(Integer index) {
        if (getPosts().getValue() != null &&
                index != null &&
                getPosts().getValue().size() > index) {
            return getPosts().getValue().get(index);
        }
        return null;
    }
}
