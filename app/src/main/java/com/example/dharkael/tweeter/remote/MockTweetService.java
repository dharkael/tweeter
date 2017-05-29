package com.example.dharkael.tweeter.remote;


import com.example.dharkael.tweeter.api.LoginBody;
import com.example.dharkael.tweeter.api.LoginResponse;
import com.example.dharkael.tweeter.api.TweetService;
import com.example.dharkael.tweeter.data.entities.Tweet;
import com.example.dharkael.tweeter.data.entities.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Nonnull;

import retrofit2.Call;
import retrofit2.mock.BehaviorDelegate;

public class MockTweetService implements TweetService {

    private final String USER_NAME;
    private final String USER_PASSWORD = "Password";
    private final ArrayList<User> followers;
    private final ArrayList<Tweet> tweets;


    private BehaviorDelegate<TweetService> behaviorDelegate;

    public MockTweetService(BehaviorDelegate<TweetService> behaviorDelegate){
        this.behaviorDelegate = behaviorDelegate;
        followers = createUsers();
        USER_NAME = followers.get(0).id;
        tweets = createTweetsForUsers(followers);

    }
    @Override
    public Call<LoginResponse> login(LoginBody body) {

        if(body == null){
            final IllegalArgumentException exception = new IllegalArgumentException("body must mot be null");
            return behaviorDelegate.returningResponse( new LoginResponse(exception)).login(null);
        }
        if(USER_NAME.equals(body.username) && USER_PASSWORD.equals(body.password)){
            return behaviorDelegate.returningResponse(new LoginResponse(USER_NAME)).login(body);
        }

        return behaviorDelegate.returningResponse(new LoginResponse(new Exception("Invalid username, password"))).login(body);
    }

    @Override
    public Call<List<User>> following(String userId)  {
        if(userId == null || !USER_NAME.equals(userId)){

           return behaviorDelegate.returningResponse(Collections.emptyList()).following(userId);
        }
        return behaviorDelegate.returningResponse(followList()).following(userId);
    }

    @Override
    public Call<List<Tweet>> timeline(String userId, long sinceId) {
        if(userId == null || !USER_NAME.equals(userId)){
            return behaviorDelegate.returningResponse(Collections.emptyList()).timeline(userId, sinceId);
        }
        ArrayList<Tweet> result = new ArrayList<>();
        for (Tweet tweet: tweets) {
            if (tweet.createdAt > sinceId){
                result.add(tweet);
            }
        }
        return  behaviorDelegate.returningResponse(result).timeline(userId, sinceId);
    }

    private List<User> followList(){
        return Collections.unmodifiableList(followers);
    }

    private static ArrayList<User> createUsers(){
        String[] ids = {"Khaavren", "Aerich", "Tazendra", "Pel"};
        ArrayList<User> results = new ArrayList<>();
        if(ids != null){
            for(int i =0 ; i<ids.length;i++){
                String id = ids[i];
                User user = new User(id.toLowerCase(), id +" NAME", i);
                results.add(user);
            }
        }
        return results;
    }

    private static ArrayList<Tweet> createTweetsForUsers(@Nonnull  List<User> users){
        AtomicInteger idGen = new AtomicInteger();
        final Random random = new Random(0);
        ArrayList<Tweet> results = new ArrayList<>();
        for (int i = 0; i < users.size(); i++) {
            final User user = users.get(i);
            for (int j = 0; j < 3; j++) {
                final int nextInt = random.nextInt(tweetsText.length);
                Tweet tweet = new Tweet("tweet"+idGen.incrementAndGet(),0, tweetsText[nextInt],user.id);
                results.add(tweet);
            }
        }
        return results;

    }

    private static String[] tweetsText = {
            "The shortened interior abides a named lad.",
            "The mouse bounces underneath the blob.",
            "The supreme revolts above the accent.",
            "The four venture distributes the backlog.",
            "How can the subsidized vocabulary withdraw?",
            "The contrived pride volunteers.",
            "The aspect absorbs the dummy detective in an audio.",
            "The wonderful writer appears after a convenience.",
            "Opposite the air slaves the worry.",
            "This life is nothing short of an awakening rekindling of heroic flow. By evolving, we exist.",
            "You and I are pilgrims of the quantum matrix. Hope requires exploration.",
            "Today, science tells us that the essence of nature is awareness. Power is a constant.",
            "Consciousness consists of sub-atomic particles of quantum energy. “Quantum” means an evolving of the authentic. Coherence requires exploration.",
            "By unveiling, we self-actualize. This life is nothing short of an awakening oasis of life-affirming inspiration.",
            "Nothing is impossible. By maturing, we heal.",
            "Knowledge requires exploration. Nothing is impossible."

    };

}
