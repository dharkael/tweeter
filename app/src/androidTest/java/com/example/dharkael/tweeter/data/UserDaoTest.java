package com.example.dharkael.tweeter.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.UiThreadTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.dharkael.tweeter.TestUtils;
import com.example.dharkael.tweeter.data.entities.AuthenticatedUserId;
import com.example.dharkael.tweeter.data.entities.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;


@RunWith(AndroidJUnit4.class)
public class UserDaoTest {
    @Rule
    public UiThreadTestRule uiThreadTestRule = new UiThreadTestRule();

    private UserDao userDao;

    private AppDatabase appDatabase;

    @Before
    public void setUp() throws Exception {
        Context context = InstrumentationRegistry.getContext();
        appDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        userDao = appDatabase.userDao();
    }

    @After
    public void tearDown() throws Exception {
        appDatabase.close();
    }

    @Test
    public void insertUserAndLoad() throws Exception {
        final String ID = "id";
        final User user = new User(ID, "name", 0);
        userDao.insertUser(user);

        final LiveData<User> userLiveData = userDao.loadUser(ID);
        final User actual = getValue(userLiveData);
        assertThat(actual, equalTo(user));

    }

    @Test
    public void updateUser() throws Exception {

        final String ID = "id";
        final User user = new User(ID, "name", 0);
        userDao.insertUser(user);
        LiveData<User> userLiveData = userDao.loadUser(ID);
        User actual = getValue(userLiveData);
        assertThat(actual, equalTo(user));

        final User updatedUser = new User(ID, "name", 1);
        userDao.updateUser(updatedUser);
        userLiveData = userDao.loadUser(ID);
        actual = getValue(userLiveData);
        assertThat(actual, equalTo(updatedUser));

    }

    <T> T getValue(LiveData<T> liveData) throws InterruptedException {
        return TestUtils.getValue(liveData, uiThreadTestRule);
    }

    @Test
    public void deleteUser() throws Exception {
        final String ID = "id";
        final User user = new User(ID, "name", 0);
        userDao.insertUser(user);

        LiveData<User> userLiveData = userDao.loadUser(ID);
        User actual = getValue(userLiveData);
        assertThat(actual, equalTo(user));

        userDao.deleteUser(user);
        userLiveData = userDao.loadUser(ID);
        actual = getValue(userLiveData);
        assertNull(actual);

    }

    @Test
    public void loadUsers() throws Exception {
        final String ID = "id";
        final String ID1 = "id1";
        final User user = new User(ID, "name", 0);
        final User user1 = new User(ID1, "name", 0);
        userDao.insertUser(user);
        userDao.insertUser(user1);

        LiveData<User> userLiveData = userDao.loadUser(ID);
        User actual = getValue(userLiveData);
        assertThat(actual, equalTo(user));

        userLiveData = userDao.loadUser(ID1);
        actual = getValue(userLiveData);
        assertThat(actual, equalTo(user1));

    }

    @Test
    public void upsertAuthenticatedUserId() throws Exception {
        final int ID = 0;
        AuthenticatedUserId userId = new AuthenticatedUserId(ID,"userId",0);
        userDao.upsertAuthenticatedUserId(userId);
        LiveData<AuthenticatedUserId> liveData = userDao.getAuthenticatedUserId(ID);
        AuthenticatedUserId actual = getValue(liveData);
        assertThat(actual, equalTo(userId));

        final int ID1 = 1;
        AuthenticatedUserId userId1 = new AuthenticatedUserId(ID1, "userId1", 0);
        userDao.upsertAuthenticatedUserId(userId1);
        liveData = userDao.getAuthenticatedUserId(ID1);
        actual = getValue(liveData);
        assertThat(actual, equalTo(userId1));

    }

    @Test
    public void deleteAuthenticatedUserId() throws Exception {
        final int ID = 0;
        AuthenticatedUserId userId = new AuthenticatedUserId(ID,"userId",0);
        userDao.upsertAuthenticatedUserId(userId);
        LiveData<AuthenticatedUserId> liveData = userDao.getAuthenticatedUserId(ID);
        AuthenticatedUserId actual = getValue(liveData);
        assertThat(actual, equalTo(userId));

        userDao.deleteAuthenticatedUserId(userId);
        liveData = userDao.getAuthenticatedUserId(ID);
        actual = getValue(liveData);
        assertNull(actual);

    }

    @Test
    public void getAuthenticatedUserId() throws Exception {
        final int ID = 0;
        AuthenticatedUserId userId = new AuthenticatedUserId(ID,"userId",0);
        userDao.upsertAuthenticatedUserId(userId);
        final LiveData<AuthenticatedUserId> liveData = userDao.getAuthenticatedUserId(ID);
        final AuthenticatedUserId actual = getValue(liveData);
        assertThat(actual, equalTo(userId));
    }

    @Test
    public void getAuthenticatedUserId1() throws Exception {
        final int ID = 0;
        AuthenticatedUserId userId = new AuthenticatedUserId(ID,"userId",0);
        userDao.upsertAuthenticatedUserId(userId);
        final LiveData<AuthenticatedUserId> liveData = userDao.getAuthenticatedUserId();
        final AuthenticatedUserId actual = getValue(liveData);
        assertThat(actual, equalTo(userId));
    }

}