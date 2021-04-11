package com.newsolutions.newsyapp.ui.updatePost;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import com.newsolutions.newsyapp.R;
import com.newsolutions.newsyapp.data.room.DataBase;
import com.newsolutions.newsyapp.data.room.post.PostsGet;
import com.newsolutions.newsyapp.databinding.ActivityDetailBinding;
import com.newsolutions.newsyapp.databinding.ActivityUpdatePostBinding;
import com.newsolutions.newsyapp.ui.detail.DetailViewModel;

import java.util.Objects;

public class UpdatePostActivity extends AppCompatActivity {
    private UpdatePostViewModel mViewModel;
    private ActivityUpdatePostBinding binding;
    View view;
    private static final String TAG = "HomeActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdatePostBinding.inflate(getLayoutInflater());
        view = binding.getRoot();
        setContentView(view);
        mViewModel = new ViewModelProvider(this).get(UpdatePostViewModel.class);
        binding.setViewModel(mViewModel);
        binding.setLifecycleOwner(this);
        binding.actionBar.tvTitle.setText(R.string.update);

        mViewModel.setData(getIntent().getParcelableExtra("post"));
        binding.btnUpdatePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataBase.getInstance(view.getContext()).postDao().updatePost(mViewModel.post.getValue());
                Intent resultIntent = getIntent();
                resultIntent.putExtra("post", mViewModel.post.getValue());
                setResult(RESULT_OK,resultIntent);
                finish();
            }
        });
//        mViewModel.getPost().observe(this, new Observer<PostsGet>() {
//            @Override
//            public void onChanged(PostsGet postsGet) {
//                Log.e(TAG, "onChanged: "+postsGet.getTitle());
//                if (TextUtils.isEmpty(Objects.requireNonNull(postsGet).getTitle())) {
//                    binding.etPostTitle.setError("Enter Post Title");
//                    binding.etPostTitle.requestFocus();
//                }else {
//                    mViewModel.update(UpdatePostActivity.this,view,postsGet);
//                }
//            }
//        });
    }
}