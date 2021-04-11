package com.newsolutions.newsyapp.ui.detail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import android.view.View;
import com.newsolutions.newsyapp.R;
import com.newsolutions.newsyapp.databinding.ActivityDetailBinding;

public class DetailActivity extends AppCompatActivity {
    private DetailViewModel mViewModel;
    private ActivityDetailBinding binding;
    View view;
    private static final String TAG = "HomeActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        view = binding.getRoot();
        setContentView(view);
        mViewModel = new ViewModelProvider(this).get(DetailViewModel.class);
        binding.setViewModel(mViewModel);
        binding.setLifecycleOwner(this);
        binding.actionBar.tvTitle.setText("DETAIL");
        mViewModel.setData(getIntent().getParcelableExtra("post"));

    }
}