package com.example.dharkael.tweeter;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

public class Transformations {

    private Transformations(){}

    public static <X, Y, Z> LiveData<Z> combine(LiveData<X> source, LiveData<Y> source2, final Function<X, Y, Z> func) {
        final MediatorLiveData<Z> result = new MediatorLiveData<>();
        result.addSource(source, new Observer<X>() {
            @Override
            public void onChanged(@Nullable X x) {
                result.setValue(func.apply(x, source2.getValue()));
            }
        });
        result.addSource(source2, new Observer<Y>() {
            @Override
            public void onChanged(@Nullable Y y) {
                result.setValue(func.apply(source.getValue(), y));
            }
        });
        return result;
    }


    public interface Function<X, Y, Z> {
        Z apply(X f, Y s);
    }
}
