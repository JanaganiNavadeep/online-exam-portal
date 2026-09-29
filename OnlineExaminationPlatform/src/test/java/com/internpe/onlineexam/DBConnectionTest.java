package com.internpe.onlineexam;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class DBConnectionTest {

    @Test
    void keepsSQLiteJdbcUrl() {
        String url = "jdbc:sqlite:online_exam.db";
        assertEquals("jdbc:sqlite:online_exam.db", DBConnection.getBaseUrl(url));
    }

    @Test
    void detectsMissingDatabaseExceptions() {
        SQLException exception = new SQLException("unable to open database file");
        assertTrue(DBConnection.isDatabaseMissing(exception));
    }

    @Test
    void doesNotTreatLockedDatabaseAsMissing() {
        SQLException exception = new SQLException("[SQLITE_BUSY] database is locked");
        assertTrue(!DBConnection.isDatabaseMissing(exception));
    }
}
