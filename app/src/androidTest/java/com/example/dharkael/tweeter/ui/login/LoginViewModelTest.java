package com.example.dharkael.tweeter.ui.login;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.UiThreadTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.dharkael.tweeter.TestUtils;
import com.example.dharkael.tweeter.api.TweetService;
import com.example.dharkael.tweeter.data.TweetDao;
import com.example.dharkael.tweeter.data.UserDao;
import com.example.dharkael.tweeter.data.entities.AuthenticatedUserId;
import com.example.dharkael.tweeter.data.entities.Tweet;
import com.example.dharkael.tweeter.data.entities.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class LoginViewModelTest {

    @Rule
    public UiThreadTestRule uiThreadTestRule = new UiThreadTestRule();


    private TweetService tweetService;
    private UserDao userDao;
    private TweetDao tweetDao;
    private MutableLiveData<String> passwordData;
    private MutableLiveData<String> usernameData;
    private LoginViewModel viewModel;
    private ExecutorService executorService;

    @Before
    @UiThreadTest
    public void setUp() throws Exception {
        tweetService = mock(TweetService.class);
        userDao = mock(UserDao.class);
        tweetDao = mock(TweetDao.class);
        usernameData = new MutableLiveData<>();
        passwordData = new MutableLiveData<>();
        executorService = Executors.newSingleThreadExecutor();
        viewModel = new LoginViewModel(tweetService, userDao, tweetDao, usernameData, passwordData, executorService);
    }

    @After
    @UiThreadTest
    public void tearDown() throws Exception {
    }

    @Test
    @UiThreadTest
    public void isValidLoginData() throws Exception {
        final LiveData<Boolean> loginData = viewModel.isValidLoginData();
        Boolean actual = getValue(loginData);
        assertThat(actual, equalTo(false));

        viewModel.setPassword("PASSWORD");
        actual = getValue(loginData);
        assertThat(actual, equalTo(false));

        viewModel.setUsername("USERNAME");
        actual = getValue(loginData);
        assertThat(actual, equalTo(true));

    }

    @Test
    @UiThreadTest
    @SuppressWarnings("unchecked")
    public void setUsername() throws Exception {
        final String NAME = "USERNAME";
        usernameData = mock(MutableLiveData.class);
        viewModel = new LoginViewModel(tweetService, userDao,tweetDao, usernameData, passwordData, executorService);
        verify(usernameData).setValue(null);
        viewModel.setUsername(NAME);
        verify(usernameData).setValue(NAME.toLowerCase());
    }

    @Test
    @UiThreadTest
    @SuppressWarnings("unchecked")
    public void setPassword() throws Exception {
        final String PASS = "PASSWORD";
        passwordData = mock(MutableLiveData.class);
        viewModel = new LoginViewModel(tweetService, userDao, tweetDao, usernameData, passwordData, executorService);
        verifyZeroInteractions(passwordData);
        viewModel.setPassword(PASS);
        verify(passwordData).setValue(PASS);
    }

    @Test
    @UiThreadTest
    public void setAuthenticatedUserId() throws Exception {
        AuthenticatedUserId authenticatedUserId = new AuthenticatedUserId(0, "userId", 0);
        verifyZeroInteractions(userDao);
        final Call<List<User>> usersCall = new Call<List<User>>() {
            @Override
            public Response<List<User>> execute() throws IOException {
                return null;
            }

            @Override
            public void enqueue(Callback<List<User>> callback) {

            }

            @Override
            public boolean isExecuted() {
                return false;
            }

            @Override
            public void cancel() {

            }

            @Override
            public boolean isCanceled() {
                return false;
            }

            @Override
            public Call<List<User>> clone() {
                return null;
            }

            @Override
            public Request request() {
                return null;
            }
        };

        final Call<List<Tweet>> tweetsCall = new Call<List<Tweet>>() {
            @Override
            public Response<List<Tweet>> execute() throws IOException {
                return null;
            }

            @Override
            public void enqueue(Callback<List<Tweet>> callback) {

            }

            @Override
            public boolean isExecuted() {
                return false;
            }

            @Override
            public void cancel() {

            }

            @Override
            public boolean isCanceled() {
                return false;
            }

            @Override
            public Call<List<Tweet>> clone() {
                return null;
            }

            @Override
            public Request request() {
                return null;
            }
        };

        when(tweetService.following(authenticatedUserId.userId)).thenReturn(usersCall);
        when(tweetService.timeline(authenticatedUserId.userId,0)).thenReturn(tweetsCall);
        viewModel.setAuthenticatedUserId(authenticatedUserId);
        CountDownLatch countDownLatch = new CountDownLatch(1);
        executorService.submit(countDownLatch::countDown);
        countDownLatch.await(2, TimeUnit.SECONDS);
        verify(userDao).upsertAuthenticatedUserId(authenticatedUserId);
    }

//    @Test
//    @UiThreadTest
//    public void login() throws Exception {
//    }


    private <T> T getValue(LiveData<T> liveData) throws InterruptedException {
        return TestUtils.getValue(liveData, uiThreadTestRule);
    }
}