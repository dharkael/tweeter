package com.example.dharkael.tweeter.ui.login;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.UiThreadTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.dharkael.tweeter.RxSchedulers;
import com.example.dharkael.tweeter.TestUtils;
import com.example.dharkael.tweeter.api.TweetService;
import com.example.dharkael.tweeter.data.UserDao;
import com.example.dharkael.tweeter.data.entities.AuthenticatedUserId;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

@RunWith(AndroidJUnit4.class)
public class LoginViewModelTest {

    @Rule
    public UiThreadTestRule uiThreadTestRule = new UiThreadTestRule();


    private TweetService tweetService;
    private UserDao userDao;
    private MutableLiveData<String> passwordData;
    private MutableLiveData<String> usernameData;
    private LoginViewModel viewModel;
    private RxSchedulers schedulers;

    @Before
    @UiThreadTest
    public void setUp() throws Exception {
        tweetService = mock(TweetService.class);
        userDao = mock(UserDao.class);
        usernameData = new MutableLiveData<>();
        passwordData = new MutableLiveData<>();
        schedulers = new RxSchedulers() {
            @Override
            public Scheduler io() {
                return Schedulers.trampoline();
            }

            @Override
            public Scheduler computation() {
                return Schedulers.trampoline();
            }

            @Override
            public Scheduler main() {
                return Schedulers.trampoline();
            }
        };
        viewModel = new LoginViewModel(tweetService, userDao, usernameData, passwordData, schedulers);
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
        viewModel = new LoginViewModel(tweetService, userDao, usernameData, passwordData, schedulers);
        verify(usernameData).setValue(null);
        viewModel.setUsername(NAME);
        verify(usernameData).setValue(NAME);
    }

    @Test
    @UiThreadTest
    @SuppressWarnings("unchecked")
    public void setPassword() throws Exception {
        final String PASS = "PASSWORD";
        passwordData = mock(MutableLiveData.class);
        viewModel = new LoginViewModel(tweetService, userDao, usernameData, passwordData, schedulers);
        verifyZeroInteractions(passwordData);
        viewModel.setPassword(PASS);
        verify(passwordData).setValue(PASS);
    }

    @Test
    @UiThreadTest
    public void setAuthenticatedUserId() throws Exception {
        AuthenticatedUserId authenticatedUserId = new AuthenticatedUserId(0, "userId", 0);
        verifyZeroInteractions(userDao);
        viewModel.setAuthenticatedUserId(authenticatedUserId);
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