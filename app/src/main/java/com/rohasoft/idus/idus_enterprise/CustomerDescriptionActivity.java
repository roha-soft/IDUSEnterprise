package com.rohasoft.idus.idus_enterprise;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.rohasoft.idus.idus_enterprise.Adapter.CustomerLoanAdapter;
import com.rohasoft.idus.idus_enterprise.Adapter.ExistingCustomer;
import com.rohasoft.idus.idus_enterprise.other.AddLoanCusList;
import com.rohasoft.idus.idus_enterprise.other.GetCustomerLaonCallBack;
import com.rohasoft.idus.idus_enterprise.other.GetLoanData;
import com.rohasoft.idus.idus_enterprise.other.ServerRequest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ayothi selvam on 08-11-2017.
 */

public class CustomerDescriptionActivity extends AppCompatActivity{

    TextView textView_cusName,textView_cusId,textView_address,textView_city,textView_phone,textView_pincode;

    TextView textView_view_schedule;

    String id,CusName,phone,address,city,pincode;

  ;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_customer_view);

        textView_cusName=(TextView) findViewById(R.id.txt_cusview_name);
        textView_cusId=(TextView) findViewById(R.id.txt_cusview_cusid);
        textView_city=(TextView) findViewById(R.id.txt_cusview_city);
        textView_pincode=(TextView) findViewById(R.id.txt_cusview_pincode);
        textView_phone=(TextView) findViewById(R.id.txt_cusview_phone);
        textView_address=(TextView) findViewById(R.id.txt_cusview_addr);
        textView_view_schedule=(TextView) findViewById(R.id.txt_cusdes_schedule);

        textView_view_schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),ScheduleActivity.class));

            }
        });



        if(getIntent().getExtras().getString("id").length()>0){
            id=getIntent().getExtras().getString("id");
            CusName=getIntent().getExtras().getString("cusName");
            phone=getIntent().getExtras().getString("phone");
            address=getIntent().getExtras().getString("address");
            city=getIntent().getExtras().getString("city");
            pincode=getIntent().getExtras().getString("pincode");

            textView_cusName.setText(CusName);
            textView_cusId.setText("CUS"+id);
            textView_phone.setText(phone);
            textView_address.setText(address);
            textView_city.setText(city);
            textView_pincode.setText(pincode);





        }


    }




    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}