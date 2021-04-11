package com.newsolutions.newsyapp.ui.auth.login;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.newsolutions.newsyapp.data.room.DataBase;
import com.newsolutions.newsyapp.data.room.user.User;
import com.newsolutions.newsyapp.ui.auth.signup.RegisterActivity;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LoginViewModel extends ViewModel {
    public MutableLiveData<String> email = new MutableLiveData<>();
    public MutableLiveData<String> password = new MutableLiveData<>();
    public MutableLiveData<User> userMutableLiveData;
    public MutableLiveData<Boolean> status = new MutableLiveData<>();
    public MutableLiveData<String> msg = new MutableLiveData<>();
    private static final String TAG = "LoginViewModel";

    public MutableLiveData<User> getUser() {
        if (userMutableLiveData == null) {
            userMutableLiveData = new MutableLiveData<>();
        }
        return userMutableLiveData;
    }

    public void onClickLogin(View v) {
        User user = new User(email.getValue(), password.getValue());
        userMutableLiveData.setValue(user);
    }

    public void onClickRegister(View view) {
        view.getContext().startActivity(new Intent(view.getContext(), RegisterActivity.class));
    }

    public void loginDB(View view, User user) {
        DataBase.getInstance(view.getContext()).userDao().count(user.getEmail())
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.e(TAG, "onSubscribe: " + d.toString());
                    }

                    @Override
                    public void onNext(@NonNull Integer integer) {
                        if (integer <= 0) {
                            status.setValue(false);
                            msg.setValue("Email Not Found");
                            Log.e(TAG, "onNext: email not found ");
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        status.setValue(false);
                        Log.e(TAG, "onError: " + e.getMessage().toString());

                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "onComplete: ");
                    }
                });
        DataBase.getInstance(view.getContext()).userDao().getPassByEmail(email.getValue())
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.e(TAG, "onSubscribe: "+d.toString());
                    }

                    @Override
                    public void onNext(@NonNull String s) {
                        if (password.getValue().equals(s)) {
                            status.setValue(true);
                            Log.e(TAG, "onNext: Passwords are match");
                        } else {
                            msg.setValue("Password is mistake");
                            status.setValue(false);
                            Log.e(TAG, "onNext: Passwords not match ");
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        status.setValue(false);
                        Log.e(TAG, "onError: " + e.getMessage().toString());
                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "onComplete: ");
                    }
                });
    }

}
