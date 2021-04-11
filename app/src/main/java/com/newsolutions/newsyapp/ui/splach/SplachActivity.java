package com.newsolutions.newsyapp.ui.splach;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import com.newsolutions.newsyapp.databinding.ActivitySplachBinding;

public class SplachActivity extends AppCompatActivity {
    private ActivitySplachBinding binding;
    private SplachViewModel mViewModel;
    private View view;
    private static final String TAG = "SplachActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = ActivitySplachBinding.inflate(getLayoutInflater());
        view = binding.getRoot();
        setContentView(view);
        mViewModel = new ViewModelProvider(this).get(SplachViewModel.class);
        binding.setViewModel(mViewModel);
        binding.setLifecycleOwner(this);
    }


    //   @Override
//    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.btn_login: {
//                startActivity(new Intent(this, LoginActivity.class));
//                break;
//            }
//            case R.id.btn_register:{
//                startActivity(new Intent(this, RegisterActivity.class));
//                break;
//            }
//            case R.id.tvSkip:{
//                startActivity(new Intent(this, HomeActivity.class));
//                break;
//            }
//
//        }
//    }
}



