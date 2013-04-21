package com.gatech.spark.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.gatech.spark.R;
import com.gatech.spark.helper.CommonHelper;
import com.gatech.spark.helper.HandlerReturnObject;
import com.gatech.spark.helper.HttpRestClient;
import com.gatech.spark.helper.SaxParser;
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
    private XYMultipleSeriesDataset mDataset;// = getDemoDataset();
    private XYMultipleSeriesRenderer mRenderer = getDemoRenderer();
    private GraphicalView mChartView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lot_expanded);

        setRendererStyling();

        HttpRestClient.getParkingLotGraph(12, new AsyncHttpResponseHandler(){
            @Override
            public void onSuccess(String s) {
                SaxParser parser = new SaxParser();
                HandlerReturnObject<JSONArray> handlerObject = parser.parseGraphDateResponse(s);
                if(handlerObject.isValid())
                {
                    //TODO do something with this data. Currently an array of arrays?? lol

                    mDataset = getDemoDataset(handlerObject.getObject());
                    setupChart();
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
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=New+York+NY"));
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
        mRenderer.setPointSize(10);
    }

    private XYMultipleSeriesRenderer getDemoRenderer() {
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        renderer.setMargins(new int[] { 20, 30, 15, 30 });
        XYSeriesRenderer r = new XYSeriesRenderer();
        r.setColor(Color.GREEN);
        r.setPointStyle(PointStyle.CIRCLE);
        r.setFillBelowLine(false);
        //r.setFillBelowLineColor(Color.WHITE);
        r.setFillPoints(true);
        renderer.addSeriesRenderer(r);
//        r = new XYSeriesRenderer();
//        r.setPointStyle(PointStyle.CIRCLE);
//        r.setColor(Color.GREEN);
//        r.setFillPoints(true);
//        renderer.addSeriesRenderer(r);
//        renderer.setAxesColor(Color.DKGRAY);
//        renderer.setLabelsColor(Color.LTGRAY);
        return renderer;
    }

    private XYMultipleSeriesDataset getDemoDataset(JSONArray array) {
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        XYSeries firstSeries = new XYSeries("Parking Information");
        try
        {
            JSONArray subArray;
            for (int i = 0; i < array.length(); i++)
            {
                subArray = (JSONArray)array.get(i);
                if(!subArray.isNull(0) && !subArray.isNull(1))
                {
                    //TODO test manipulation of data to get a chart that shows something.
                    firstSeries.add(Math.log10( subArray.getDouble(1) ), i / 2); //subArray.getDouble(0) FOR Y
                }
            }
            dataset.addSeries(firstSeries);
        }
        catch (JSONException e)
        {
            CommonHelper.showLongToast(LotExpandedActivity.this, "WTF IS up with JSON ARRAY IN ARRAY IN ARRAY");
        }
        return dataset;
    }



}