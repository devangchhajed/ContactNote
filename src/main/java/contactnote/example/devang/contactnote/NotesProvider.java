package contactnote.example.devang.contactnote;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

/**
 * Created by Devang on 4/6/2015.
 */
public class NotesProvider extends ContentProvider {

    static final String PROVIDER_NAME = "contactnote.example.devang.contactnote";
    static final String URL = "content://" + PROVIDER_NAME + "/notes";
    static final Uri CONTENT_URI = Uri.parse(URL);

    static final String _ID = "_id";
    static final String reff = "refference";
    static final String notes = "notes";
    private DatabaseHandler mOpenHelper;
    public static final int NOTES = 1;
    public static final int NOTES_ID = 2;
    private static final UriMatcher sURIMatcher =
            new UriMatcher(UriMatcher.NO_MATCH);



    @Override
    public boolean onCreate() {
        mOpenHelper = new DatabaseHandler(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        queryBuilder.setTables(DatabaseHandler.TABLE_NAME);

        int uriType = sURIMatcher.match(uri);

        switch (uriType) {
            case NOTES_ID:
                queryBuilder.appendWhere(mOpenHelper.KEY_ID + "="
                        + uri.getLastPathSegment());
                break;
            case NOTES:
                break;
            default:
                throw new IllegalArgumentException("Unknown URI");
        }

        Cursor cursor = queryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int match = sURIMatcher.match(uri);
        int rowsUpdated;
        switch(match){
            case NOTES_ID:
                rowsUpdated = db.update(mOpenHelper.TABLE_NAME,values,selection,selectionArgs);
                break;
            case NOTES:
                rowsUpdated = db.update(mOpenHelper.TABLE_NAME,values,selection,selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if(rowsUpdated!=0)
            getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;

    }
}
