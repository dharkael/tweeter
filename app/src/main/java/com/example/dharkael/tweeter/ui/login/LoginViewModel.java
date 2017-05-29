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
import com.example.dharkael.tweeter.data.TweetDao;
import com.example.dharkael.tweeter.data.UserDao;
import com.example.dharkael.tweeter.data.entities.AuthenticatedUserId;
import com.example.dharkael.tweeter.data.entities.Tweet;
import com.example.dharkael.tweeter.data.entities.User;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;

import javax.annotation.Nonnull;

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
    private final TweetDao tweetDao;
    private final LiveData<Boolean> validLoginData;
    private final MutableLiveData<String> passwordData;
    private final MutableLiveData<String> usernameData;
    private final ExecutorService executorService;

    public LoginViewModel(TweetService tweetService, UserDao userDao, TweetDao tweetDao, MutableLiveData<String> usernameData, MutableLiveData<String> passwordData, ExecutorService executorService) {
        this.tweetService = tweetService;
        this.userDao = userDao;
        this.tweetDao = tweetDao;
        this.usernameData = usernameData;
        this.passwordData = passwordData;
        validLoginData = combine(usernameData,
                passwordData, this::validate);
        usernameData.setValue(null);
         this.executorService = executorService;
    }

    private boolean validate(String username, String password) {
        return !(TextUtils.isEmpty(username) || TextUtils.isEmpty(password));
    }

    LiveData<Boolean> isValidLoginData() {
        return validLoginData;
    }

    void setUsername(CharSequence username) {
        String data = username == null ? null : username.toString().toLowerCase();
        usernameData.setValue(data);
    }

    void setPassword(CharSequence password) {
        String data = password == null ? null : password.toString();
        passwordData.setValue(data);
    }

    void setAuthenticatedUserId(AuthenticatedUserId authenticatedUserId) {
        final String userId =authenticatedUserId.userId;
        executorService.submit(()->{
            userDao.upsertAuthenticatedUserId(authenticatedUserId);
            downloadFollowersAndTimeline(userId);
        });
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

    void downloadFollowersAndTimeline(@Nonnull String userId)  {
        final Call<List<User>> followingCall = tweetService.following(userId);
        try {
            final Response<List<User>> followingResponse = followingCall.execute();
            if (followingResponse.isSuccessful()) {
                final List<User> users = followingResponse.body();
                userDao.insertUsers(users);
                final Call<List<Tweet>> timelineCall = tweetService.timeline(userId, -1);
                final Response<List<Tweet>> timelineResponse = timelineCall.execute();
                if(timelineResponse.isSuccessful()){
                    final List<Tweet> tweets = timelineResponse.body();
                    tweetDao.insertTweets(tweets);
                }
            }
        } catch (IOException e) {
           e.printStackTrace();
        }

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
