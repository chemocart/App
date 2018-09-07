package com.chemocart.chemocartseller;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class log_in extends AppCompatActivity {
    TextView txt,txt1;
    Button btn;
    android.widget.EditText e1;
    android.support.design.widget.TextInputEditText e2;
    private ProgressDialog pd = null;
    TelephonyManager tm;
    private String imei, phone_number, email, password;

    //TEST
    private static final String PREFER_NAME = "Reg";
    UserSession session;
    SharedPreferences.Editor editor;
    private SharedPreferences sharedPreferences;
    //TEST
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        if(getResources().getBoolean(R.bool.portrait_only)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        e1 = (android.widget.EditText)findViewById(R.id.email);
        e2 = (android.support.design.widget.TextInputEditText)findViewById(R.id.password);
        tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        txt =(TextView) findViewById(R.id.signup);
        txt1 = findViewById(R.id.forgetpassword);

        //TEST
        session = new UserSession(getApplicationContext());
        Toast.makeText(getApplicationContext(),
                "User Login Status: " + session.isUserLoggedIn(),
                Toast.LENGTH_LONG).show();
        session.checkLogin();
        sharedPreferences = getApplicationContext().getSharedPreferences("Reg", 0);
        email = sharedPreferences.getString("Email","");
        password = sharedPreferences.getString("txtPassword", "");
        /*if(sharedPreferences.contains("phoneNumber")){
            phone_number = String.valueOf(sharedPreferences.getInt("phoneNumber", 0));
            Toast.makeText(getApplicationContext(), phone_number, Toast.LENGTH_LONG).show();

        }*/


        //TEST
        txt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(log_in.this,ForgetPassword.class);
                    startActivity(intent);
            }
        });
        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openlog();
                finish();
            }
        });
        btn = (Button) findViewById(R.id.homepage);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = e1.getText().toString();
                String password = e2.getText().toString();

                if (ActivityCompat.checkSelfPermission(log_in.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    if (ActivityCompat.shouldShowRequestPermissionRationale(log_in.this,
                            Manifest.permission.READ_PHONE_STATE)) {

                    } else {
                        int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 0;
                        ActivityCompat.requestPermissions(log_in.this,
                                new String[]{Manifest.permission.READ_PHONE_STATE},
                                MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                    }
                } else {
                    imei = tm.getDeviceId();
                    Toast.makeText(log_in.this, "IMEI : "+imei, Toast.LENGTH_SHORT).show();
                }



                if(email.equals(""))
                {
                    e1.setError("Please Enter Email Address");
                }
                else
                {
                    if(password.equals(""))
                    {
                        e2.setError("Please Enter Password");
                    }
                    else {
                        new log_in.JSONTask().execute("https://chemocart.com/seller-API/v1/login");
                        log_in.this.pd = ProgressDialog.show(log_in.this, "Wait", "Logging In", true, false);
                    }
                 }
            }
        });

    }
    class JSONTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... urls) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                // Create data variable for sent values to server
                String email = e1.getText().toString();
                String password = e2.getText().toString();
                String data = URLEncoder.encode("email", "UTF-8")
                        + "=" + URLEncoder.encode(email, "UTF-8");

                data += "&" + URLEncoder.encode("password", "UTF-8") + "="
                        + URLEncoder.encode(password, "UTF-8");

                data += "&" + URLEncoder.encode("imei", "UTF-8")
                        + "=" + URLEncoder.encode(imei, "UTF-8");



                URL url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setChunkedStreamingMode(0);
                OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
                writer.write(data);
                writer.flush();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();

                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                String finalJson = buffer.toString();
                JSONObject jsonObj = new JSONObject(finalJson);
                //TEST


                editor = sharedPreferences.edit();
                String apikey = jsonObj.getString("apiKey");
                editor.putString("API_KEY",apikey);
                editor.commit();
                //TEST
                String status = jsonObj.getString("status");
                //String message = jsonObj.getString("message");
                return status;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Toast.makeText(log_in.this, "Status "+result, Toast.LENGTH_SHORT).show();
            if (log_in.this.pd != null) {
                log_in.this.pd.dismiss();
            }
            if(result.equals("16")) {
                //TEST

                session.createUserLoginSession(e1.getText().toString(),
                        e2.getText().toString());
                //TEST
                Toast.makeText(log_in.this, "Successfully Logged In", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(log_in.this, Home_page.class);
                startActivity(intent);
                //finish();
            }else if(result.equals("10")){
                Toast.makeText(log_in.this, "User Not Exist", Toast.LENGTH_LONG).show();

            }
            else if(result.equals("11")){
                Toast.makeText(log_in.this, "Log in Failed Incorrect Credential", Toast.LENGTH_LONG).show();
            }
            else if(result.equals("12")){
                /*session.createUserLoginSession(e1.getText().toString(),
                        e2.getText().toString());*/
//                phone_number = sharedPreferences.getString("phoneNumber", "");
                Toast.makeText(log_in.this, "Mobile number Not Verified", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(log_in.this,OTP.class);
//                intent.putExtra("phoneNumber","" );
                intent.putExtra("email",e1.getText().toString());
                intent.putExtra("password",e2.getText().toString());
                intent.putExtra("imei",imei);
                startActivity(intent);
                finish();
            }
            else if(result.equals("13")){
                Toast.makeText(log_in.this, "Email not verified kindly go to your email id and Verfiy", Toast.LENGTH_LONG).show();
                e1.setText("");
                e2.setText("");
            }
            else if(result.equals("14")){
                Toast.makeText(log_in.this, "Your Profile is Deactivated by Admin", Toast.LENGTH_LONG).show();
                e1.setText("");
                e2.setText("");
            }
            else if(result.equals("15")){
                Toast.makeText(log_in.this, "You can not Login with this email.This email is registered in buyer account", Toast.LENGTH_LONG).show();
                e1.setText("");
                e2.setText("");
            }
            else {
                Toast.makeText(log_in.this, "Server Error!!! Try After Some time", Toast.LENGTH_LONG).show();
                e1.setText("");
                e2.setText("");
            }
        }
    }
    public void openlog(){
        Intent intent2 = new Intent(this,sign_up.class);
        startActivity(intent2);
    }
}
