package zimkand.de.todoapphue18;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static ListView lv;
    private Calendar cal = Calendar.getInstance();
    private NoteAdapter noteAdapter;
    //private List<Note> notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        lv = findViewById(R.id.notesList);
        noteAdapter = new NoteAdapter(this, NoteEditor.notesList);

        // register the context menu
        registerForContextMenu(lv);


        loadNotesFromCSV();
        bindViewToAdapter();
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        getMenuInflater().inflate(R.menu.context_menu, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

        // context menu
    @Override
    public boolean onContextItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.context_details) {
            AdapterView.AdapterContextMenuInfo info =
                    (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

            if (info != null) {
                int pos = info.position;
                Note currentNote = (Note) lv.getAdapter().getItem(pos);

                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle(currentNote.getTitle() + " : ");
                alert.setMessage(currentNote.getDetails()+"\n"+"\n"+currentNote.getDateTime());
                alert.setNegativeButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alert.show();
            }

            Toast.makeText(this, "details opened", Toast.LENGTH_LONG).show();
            return true;
        } else if (item.getItemId() == R.id.context_edit) {

            AdapterView.AdapterContextMenuInfo info =
                    (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            if (info != null) {
                int pos = info.position;
                Note currentNote = (Note) lv.getAdapter().getItem(pos);

                Intent intent = new Intent(this, NoteEditor.class);
                intent.setAction("edit");
                Bundle bundle = new Bundle();

                bundle.putString("title", currentNote.getTitle());
                bundle.putString("details", currentNote.getDetails());
                bundle.putString("dateAndTime", currentNote.getDateTime());

                NoteEditor.deleteNote(pos);

                intent.putExtras(bundle);
                startActivity(intent);

            }

            Toast.makeText(this, "editing note", Toast.LENGTH_LONG).show();
            return true;
        } else if (item.getItemId() == R.id.context_delete) {
            AdapterView.AdapterContextMenuInfo info =
                    (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

            if (info != null) {
                int pos = info.position;
                Note currentNote = (Note) lv.getAdapter().getItem(pos);

                NoteEditor.deleteNote(pos);
                bindViewToAdapter();

                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle(currentNote.getTitle() + " deleted !");
                alert.setNegativeButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alert.show();
            }
            Toast.makeText(this, "note deleted", Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onContextItemSelected(item);
    }


    /////////////////////////////////


    public void loadNotesFromCSV() {
        try {
            FileInputStream fis = openFileInput(NoteEditor.filename);

            BufferedReader in = new BufferedReader(new InputStreamReader(fis));
            String line;

            while ((line = in.readLine()) != null) {
                String[] tmp = line.split(";");
                Note n = new Note(tmp[0], tmp[1], tmp[2]);

                if (!NoteEditor.notesList.contains(n)) {
                    NoteEditor.addNote(n);
                }
            }
            in.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void bindViewToAdapter() {
        List<Note> noteList = NoteEditor.getNotes();
        noteAdapter = new NoteAdapter(this, noteList);

        lv.setAdapter(noteAdapter);
    }

    ///////////////////////////////

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {     //sets the menu (plus)
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) { //if plus is selected
        if (item.getItemId() == R.id.add) {
            Intent intent = new Intent(this, NoteEditor.class);  //starts NotizenEditor
            startActivity(intent);
            Toast.makeText(this, "add note", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }


}