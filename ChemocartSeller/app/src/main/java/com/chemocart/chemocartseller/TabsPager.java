package com.chemocart.chemocartseller;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class TabsPager extends FragmentStatePagerAdapter {

    String[] titles =new String[]{"Pending Chat","Order Received","Sample Requested","Alert"};
    private int[] imageResId = { R.drawable.image, R.drawable.image };
    private Context context;

    public TabsPager(FragmentManager fm) {
        super(fm);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){

            case 0:
                Pending_chat_frag pending_chat_frag=new Pending_chat_frag();
                return pending_chat_frag;
            case 1:
                Order_received_frag order_received_frag=new Order_received_frag();
                return order_received_frag;
            case 2:
                Sample_requested_frag sample_requested_frag=new Sample_requested_frag();
                return sample_requested_frag;
            case 3:
                Alert_frag alert_frag=new Alert_frag();
                return alert_frag;
        }

        return null;
    }



    @Override
    public int getCount() {
        return 4;
    }

    /*public View getTabView(int position) {
        // Given you have a custom layout in `res/layout/custom_tab.xml` with a TextView and ImageView
        View v = LayoutInflater.from(context).inflate(R.layout.custom_tab, null);
        TextView tv = (TextView) v.findViewById(R.id.textView);
        tv.setText(titles[position]);
        ImageView img = (ImageView) v.findViewById(R.id.imgView);
        img.setImageResource(imageResId[position]);
        return v;

    }*/
}