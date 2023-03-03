package com.studhub.app;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import com.studhub.app.database.Database;
import com.studhub.app.database.MockDatabase;

import org.junit.Test;

import java.util.concurrent.CompletionException;

public class MockDatabaseTest {
    @Test
    public void retrievedValueIsIdenticalToSetValue() {
        Database db = new MockDatabase();
        String key = "db-entry";
        int value = 27419;

        db.set(key, value);

        assertEquals((int) db.get(key, Integer.class).join(), value);
    }

    @Test
    public void nonExistingValueThrowsNoSuchFieldException() {
        Database db = new MockDatabase();
        String key = "db-entry";
        int value = 27419;

        db.set(key, value);
        assertThrows(CompletionException.class, () -> db.get("qwe", String.class).join());
    }

    @Test
    public void overriddenEntryIsCorrectlyOverridden() {
        Database db = new MockDatabase();
        String key = "db-entry";
        int value1 = 27419;
        int value2 = 1937102;

        db.set(key, value1);
        db.set(key, value2);

        assertEquals((int) db.get(key, Integer.class).join(), value2);
    }

    @Test
    public void nullPathInGetThrowsIllegalArgumentException() {
        Database db = new MockDatabase();
        assertThrows(IllegalArgumentException.class, () -> db.get(null, String.class));
    }

    @Test
    public void nullTypeInGetThrowsIllegalArgumentException() {
        Database db = new MockDatabase();
        assertThrows(IllegalArgumentException.class, () -> db.get("path", null));
    }

    @Test
    public void nullPathInSetThrowsIllegalArgumentException() {
        Database db = new MockDatabase();
        assertThrows(IllegalArgumentException.class, () -> db.set(null, ""));
    }
}
