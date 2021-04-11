package com.newsolutions.newsyapp.ui.splach;
import android.content.Intent;
import android.view.View;
import androidx.lifecycle.ViewModel;
import com.newsolutions.newsyapp.ui.auth.login.LoginActivity;
import com.newsolutions.newsyapp.ui.auth.signup.RegisterActivity;
import com.newsolutions.newsyapp.ui.home.HomeActivity;
import com.newsolutions.newsyapp.ui.main.MainActivity;

public class SplachViewModel extends ViewModel {
    public void onClickRegister(View view) {
        view.getContext().startActivity(new Intent(view.getContext(), RegisterActivity.class));
    }
    public void onClickLogin(View view) {
        view.getContext().startActivity(new Intent(view.getContext(), LoginActivity.class));
    }
    public void onClickHome(View view) {
        view.getContext().startActivity(new Intent(view.getContext(), MainActivity.class));
    }
}
