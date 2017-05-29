package com.example.dharkael.tweeter.ui;

import android.text.Editable;
import android.util.Log;

import io.reactivex.functions.Consumer;

public class TextChangedWatcher implements android.text.TextWatcher {
    private Consumer<CharSequence> consumer;

    public TextChangedWatcher(Consumer<CharSequence> consumer) {
        this.consumer = consumer;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        try {
            consumer.accept(s);
        } catch (Exception e) {
            Log.e("TextChangedWatcher", e.getMessage(), e);
            e.printStackTrace();
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
    }
}
