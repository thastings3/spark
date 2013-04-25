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
import java.util.ArrayList;

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
        mRenderer.setPointSize(4);
        mRenderer.setShowGridY(true);
        mRenderer.setShowGridX(true);
        mRenderer.setGridColor(Color.WHITE);
        mRenderer.setShowLegend(false);
    }

    private XYMultipleSeriesRenderer getDemoRenderer() {
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();

        XYSeriesRenderer r = new XYSeriesRenderer();
        r.setPointStyle(PointStyle.POINT);
        r.setLineWidth(6);
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

    private void addHotRenderer(XYMultipleSeriesRenderer renderer)
    {
        //HOT
        renderer.setMargins(new int[]{20, 30, 15, 30});
        XYSeriesRenderer r = new XYSeriesRenderer();
        r.setColor(getResources().getColor(R.color.graph_dark_yellow));
        r.setPointStyle(PointStyle.CIRCLE);
        r.setFillBelowLine(false);
        r.setFillPoints(true);
        renderer.addSeriesRenderer(r);
    }

    private void addMildRenderer(XYMultipleSeriesRenderer renderer)
    {
        //COLD
        renderer.setMargins(new int[]{20, 30, 15, 30});
        XYSeriesRenderer r = new XYSeriesRenderer();
        r.setColor(getResources().getColor(R.color.graph_yellow));
        r.setPointStyle(PointStyle.CIRCLE);
        r.setFillBelowLine(false);
        r.setFillPoints(true);
        renderer.addSeriesRenderer(r);
    }

    private void addNeutralRenderer(XYMultipleSeriesRenderer renderer)
    {
        //Neutral
        renderer.setMargins(new int[]{20, 30, 15, 30});
        XYSeriesRenderer r = new XYSeriesRenderer();
        r.setColor(getResources().getColor(R.color.graph_green));
        r.setPointStyle(PointStyle.CIRCLE);
        r.setFillBelowLine(false);
        r.setFillPoints(true);
        renderer.addSeriesRenderer(r);
    }

    private XYMultipleSeriesDataset getDemoDataset(JSONArray array) {
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        ArrayList<XYSeries> serieses = new ArrayList<XYSeries>();

        boolean wasHot = false, wasMild = false, wasNeutral = false;
        XYSeries wasHotSeries = new XYSeries(""), wasMildSeries = new XYSeries(""), wasNeutralSeries = new XYSeries("");

        //XYSeries neutralSeries = new XYSeries("Spots Taken");
        //XYSeries mildSeries = new XYSeries("");
        //XYSeries hotSeries = new XYSeries("");


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
                    occupiedSpotCount = (int)subArray.getDouble(0);
                    if( occupiedSpotCount >=  (.9 * parkingLot.getCapacity())  )
                    {
                        if( wasHot )
                        {
                            wasHotSeries.add(subArray.getDouble(1), subArray.getDouble(0));
                        }
                        else
                        {
                            if(wasHotSeries.getItemCount() > 0)
                            {
                                serieses.add(wasHotSeries);
                                addHotRenderer(mRenderer);
                                wasHotSeries = new XYSeries("");
                            }
                            else
                            {
                                wasHotSeries.add(subArray.getDouble(1), subArray.getDouble(0));
                            }
                        }

                        wasHot = true;
                        wasMild = false;
                        wasNeutral = false;
                    }
                    else if(  (occupiedSpotCount >= (.5 * parkingLot.getCapacity()))  &&   ( occupiedSpotCount < (.9 * parkingLot.getCapacity())) )
                    {

                        if( wasMild )
                        {
                            wasMildSeries.add(subArray.getDouble(1), subArray.getDouble(0));
                        }
                        else
                        {
                            if(wasMildSeries.getItemCount() > 0)
                            {
                                serieses.add(wasMildSeries);
                                addMildRenderer(mRenderer);
                                wasMildSeries = new XYSeries("");
                            }
                            else
                            {
                                wasMildSeries.add(subArray.getDouble(1), subArray.getDouble(0));
                            }
                        }
                        //mildSeries.add( subArray.getDouble(1) ,subArray.getDouble(0));
                        wasMild = true;
                        wasHot = false;
                        wasNeutral = false;
                    }
                    else {

                        if( wasNeutral )
                        {
                            wasNeutralSeries.add(subArray.getDouble(1), subArray.getDouble(0));
                        }
                        else
                        {
                            if(wasNeutralSeries.getItemCount() > 0)
                            {
                                serieses.add(wasNeutralSeries);
                                addNeutralRenderer(mRenderer);
                                wasNeutralSeries = new XYSeries("");
                            }
                            else
                            {
                                wasNeutralSeries.add(subArray.getDouble(1), subArray.getDouble(0));
                            }
                        }
                       // neutralSeries.add( subArray.getDouble(1) ,subArray.getDouble(0));
                        wasNeutral = true;
                        wasMild = false;
                        wasHot = false;
                    }
                    //neutralSeries.add( subArray.getDouble(1) ,subArray.getDouble(0));

                    secondSeries.add(subArray.getDouble(1), parkingLot.getCapacity());
                    lastX =                               subArray.getDouble(1);
                }
            }

            if(wasNeutralSeries.getItemCount() > 0)
            {
                serieses.add(wasNeutralSeries);
                addNeutralRenderer(mRenderer);
            }

            if(wasMildSeries.getItemCount() > 0)
            {
                serieses.add(wasMildSeries);
                addMildRenderer(mRenderer);
                wasMildSeries = new XYSeries("");
            }
            if(wasHotSeries.getItemCount() > 0)
            {
                serieses.add(wasHotSeries);
                addHotRenderer(mRenderer);
                wasHotSeries = new XYSeries("");
            }


            dataset.addSeries(secondSeries);
            for(int i = 0; i < serieses.size(); i++)
            {
                dataset.addSeries(serieses.get(i));
            }

//            dataset.addSeries(neutralSeries);
//            dataset.addSeries(hotSeries);
//            dataset.addSeries(mildSeries);

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