package com.newsolutions.newsyapp.data.roomcontentprovider;

import android.database.Cursor;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface SumOpDao {
    // Insert a operation data into the table, @return row ID for newly inserted data
    @Insert
    long insert(SumOp numbers);
    @Query("SELECT * FROM SumOp ")

    //@return A {@link Cursor} of all rows in the table
    Cursor findAll();

     // @return A number of rows deleted
    @Query("DELETE FROM SumOp WHERE _id = :id ")
    int delete(long id);

    // @return A number of rows updated
    @Update
    int update(SumOp numbers);

}
