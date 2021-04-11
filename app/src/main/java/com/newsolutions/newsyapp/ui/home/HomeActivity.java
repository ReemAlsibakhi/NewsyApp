package com.newsolutions.newsyapp.ui.home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.newsolutions.newsyapp.R;
import com.newsolutions.newsyapp.adapter.PostsAdapter;
import com.newsolutions.newsyapp.data.networking.MainClient;
import com.newsolutions.newsyapp.data.room.DataBase;
import com.newsolutions.newsyapp.data.room.post.PostsGet;
import com.newsolutions.newsyapp.databinding.ActivityHomeBinding;
import com.newsolutions.newsyapp.ui.detail.DetailActivity;
import com.newsolutions.newsyapp.ui.updatePost.UpdatePostActivity;

import java.util.ArrayList;
import java.util.List;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {
    private HomeViewModel mViewModel;
    private ActivityHomeBinding binding;
    View view;
    List<PostsGet> postList = new ArrayList<PostsGet>();
    private PostsAdapter paginationAdapter;
    private static final String TAG = "HomeActivity";
    private int pastVisiblesItems = 0;
    private int visibleItemCount = 0;
    private int totalItemCount = 0;
    private boolean loading = false;
    Handler handler;
    private int end = 10;
    private int index=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        view = binding.getRoot();
        setContentView(view);
        mViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding.setViewModel(mViewModel);
        binding.setLifecycleOwner(this);
        if (savedInstanceState == null) {
            mViewModel.init();
        }
        handler = new Handler(Looper.getMainLooper());
        binding.actionBar.tvTitle.setText(R.string.home);

        if(checkInternet()){
            loadFirstPage();
        }else {
            getFromRoom();
        }
        initRecycler();
        //  viewPosts();
    }
    private void initRecycler() {
        LinearLayoutManager lm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.recyclerPosts.setLayoutManager(lm);
        paginationAdapter = new PostsAdapter(postList, HomeActivity.this);
        binding.recyclerPosts.setAdapter(paginationAdapter);
        paginationAdapter.setOnItemClickListener(new PostsAdapter.OnItemClickListener() {
            @Override
            public void onClicked(int position, PostsGet post) {
                Intent intent=new Intent(HomeActivity.this, UpdatePostActivity.class);
                intent.putExtra("post",post);
                intent.putExtra("position",position);
                 startActivityForResult(intent,1);
                Log.e(TAG, "onClickUpdate: "+position+" : "+post);
            }
        });

        binding.recyclerPosts.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.d(TAG, dy + "");
                if (dy > 0) {
                    if (!loading) {
                        visibleItemCount = lm.getChildCount();
                        totalItemCount = lm.getItemCount();
                        pastVisiblesItems = lm.findFirstVisibleItemPosition();
                        if (visibleItemCount + pastVisiblesItems == totalItemCount) {
                            loading = true;
                            binding.progressLoading.setVisibility(View.VISIBLE);
                            Log.e(TAG, "onScrolled: loading " + loading);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    postList.remove(postList.size() - 1);
                                    paginationAdapter.notifyItemRemoved(postList.size());
                                    index = postList.size();
                                    end = index + 10;
                                    loadNextPage();
                                    Log.d("index ", index + "");
                                    Log.d("end ", end + "");
                                    paginationAdapter.notifyDataSetChanged();
                                    binding.progressLoading.setVisibility(View.GONE);
                                }
                            }, 3000);
                        }
                        loading = false;
                    }
                }
            }
        });
    }
    private void viewPosts() {
        mViewModel.fetchList();
        mViewModel.getFromRoom(view);
        mViewModel.getPosts().observe(this, new Observer<List<PostsGet>>() {
            @Override
            public void onChanged(List<PostsGet> posts) {
                mViewModel.setPostsInAdapter(posts);
            }
        });
        //     setupListClick();
    }
    private void setupListClick() {
        mViewModel.getSelected().observe(this, new Observer<PostsGet>() {
            @Override
            public void onChanged(PostsGet postsGet) {
                if (postsGet != null) {
                    Intent intent = new Intent(HomeActivity.this, DetailActivity.class);
                    intent.putExtra("post", postsGet);
                    startActivity(intent);
                    Log.e(TAG, "onChanged: " + postsGet);
                    Toast.makeText(getApplicationContext(), "You selected a " + postsGet.getTitle(), Toast.LENGTH_SHORT).show();
                }
            }
        }); }
    private void loadNextPage() {
        Callback<List<PostsGet>> callback = new Callback<List<PostsGet>>() {
            @Override
            public void onResponse(Call<List<PostsGet>> call, Response<List<PostsGet>> response) {
                assert response.body() != null;
                Log.e(TAG, "onResponse: " + response.body().size());
                paginationAdapter.setPosts(getPost(visibleItemCount, response.body()));
                Log.e(TAG, "onResponse: next page"+postList.size());
                DataBase.getInstance(getApplicationContext()).postDao().insertPost(postList);
            }
            @Override
            public void onFailure(Call<List<PostsGet>> call, Throwable t) {
                Log.e("failure", t.getMessage(), t); }
        };
        MainClient.getInstance().getPosts().enqueue(callback);
    }
    private void loadFirstPage() {
        binding.progressBar.setVisibility(View.VISIBLE);
        Callback<List<PostsGet>> callback = new Callback<List<PostsGet>>() {
            @Override
            public void onResponse(Call<List<PostsGet>> call, Response<List<PostsGet>> response) {
                binding.progressBar.setVisibility(View.GONE);
                assert response.body() != null;
                Log.e(TAG, "onResponse: " + response.body().size());
                paginationAdapter.setPosts(getPost(index, response.body()));
                Log.e(TAG, "onResponse: postlist " + postList.size());
                DataBase.getInstance(getApplicationContext()).postDao().insertPost(postList);
            }
            @Override
            public void onFailure(Call<List<PostsGet>> call, Throwable t) {
                Log.e("failure", t.getMessage(), t);
            }};
        MainClient.getInstance().getPosts().enqueue(callback);
    }
    public List<PostsGet> getPost(int index, List<PostsGet> response) {
        for(int i=index; i<end; i++) {
            postList.add(response.get(i));
            Log.d(TAG, postList.get(i).getTitle()+"");
        }
        return postList; }
    public void getFromRoom() {
        DataBase.getInstance(getApplicationContext()).postDao().getAll()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<List<PostsGet>>() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
                        Log.e(TAG, "onSubscribe: " + d.toString());
                    }
                    @Override
                    public void onNext(@io.reactivex.annotations.NonNull List<PostsGet> list) {
//                        postList=getPost(index,list);
                        postList.addAll(list);
                        paginationAdapter.setPosts(getPost(index, list));
                        Log.e(TAG, "getFromRoom: " + postList);
                    }
                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        Log.e(TAG, "onError: " + e.getMessage().toString());
                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "onComplete: ");
                    }
                });
    }
    public Boolean checkInternet(){
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        } else
            connected = false;
        return connected; }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                PostsGet postsGet = data.getParcelableExtra("post");
                int updateIndex = data.getIntExtra("position", -1);
                postList.set(updateIndex, postsGet);
                paginationAdapter.notifyItemChanged(updateIndex);
                Log.e(TAG, "onActivityResult: "+updateIndex );
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Log.e(TAG, "onActivityResult: "+resultCode );
            }
        }
    }
}
