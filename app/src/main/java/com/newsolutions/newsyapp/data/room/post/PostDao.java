package com.newsolutions.newsyapp.data.room.post;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;
import io.reactivex.Observable;

@Dao
public interface PostDao {
    @Query("SELECT * FROM POSTS_TABLE")
    Observable<List<PostsGet>> getAll();
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPost(List<PostsGet> post);
    @Insert
    void insertAll(PostsGet... post);
    @Delete
    void deletePost(PostsGet post);
    @Query("DELETE  FROM posts_table")
    void deleteAllPosts();
    @Update
    void updatePost(PostsGet... post);
    @Query("UPDATE posts_table SET title = :postTitle WHERE id = :id")
    void updatePostById(Integer id, String postTitle);
    @Query("SELECT * FROM posts_table WHERE id IN (:id)")
    List<PostsGet> loadAllByIds(int[] id);

}
