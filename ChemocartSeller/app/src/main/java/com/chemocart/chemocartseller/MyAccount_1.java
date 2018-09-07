package com.chemocart.chemocartseller;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MyAccount_1 extends AppCompatActivity {
    Button btn;
    Button btn1;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(MyAccount_1.this, Home_page.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account_1);
        Toolbar toolbar = findViewById(R.id.toolbar_account);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(0);
        getSupportActionBar().setLogo(R.drawable.logo_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyAccount_1.this, Home_page.class);
                startActivity(intent);
            }
        });
        btn = findViewById(R.id.button5);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // AlertDialog.Builder mBuilder = new AlertDialog.Builder(MyAccount_1.this);
                //View mView = getLayoutInflater().inflate(R.layout.personal_details,null);
                Dialog d=new Dialog(MyAccount_1.this);
                d.setContentView(R.layout.personal_details);
                final EditText editText = d.findViewById(R.id.editText);
                final EditText editText2 = d.findViewById(R.id.editText2);
                final EditText editText3 = d.findViewById(R.id.editText3);
                final EditText editText4 = d.findViewById(R.id.editText4);
                final TextView textView = findViewById(R.id.textView7);
                final TextView textView2 = findViewById(R.id.textView8);
                final TextView textView3 = findViewById(R.id.textView9);
                final TextView textView4 = findViewById(R.id.textView10);
                btn1 = d.findViewById(R.id.button_personal);
                btn1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        textView.setText(editText.getText().toString());
                        textView2.setText(editText2.getText().toString());
                        textView3.setText(editText3.getText().toString());
                        textView4.setText(editText4.getText().toString());
                    }
                });
                d.show();

                /*mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.show();*/
            }
        });


    }

}
