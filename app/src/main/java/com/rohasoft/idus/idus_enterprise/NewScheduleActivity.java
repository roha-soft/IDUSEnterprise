package com.rohasoft.idus.idus_enterprise;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.rohasoft.idus.idus_enterprise.Adapter.Schedule;
import com.rohasoft.idus.idus_enterprise.Adapter.ScheduleAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Ayothi selvam on 10-11-2017.
 */

public class NewScheduleActivity extends AppCompatActivity {
    List<Schedule> list;
    ListView listView;

    Button button_share;
    ArrayList<String> arrayList;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView= (ListView) findViewById(R.id.listView);
        button_share= (Button) findViewById(R.id.button_scheduleShare);

        list=new ArrayList<>();
        arrayList=new ArrayList<>();


        String tot_amount=getIntent().getExtras().getString("totalAmount");
        String duration_tot=getIntent().getExtras().getString("loanDurations");


        int tot_amt = Integer.parseInt(tot_amount);
        int duration = Integer.parseInt(duration_tot);
        String dt = getIntent().getExtras().getString("startDate");
        String loanOption = getIntent().getExtras().getString("loanOptions");

        int bal_amt=tot_amt;
        int due_amt=tot_amt/duration;

        button_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                share();
            }
        });


       // Toast.makeText(getApplicationContext(),tot_amt+"",Toast.LENGTH_SHORT).show();



        // Start date
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(dt));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        list.add(new Schedule("Due Date","Pay Amount","Balance Amount"));
        for (int i=0;i<duration;i++){
            bal_amt=bal_amt-due_amt;
            if (loanOption.equals("Daily")){

                c.add(Calendar.DATE, 1 );

            }
            else if(loanOption.equals("Weekly")){


                c.add(Calendar.DATE, 7);

            }
            else if(loanOption.equals("By Weekly")){


                c.add(Calendar.DATE, 15);

            }
            else if(loanOption.equals("Monthly")){


                c.add(Calendar.MONTH, 1);

            }

            SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
            String output = sdf1.format(c.getTime());

            list.add(new Schedule(output,due_amt+"",bal_amt+""));
            arrayList.add("Due Date -"+output+"----"+" pay Amount - "+due_amt+"----"+"Balance due - "+bal_amt+"\n");


        }

        ScheduleAdapter adapter=new ScheduleAdapter(this,R.layout.schedule_item,list);


        listView.setAdapter(adapter);

    }

    private void share() {
        Intent share=new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
//                String shareBodyText = "i am test data for share button \n kris";
        String shareBodyText = arrayList+"";
        share.putExtra(android.content.Intent.EXTRA_SUBJECT, "Due Payment Details  ");
        share.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
        startActivity(Intent.createChooser(share, "Choose sharing method"));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
