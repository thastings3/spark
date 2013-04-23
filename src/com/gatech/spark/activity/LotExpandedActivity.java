package com.gatech.spark.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.gatech.spark.R;
import com.gatech.spark.helper.CommonHelper;
import com.gatech.spark.helper.HandlerReturnObject;
import com.gatech.spark.helper.HttpRestClient;
import com.gatech.spark.helper.SaxParser;
import com.gatech.spark.model.SparkParkingLot;
import com.loopj.android.http.AsyncHttpResponseHandler;
import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;

/**
 * Created with IntelliJ IDEA.
 * User: tyler
 * To change this template use File | Settings | File Templates.
 */
public class LotExpandedActivity extends Activity {

    public static final String SUBSCRIPTION = "subscription";

    public static final String TYPE = "type";
    private XYMultipleSeriesDataset mDataset;
    private XYMultipleSeriesRenderer mRenderer = getDemoRenderer();
    private GraphicalView mChartView;
    public static final String PARKING_LOT = "parking_lot_object";
    public SparkParkingLot parkingLot;
    private TextView parkingLotName, spotsAvailable;
    public int occupiedSpotCount = 0;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lot_expanded);
        findViewsById();
        setRendererStyling();

        Intent previousIntent = getIntent();
        if( previousIntent != null && previousIntent.getParcelableExtra(PARKING_LOT) != null )
        {
            parkingLot = (SparkParkingLot)previousIntent.getParcelableExtra(PARKING_LOT);
            populateUI(true);
            HttpRestClient.getParkingLotGraph(parkingLot.getParkingLotID(), new AsyncHttpResponseHandler(){
                @Override
                public void onSuccess(String s) {
                    SaxParser parser = new SaxParser();
                    HandlerReturnObject<JSONArray> handlerObject = parser.parseGraphDateResponse(s);
                    if(handlerObject.isValid())
                    {
                        mDataset = getDemoDataset(handlerObject.getObject());
                        setupChart();
                        populateUI(false);
                    }
                    else {
                        CommonHelper.showLongToast(LotExpandedActivity.this, "Error parsing JSON");
                    }
                }

                @Override
                public void onFailure(Throwable throwable, String s) {
                    super.onFailure(throwable, s);    //To change body of overridden methods use File | Settings | File Templates.
                }


            });
        }
        else
        {
            CommonHelper.showLongToast(LotExpandedActivity.this, "Error, no parking lot found");
        }


    }

    private void findViewsById()
    {
        parkingLotName = (TextView)findViewById(R.id.lotNumber);
        spotsAvailable = (TextView)findViewById(R.id.spotsAvailable);
    }

    private void populateUI(boolean isLoading)
    {
        parkingLotName.setText("LOT " + parkingLot.getParkingLotID() );
        if(isLoading)
        {
            spotsAvailable.setText( parkingLot.getCapacity() + " Total Spots");
        }
        else
        {
            spotsAvailable.setText( parkingLot.getCapacity() - occupiedSpotCount + " Spots Available");
        }
    }

    public void setupChart()
    {
        if (mChartView == null) {
            LinearLayout layout = (LinearLayout) findViewById(R.id.chart);
            mChartView = ChartFactory.getLineChartView(this, mDataset,
                    mRenderer);
            mRenderer.setSelectableBuffer(100);
            layout.addView(mChartView, new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
        }
        else
        {
            mChartView.repaint();
        }
    }

    public void startNavigation(View v )
    {

        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:ll=" + parkingLot.getLocation().getLatitude() + "," + parkingLot.getLocation().getLongitude()));
        //Intent i = new Intent(Intent.ACTION_VIEW, ContentURI.create("http://maps.google.com/?daddr=53.316518,-1.029282") );
        startActivity(i);
    }

    private void setRendererStyling() {
        mRenderer.setApplyBackgroundColor(true);
        mRenderer.setBackgroundColor(Color.argb(100, 50, 50, 50));
        mRenderer.setAxisTitleTextSize(16);
        mRenderer.setChartTitleTextSize(20);
        mRenderer.setLabelsTextSize(15);
        mRenderer.setLegendTextSize(15);
        //top, left, bottom, right -- for margins.
        mRenderer.setMargins(new int[] { 20, 30, 15, 30 });
        mRenderer.setZoomButtonsVisible(true);
        mRenderer.setPointSize(1);
    }

    private XYMultipleSeriesRenderer getDemoRenderer() {
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
//        renderer.addXTextLabel();
        renderer.setMargins(new int[]{20, 30, 15, 30});
        XYSeriesRenderer r = new XYSeriesRenderer();
        r.setColor(Color.GREEN);
        r.setPointStyle(PointStyle.CIRCLE);
        r.setFillBelowLine(false);
        //r.setFillBelowLineColor(Color.WHITE);
        r.setFillPoints(true);
        renderer.addSeriesRenderer(r);


        r = new XYSeriesRenderer();
        r.setPointStyle(PointStyle.POINT);
        r.setColor(Color.RED);
        r.setFillPoints(true);
        renderer.addSeriesRenderer(r);
        renderer.setAxesColor(Color.DKGRAY);
        renderer.setLabelsColor(Color.BLUE);

        //renderer.setXLabels(0);
        renderer.clearXTextLabels();
        renderer.setLabelsTextSize(10);
        renderer.setLabelsColor(Color.RED);

        return renderer;
    }

    private XYMultipleSeriesDataset getDemoDataset(JSONArray array) {
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        XYSeries firstSeries = new XYSeries("Spots Taken");

        XYSeries secondSeries = new XYSeries("Max Capacity");

        try
        {
            JSONArray subArray;
            double lastX = 0;
            for (int i = 0; i < array.length(); i++)
            {
                subArray = (JSONArray)array.get(i);
                if(!subArray.isNull(0) && !subArray.isNull(1))
                {
                    //TODO test manipulation of data to get a chart that shows something.
                    firstSeries.add( subArray.getDouble(1) ,subArray.getDouble(0)); //i / 2); // FOR Y
                    occupiedSpotCount = (int)subArray.getDouble(0);
                    secondSeries.add(subArray.getDouble(1), parkingLot.getCapacity());
                    lastX =                               subArray.getDouble(1);
                }
            }
            Log.e("lastX" , lastX + "");
            dataset.addSeries(firstSeries);
            dataset.addSeries(secondSeries);
            mRenderer.clearXTextLabels();
            mRenderer.setXLabels(0);
            addXaxisLabels(mRenderer, lastX );
        }
        catch (JSONException e)
        {
            CommonHelper.showLongToast(LotExpandedActivity.this, "JSON ARRAY IN ARRAY IN ARRAY");
        }
        return dataset;
    }

    private void addXaxisLabels(XYMultipleSeriesRenderer renderer, double lastX)
    {
        renderer.addXTextLabel( lastX  , "now" );
        renderer.addXTextLabel( lastX - 3600 , "-1 hour" );
        renderer.addXTextLabel( lastX - (3600 *2) , "-2 hours" );
        renderer.addXTextLabel( lastX - (3600 *3) , "-3 hours" );
        renderer.addXTextLabel( lastX - (3600 *4) , "-4 hours" );
        renderer.addXTextLabel( lastX - (3600 *5) , "-5 hours" );
        renderer.addXTextLabel( lastX - (3600 *6) , "-6 hours" );
    }



}