package contactnote.example.devang.contactnote;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import static android.widget.AdapterView.OnItemClickListener;


public class settings extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#5a1fdb"));
        ab.setBackgroundDrawable(colorDrawable);
        final ListView listView = (ListView) findViewById(R.id.settings_list);

        String[] values = new String[] {
                "Help",
                "Delete All Notes",
                "About",

        };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);


        // Assign adapter to ListView
        listView.setAdapter(adapter);

        // ListView Item Click Listener
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition     = position;

                switch (itemPosition) {
                    case 0:
                        Intent help=new Intent(settings.this,help.class);
                        startActivity(help);
                        break;
                    case 1:
                        //alertMessage();
                        Toast.makeText(settings.this,"Coming Soon",Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Intent about=new Intent(settings.this,about.class);
                        startActivity(about);
                        break;
                }
            }

        });

    }
    DatabaseHandler databaseHandler;
    public void alertMessage()
    {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                switch (which)
                { case DialogInterface.BUTTON_POSITIVE:
                // Yes button clicked
                    //int tableCount=databaseHandler.getContactsCount();
//                    databaseHandler.dropTable(tableCount);
//                    contactNotesAdapter.changeCursor(databaseHandler.getAllContactRecords());

                    Toast.makeText(settings.this,"All Notes Deleted", Toast.LENGTH_LONG).show();
                    break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this); builder.setMessage("Delete All Notes?")
            .setPositiveButton("Delete", dialogClickListener)
            .setNegativeButton("Cancel", dialogClickListener).show();
    }
}