package com.gatech.spark.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.gatech.spark.R;
import com.gatech.spark.database.SqliteHelper;
import com.gatech.spark.helper.CommonHelper;
import com.gatech.spark.helper.HandlerReturnObject;
import com.gatech.spark.helper.HttpRestClient;
import com.gatech.spark.helper.SaxParser;
import com.gatech.spark.model.Place;
import com.gatech.spark.model.Subscription;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.image.SmartImageView;

/**
 * Created with IntelliJ IDEA.
 * To change this template use File | Settings | File Templates.
 */
public class PlaceExpandedActivity extends Activity {

    private Place place;
    public static final String PLACE = "place_expanded_activity_place";
    public TextView nameTextView, addressTextView, priceTextView, openTextView, vicinityTextView, ratingTextView, phoneTextView,websiteTextView;
    public SmartImageView iconImageView;
    private Button findParkingButton;
    private ProgressDialog pDialog;
    private SqliteHelper dbHelper;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place_expanded);
        findViewsById();
        dbHelper = SqliteHelper.getDbHelper( getApplicationContext());

        Intent previousIntent = getIntent();
        place = previousIntent.getParcelableExtra( PLACE );
        if(place == null)
        {
            CommonHelper.showLongToast(PlaceExpandedActivity.this, "Error obtaining Google Place.");
        }
        else
        {
            //populate the ui
            HttpRestClient.getDetailedPlace(place.getReference(), new AsyncHttpResponseHandler(){
                @Override
                public void onStart() {
                    pDialog = new ProgressDialog(PlaceExpandedActivity.this );
                    pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    pDialog.setCancelable( false );
                    pDialog.setTitle( "Searching for more data..." );
                    pDialog.show();
                }

                @Override
                public void onSuccess(String s) {
                    SaxParser parser = new SaxParser();
                    HandlerReturnObject<Place> handlerObject = parser.parseDetailedPlaceXmlResponse(s);
                    if(handlerObject.isValid())
                    {
                        place = handlerObject.getObject();
                        populateUI();
                    }
                    else
                    {
                        CommonHelper.showLongToast(PlaceExpandedActivity.this, "ERROR: " + handlerObject.getMessage());
                    }
                }

                @Override
                public void onFailure(Throwable throwable, String s) {
                    CommonHelper.showLongToast(PlaceExpandedActivity.this, "ERROR: " + s);
                }

                @Override
                public void onFinish() {
                    pDialog.dismiss();
                }
            });
            //populateUI();
            //Log.e("photo", place.getPhoto().getPhotoReference());
        }
    }

    private void populateUI()
    {
        nameTextView.setText(place.getName());
        addressTextView.setText(place.getFormattedAddress());

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
        if(place.getRating() != -1)
        {
            ratingTextView.setText(place.getRating() + " AVG RATING");
        }

        if(place.getPriceLevel() != -1)
        {
            priceTextView.setText(place.getPriceLevel() + " PRICE LEVEL");
        }


        phoneTextView.setText(place.getPhoneNumber());
        websiteTextView.setText(place.getWebsite());

        iconImageView.setImageUrl(place.getIconLink());
    }

    private void findViewsById()
    {
        nameTextView = (TextView)findViewById(R.id.nameTextView);
        addressTextView = (TextView)findViewById(R.id.addressTextView);
        priceTextView = (TextView)findViewById(R.id.priceLevelTextView);
        openTextView = (TextView)findViewById(R.id.openTextView);
        vicinityTextView = (TextView)findViewById(R.id.vicinityTextView);
        ratingTextView = (TextView)findViewById(R.id.ratingTextView);
        iconImageView = (SmartImageView)findViewById(R.id.iconImage);
        findParkingButton = (Button) findViewById(R.id.findParkingButton);

        phoneTextView = (TextView)findViewById(R.id.phoneTextView);
        websiteTextView = (TextView)findViewById(R.id.websiteTextView);
        setupCall();
        setupWebsite();
        setupFindParkingButton();
    }

    private void setupFindParkingButton()
    {
        findParkingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    HandlerReturnObject<Subscription> handlerObject = dbHelper.insertSubscription(new Subscription(place.getName(), place.getLocation().getLatitude(), place.getLocation().getLongitude()));
                    if(handlerObject.isValid())
                    {
                        CommonHelper.showLongToast(PlaceExpandedActivity.this, "You are now subscribed!");
                        PlaceExpandedActivity.this.finish();
                    }
                    else
                    {
                        CommonHelper.showLongToast(PlaceExpandedActivity.this, "Error subscribing: "  + handlerObject.getMessage());
                    }
            }
        });

    }

    private void setupCall()
    {
        phoneTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!place.getPhoneNumber().isEmpty())
                {
                    String uri = "tel:" + place.getPhoneNumber() ;
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse(uri));
                    startActivity(intent);
                }
            }
        });

    }

    private void setupWebsite()
    {
        websiteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                      if(!place.getWebsite().isEmpty())
                      {
                          String url = place.getWebsite();
                          Intent i = new Intent(Intent.ACTION_VIEW);
                          i.setData(Uri.parse(url));
                          startActivity(i);
                      }
            }
        });


    }

}