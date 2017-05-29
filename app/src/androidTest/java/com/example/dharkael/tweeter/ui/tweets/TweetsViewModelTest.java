package com.example.dharkael.tweeter.ui.tweets;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.UiThreadTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.dharkael.tweeter.TestUtils;
import com.example.dharkael.tweeter.data.TweetDao;
import com.example.dharkael.tweeter.data.UserDao;
import com.example.dharkael.tweeter.data.entities.Tweet;
import com.example.dharkael.tweeter.data.entities.User;
import com.example.dharkael.tweeter.data.pojos.TweetAndSender;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class TweetsViewModelTest {
    @Rule
    public UiThreadTestRule uiThreadTestRule = new UiThreadTestRule();
    private TweetDao tweetDao;
    private UserDao userDao;
    private TweetsViewModel viewModel;
    private ExecutorService executorService;

    @Before
    public void setUp() throws Exception {
        tweetDao = mock(TweetDao.class);
        userDao = mock(UserDao.class);
       executorService = Executors.newSingleThreadExecutor();
        viewModel = new TweetsViewModel(tweetDao, userDao, executorService);
    }

    @Test
    public void setUserIdThenSetSameUserId() throws Exception {
        String userId = "USER_ID";
        viewModel.setUserId(userId);
        verify(userDao).loadUser(userId);
        verify(tweetDao).getTweetAndSenders();
        viewModel.setUserId(userId);
        verifyNoMoreInteractions(userDao);
        verifyNoMoreInteractions(tweetDao);
    }

    @Test
    public void setUserIdThenSetDifferentUserId() throws Exception {
        String userId = "USER_ID";
        String userId1 = userId + "1";
        viewModel.setUserId(userId);
        verify(userDao).loadUser(userId);
        verify(tweetDao).getTweetAndSenders();
        viewModel.setUserId(userId1);
        verify(userDao).loadUser(userId1);
        verify(tweetDao, times(2)).getTweetAndSenders();
    }

    @Test
    public void setUserIdNull() throws Exception {
        viewModel.setUserId(null);
        verify(userDao).loadUser(null);
        verify(tweetDao).getTweetAndSenders();
    }

    @Test
    @UiThreadTest
    public void getUser() throws Exception {
        final User user = TestUtils.createUser();
        MutableLiveData<User> userData = new MutableLiveData<>();
        userData.setValue(user);
        when(userDao.loadUser(user.id)).thenReturn(userData);

        viewModel.setUserId(user.id);
        final LiveData<User> liveData = viewModel.getUser();
        final User actual = getValue(liveData);
        Assert.assertThat(actual, equalTo(user));

    }

    @Test
    @UiThreadTest
    public void getTweetAndSenders() throws Exception {
        final User user = TestUtils.createUser();
        final Tweet tweet = TestUtils.creatTweet();
        final Tweet tweet1 = TestUtils.creatTweet1();
        final TweetAndSender tweetAndSender = new TweetAndSender(tweet, user);
        final TweetAndSender tweetAndSender1 = new TweetAndSender(tweet1, user);
        final ArrayList<TweetAndSender> tweetAndSenders = new ArrayList<>();
        tweetAndSenders.add(tweetAndSender);
        tweetAndSenders.add(tweetAndSender1);
        MutableLiveData<List<TweetAndSender>> tweetAndSendersData = new MutableLiveData<>();
        tweetAndSendersData.setValue(tweetAndSenders);

        when(tweetDao.getTweetAndSenders()).thenReturn(tweetAndSendersData);
        viewModel.setUserId(user.id);
        final LiveData<List<TweetAndSender>> liveData = viewModel.getTweetAndSenders();
        final List<TweetAndSender> actual = getValue(liveData);
        Assert.assertThat(actual, equalTo(tweetAndSenders));

    }

    private <T> T getValue(LiveData<T> liveData) throws InterruptedException {
        return TestUtils.getValue(liveData, uiThreadTestRule);
    }

}