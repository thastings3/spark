package com.gatech.spark.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import com.gatech.spark.R;

/**
 * Created with IntelliJ IDEA.
 * User: tyler
 * To change this template use File | Settings | File Templates.
 */
public class LotExpandedActivity extends Activity {

    public static final String SUBSCRIPTION = "subscription";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lot_expanded);

    }

    public void startNavigation(View v )
    {
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=New+York+NY"));
        startActivity(i);
    }


}