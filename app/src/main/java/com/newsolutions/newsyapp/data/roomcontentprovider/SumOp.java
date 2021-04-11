package com.newsolutions.newsyapp.data.roomcontentprovider;

import android.content.ContentValues;
import android.provider.BaseColumns;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "sumOp")
public class SumOp {
    public static final String COLUMN_ID = BaseColumns._ID;
    public static final String COLUMN_NUM1 = "num1";
    public static final String COLUMN_NUM2 = "num2";
    public static final String COLUMN_RESULT = "result";
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true, name = COLUMN_ID)
    private int id;
    @ColumnInfo(name = COLUMN_NUM1)
    private String num1;
    @ColumnInfo(name = COLUMN_NUM2)
    private String num2;
    @ColumnInfo(name = COLUMN_RESULT)
    private String result;

    public SumOp() {
    }

    public SumOp(String num1, String num2) {
        this.num1 = num1;
        this.num2 = num2;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNum1() {
        return num1;
    }

    public void setNum1(String num1) {
        this.num1 = num1;
    }

    public String getNum2() {
        return num2;
    }

    public void setNum2(String num2) {
        this.num2 = num2;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public static SumOp fromContentValues
            (ContentValues contentValues) {
        SumOp sumOp = new SumOp();
        if (contentValues.containsKey(COLUMN_ID)) {
            sumOp.setId(contentValues.getAsInteger(COLUMN_ID));
        }
        if (contentValues.containsKey(COLUMN_NUM1)) {
            sumOp.setNum1(contentValues.getAsString(COLUMN_NUM1));
        }
        if (contentValues.containsKey(COLUMN_NUM2)) {
            sumOp.setNum2(contentValues.getAsString(COLUMN_NUM2));
        }
        if (contentValues.containsKey(COLUMN_RESULT)) {
            sumOp.setResult(contentValues.getAsString(COLUMN_RESULT));
        }
        return sumOp;
    }
}
