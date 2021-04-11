package com.newsolutions.newsyapp.ui.auth.signup;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.newsolutions.newsyapp.data.room.DataBase;
import com.newsolutions.newsyapp.data.room.user.User;
import com.newsolutions.newsyapp.ui.auth.login.LoginActivity;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RegisterViewModel extends ViewModel {
    public MutableLiveData<String> userName = new MutableLiveData<>();
    public MutableLiveData<String> mobile = new MutableLiveData<>();
    public MutableLiveData<String> email = new MutableLiveData<>();
    public MutableLiveData<String> password = new MutableLiveData<>();
    public MutableLiveData<Boolean> statusRegister = new MutableLiveData<>();
    MutableLiveData<User> userMutableLiveData;
    private static final String TAG = "RegisterViewModel";

    public MutableLiveData<User> getUser() {
        if (userMutableLiveData == null) {
            userMutableLiveData = new MutableLiveData<>(); }
        return userMutableLiveData; }
    public void onClickRegister(View view) {
        User user = new User(userName.getValue(), mobile.getValue(),email.getValue(), password.getValue());
        userMutableLiveData.setValue(user);
    }
    public void onClickLogin(View view) {
        view.getContext().startActivity(new Intent(view.getContext(), LoginActivity.class));
    }
    public void insertUser(View view, User user) {
//        long i = DataBase.getInstance(view.getContext()).userDao().insertUser(user);
//        Log.e(TAG, "insertUser: " + i);
//        if (i > 0) {
//            status.setValue(true);
    //}
            DataBase.getInstance(view.getContext()).userDao().insertUser(user)
                    //backgroundThread
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
                    //Observer
                    .subscribe(new CompletableObserver() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {
                            Log.e(TAG, "onSubscribe: " + d.toString());
                        }
                        @Override
                        public void onComplete() {
                            Log.e(TAG, "onComplete: ");
                            statusRegister.setValue(true);
                        }
                        @Override
                        public void onError(@NonNull Throwable e) {
                            statusRegister.setValue(false);
                            Log.e(TAG, "onError: " + e.getMessage());
                        }
                    }); }
    }
