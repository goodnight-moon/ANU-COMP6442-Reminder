package com.anu.dolist;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.view.inputmethod.EditorInfoCompat;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.anu.dolist.db.Event;
import com.anu.dolist.db.EventRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.anu.dolist.MainActivity.arrayAdapter;
import static com.anu.dolist.MainActivity.list;



/**
 * @author: Limin Deng(u6849956)
 */
public class EditorActivity extends AppCompatActivity {


    int noteId;
    final static int RQS_1 = 1;
    public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    private final static String default_notification_channel_id = "default" ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);


        // get data
        Intent go = getIntent();
        String eventTitle = go.getStringExtra("title");
        String eventLocation = go.getStringExtra("location");
        String eventStart = go.getStringExtra("start");
        String eventEnd = go.getStringExtra("end");
        String eventAlert = go.getStringExtra("alert");
        String eventUrl = go.getStringExtra("url");
        String eventNotes = go.getStringExtra("notes");

        // get all UIs
        TextView cancel = findViewById(R.id.edit_tb_left);
        final TextView add = findViewById(R.id.edit_tb_right);

        // UI
        final EditText editTitle = findViewById(R.id.edit_event_title);
        final EditText editLocation = findViewById(R.id.edit_event_location);
        final EditText editUrl = findViewById(R.id.edit_event_url);
        final EditText editNote = findViewById(R.id.edit_event_notes);
        final Button editStart = findViewById(R.id.edit_event_date);
        final Button editEnd = findViewById(R.id.edit_event_time);
        final Button editAlert = findViewById(R.id.edit_event_alert);

        // change right button on the toolbar
        if (eventTitle != null) {
            add.setText("Update");
        }else {
            add.setText("Add");
        }

        // fill in data
        if (eventTitle != null) {
            editTitle.setText(eventTitle);
        }
        if (eventLocation != null) {
            editLocation.setText(eventLocation);
        }
        if (eventStart != null) {
            editStart.setText(eventStart);
        }
        if (eventEnd != null) {
            editEnd.setText(eventEnd);
        }
        if (eventAlert != null) {
            editAlert.setText(eventAlert);
        }
        if (eventUrl != null) {
            editUrl.setText(eventUrl);
        }
        if (eventNotes != null) {
            System.out.println(editNote.getText().toString());;
            editNote.setText(eventNotes);
        }


        final Calendar mCalendar = Calendar.getInstance();


        /**
         * Floating Action Bar
         */
        FloatingActionButton fab = findViewById(R.id.edit_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // what is this?
                if (editTitle.getText().toString().equals("")) {
                    Snackbar.make(view, "Title cannot be emptyt", Snackbar.LENGTH_LONG)
                            .setAction("Action", null)
                            .show();
                } else {

                    // insert one record
                    if (add.getText().toString().equals("Add")) {

                        final EventRepository er = new EventRepository(getApplication());
                        System.out.println(editTitle.getText().toString());
                        Event newEvent = new Event(editTitle.getText().toString());
                        newEvent.location = editLocation.getText().toString();
                        newEvent.starts = editStart.getText().toString();
                        newEvent.ends = editEnd.getText().toString();
                        newEvent.alert = editAlert.getText().toString();
                        newEvent.url = editUrl.getText().toString();
                        newEvent.notes = editNote.getText().toString();
                        newEvent.category = false;

                        er.insertOneEvent(newEvent);


                        // show info
                        Context context = getApplicationContext();
                        CharSequence text = "Add completely";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, text,  duration);
                        toast.show();



                        // after toast, finish the activity
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent(EditorActivity.this, MainActivity.class));
                                EditorActivity.this.finish();
                            }
                        }, 1000);

                        // update
                    } else {

                        final EventRepository er = new EventRepository(getApplication());
                        Event newEvent = new Event(editTitle.getText().toString());
                        newEvent.location = editLocation.getText().toString();
                        newEvent.starts = editStart.getText().toString();
                        newEvent.ends = editEnd.getText().toString();
                        newEvent.alert = editAlert.getText().toString();
                        newEvent.url = editUrl.getText().toString();
                        newEvent.notes = editNote.getText().toString();
                        newEvent.category = false;

                        er.updateOneEvent(newEvent);


                        // show info
                        Context context = getApplicationContext();
                        CharSequence text = "Update completely";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, text,  duration);
                        toast.show();



                        // after toast, finish the activity
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent(EditorActivity.this, MainActivity.class));
                                EditorActivity.this.finish();
                            }
                        }, 1000);

                    }

                }


            }
        });


        /**
         * select time
         */
        editEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO Auto-generated method stub
                Calendar mCalendar = Calendar.getInstance();
                int hour = mCalendar.get(Calendar.HOUR_OF_DAY);
                int minute = mCalendar.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(EditorActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
//                        mCalendar.set(Calendar.HOUR, selectedHour);
//                        mCalendar.set(Calendar.MINUTE, selectedMinute);
                        editEnd.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });


        /**
         * select date
         */
        editStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mCalendar = Calendar.getInstance();

                // show the date picker
                new DatePickerDialog(
                        EditorActivity. this, date ,
                        mCalendar .get(Calendar. YEAR ) ,
                        mCalendar .get(Calendar. MONTH ) ,
                        mCalendar .get(Calendar. DAY_OF_MONTH )
                ).show() ;

            }

            // update date picker
            DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    mCalendar.set(Calendar.YEAR, year);
                    mCalendar.set(Calendar.MONTH, monthOfYear);
                    mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    updateLabel();
                }
            };

            private void updateLabel () {
                Log.d("Shark ", "updateLabel");

                String myFormat = "dd/MM/yy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat , Locale. getDefault ());
                Date date = mCalendar.getTime();
                editStart.setText(sdf.format(date));
                scheduleNotification(getNotification( editStart.getText().toString()), 10);
            }

            private void scheduleNotification (Notification notification , long delay) {

                Intent notificationIntent = new Intent( EditorActivity.this, MyNotification.class );
                notificationIntent.putExtra(MyNotification. NOTIFICATION_ID , 1 );
                notificationIntent.putExtra(MyNotification. NOTIFICATION , notification);
                PendingIntent pendingIntent = PendingIntent. getBroadcast ( EditorActivity.this, 0 , notificationIntent , PendingIntent. FLAG_UPDATE_CURRENT ) ;
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context. ALARM_SERVICE );
                assert alarmManager != null;
                alarmManager.set(AlarmManager. ELAPSED_REALTIME_WAKEUP , delay , pendingIntent);
            }

            private Notification getNotification (String content) {


                // create a notification
                NotificationCompat.Builder builder = new NotificationCompat.Builder( EditorActivity.this, default_notification_channel_id ) ;
                builder.setContentTitle( "Scheduled Notification" ) ;
                builder.setContentText(content) ;
                builder.setSmallIcon(R.drawable. ic_launcher_foreground ) ;
//                builder.setAutoCancel( true ) ;
                builder.setChannelId( NOTIFICATION_CHANNEL_ID ) ;
                return builder.build() ;
            }

        });


        /**
         * tooar
         */
        Toolbar tb = findViewById(R.id.edit_toolbar);
        setSupportActionBar(tb);
        // use customized Cancel instead
        // click home button
