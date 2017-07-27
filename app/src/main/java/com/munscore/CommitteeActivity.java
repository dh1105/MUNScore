package com.munscore;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CommitteeActivity extends AppCompatActivity {

    Button next;
    EditText committee_name;
    View parentLayout;
    private final int ACT =1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parentLayout = findViewById(android.R.id.content);
        setContentView(R.layout.activity_committee);
        next = (Button) findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getText() != null){
                    String committee_name = getText();
                    Intent i =  new Intent(CommitteeActivity.this, ParamClass.class);
                    i.putExtra("name", committee_name);
                    startActivityForResult(i, ACT);
                }
            }
        });
        committee_name = (EditText) findViewById(R.id.committee_name);
        this.invalidateOptionsMenu();
    }

    @Override
    public void onBackPressed() {
        if(!committee_name.getText().toString().isEmpty()) {
            AlertDialog.Builder al = new AlertDialog.Builder(CommitteeActivity.this);
            al.setCancelable(true);
            al.setMessage("Are you sure you want to go back? Going back will not create a new committee");
            al.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent back = new Intent(CommitteeActivity.this, MainActivity.class);
                    back.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    back.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(back);
                }
            });
            al.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            al.show();
        }else {
            Intent back = new Intent(CommitteeActivity.this, MainActivity.class);
            back.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            back.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(back);
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        menu.findItem(R.id.action_settings).setVisible(false);
        menu.findItem(R.id.action_deltext).setVisible(false);
        menu.findItem(R.id.action_addtext).setVisible(false);
        menu.findItem(R.id.action_create).setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case ACT:
                if(resultCode == RESULT_OK){
                    Intent in = new Intent(CommitteeActivity.this, MainActivity.class);
                    setResult(Activity.RESULT_OK, in);
                    finish();
                }
        }
    }

    public String getText(){
        if(committee_name.getText().toString().isEmpty()){
            Snackbar.make(parentLayout, "Please enter the committee name", Snackbar.LENGTH_LONG).show();
            return null;
        }
        else{
            return committee_name.getText().toString();
        }
    }
}
