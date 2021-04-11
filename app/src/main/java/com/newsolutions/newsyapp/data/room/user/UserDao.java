package com.newsolutions.newsyapp.data.room.user;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

@Dao
public interface UserDao {
    @Insert
    Completable insertUser(User user);
    @Insert
    Completable insertAllUser(User... user);
    @Delete
    Completable deleteUser(User user);
    @Update
    Completable updateUser(User... user);
    @Query("DELETE FROM user_table")
    Completable deleteAllUser();
    @Query("UPDATE user_table SET email = :email WHERE id = :id")
    Completable updateUserById(Integer id, String email);
    @Query("select * from user_table")
    Observable<List<User>> getAllUser();
    @Query("SELECT * FROM user_table WHERE id IN (:userIds)")
    Observable<List<User>> getAllUserByIds(int[] userIds);
    @Query("SELECT COUNT(*) FROM user_table WHERE Email like :email LIMIT 1")
    Observable<Integer> count(String email);
    @Query("SELECT Password FROM user_table WHERE Email = :email LIMIT 1")
    Observable<String> getPassByEmail(String email);
}