//        tb.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // because previous view is not finished
//                finish();
//            }
//        });
        // make sure toolbar is not null
        if (getSupportActionBar() != null){
            // show back arrow
            // we use customized Cancel text instead
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        // add menu
        // use customized Add text instead
//        tb.inflateMenu(R.menu.add_note);



        /**
         * Callbacks for cancel and add actions
         * @author: Limin Deng(u6849956)
         */
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EditorActivity.this, MainActivity.class));
                finish();
            }
        });


        /**
         * alert
         */
        editAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(EditorActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Do you want to set alarm")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                editAlert.setText("Alarm set");
//                                Intent intent = new Intent(EditorActivity.this, AlarmActivity.class);
//                                PendingIntent pendingIntent = PendingIntent.getBroadcast(EditorActivity.this, RQS_1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//                                AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
//                                alarmManager.set(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), pendingIntent);
                            }
                        })
                        .setNegativeButton("No",null)
                        .show();

            }
        });


        /**
         * add / update event
         */
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                // empty not allowed
                if (editTitle.getText().toString().equals("")) {
                    // show alert
                    new AlertDialog.Builder(EditorActivity.this)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("Title cannot be empty")
                            .setTitle("Title cannot be empty")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    System.out.println("do nothing");
                                }
                            })
                            //.setNegativeButton("No",null)
                            .show();

                } else {

                    // insert one record
                    if (add.getText().toString().equals("Add")) {

                        final EventRepository er = new EventRepository(getApplication());
                        System.out.println(editTitle.getText().toString());
                        Event newEvent = new Event(editTitle.getText().toString());
                        newEvent.location = editLocation.getText().toString();
                        newEvent.starts = editStart.getText().toString();
                        newEvent.ends = editEnd.getText().toString();
                        newEvent.alert = editAlert.getText().toString();
                        newEvent.url = editUrl.getText().toString();
                        newEvent.notes = editNote.getText().toString();
                        newEvent.category = false;

                        er.insertOneEvent(newEvent);


                        // show info
                        Context context = getApplicationContext();
                        CharSequence text = "Add completely";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, text,  duration);
                        toast.show();



                        // after toast, finish the activity
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent(EditorActivity.this, MainActivity.class));
                                EditorActivity.this.finish();
                            }
                        }, 1000);

                        // update
                    } else {

                        final EventRepository er = new EventRepository(getApplication());
                        Event newEvent = new Event(editTitle.getText().toString());
                        newEvent.location = editLocation.getText().toString();
                        newEvent.starts = editStart.getText().toString();
                        newEvent.ends = editEnd.getText().toString();
                        newEvent.alert = editAlert.getText().toString();
                        newEvent.url = editUrl.getText().toString();
                        newEvent.notes = editNote.getText().toString();
                        newEvent.category = false;

                        er.updateOneEvent(newEvent);


                        // show info
                        Context context = getApplicationContext();
                        CharSequence text = "Update completely";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, text,  duration);
                        toast.show();



                        // after toast, finish the activity
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent(EditorActivity.this, MainActivity.class));
                                EditorActivity.this.finish();
                            }
                        }, 1000);

                    }

                }

            }
        });


        // title change listener
        editTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        editTitle.setText(eventTitle);

        /**
         * @author: Supriya Kamble(u6734521)
         * get the intent id from MainActivity and put it here
         * extra caution of -1 is put, to avoid getting wrong id
         */
        EditText editText = findViewById(R.id.edit_event_notes);

//        Intent intent = getIntent();
//        noteId = intent.getIntExtra("noteId",-1);
//
//        if(noteId != -1){
//            editText.setText(MainActivity.list.get(noteId));
//        }else{
//            MainActivity.list.add(""); // here it set
//            noteId = MainActivity.list.size()-1;
//            MainActivity.arrayAdapter.notifyDataSetChanged();
//        }


        /**
         * update text
         */
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                list.set(noteId,String.valueOf(charSequence));
                arrayAdapter.notifyDataSetChanged();

//                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.anu.dolist", Context.MODE_PRIVATE);
//                HashSet<String> set = new HashSet(MainActivity.list);
//                sharedPreferences.edit().putStringSet("notes",set).apply();


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        ActionBar ab = getSupportActionBar();
        ab.setTitle("Edit Event");

    }


}
