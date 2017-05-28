package com.example.dharkael.tweeter.ui.tweets;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.dharkael.tweeter.data.entities.Tweet;
import com.example.dharkael.tweeter.data.entities.User;
import com.example.dharkael.tweeter.data.pojos.TweetAndSender;
import com.example.dharkael.tweeter.databinding.ItemTweetsBinding;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> {

    private List<TweetAndSender> tweetAndSenders = Collections.emptyList();

    void setTweetAndSenders(List<TweetAndSender> tweetAndSenders) {
        this.tweetAndSenders = (tweetAndSenders == null) ? Collections.emptyList() : tweetAndSenders;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        final ItemTweetsBinding binding = ItemTweetsBinding.inflate(layoutInflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final TweetAndSender tweetAndSender = tweetAndSenders.get(position);
        holder.bind(tweetAndSender.tweet, tweetAndSender.sender);
    }

    @Override
    public int getItemCount() {
        return tweetAndSenders.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        static DateTimeFormatter dtf = DateTimeFormat.forStyle("LL").withLocale(Locale.US);


        ItemTweetsBinding binding;

        ViewHolder(ItemTweetsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Tweet tweet, User sender) {
            binding.setTweet(tweet);
            binding.setSender(sender);

            final String tweetedAt = dtf.print(tweet.createdAt);
            binding.setTweetedAt(tweetedAt);
            binding.executePendingBindings();

        }

    }
}
