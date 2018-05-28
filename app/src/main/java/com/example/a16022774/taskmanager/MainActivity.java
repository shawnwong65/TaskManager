package com.example.a16022774.taskmanager;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> al;
    ListView lv;
    ArrayAdapter aa;
    Button btnAdd;
    int reqCode = 12345;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = (Button) findViewById(R.id.btnAdd);
        lv = (ListView) findViewById(R.id.lv);

        al = new ArrayList<String>();
        DBHelper db = new DBHelper(MainActivity.this);

        al.clear();
        al.addAll(db.getAllTasks());
        db.close();

        aa = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, al);
        lv.setAdapter(aa);
        aa.notifyDataSetChanged();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, AddActivity.class);
                startActivityForResult(i, 1);
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DBHelper db = new DBHelper(MainActivity.this);
                String data = al.get(position);
                String i = data.split(" ")[0];
                db.deleteTask(Integer.parseInt(i));
                al.clear();
                al.addAll(db.getAllTasks());
                db.close();
                aa.notifyDataSetChanged();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == 1){
            DBHelper db = new DBHelper(MainActivity.this);

            al.clear();
            al.addAll(db.getAllTasks());
            db.close();
            aa.notifyDataSetChanged();

            String name = data.getStringExtra("name");
            int timer = data.getIntExtra("time", 0);
            Log.i("Data", name + " " + timer);

            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.SECOND, timer);

            Intent intent = new Intent(MainActivity.this, ScheduledNotificationReceiver.class);

            intent.putExtra("name", name);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, reqCode, intent, PendingIntent.FLAG_CANCEL_CURRENT);

            AlarmManager am = (AlarmManager) getSystemService(Activity.ALARM_SERVICE);
            am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
        }
    }
}
