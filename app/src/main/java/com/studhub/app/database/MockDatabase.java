package com.studhub.app.database;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class MockDatabase implements Database {
    private final Map<String, Object> db = new HashMap<>();

    @Override
    public <T> CompletableFuture<T> get(String path, Class<T> type) {
        if (path == null)
            throw new IllegalArgumentException("path cannot be null");

        if (type == null)
            throw new IllegalArgumentException("type cannot be null");

        CompletableFuture<T> future = new CompletableFuture<>();

        if (db.containsKey(path)) {
            future.complete(type.cast(db.get(path)));
        } else {
            future.completeExceptionally(new NoSuchFieldException());
        }

        return future;
    }

    @Override
    public <T> CompletableFuture<Boolean> set(String path, T data) {
        if (path == null)
            throw new IllegalArgumentException("path cannot be null");

        db.put(path, data);

        return CompletableFuture.completedFuture(true);
    }
}
