package com.studhub.app.database;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class FireDatabase implements Database {
    private final DatabaseReference db = FirebaseDatabase.getInstance().getReference();

    @Override
    public <T> CompletableFuture<T> get(String path, Class<T> type) {
        if (path == null)
            throw new IllegalArgumentException("path cannot be null");

        if (type == null)
            throw new IllegalArgumentException("type cannot be null");



        CompletableFuture<T> future = new CompletableFuture<>();

        db.child(path).get()
                .addOnSuccessListener(snapshot -> {
                    if (snapshot.getValue() == null) {
                        future.completeExceptionally(new NoSuchFieldException());
                    } else {
                        future.complete(snapshot.getValue(type));
                    }})
                .addOnFailureListener(future::completeExceptionally);

        return future;
    }

    @Override
    public <T> CompletableFuture<Boolean> set(String path, T data) {
        if (path == null)
            throw new IllegalArgumentException("path cannot be null");

        CompletableFuture<Boolean> future = new CompletableFuture<>();

        db.child(path).setValue(data)
                .addOnSuccessListener(x -> future.complete(true))
                .addOnFailureListener(future::completeExceptionally);

        return future;
    }
}
