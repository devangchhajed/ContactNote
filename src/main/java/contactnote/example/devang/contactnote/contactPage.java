package contactnote.example.devang.contactnote;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;


public class contactPage extends ActionBarActivity {

    private Uri contactUri;
    private DatabaseHandler databaseHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_page);

        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#5a1fdb"));
        ab.setBackgroundDrawable(colorDrawable);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }

        // get data via the key
        String value1 = extras.getString(Intent.EXTRA_TEXT);
        if (extras.getString("noted") != null) {
            String value2 = extras.getString("noted");
            EditText tnote= (EditText) findViewById(R.id.noteText);
            tnote.setText(value2+"");
        }
        contactUri= Uri.parse(value1);
        renderContact(contactUri);

    }

    public void openContact(View view)
    {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        //Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, String.valueOf(contactUri));
        intent.setData(contactUri);
        this.startActivity(intent);
    }

    private void renderContact(Uri uri) {

        TextView contactNameView = (TextView) findViewById(R.id.contact_name);
        ImageView photoView = (ImageView) findViewById(R.id.contact_portrait);


        contactNameView.setText(getDisplayName(uri));
            photoView.setImageBitmap(getPhoto(uri));


    }

    public void callContact()
    {
        Toast.makeText(this,"Feature Under Development.",Toast.LENGTH_SHORT).show();
        //retrieveContactNumber(contactUri);
    }


    private void retrieveContactNumber(Uri uriContact) {

        String contactNumber = null;
        String contactID=null;

        // getting contacts ID
        Cursor cursorID = getContentResolver().query(uriContact,
                new String[]{ContactsContract.Contacts._ID},
                null, null, null);

        if (cursorID.moveToFirst()) {

            contactID = cursorID.getString(cursorID.getColumnIndex(ContactsContract.Contacts._ID));
        }

        cursorID.close();

        // Using the contact ID now we will get contact phone number
        Cursor cursorPhone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},

                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
                        ContactsContract.CommonDataKinds.Phone.TYPE + " = " +
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,

                new String[]{contactID},
                null);

        if (cursorPhone.moveToFirst()) {
            contactNumber = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            Toast.makeText(this,"phone Number : "+contactNumber,Toast.LENGTH_SHORT).show();
        }

        cursorPhone.close();

    }

    private Bitmap getPhoto(Uri uri) {
        Bitmap photo = null;

        Cursor contactCursor = getContentResolver().query(uri,
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
                    .openContactPhotoInputStream(getContentResolver(),
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


    private String getDisplayName(Uri uri) {
        String displayName = null;

        Cursor cur = getContentResolver().query(uri, null, null, null, null);
        if (cur.moveToFirst()) {
            displayName = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        }
        cur.close();

        return displayName;
    }


    public void saveNote(View view)
    {
        Intent intent = getIntent();

        EditText notesView = (EditText) findViewById(R.id.noteText);
        intent.putExtra("note", notesView.getText().toString());
        intent.putExtra("refference",contactUri.toString());
        this.setResult(RESULT_OK, intent);
        finish();

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contact_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
