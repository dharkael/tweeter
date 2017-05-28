package com.example.dharkael.tweeter.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.UiThreadTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.dharkael.tweeter.TestUtils;
import com.example.dharkael.tweeter.data.entities.Tweet;
import com.example.dharkael.tweeter.data.entities.User;
import com.example.dharkael.tweeter.data.pojos.TweetAndSender;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static com.example.dharkael.tweeter.TestUtils.*;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;


@RunWith(AndroidJUnit4.class)
public class TweetDaoTest {
    @Rule
    public UiThreadTestRule uiThreadTestRule = new UiThreadTestRule();

    private TweetDao tweetDao;

    private User user;

    private AppDatabase appDatabase;

    @Before
    public void setUp() throws Exception {
        Context context = InstrumentationRegistry.getContext();
        appDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        tweetDao = appDatabase.tweetDao();
        user = createUser();
        appDatabase.userDao().insertUser(user);
    }

    @After
    public void tearDown() throws Exception {
        appDatabase.close();
    }

    @Test
    public void insertAndGetTweets() throws Exception {
        Tweet tweet = creatTweet();
        Tweet tweet1 = creatTweet1();
        List<Tweet> expected = new ArrayList<>();
        expected.add(tweet);
        expected.add(tweet1);
        tweetDao.insertTweet(tweet);
        tweetDao.insertTweet(tweet1);
        final LiveData<List<Tweet>> liveData = tweetDao.getTweets(user.id);
        final List<Tweet> actual = getValue(liveData);
        assertThat(actual, equalTo(expected));
    }

    @Test
    public void getTweetAndSenders() throws Exception {
        Tweet tweet = creatTweet();
        Tweet tweet1 = creatTweet1();
        TweetAndSender tweetAndSender = new TweetAndSender(tweet,user);
        TweetAndSender tweetAndSender1 = new TweetAndSender(tweet1,user);
        List<TweetAndSender> expected = new ArrayList<>();
        expected.add(tweetAndSender);
        expected.add(tweetAndSender1);

        tweetDao.insertTweet(tweet);
        tweetDao.insertTweet(tweet1);
        final LiveData<List<TweetAndSender>> liveData = tweetDao.getTweetAndSenders(user.id);
        final List<TweetAndSender> actual = getValue(liveData);
        assertThat(actual, equalTo(expected));
    }

    private <T> T getValue(LiveData<T> liveData) throws InterruptedException {
        return TestUtils.getValue(liveData, uiThreadTestRule);
    }

}