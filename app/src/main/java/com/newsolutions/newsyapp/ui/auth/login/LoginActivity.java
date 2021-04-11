package com.newsolutions.newsyapp.ui.auth.login;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.newsolutions.newsyapp.databinding.ActivityLoginBinding;
import com.newsolutions.newsyapp.data.room.user.User;
import com.newsolutions.newsyapp.ui.home.HomeActivity;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private LoginViewModel mViewModel;
    private ActivityLoginBinding binding;
    View view;
    private static final String TAG = "LoginActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        view = binding.getRoot();
        setContentView(view);
        mViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        binding.setViewModel(mViewModel);
        binding.setLifecycleOwner(this);
        validateForm();
        observeStatus();
    }
    private void validateForm() {
        mViewModel.getUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {
                Log.e(TAG, "onChanged: "+user.getEmail()+user.getPassword() );
                if (TextUtils.isEmpty(Objects.requireNonNull(user).getEmail())) {
                    binding.etEmail.setError("Enter an E-Mail Address");
                    binding.etEmail.requestFocus();
                }
                else if (!user.isEmailValid()) {
                    binding.etEmail.setError("Enter a Valid E-mail Address");
                    binding.etEmail.requestFocus();
                }
                else if (TextUtils.isEmpty(Objects.requireNonNull(user).getPassword())) {
                    binding.etPassword.setError("Enter a Password");
                    binding.etPassword.requestFocus();
                }
                else if (!user.isPasswordLengthGreaterThan5()) {
                    binding.etPassword.setError("Enter at least 6 Digit password");
                    binding.etPassword.requestFocus();
                }
                else {
                    mViewModel.loginDB(view,user);
                } }});
    }
    private void observeStatus() {
        mViewModel.status.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean status) {
                if (status){
                    Log.e(TAG, "onChanged: true: " + status);
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    finish();
                }else {
                    Log.e(TAG, "onChanged: false: " + status);
                    Toast.makeText(getApplicationContext(),"This Email not exist or password is mistake",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}