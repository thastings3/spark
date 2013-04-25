package com.gatech.spark.helper;

import android.content.Context;
import com.loopj.android.http.*;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.protocol.HttpContext;

import java.net.CookieStore;

/**
 * Created with IntelliJ IDEA.
 * User: tyler
 * Date: 4/3/13
 * Time: 8:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class HttpRestClient {
    public static final String     BASE_URL = "http://google.com/";
    public static final String     API_KEY  = "AIzaSyA_i2Z3XEQ74NQ71KpemvtPs6WuZwhwu4c";

    // default timeout = first number times 1000 to convert to millisecs
    private static final int       DEFAULT_TIMEOUT    = 30 * 1000;

    private static AsyncHttpClient client             = new AsyncHttpClient();

    /**
     * Sets up the cookie store that will persist the cookie through the
     * session.
     *
     * @param context
     */
    public static void setupCookieStore( Context context )
    {
        client = new AsyncHttpClient();
        client.setCookieStore( new PersistentCookieStore( context ) );
        clearCookieStore();
        client.setTimeout( DEFAULT_TIMEOUT );
    }

    public static CookieStore getCookieStore()
    {
        return (CookieStore) client.getHttpContext().getAttribute( ClientContext.COOKIE_STORE );
    }

    public static void clearCookieStore()
    {
        HttpContext httpContext = client.getHttpContext();
        PersistentCookieStore cookieStore = ( (PersistentCookieStore) httpContext.getAttribute( ClientContext.COOKIE_STORE ) );
        cookieStore.clear();
    }

    public static void getPlaces(double latitude, double longitude, int radius , AsyncHttpResponseHandler responseHandler)
    {
        RequestParams params = new RequestParams();
        String baseURL = "https://maps.googleapis.com/maps/api/place/nearbysearch/xml";
        params.put( "location", latitude + "," + longitude );
        params.put( "radius", radius + "" );
        params.put( "sensor", "false" );
        params.put( "types", "food" ); //TOdo change the hardcoded type... what to search for??
        params.put( "key", API_KEY );
        client.get( baseURL , params, responseHandler );
        //&name=harbour
        //String url = "https://maps.googleapis.com/maps/api/place/textsearch/xml?query=restaurants&sensor=true&location=33.792616,-84.397683&radius=1000&key=AIzaSyA_i2Z3XEQ74NQ71KpemvtPs6WuZwhwu4c";
    }

    public static void getPlaces(String query, double latitude, double longitude, int radius, AsyncHttpResponseHandler responseHandler)
    {
        RequestParams params = new RequestParams();
        String baseURL = "https://maps.googleapis.com/maps/api/place/textsearch/xml";
        params.put( "query", query);
        params.put( "location", latitude + "," + longitude );
        params.put( "radius", radius + "" );
        params.put( "sensor", "false" );
        params.put( "key", API_KEY );
        client.get( baseURL , params, responseHandler );
    }

    public static void getPlaceReferencePhoto(String photoReference , BinaryHttpResponseHandler responseHandler)
    {
        RequestParams params = new RequestParams();
        String baseURL = "https://maps.googleapis.com/maps/api/place/photo";
        params.put( "maxwidth", "400" );
        params.put( "photoreference", photoReference );
        params.put( "sensor", "true" );
        params.put( "key", API_KEY );
        client.get( baseURL , params, responseHandler );
    }

    public static void getDetailedPlace(String reference , AsyncHttpResponseHandler responseHandler)
    {
        RequestParams params = new RequestParams();
        String baseURL = "https://maps.googleapis.com/maps/api/place/details/xml";
        params.put( "reference", reference );
        params.put( "sensor", "false" );
        params.put( "key", API_KEY );
        client.get( baseURL , params, responseHandler );
    }

    public static void getLocationById(int id , AsyncHttpResponseHandler responseHandler)
    {
        String baseURL = "http://centspark6675.cloudapp.net:8080/spark/webresources/location/" + id;
        client.get( baseURL , null , responseHandler );
    }

    public static void getChartingDateForLocation(int id , AsyncHttpResponseHandler responseHandler)
    {
        String baseURL = "http://centspark6675.cloudapp.net/render?target=parkinglot." + id + "&format=json";
        client.get( baseURL , null , responseHandler );
    }

    /**
     * queries our DB and gets parking lots that we can subscribe to.
     * @param latitude
     * @param longitude
     * @param radiusInMeters
     * @param responseHandler
     */
    public static void getNearbyParkingLots(double latitude, double longitude, int radiusInMeters, AsyncHttpResponseHandler responseHandler)
    {
        String baseURL = "http://centspark6675.cloudapp.net:8080/spark/webresources/parkinglot/findLotsByLocation";

        RequestParams params = new RequestParams();
        params.put( "longitude", longitude + "" );
        params.put( "latitude", latitude + "" );
        params.put( "maxdistance", radiusInMeters + "" );
        params.put( "maxresults", "5" );
        client.get( baseURL , params , responseHandler );
    }


    /**
     * Gets the list of locations from our DB
     * @param responseHandler
     */
    public static void getSparkLocations(AsyncHttpResponseHandler responseHandler)
    {
        String baseURL = "http://centspark6675.cloudapp.net:8080/spark/webresources/location";
        client.get( baseURL , null , responseHandler );
    }

    /**
     * Gets the list of addresses from our DB
     * @param responseHandler
     */
    public static void getSparkAddresses(AsyncHttpResponseHandler responseHandler)
    {
        String baseURL = "http://centspark6675.cloudapp.net:8080/spark/webresources/address";
        client.get( baseURL , null , responseHandler );
    }

    public static void getParkingLotGraph(int pkParkingLot, AsyncHttpResponseHandler responseHandler)
    {
        String baseURL = "http://centspark6675.cloudapp.net/render?target=parkinglot." + pkParkingLot + "&format=json&from=-6h";
        client.get( baseURL , null , responseHandler );
    }


}
