package com.example.dharkael.tweeter.ui.tweets;

import android.app.Dialog;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.LifecycleRegistryOwner;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;

import com.example.dharkael.tweeter.R;
import com.example.dharkael.tweeter.TweeterApp;
import com.example.dharkael.tweeter.databinding.ActivityTweetsBinding;
import com.example.dharkael.tweeter.databinding.DialogAddTweetBinding;
import com.example.dharkael.tweeter.ui.TextChangedWatcher;

import javax.inject.Inject;

public class TweetsActivity extends AppCompatActivity implements LifecycleRegistryOwner {

    static final String KEY_USER_ID = "TweetsActivity.KEY_USER_ID";
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    ActivityTweetsBinding activityBinding;
    RecyclerView.LayoutManager layoutManager;
    TweetsAdapter tweetsAdapter;
    TweetsViewModel viewModel;


    public static Intent intentForStart(Context context, String userId) {
        Intent intent = new Intent(context, TweetsActivity.class);
        intent.putExtra(KEY_USER_ID, userId);
        return intent;
    }

    LifecycleRegistry lifecycleRegistry = new LifecycleRegistry(this);

    @Override
    public LifecycleRegistry getLifecycle() {
        return lifecycleRegistry;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ((TweeterApp) getApplication()).appComponent().inject(this);
        super.onCreate(savedInstanceState);
        activityBinding = DataBindingUtil.setContentView(this, R.layout.activity_tweets);

        setSupportActionBar(activityBinding.toolbarTweets);
        final String userId = getIntent().getStringExtra(KEY_USER_ID);
        final RecyclerView recyclerView = activityBinding.recyclerTweetsList;
        tweetsAdapter = new TweetsAdapter();
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(tweetsAdapter);
        recyclerView.setLayoutManager(layoutManager);
        bindViewModel(userId);
    }


    private void bindViewModel(String userId) {
        viewModel =
                ViewModelProviders.of(this, viewModelFactory)
                        .get(TweetsViewModel.class);
        viewModel.setUserId(userId);
        viewModel.getTweetAndSenders().observe(this, tweetsAdapter::setTweetAndSenders);

        viewModel.getUser().observe(this, user -> {
            activityBinding.setUser(user);
            activityBinding.executePendingBindings();
        });

        viewModel.getAuthenticatedUserId().observe(this, authenticatedUserId -> {
            if(authenticatedUserId == null){
                Log.i("TweetsActivity", "authenticatedUserId == null");
                finish();
                finish();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tweets, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.item_menu_tweets_add_tweet:
                  showAddTweetDialog();
                break;
            case R.id.item_menu_tweets_sign_out:
                viewModel.signOut();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    private void showAddTweetDialog(){
        final Dialog dialog = new Dialog(this);
        final DialogAddTweetBinding dialogBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_add_tweet, null, false);
        dialog.setContentView(dialogBinding.getRoot());
        final Window dialogWindow = dialog.getWindow();
        if(dialogWindow != null){
            dialogWindow.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        }
        dialog.setTitle(R.string.add_tweet);
        dialogBinding.editAddTweetText.addTextChangedListener(new TextChangedWatcher(charSequence ->{
          dialogBinding.btnAddTweetSubmit.setEnabled( !TextUtils.isEmpty(charSequence));
        }));
        dialogBinding.btnAddTweetCancel.setOnClickListener(v -> dialog.dismiss());
        dialogBinding.btnAddTweetSubmit.setOnClickListener(v ->{
            final String tweetText = dialogBinding.editAddTweetText.getText().toString();
            if(TextUtils.isEmpty(tweetText)){
                return;
            }
            viewModel.addTweet(tweetText);
            dialog.dismiss();
        });
        dialog.show();
    }

}
