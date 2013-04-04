package com.gatech.spark.application;

import android.app.Application;
import com.gatech.spark.helper.HttpRestClient;

/**
 * Created with IntelliJ IDEA.
 * User: tyler
 * Date: 4/3/13
 * Time: 9:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class Spark extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        HttpRestClient.setupCookieStore(getApplicationContext());
    }
}
