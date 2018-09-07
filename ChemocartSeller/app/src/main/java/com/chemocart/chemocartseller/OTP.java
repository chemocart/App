package com.chemocart.chemocartseller;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;

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
import java.util.concurrent.TimeUnit;

public class OTP extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mcallbacks;
    EditText e1,e2;
    Button b1;
    TextView t1,t2,t3;
    private String mVerificationId;
    private int btn_type = 0;
    private ProgressDialog pd = null;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    //TEST
    private static final String PREFER_NAME = "Reg";
    UserSession session;
    SharedPreferences.Editor editor;
    private SharedPreferences sharedPreferences;
    Context context;
    String phone_number, email;
    DetailModel detailModel = new DetailModel();

    //TEST
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(0);
        getSupportActionBar().setIcon(R.drawable.logo_main);

        context = OTP.this;
        sharedPreferences = getApplicationContext().getSharedPreferences("Reg", 0);

        mAuth = FirebaseAuth.getInstance();
        e1 = findViewById(R.id.editText);
        e2 = findViewById(R.id.editText2);
        b1 = findViewById(R.id.button2);
        t1 = findViewById(R.id.textView);
        t2 = findViewById(R.id.textView2);
        t3 = findViewById(R.id.textView3);
        e2.setEnabled(false);

//        if(sharedPreferences.contains("phoneNumber")){
//            phone_number = String.valueOf(sharedPreferences.getInt("phoneNumber", 0));
//        }
//        phone_number = sharedPreferences.getString("phoneNumber", "");
//        phone_number = getIntent().getStringExtra("phoneNumber");
        phone_number = detailModel.getPhonenumber();
        email = detailModel.getEmail();
//        email =getIntent().getStringExtra("email");
        String password = getIntent().getStringExtra("password");
        String imei = getIntent().getStringExtra("imei");
        e1.setEnabled(true);
        if(phone_number.equals("")) {
            e1.setEnabled(true);
//            phone_number = userSession.getphoneNumber();
        }
        else {
            e1.setText(phone_number);
            Toast.makeText(this, "Email : "+email+"\n Password : "+password+"\n imei : "+imei, Toast.LENGTH_SHORT).show();
            e1.setEnabled(false);
        }
        t2.setVisibility(View.INVISIBLE);
        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                e1.setEnabled(true);
            }
        });

        /*Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                t2.setVisibility(View.VISIBLE);
            }
        },30000);*/
        t2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_type = 0;
                t2.setVisibility(View.INVISIBLE);
                new CountDownTimer(60000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        t3.setText("" + millisUntilFinished / 1000);
                    }

                    public void onFinish() {
                        t3.setText("");
                        t2.setVisibility(View.VISIBLE);
                    }
                }.start();
                if(btn_type == 0) {
                    e1.setEnabled(false);
                    b1.setEnabled(false);
                    e2.setEnabled(false);
                    String phonenumber = e1.getText().toString();
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            phonenumber,
                            60,
                            TimeUnit.SECONDS,
                            OTP.this,
                            mcallbacks
                    );
                }
                else
                {
                    b1.setEnabled(false);
                    e2.setEnabled(false);
                    e1.setEnabled(false);
                    t3.setVisibility(View.INVISIBLE);
                    String verificationCode = e2.getText().toString();
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId,verificationCode);
                    signInWithPhoneAuthCredential(credential);
                }

            }
        });
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CountDownTimer(60000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        t3.setText("" + millisUntilFinished / 1000);
                    }

                    public void onFinish() {
                        t3.setText("");
                        t2.setVisibility(View.VISIBLE);
                    }
                }.start();
                if(btn_type == 0) {
                    e1.setEnabled(false);
                    b1.setEnabled(false);
                    e2.setEnabled(false);
                    String phonenumber = e1.getText().toString();
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            phonenumber,
                            60,
                            TimeUnit.SECONDS,
                            OTP.this,
                            mcallbacks
                    );
                }
                else
                {
                    b1.setEnabled(false);
                    e2.setEnabled(false);
                    e1.setEnabled(false);
                    t3.setVisibility(View.INVISIBLE);
                    String verificationCode = e2.getText().toString();
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId,verificationCode);
                    signInWithPhoneAuthCredential(credential);
                }
            }
        });

        mcallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(OTP.this, "Verification Failed", Toast.LENGTH_SHORT).show();
                b1.setEnabled(true);
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;
                btn_type = 1;
                b1.setEnabled(true);
                e2.setEnabled(true);
                e2.setHint("Enter Verification code here");
                b1.setText("Verify Code");

                // ...
            }

        };
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            FirebaseUser user = task.getResult().getUser();
                            new OTP.JSONTask().execute("https://chemocart.com/seller-API/v1/loginAfterVerification");
                            OTP.this.pd = ProgressDialog.show(OTP.this, "Wait", "Logging In", true, false);
                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                e2.setError("Invalid code.");
                                e2.setEnabled(true);
                                b1.setEnabled(true);
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
            String fcm_id = FirebaseInstanceId.getInstance().getToken();
            try {
                // Create data variable for sent values to server
                String phone_number = getIntent().getStringExtra("Phone_number");
                String email =getIntent().getStringExtra("email");
                String password = getIntent().getStringExtra("password");
                String imei = getIntent().getStringExtra("imei");
                if(phone_number.equals(""))
                {
                    phone_number = e1.getText().toString();
                }
                //Toast.makeText(OTP.this, "Phone Number "+phone_number, Toast.LENGTH_SHORT).show();
                String data = URLEncoder.encode("email", "UTF-8")
                        + "=" + URLEncoder.encode(email, "UTF-8");

                data += "&" + URLEncoder.encode("password", "UTF-8") + "="
                        + URLEncoder.encode(password, "UTF-8");

                data += "&" + URLEncoder.encode("imei", "UTF-8")
                        + "=" + URLEncoder.encode(imei, "UTF-8");

                data += "&" + URLEncoder.encode("mobile_number", "UTF-8")
                        + "=" + URLEncoder.encode(phone_number, "UTF-8");


                data += "&" + URLEncoder.encode("fcm_device_id", "UTF-8")
                        + "=" + URLEncoder.encode(fcm_id, "UTF-8");


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

                //TEST
                sharedPreferences = getApplicationContext().getSharedPreferences("Reg", 0);
                editor = sharedPreferences.edit();
                String apikey = jsonObj.getString("apiKey");
                editor.putString("API_KEY",apikey);
                editor.commit();
                //TEST
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
            Toast.makeText(OTP.this, "Error "+result, Toast.LENGTH_SHORT).show();
            if (OTP.this.pd != null) {
                OTP.this.pd.dismiss();
            }
            if (result.equals("16")) {
                Toast.makeText(OTP.this, "Successfully Logged In", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(OTP.this, Home_page.class);
                startActivity(intent);
                finish();

            }  else if(result.equals("18")){
                Toast.makeText(OTP.this, "Server Error!!! Try After Some time", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(OTP.this, log_in.class);
                startActivity(intent);
                finish();
            }
            else if(result.equals("15"))
            {
                Toast.makeText(OTP.this, "This email is registered in buyer account", Toast.LENGTH_SHORT).show();
            }
            else if(result.equals("11"))
            {
                Toast.makeText(OTP.this, "Log in failed!!! Try After Some time", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(OTP.this, log_in.class);
                startActivity(intent);
                finish();
            }
            else if(result.equals("10"))
            {
                Toast.makeText(OTP.this, "Email does not exists", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(OTP.this, "Something went wrong!!! Try Again", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(OTP.this, log_in.class);
                startActivity(intent);
                finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(OTP.this, log_in.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
