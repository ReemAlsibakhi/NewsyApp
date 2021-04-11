package com.newsolutions.newsyapp.contentprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.newsolutions.newsyapp.data.room.DataBase;
import com.newsolutions.newsyapp.data.roomcontentprovider.SumOp;
import com.newsolutions.newsyapp.data.roomcontentprovider.SumOpDao;

// will be used to expose the data of database.
public class OperationContentProvider extends ContentProvider {
    private static final String TAG = "OperationContentProvide";
    private static final String TABLE_NAME = "sumOp";
    private SumOpDao sumOpDao;
    // Authority of this content provider
    public static final String AUTHORITY = "com.newsolutions.newsyapp.contentprovider.OperationContentProvider";
    public static final String URL = "content://"+AUTHORITY+"/"+TABLE_NAME;
    public static final Uri CONTENT_URI = Uri.parse(URL);
    //The match code for some items in the table
    public static final int ID_OPERATION_DATA = 1;
    //The match code for an item in the  table
    public static final int ID_OPERTATION_DATA_ITEM = 2;
    public static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHORITY, TABLE_NAME, ID_OPERATION_DATA);
        uriMatcher.addURI(AUTHORITY, TABLE_NAME + "/*", ID_OPERTATION_DATA_ITEM);
    }

    @Override
    public boolean onCreate() {
        sumOpDao = DataBase.getInstance(getContext()).sumOpDao();
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Log.e(TAG, "query");
        Cursor cursor;
        switch (uriMatcher.match(uri)) {
            case ID_OPERATION_DATA:
                cursor = sumOpDao.findAll();
                if (getContext() != null) {
                    cursor.setNotificationUri(getContext().getContentResolver(), uri);
                    return cursor;
                }
            default:
                throw new IllegalArgumentException
                        ("Unknown URI: " + uri); }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        Log.e(TAG, "insert");
        switch (uriMatcher.match(uri)) {
            case ID_OPERATION_DATA:
                if (getContext() != null) {
                    long id = sumOpDao.insert(SumOp.fromContentValues(values));
                    if (id != 0) {
                        getContext().getContentResolver().notifyChange(uri, null);
                        Log.e(TAG, "insert: id"+id );
                        return ContentUris.withAppendedId(uri, id);
                    }
                }
            case ID_OPERTATION_DATA_ITEM:
                throw new IllegalArgumentException
                        ("Invalid URI: Insert failed" + uri);
            default:
                throw new IllegalArgumentException
                        ("Unknown URI: " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        Log.e(TAG, "delete");
        switch (uriMatcher.match(uri)) {
            case ID_OPERATION_DATA:
                throw new IllegalArgumentException
                        ("Invalid uri: cannot delete");
            case ID_OPERTATION_DATA_ITEM:
                if (getContext() != null) {
                    int count = sumOpDao.delete(ContentUris.parseId(uri));
                    getContext().getContentResolver().notifyChange(uri, null);
                    return count;
                }
            default:
                throw new IllegalArgumentException
                        ("Unknown URI:" + uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        Log.d(TAG, "update");
        switch (uriMatcher.match(uri)) {
            case ID_OPERATION_DATA:
                if (getContext() != null) {
                    int count = sumOpDao.update(SumOp.fromContentValues(values));
                    if (count != 0) {
                        getContext().getContentResolver().notifyChange(uri, null);
                        return count;
                    }
                }
            case ID_OPERTATION_DATA_ITEM:
                throw new IllegalArgumentException
                        ("Invalid URI:  cannot update");
            default:
                throw new IllegalArgumentException
                        ("Unknown URI: " + uri);
        }

    }

}

