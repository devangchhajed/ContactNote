package contactnote.example.devang.contactnote;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {
    private DatabaseHandler databaseHandler;
    private ContactNotesAdapter contactNotesAdapter;
    private static final int PICK_CONTACT_REQUEST = 0;
    private Uri contactUri;
    private static final int UPDATE_CONTACT_REQUEST=2;
    private static final int CONTACT_ENTRY_REQUEST_CODE = 1;
    private static int keyid=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#5a1fdb"));
        ab.setBackgroundDrawable(colorDrawable);

        databaseHandler = new DatabaseHandler(this);
        ListView listView = (ListView) findViewById(R.id.notes_list);
        contactNotesAdapter = new ContactNotesAdapter(this,databaseHandler.getAllContactRecords());
        listView.setAdapter(contactNotesAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Cursor cur = (Cursor) parent.getItemAtPosition(position);
                keyid=cur.getInt(cur.getColumnIndex("_id"));
                Contact fetch=databaseHandler.getContact(keyid);
                String urif=fetch.getRefference();
                String notef=fetch.getContactNote();
                Intent intent=new Intent(MainActivity.this,contactPage.class).putExtra(Intent.EXTRA_TEXT,urif);
                intent.putExtra("noted",notef);
                startActivityForResult(new Intent(intent), UPDATE_CONTACT_REQUEST);

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                alertMessage(parent, position);
                return true;
            }
        });
    }
    public void alertMessage(final AdapterView<?> parent1, final int position1)
    {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                switch (which)
                { case DialogInterface.BUTTON_POSITIVE:
                    Cursor cur = (Cursor) parent1.getItemAtPosition(position1);
                    int listid=cur.getInt(cur.getColumnIndex("_id"));
                    databaseHandler.deleteContact(listid);
                    contactNotesAdapter.changeCursor(databaseHandler.getAllContactRecords());
                    break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this); builder.setMessage("Delete Note?")
            .setPositiveButton("Delete", dialogClickListener)
            .setNegativeButton("Cancel", dialogClickListener).show();
    }
    public void contactSelection(View view)
    {
        startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), PICK_CONTACT_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CONTACT_ENTRY_REQUEST_CODE)
        {
            if (resultCode == RESULT_OK) {
                String time = data.getStringExtra("refference");
                String notes = data.getStringExtra("note");
                databaseHandler.saveRecord(time, notes);
                contactNotesAdapter.changeCursor(databaseHandler.getAllContactRecords());
            }
        }

        if (requestCode == PICK_CONTACT_REQUEST) {
            if (resultCode == RESULT_OK) {
                Log.d("Selection = ", data.toString());
                contactUri = data.getData();
                Intent intent=new Intent(this,contactPage.class).putExtra(Intent.EXTRA_TEXT,data.getData().toString());
                startActivityForResult(new Intent(intent), CONTACT_ENTRY_REQUEST_CODE);
            }else {
                Toast.makeText(this, "No Contact Selected", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == UPDATE_CONTACT_REQUEST)
        {
            if (resultCode == RESULT_OK) {
                String time = data.getStringExtra("refference");
                String notes = data.getStringExtra("note");
                databaseHandler.updateContact(new Contact(keyid,time,notes));
                contactNotesAdapter.changeCursor(databaseHandler.getAllContactRecords());
            }
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            Intent intent=new Intent(this,settings.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}