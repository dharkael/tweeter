package com.example.dharkael.tweeter.ui.tweets;

import android.arch.lifecycle.LifecycleActivity;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.dharkael.tweeter.R;
import com.example.dharkael.tweeter.TweeterApp;
import com.example.dharkael.tweeter.databinding.ActivityTweetsBinding;

import javax.inject.Inject;

public class TweetsActivity extends LifecycleActivity {

    static final String KEY_USER_ID = "TweetsActivity.KEY_USER_ID";
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    ActivityTweetsBinding activityBinding;
    RecyclerView.LayoutManager layoutManager;
    TweetsAdapter tweetsAdapter;


    public static Intent intentForStart(Context context, String userId) {
        Intent intent = new Intent(context, TweetsActivity.class);
        intent.putExtra(KEY_USER_ID, userId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ((TweeterApp) getApplication()).appComponent().inject(this);
        super.onCreate(savedInstanceState);
        activityBinding = DataBindingUtil.setContentView(this, R.layout.activity_tweets);
        final String userId = getIntent().getStringExtra(KEY_USER_ID);
        final RecyclerView recyclerView = activityBinding.recyclerTweetsList;
        tweetsAdapter = new TweetsAdapter();
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(tweetsAdapter);
        recyclerView.setLayoutManager(layoutManager);
        bindViewModel(userId);
    }

    private void bindViewModel(String userId) {
        TweetsViewModel viewModel =
                ViewModelProviders.of(this, viewModelFactory)
                        .get(TweetsViewModel.class);
        viewModel.setUserId(userId);
        viewModel.getTweetAndSenders().observe(this, tweetsAdapter::setTweetAndSenders);

        viewModel.getUser().observe(this, user -> {
            activityBinding.setUser(user);
            activityBinding.executePendingBindings();
        });

    }
}
