package com.studhub.app.database;

import java.util.concurrent.CompletableFuture;

public interface Database {
    /**
     * Retrieves the data of type `T` stored at key `path`
     *
     * @param path the path to the db entry
     * @return a future which resolves to the database query result or an error
     * @param <T> the type of the object to return
     *
     * @throws IllegalArgumentException if path is null or type is null
     */
    <T> CompletableFuture<T> get(String path, Class<T> type);

    /**
     * Stores the given `data` at key `path`
     *
     * @param path the path to the db entry
     * @return a future which resolves to true iff the query succeeded
     *
     * @throws IllegalArgumentException if path is null
     */
    <T> CompletableFuture<Boolean> set(String path, T data);
}
