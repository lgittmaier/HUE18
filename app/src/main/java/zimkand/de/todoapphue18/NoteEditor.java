package zimkand.de.todoapphue18;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class NoteEditor extends AppCompatActivity {
    EditText editTitle, editDetails, dateAndTime;

    MainActivity ma = new MainActivity();

    Calendar tmpCalendar;
    //Calendar calendar;
    //String currentDate;
    //String currentTime;
    public static final String filename = "notes5.csv";

    static List<Note> notesList = new ArrayList<Note>();   //notes created


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notizen_editor);


        editTitle = findViewById(R.id.editTitle);
        editDetails = findViewById(R.id.editDetails);
        dateAndTime = findViewById(R.id.dateTime);

        try {   // for contextmenu edit
            if (getIntent().getAction().equals("edit")) {        //TODO  is the if entered??

                editTitle.setText(getIntent().getExtras().getString("title"));
                editDetails.setText(getIntent().getExtras().getString("details"));
                dateAndTime.setText(getIntent().getExtras().getString("dateAndTime"));

            }
        }catch (Exception e){}

        dateAndTime.setInputType(InputType.TYPE_NULL); // don't show keyboard

        setDateAndTime(dateAndTime);


        // the current date and time when a note is created
        /*calendar = Calendar.getInstance();
        currentDate = fill(calendar.get(Calendar.DAY_OF_MONTH)) + "."
                + fill((calendar.get(Calendar.MONTH) + 1)) + "."
                + calendar.get(Calendar.YEAR);

        currentTime = fill(calendar.get(Calendar.HOUR)) + ":"
                + fill(calendar.get(Calendar.MINUTE));*/

    }

    public void setDateAndTime(EditText dateAndTime) {

        dateAndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimeDialog(dateAndTime);
            }
        });
    }

    public void showDateTimeDialog(EditText dateAndTime) {
        Calendar calendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {     // to set the date
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);


                TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {  // to set the time
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                        dateAndTime.setText(sdf.format(calendar.getTime()));
                        tmpCalendar = calendar;
                    }
                };
                new TimePickerDialog(NoteEditor.this, timeSetListener,
                        calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
            }
        };
        new DatePickerDialog(NoteEditor.this, dateSetListener,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();

    }


    /*private String fill(int i) {     //changes 5 to "05"
        if (i < 10) {
            return "0" + i;
        }
        return String.valueOf(i);
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {     //sets the check (save) icon
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_menu, menu);
        return true;
    }

    public void writeNoteToCSV(List<Note> noteList) {        //writes into CSV-File

        try {
            FileOutputStream fos = openFileOutput(filename, MODE_PRIVATE);
            PrintWriter out = new PrintWriter(new OutputStreamWriter(fos));
            for (int i = 0; i < noteList.size(); i++) {
                out.println(noteList.get(i).getDateTime() + ";" + noteList.get(i).getTitle() + ";" + noteList.get(i).getDetails());
            }
            out.flush();
            out.close();
        } catch (FileNotFoundException exp) {
            Log.d("TAG", exp.getStackTrace().toString());
        }

        noteList.clear();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.quit) {       //if quit is selected
            Toast.makeText(this, "quit", Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
        if (item.getItemId() == R.id.save) {         //if save is selected

            if (!(editTitle.getText().toString().equals("") || editTitle.getText() == null)) {

                Note note = new Note(dateAndTime.getText().toString(),
                        editTitle.getText().toString(),
                        editDetails.getText().toString());

                notesList.add(note);
                writeNoteToCSV(notesList);

                ma.bindViewToAdapter();
                Toast.makeText(this, "save note", Toast.LENGTH_SHORT).show();
                onBackPressed();


            }
        }
        return super.onOptionsItemSelected(item);
    }


    public static List<Note> getNotes() {
        return notesList;
    }

    public static void addNote(Note note) {
        notesList.add(note);
    }

    public static void deleteNote(int position) {
        notesList.remove(position);
    }
}