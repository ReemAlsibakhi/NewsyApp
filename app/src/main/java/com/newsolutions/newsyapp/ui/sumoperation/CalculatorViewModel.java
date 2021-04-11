package com.newsolutions.newsyapp.ui.sumoperation;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.newsolutions.newsyapp.contentprovider.OperationContentProvider;
import com.newsolutions.newsyapp.data.roomcontentprovider.SumOp;

public class CalculatorViewModel extends ViewModel {
    private static final String TAG = "CalculatorViewModel";
    public MutableLiveData<String> num1 = new MutableLiveData<>();
    public MutableLiveData<String> num2 = new MutableLiveData<>();
    public MutableLiveData<SumOp> mutableLiveData;

    public MutableLiveData<SumOp> getNum() {
        if (mutableLiveData == null) {
            mutableLiveData = new MutableLiveData<>();
        }
        return mutableLiveData;
    }

    public void onClickCalculate() {
        mutableLiveData.setValue(new SumOp(num1.getValue(), num2.getValue()));
    }

    public void calc(SumOp num, View view) {
        int res = Integer.parseInt(num.getNum1()) + Integer.parseInt(num.getNum2());
        Log.e(TAG, "calc: " + res);
        // Defines a new Uri object that receives the result of the insertion
        Uri newUri;
        // Defines an object to contain the new values to insert
        ContentValues newValues = new ContentValues();
        /*
         * Sets the values of each column and inserts the word. The arguments to the "put"
         * method are "column name" and "value"
         */
        newValues.put(SumOp.COLUMN_NUM1, num.getNum1());
        newValues.put(SumOp.COLUMN_NUM2, num.getNum2());
        newValues.put(SumOp.COLUMN_RESULT, String.valueOf(res));
        newUri = view.getContext().getContentResolver().insert(OperationContentProvider.CONTENT_URI, newValues);
        Log.e(TAG, "calc: newUri " + newUri.toString()+"content value: "+newValues);

    }

    public void getData(View v) {
        String res="";
        Cursor mCursor = v.getContext().getContentResolver().query(OperationContentProvider.CONTENT_URI, null, null, null, "_id");
        if (mCursor.moveToFirst()){
            do {
                res+=  mCursor.getString(mCursor.getColumnIndex(SumOp.COLUMN_RESULT));
               Log.e(TAG, "getData result: "+res );
            }while (mCursor.moveToNext());

        }
    }
}