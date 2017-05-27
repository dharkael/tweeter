package com.example.dharkael.tweeter.ui.login;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.example.dharkael.tweeter.api.LoginBody;
import com.example.dharkael.tweeter.api.LoginResponse;
import com.example.dharkael.tweeter.api.TweetService;
import com.example.dharkael.tweeter.data.UserDao;
import com.example.dharkael.tweeter.data.entities.AuthenticatedUserId;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.dharkael.tweeter.Transformations.combine;
import static com.example.dharkael.tweeter.ui.login.LoginViewModel.Status.ERROR;
import static com.example.dharkael.tweeter.ui.login.LoginViewModel.Status.LOADING;
import static com.example.dharkael.tweeter.ui.login.LoginViewModel.Status.SUCCESS;

public class LoginViewModel extends ViewModel {
    private final TweetService tweetService;
    private final UserDao userDao;
    private final LiveData<Boolean> validLoginData;
    private MutableLiveData<String> passwordData = new MutableLiveData<>();
    private MutableLiveData<String> usernameData = new MutableLiveData<>();

    @Inject
    public LoginViewModel(TweetService tweetService, UserDao userDao) {
        this.tweetService = tweetService;
        this.userDao = userDao;
        validLoginData = combine(usernameData,
                passwordData, this::validate);
        usernameData.setValue(null);
    }

    private boolean validate(String username, String password) {
        return !(TextUtils.isEmpty(username) || TextUtils.isEmpty(password));
    }

    LiveData<Boolean> isValidLoginData() {
        return validLoginData;
    }

    void setUsername(CharSequence username) {
        String data = username == null ? null : username.toString();
        usernameData.setValue(data);
    }

    void setPassword(CharSequence password) {
        String data = password == null ? null : password.toString();
        passwordData.setValue(data);
    }

    void setAuthenticatedUserId(AuthenticatedUserId authenticatedUserId) {
        Observable.just(authenticatedUserId)
                .singleElement()
                .observeOn(Schedulers.io())
                .subscribe(userDao::upsertAuthenticatedUserId);
    }

    LiveData<Resource<LoginResponse>> login() {
        final String username = usernameData.getValue();
        final String password = passwordData.getValue();
        final LoginBody loginBody = new LoginBody(username, password);

        //TODO make network call to validate user and return response
        //TODO if we get a failure display to user
        //Would stuff this

        MutableLiveData<Resource<LoginResponse>> result = new MutableLiveData<>();
        result.setValue(Resource.loading());
        final Call<LoginResponse> call = tweetService.login(loginBody);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    result.setValue(Resource.success(response.body()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                result.setValue(Resource.error(t.getLocalizedMessage()));
            }
        });

        return result;
    }


    public enum Status {
        ERROR, LOADING, SUCCESS
    }

    /*
     This class taken (and modified) from https://developer.android.com/topic/libraries/architecture/guide.html#addendum
     */
    static public class Resource<T> {
        @NonNull
        public final Status status;
        @Nullable
        public final T data;
        @Nullable
        public final String message;

        private Resource(@NonNull Status status, @Nullable T data, @Nullable String message) {
            this.status = status;
            this.data = data;
            this.message = message;
        }

        public static <T> Resource<T> success(@NonNull T data) {
            return new Resource<>(SUCCESS, data, null);
        }

        public static <T> Resource<T> error(String msg) {
            return new Resource<>(ERROR, null, msg);
        }

        public static <T> Resource<T> loading() {
            return new Resource<>(LOADING, null, null);
        }
    }
}
