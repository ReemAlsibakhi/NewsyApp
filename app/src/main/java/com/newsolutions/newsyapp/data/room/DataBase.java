package com.newsolutions.newsyapp.data.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
;import com.newsolutions.newsyapp.data.room.post.PostDao;
import com.newsolutions.newsyapp.data.room.post.PostsGet;
import com.newsolutions.newsyapp.data.room.user.User;
import com.newsolutions.newsyapp.data.room.user.UserDao;
import com.newsolutions.newsyapp.data.roomcontentprovider.SumOp;
import com.newsolutions.newsyapp.data.roomcontentprovider.SumOpDao;

@Database(entities = {User.class, PostsGet.class, SumOp.class}, version = 1)
public abstract class DataBase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract PostDao postDao();
    public abstract SumOpDao sumOpDao();

     private static DataBase instance;
    //Singleton pattern
    public static synchronized  DataBase getInstance(Context context){
        if (instance==null){
            instance = Room.databaseBuilder(context.getApplicationContext()
                    ,DataBase.class, "db")
                    .allowMainThreadQueries()
                    .build()
            ;
        }
        return instance;
    }
}
