package com.chemocart.chemocartseller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class ForgetPassword extends AppCompatActivity {
    EditText username, password, otp;
    Button submit, set, resend;
    private ProgressDialog pd = null;
    private ProgressDialog pd1 = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(0);
        getSupportActionBar().setIcon(R.drawable.logo_main);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        otp = findViewById(R.id.otp);
        submit = findViewById(R.id.button3);
        set = findViewById(R.id.submit);
        resend = findViewById(R.id.resend);

        String Username = username.getText().toString();
        String Password = password.getText().toString();
        String OTP = otp.getText().toString();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (username.equals("")) {
                    username.setError("Enter Username/Email");
                    Toast.makeText(ForgetPassword.this, "Enter Username/Email", Toast.LENGTH_SHORT).show();
                } else {
                    new JSONTask().execute("https://chemocart.com/seller-API/v1/forget-password");
                    ForgetPassword.this.pd = ProgressDialog.show(ForgetPassword.this, "Wait", "Sending OTP", true, false);
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
                String email = username.getText().toString();
                //Toast.makeText(OTP.this, "Phone Number "+phone_number, Toast.LENGTH_SHORT).show();
                String data = URLEncoder.encode("user_email", "UTF-8")
                        + "=" + URLEncoder.encode(email, "UTF-8");


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
                String error = jsonObj.getString("status");
                //String message = jsonObj.getString("message");
                return error;
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
            Toast.makeText(ForgetPassword.this, "Status " + result, Toast.LENGTH_SHORT).show();
            if (ForgetPassword.this.pd != null) {
                ForgetPassword.this.pd.dismiss();
            }
            if (result.equals("16")) {
                Toast.makeText(ForgetPassword.this, "OTP sent!!! Enter OTP & Password", Toast.LENGTH_SHORT).show();
                set.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(password.equals(""))
                        {
                            password.setError("Password Mandatory");
                            Toast.makeText(ForgetPassword.this, "Password Mandatory", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            if(otp.equals(""))
                            {
                                otp.setError("OTP Mandatory");
                                Toast.makeText(ForgetPassword.this, "OTP Mandatory", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                new JSONTask1().execute("https://chemocart.com/seller-API/v1/reset-password");
                                ForgetPassword.this.pd1 = ProgressDialog.show(ForgetPassword.this, "Wait", "Setting Password", true, false);
                            }
                        }
                    }
                });

                resend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (username.equals("")) {
                            username.setError("Enter Username/Email");
                            Toast.makeText(ForgetPassword.this, "Enter Username/Email", Toast.LENGTH_SHORT).show();
                        } else {
                            new JSONTask().execute("https://chemocart.com/seller-API/v1/forget-password");
                            ForgetPassword.this.pd = ProgressDialog.show(ForgetPassword.this, "Wait", "Sending OTP", true, false);
                        }

                    }
                });

            } else if(result.equals("20")) {
                Toast.makeText(ForgetPassword.this, "Server Error!!! Try After Some time", Toast.LENGTH_SHORT).show();

            }
            else if(result.equals("21")) {
                Toast.makeText(ForgetPassword.this, "OTP not sent!!! Try Again", Toast.LENGTH_SHORT).show();

            }
            else if(result.equals("19")) {
                Toast.makeText(ForgetPassword.this, "User name not exists", Toast.LENGTH_SHORT).show();
            }
        }
    }

    class JSONTask1 extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... urls) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                // Create data variable for sent values to server
                String Password = password.getText().toString();
                String OTP = otp.getText().toString();
                String Username = username.getText().toString();
                //Toast.makeText(OTP.this, "Phone Number "+phone_number, Toast.LENGTH_SHORT).show();
                String data = URLEncoder.encode("new_password", "UTF-8")
                        + "=" + URLEncoder.encode(Password, "UTF-8");

                data += "&" + URLEncoder.encode("OTP", "UTF-8") + "="
                        + URLEncoder.encode(OTP, "UTF-8");

                data += "&" + URLEncoder.encode("user_email", "UTF-8") + "="
                        + URLEncoder.encode(Username, "UTF-8");

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
                String error = jsonObj.getString("status");
                //String message = jsonObj.getString("message");
                return error;
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
            Toast.makeText(ForgetPassword.this, "Error " + result, Toast.LENGTH_SHORT).show();
            if (ForgetPassword.this.pd1 != null) {
                ForgetPassword.this.pd1.dismiss();
            }
            if (result.equals("16")) {
                Toast.makeText(ForgetPassword.this, "Password successfully changed", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ForgetPassword.this, log_in.class);
                startActivity(intent);
                finish();

            } else if(result.equals("22")) {
                Toast.makeText(ForgetPassword.this, "OTP Incorrect!!! Try Again", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(ForgetPassword.this, "Something went wrong!!! Please Try Again", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

