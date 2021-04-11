package com.newsolutions.newsyapp.data.room.user;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Patterns;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_table", indices = {@Index(value = {"email"}, unique = true)})
public class User implements Parcelable {

  public static final Creator CREATOR = new Creator() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }
        public User[] newArray(int size) {
            return new User[size];
        }
    };

  @PrimaryKey(autoGenerate = true)
    int id;
    @ColumnInfo(name = "user_name")
    String userName;
    @ColumnInfo(name = "email")
    String email;
    @ColumnInfo(name = "mobile")
    String mobile;
    @ColumnInfo(name = "password")
    String password;
    public User() {
    }
    public User(String userName,String mobile,String email,String password) {
        this.userName = userName;
        this.mobile=mobile;
        this.email = email;
        this.password = password;
    }
    public User( Integer id,String userName, String email, String mobile, String password) {
        this.userName = userName;
        this.email = email;
        this.mobile = mobile;
        this.password = password;
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEmailValid() {
        return Patterns.EMAIL_ADDRESS.matcher(getEmail()).matches();
    }

    public boolean isPasswordLengthGreaterThan5() {
        return getPassword().length() > 5;
    }
    public boolean isMobileLengthGreaterThan10() {
        return getMobile().length() > 9;
    }

    // Parcelling part
    public User(Parcel in){
        this.id = in.readInt();
        this.userName = in.readString();
        this.email=  in.readString();
        this.mobile=  in.readString();
        this.password=  in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
          parcel.writeInt(this.id);
          parcel.writeString(this.userName);
          parcel.writeString(this.email);
          parcel.writeString(this.mobile);
          parcel.writeString(this.password);
    }
}
