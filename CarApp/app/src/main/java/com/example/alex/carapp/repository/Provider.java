package com.example.alex.carapp.repository;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Provider {
    private static Executor executor;
    public static Executor getExecutor() {
        if(executor == null) {
            executor = Executors.newSingleThreadExecutor();
        }
        return executor;
    }
}
