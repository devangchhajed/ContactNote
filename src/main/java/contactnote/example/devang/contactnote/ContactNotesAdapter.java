package contactnote.example.devang.contactnote;


/**
 * Created by Devang on 3/25/2015.
 */
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import android.content.ContentUris;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;


public class ContactNotesAdapter extends CursorAdapter {

    public ContactNotesAdapter(Context context, Cursor cursor) {
        super(context, cursor);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView contact_view = (TextView) view.findViewById(R.id.contact_view);
        String uri=cursor.getString(1);
        contact_view.setText(getDisplayName(uri, context));
        ImageView photoView = (ImageView) view.findViewById(R.id.imageView);
        photoView.setImageBitmap(getPhoto(uri,context));
        TextView notesTextView = (TextView) view.findViewById(R.id.note_view);
        notesTextView.setText(cursor.getString(2));

    }

    private String getDisplayName(String u,Context context) {
        String displayName = null;
        Uri uri= Uri.parse(u);

        Cursor cur = context.getContentResolver().query(uri, null, null, null, null);
        if (cur.moveToFirst()) {
            displayName = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        }
        cur.close();

        return displayName;
    }


    private Bitmap getPhoto(String u,Context context) {
        Bitmap photo = null;
        Uri uri= Uri.parse(u);

        Cursor contactCursor = context.getContentResolver().query(uri,
                new String[] { ContactsContract.Contacts._ID }, null, null,
                null);
        String id = null;
        if (contactCursor.moveToFirst()) {
            id = contactCursor.getString(contactCursor
                    .getColumnIndex(ContactsContract.Contacts._ID));
        }
        contactCursor.close();
        try {
            InputStream input = ContactsContract.Contacts
                    .openContactPhotoInputStream(context.getContentResolver(),
                            ContentUris.withAppendedId(
                                    ContactsContract.Contacts.CONTENT_URI,
                                    new Long(id).longValue()));
            if (input != null) {
                photo = BitmapFactory.decodeStream(input);
                // Close the input stream here, otherwise it'll throw a nullpointer
                input.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return photo;
    }



    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.contact_list_item, parent, false);
        return view;
    }

}