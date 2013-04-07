package com.gatech.spark.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import com.gatech.spark.R;
import com.gatech.spark.helper.CommonHelper;
import com.gatech.spark.model.Place;

/**
 * Created with IntelliJ IDEA.
 * To change this template use File | Settings | File Templates.
 */
public class PlaceExpandedActivity extends Activity {

    private Place place;
    public static final String PLACE = "place_expanded_activity_place";
    public TextView nameTextView, addressTextView, priceTextView, openTextView, vicinityTextView, ratingTextView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place_expanded);
        findViewsById();

        Intent previousIntent = getIntent();
        place = previousIntent.getParcelableExtra( PLACE );
        if(place == null)
        {
            CommonHelper.showLongToast(PlaceExpandedActivity.this, "Error obtaining Google Place.");
        }
        else
        {
            //populate the ui
            populateUI();
            Log.e("photo", place.getPhoto().getPhotoReference());
        }
    }

    private void populateUI()
    {
        nameTextView.setText(place.getName());
        addressTextView.setText(place.getFormattedAddress());
        priceTextView.setText(place.getPriceLevel() + "");
        if(place.getOpenNow() != null)
        {
            if(place.getOpenNow())
            {
                openTextView.setText("OPEN NOW");
            }
            else {
                openTextView.setText("CLOSED");
            }
        }

        vicinityTextView.setText(place.getVicinity());
        ratingTextView.setText(place.getRating() + "");
    }

    private void findViewsById()
    {
        nameTextView = (TextView)findViewById(R.id.nameTextView);
        addressTextView = (TextView)findViewById(R.id.addressTextView);
        priceTextView = (TextView)findViewById(R.id.priceLevelTextView);
        openTextView = (TextView)findViewById(R.id.openTextView);
        vicinityTextView = (TextView)findViewById(R.id.vicinityTextView);
        ratingTextView = (TextView)findViewById(R.id.ratingTextView);
    }

}