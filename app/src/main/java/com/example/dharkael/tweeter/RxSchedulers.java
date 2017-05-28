package com.example.dharkael.tweeter;

import io.reactivex.Scheduler;

public interface RxSchedulers {
    Scheduler io();
    Scheduler computation();
    Scheduler main();
}
