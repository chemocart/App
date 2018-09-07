package com.chemocart.chemocartseller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Home_page extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{
    private ProgressDialog pd = null;
    //TEST
    UserSession session;
    private static final String PREFER_NAME = "Reg";
    SharedPreferences.Editor editor;
    private SharedPreferences sharedPreferences;
    //TEST

    TextView gst,profile,name,name1,mobile,About,total_order,total_payment,total_product,payment_info,email;
    ImageView profile_active_image,user_image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        sharedPreferences = getApplicationContext().getSharedPreferences("Reg",0);
        String apikey = sharedPreferences.getString("API_KEY","");
        Toast.makeText(this, "API_KEY"+apikey, Toast.LENGTH_SHORT).show();
        new Home_page.JSONTask().execute("https://chemocart.com/seller-API/v1/home");
        Home_page.this.pd = ProgressDialog.show(Home_page.this, "Wait", "Loading....", true, false);
        //For passsing API_KEY(token) in header
        /*
        URL url = new URL(strings[0]);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        String token = "" + new String(accessToken);
        con.setRequestMethod("GET");
        con.setRequestProperty("AuthToken", token);
        con.setReadTimeout(15000);
        con.setConnectTimeout(15000);
        con.setDoOutput(false);
        * */
        if(getResources().getBoolean(R.bool.portrait_only)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(0);
        getSupportActionBar().setIcon(R.drawable.logo_main);

        //For Tab View

        final TabLayout tabLayout=(TabLayout) findViewById(R.id.tabs);
        final ViewPager viewPager=(ViewPager) findViewById(R.id.viewpager);

        TabsPager tabsPager=new TabsPager(getSupportFragmentManager());
        viewPager.setAdapter(tabsPager);
        tabLayout.setupWithViewPager(viewPager);
        /*For Customizing Tab View
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(tabsPager.getTabView(i));
        }*/
        /*tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });*/



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        enableExpandableList();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    boolean doubleBackToExitPressedOnce = false;
    /*This back press will let app to come out on double click but on restart app will open from login without showing home screen and splash screen*/
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce) {
                moveTaskToBack(true);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
                //super.onBackPressed();
                //return ;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);}

    }

    /*This is normal back press,back activity by activity*/
    /*@Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void enableExpandableList() {
        final ArrayList<String> listDataHeader = new ArrayList<String>();
        final HashMap<String, List<String>> listDataChild = new HashMap<String, List<String>>();
        ExpandableListView expListView = (ExpandableListView) findViewById(R.id.left_drawer);

        prepareListData(listDataHeader, listDataChild);

        ExpandableListAdapter listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        // setting list adapter
        expListView.setAdapter(listAdapter);
        //Changing Arrow of Group Item
        /*DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        expListView.setGroupIndicator(getResources().getDrawable(R.drawable.my_group_statelist));
        expListView.setIndicatorBounds(width - GetPixelFromDips(50), width - GetPixelFromDips(10));*/





        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                if(groupPosition == 4)
                {
                    Intent parent4Intent = new Intent(getBaseContext(), Transactions.class);
                    startActivity(parent4Intent);
                    return true;
                }
                else if(groupPosition == 5)
                {
                    Intent parent5Intent = new Intent(getBaseContext(), Transactions.class);
                    startActivity(parent5Intent);
                    return true;
                }
                else if(groupPosition == 6)
                {
                    Intent parent6Intent = new Intent(getBaseContext(), Transactions.class);
                    startActivity(parent6Intent);
                    return true;
                }
                else if(groupPosition == 7)
                {
                    Intent parent7Intent = new Intent(getBaseContext(), Transactions.class);
                    startActivity(parent7Intent);
                    return true;
                }
                else if(groupPosition == 8)
                {
                    Intent parent8Intent = new Intent(getBaseContext(), Transactions.class);
                    startActivity(parent8Intent);
                    return true;
                }
                else if(groupPosition == 9)
                {
                    session = new UserSession(getApplicationContext());
                    session.logoutUser();
                    finish();
                    return true;
                }
                else {
                    // Toast.makeText(getApplicationContext(),
                    // "Group Clicked " + listDataHeader.get(groupPosition),
                    // Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        });
        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                /*Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Expanded",
                        Toast.LENGTH_SHORT).show();*/
            }
        });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                /*Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Collapsed",
                        Toast.LENGTH_SHORT).show();*/

            }
        });

        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
                // Temporary code:
                if(groupPosition==0){
                    if(childPosition == 0){
                        Intent child0Intent = new Intent(getBaseContext(), MyAccount_1.class);
                        startActivity(child0Intent);}
                    if(childPosition == 1){
                        Intent child1Intent = new Intent(getBaseContext(), MyAccount_1.class);
                        startActivity(child1Intent);}
                    if(childPosition == 2){
                        Intent child2Intent = new Intent(getBaseContext(), MyAccount_1.class);
                        startActivity(child2Intent);}
                }
                if(groupPosition==1){
                    if(childPosition == 0){
                        Intent child0Intent = new Intent(getBaseContext(), Product_1.class);
                        startActivity(child0Intent);}
                    if(childPosition == 1){
                        Intent child1Intent = new Intent(getBaseContext(), Product_1.class);
                        startActivity(child1Intent);}
                    if(childPosition == 2){
                        Intent child2Intent = new Intent(getBaseContext(), Product_1.class);
                        startActivity(child2Intent);}
                }
                if(groupPosition==2) {
                    if (childPosition == 0) {
                        Intent child0Intent = new Intent(getBaseContext(), MyAccount_1.class);
                        startActivity(child0Intent);
                    }
                    if (childPosition == 1) {
                        Intent child1Intent = new Intent(getBaseContext(), MyAccount_1.class);
                        startActivity(child1Intent);
                    }
                    if (childPosition == 2) {
                        Intent child2Intent = new Intent(getBaseContext(), MyAccount_1.class);
                        startActivity(child2Intent);
                    }
                }
                if(groupPosition==3){
                    if(childPosition == 0){
                        Intent child0Intent = new Intent(getBaseContext(), MyAccount_1.class);
                        startActivity(child0Intent);}
                    if(childPosition == 1){
                        Intent child1Intent = new Intent(getBaseContext(), MyAccount_1.class);
                        startActivity(child1Intent);}
                    if(childPosition == 2){
                        Intent child2Intent = new Intent(getBaseContext(), MyAccount_1.class);
                        startActivity(child2Intent);}
                }
                // till here
                /*Toast.makeText(
                        getApplicationContext(),
                        listDataHeader.get(groupPosition)
                                + " : "
                                + listDataChild.get(
                                listDataHeader.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT)
                        .show();*/
                return false;
            }
        });}

    /*private int GetPixelFromDips(float pixels) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }*/

    private void prepareListData(List<String> listDataHeader, Map<String,
            List<String>> listDataChild) {


        // Adding child data
        listDataHeader.add(">>      My Account");
        listDataHeader.add(">>      Product");
        listDataHeader.add(">>      Orders");
        listDataHeader.add(">>      Payments Details");
        listDataHeader.add("          Transactions");
        listDataHeader.add("          Home");
        listDataHeader.add("          FAQ's");
        listDataHeader.add("          Contact Us");
        listDataHeader.add("          Chemocart.com");
        listDataHeader.add("          Logout");

        // Adding child data
        List<String> top = new ArrayList<String>();
        top.add("View Personal Details");
        top.add("Edit Personal Details");
        top.add("View My Documents");
        top.add("Upload Documents");
        top.add("Upload Profile Photo");


        List<String> mid = new ArrayList<String>();
        mid.add("Add a Product");
        mid.add("View Product");
        mid.add("Inactive Product");
        mid.add("Request a New Product");

        List<String> bottom = new ArrayList<String>();
        bottom.add("View all Orders");
        bottom.add("Pending Orders");
        bottom.add("Completed Orders");

        List<String> bot = new ArrayList<String>();
        bot.add("Payment Details");
        bot.add("Total Paymnet");


        listDataChild.put(listDataHeader.get(0), top); // Header, Child data
        listDataChild.put(listDataHeader.get(1), mid);
        listDataChild.put(listDataHeader.get(2), bottom);
        listDataChild.put(listDataHeader.get(3), bot);
    }





    class JSONTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... urls) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            String fcm_id = FirebaseInstanceId.getInstance().getToken();
            try {
                // Create data variable for sent values to server
                String apikey = sharedPreferences.getString("API_KEY","");
                URL url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Authorization",apikey);
                connection.setReadTimeout(15000);
                connection.setConnectTimeout(15000);
                connection.setDoOutput(false);
                connection.setChunkedStreamingMode(0);


                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();

                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                String finalJson = buffer.toString();
                JSONObject jsonObj = new JSONObject(finalJson);
                String error = jsonObj.getString("error");
                String email = jsonObj.getString("email");
                String username = jsonObj.getString("user_name");
                String verifyStatus = jsonObj.getString("verify_status");
                String sellerImage = jsonObj.getString("seller_image");
                String companyName = jsonObj.getString("company_name");
                String companyPhone = jsonObj.getString("company_phone");
                String companyGSTNumber = jsonObj.getString("company_tin_number");
                String AboutMe = jsonObj.getString("about_me");
                String firstName = jsonObj.getString("first_name");
                String lastName = jsonObj.getString("last_name");
                String totalOrder = jsonObj.getString("total_order");
                String totalPayment = jsonObj.getString("total_payment");
                String totalProduct = jsonObj.getString("total_product");
                //TEST
                sharedPreferences = getApplicationContext().getSharedPreferences("Reg", 0);
                editor = sharedPreferences.edit();
                editor.putString("ERROR",error);
                editor.putString("EMAIL",email);
                editor.putString("USERNAME",username);
                editor.putString("VERIFYSTATUS",verifyStatus);
                editor.putString("SELLERIMAGE",sellerImage);
                editor.putString("COMPANYNAME",companyName);
                editor.putString("COMPANYPHONE",companyPhone);
                editor.putString("GSTNUMBER",companyGSTNumber);
                editor.putString("ABOUTME",AboutMe);
                editor.putString("FIRSTNAME",firstName);
                editor.putString("LASTNAME",lastName);
                editor.putString("TOTALORDER",totalOrder);
                editor.putString("TOTALPAYMENT",totalPayment);
                editor.putString("TOTALPRODUCT",totalProduct);
                editor.commit();
                //TEST
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
            Toast.makeText(Home_page.this, "Result "+result, Toast.LENGTH_SHORT).show();
            if (Home_page.this.pd != null) {
                Home_page.this.pd.dismiss();
                gst = findViewById(R.id.content_1);
                profile = findViewById(R.id.Profile);
                profile_active_image = findViewById(R.id.image_active);
                mobile = findViewById(R.id.mobile_);
                name = findViewById(R.id.name_);
                name1 = findViewById(R.id.name_1);
                About = findViewById(R.id.about_me);
                total_order = findViewById(R.id.textView5);
                total_payment = findViewById(R.id.textView6);
                total_product = findViewById(R.id.textView9);
                user_image = findViewById(R.id.imageView);
                email = findViewById(R.id.email_);
                gst.setText("  GST  :  "+sharedPreferences.getString("GSTNUMBER",""));
                String verify_status = sharedPreferences.getString("VERIFYSTATUS","");
                if(verify_status.equals("0"))
                {
                    profile.setText("INACTIVE");
                    profile.setTextSize(10);
                }
                name.setText(""+sharedPreferences.getString("USERNAME","User Name"));
                name1.setText(""+sharedPreferences.getString("USERNAME","User Name"));
                mobile.setText(""+sharedPreferences.getString("COMPANYPHONE",""));
                if(!sharedPreferences.getString("ABOUTME","").equals(""))
                    About.setText(""+sharedPreferences.getString("ABOUTME","We at just chemical established in 1982 are one of the pioneer manufacturers, distributors,importers &amp; product developers of chemicals and chemical specialities for industries"));
                total_order.setText("Orders : "+sharedPreferences.getString("TOTALORDER","NULL"));
                total_payment.setText("Rs "+sharedPreferences.getString("TOTALPAYMENT","0"));
                total_product.setText("Products : "+sharedPreferences.getString("TOTALPRODUCT","0"));
                email.setText(""+sharedPreferences.getString("EMAIL",""));
            }

        }
    }
}
