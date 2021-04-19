package zimkand.de.todoapphue18;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NoteAdapter extends BaseAdapter {

    LayoutInflater layoutInflater;
    List<Note> notes;
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
    Date now = new Date();
    Calendar dueDate = Calendar.getInstance();


    public NoteAdapter(Context context, List<Note> notes) {
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.notes = notes;
    }

    @Override
    public int getCount() {
        return notes.size();
    }

    @Override
    public Object getItem(int position) {
        return notes.get(position);
    }


    @Override
    public long getItemId(int position) {
        return 0L;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Note note = notes.get(position);     //current Note


        // due date of note
        try {
            dueDate.setTime(sdf.parse(note.getDateTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        View view;
        if (convertView == null) {
            view = layoutInflater.inflate(R.layout.listview_object, null);

        } else {
            view = convertView;
        }


        // sets the fields in the listview
        TextView titleTextView = view.findViewById(R.id.titleField);
        TextView dateTextView = view.findViewById(R.id.dateField);
        titleTextView.setText(note.getTitle());
        dateTextView.setText(note.getDateTime());


        if (dueDate.getTime().getTime() < now.getTime()) {                    // if note is due -> set background darkred 
            titleTextView.setBackgroundResource(R.color.darkred);
            dateTextView.setBackgroundResource(R.color.darkred);
        } else {
            titleTextView.setBackgroundResource(R.color.darkgrey);
            dateTextView.setBackgroundResource(R.color.darkgrey);
        }

        return view;
    }
}
