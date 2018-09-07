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
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthProvider;

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

public class sign_up extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mcallbacks;
    private String mVerificationId;
    private int btn_type = 0;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    Button btn;
    private EditText e1,e2,e3,e4;
    private ProgressDialog pd = null;
    TelephonyManager tm;
    private String imei;
    //TEST
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    UserSession session;
    //TEST
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_layout);
//        Footer.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        if(getResources().getBoolean(R.bool.portrait_only)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        btn = findViewById(R.id.button4);
        e1 = findViewById(R.id.email);
        e2 = findViewById(R.id.password);
        e3 = findViewById(R.id.phonenumber);
        e4 = findViewById(R.id.gst);
        mAuth = FirebaseAuth.getInstance();
        tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        //TEST
        sharedPreferences = getApplicationContext().getSharedPreferences("Reg", 0);
        editor = sharedPreferences.edit();
        //TEST

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email,password,phone,GST;
                email = e1.getText().toString();
                password = e2.getText().toString();
                phone = e3.getText().toString();
                GST = e4.getText().toString();




                if (ActivityCompat.checkSelfPermission(sign_up.this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    if (ActivityCompat.shouldShowRequestPermissionRationale(sign_up.this,
                            android.Manifest.permission.READ_PHONE_STATE)) {

                    } else {
                        int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 0;
                        ActivityCompat.requestPermissions(sign_up.this,
                                new String[]{Manifest.permission.READ_PHONE_STATE},
                                MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                    }
                } else {
                    imei = tm.getDeviceId();

                    Toast.makeText(sign_up.this, "IMEI : "+imei, Toast.LENGTH_SHORT).show();
                }



                if(email.equals(""))
                {
                    e1.setError("Please Enter Email Address");
                }
                else {
                    if(password.equals(""))
                    {
                        e2.setError("Please Enter Password");
                    }
                    else {
                        if(phone.equals(""))
                        {
                            e3.setError("Please Enter Phone number");
                        }
                        else {
                            if(GST.equals(""))
                            {
                                e4.setError("Please Enter GST Number");
                            }
                            else {
                                new JSONTask().execute("https://chemocart.com/seller-API/v1/register");
                                sign_up.this.pd = ProgressDialog.show(sign_up.this, "Wait", "Signing up", true, false);

                            }
                        }
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
                String email,password,phone,GST;
                email = e1.getText().toString();
                password = e2.getText().toString();
                phone = e3.getText().toString();
                GST = e4.getText().toString();






                // Create data variable for sent values to server
                String data = URLEncoder.encode("user_email", "UTF-8")
                        + "=" + URLEncoder.encode(email, "UTF-8");

                data += "&" + URLEncoder.encode("password", "UTF-8") + "="
                        + URLEncoder.encode(password, "UTF-8");

                data += "&" + URLEncoder.encode("phone_number", "UTF-8")
                        + "=" + URLEncoder.encode(phone, "UTF-8");

                data += "&" + URLEncoder.encode("gst_number", "UTF-8")
                        + "=" + URLEncoder.encode(GST, "UTF-8");

                data += "&" + URLEncoder.encode("device_imei", "UTF-8")
                        + "=" + URLEncoder.encode(imei, "UTF-8");

               /* DetailModel detailModel = new DetailModel();
                detailModel.setEmail(email);
                detailModel.setPhonenumber(phone);
*/




                URL url = new URL(urls[0]);
                connection = (HttpURLConnection)url.openConnection();
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
                String status = jsonObj.getString("status");

                DetailModel detailModel = new DetailModel();
                detailModel.setEmail(email);
                detailModel.setPhonenumber(phone);
                detailModel.setPassword(password);
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
            if (sign_up.this.pd != null) {
                sign_up.this.pd.dismiss();
            }
            if(result == "3")
            {
                Toast.makeText(sign_up.this, "User already registered", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(sign_up.this,log_in.class);
//                intent.putExtra("phoneNumber",e3.getText().toString());
                startActivity(intent);
                finish();
            }
            else if(result == "1"){
                Intent intent = new Intent(sign_up.this,OTP.class);
                //TEST
                editor.putString("Email",e1.getText().toString());
                editor.putString("txtPassword",e2.getText().toString());
//                editor.putInt("phoneNumber", Integer.parseInt(e3.getText().toString()));
                editor.commit();
                session = new UserSession(getApplicationContext());
                session.createUserLoginSession(e1.getText().toString(),e2.getText().toString());
//                private Session session;//global variable
//                session = new Session(cntx); //in oncreate
//and now we set sharedpreference then use this like

//                session.setphonenumber("Phone_number");
                //TEST
                intent.putExtra("phoneNumber",e3.getText().toString());
                intent.putExtra("email",e1.getText().toString());
                intent.putExtra("password",e2.getText().toString());
                intent.putExtra("imei",imei);
                startActivity(intent);
                finish();
                Toast.makeText(sign_up.this, "Successfully Registered", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(sign_up.this, "Try Again", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent= new Intent(sign_up.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
