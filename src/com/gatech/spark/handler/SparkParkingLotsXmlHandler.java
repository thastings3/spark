package com.gatech.spark.handler;

import com.gatech.spark.helper.CommonHelper;
import com.gatech.spark.helper.HandlerReturnObject;
import com.gatech.spark.model.Place;
import com.gatech.spark.model.SparkAddress;
import com.gatech.spark.model.SparkLocation;
import com.gatech.spark.model.SparkParkingLot;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: tyler
 * Date: 4/21/13
 * Time: 8:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class SparkParkingLotsXmlHandler extends DefaultHandler
{
    private StringBuffer      buffer   = new StringBuffer();
    private boolean           isValid  = false;
    private String            message  = "";

    private ArrayList<SparkParkingLot> parkingLots = new ArrayList<SparkParkingLot>();
    private SparkParkingLot parkingLot;
    private SparkLocation location;
    private boolean inLocation = false;
    private SparkAddress  address;
    private boolean inAddress = false;
    private DefaultHandler    parent;
    private XMLReader reader;

    public SparkParkingLotsXmlHandler( XMLReader reader )
    {
        this( null, reader );
    }

    public SparkParkingLotsXmlHandler( DefaultHandler parent, XMLReader reader ) {
        parkingLots = new ArrayList<SparkParkingLot>();
        this.parent = parent;
        this.reader = reader;
    }

    /**
     * Called when an element tag starts <EXAMPLE>
     *
     * @param uri
     * @param localName
     * @param qName
     * @param attributes
     *
     * @throws org.xml.sax.SAXException
     */
    @Override
    public void startElement( String uri, String localName, String qName, Attributes attributes ) throws SAXException
    {
        buffer.setLength( 0 );
        if ( localName.equals( "parkingLots" ) ) {
            parkingLots = new ArrayList<SparkParkingLot>();
        }
        else if ( localName.equals( "parkingLot" ) ) {
            parkingLot = new SparkParkingLot();
        }
        else if ( localName.equals( "locationID" ) ) {
            if(!inLocation)
            {
                location = new SparkLocation();
                inLocation = true;
            }

        }
        else if ( localName.equals( "addressID" ) ) {
            if(!inAddress)
            {
                address = new SparkAddress();
                inAddress = true;
            }

        }

    }

    /**
     * Called when an element tag ends </EXAMPLE>
     *
     * @param uri
     * @param localName
     * @param qName
     *
     * @throws SAXException
     */
    @Override
    public void endElement( String uri, String localName, String qName ) throws SAXException
    {
        if ( localName.equals( "parkingLot" ) ) {
            isValid = true;
            message = "";
            parkingLots.add(parkingLot);
        }
        else if ( localName.equals( "capacity" ) ) {
            parkingLot.setCapacity(CommonHelper.convertStringToInt(buffer.toString()));
        }
        else if ( localName.equals( "covered" ) ) {
            parkingLot.setCovered(CommonHelper.convertStringToBoolean(buffer.toString()));
        }
        else if ( localName.equals( "parkingLotID" ) ) {
            parkingLot.setParkingLotID(CommonHelper.convertStringToInt(buffer.toString()));
        }
        else if ( localName.equals( "price" ) ) {
            parkingLot.setPrice(CommonHelper.convertStringToDouble(buffer.toString()));
        }
        else if ( localName.equals( "priceTypeID" ) ) {
            parkingLot.setPriceTypeID(CommonHelper.convertStringToInt(buffer.toString()));
        }
        else if ( localName.equals( "locationID" ) ) {
            if(inLocation)
            {
                location.setId(CommonHelper.convertStringToInt(buffer.toString()));
                inLocation = false;
            }
            else
            {
                parkingLot.setLocation(location);
            }
        }
        else if ( localName.equals( "addressID" ) ) {
            if(inAddress)
            {
                address.setId(CommonHelper.convertStringToInt(buffer.toString()));
                inAddress = false;
            }
            else
            {
                location.setSparkAddress(address);
            }

        }
        else if ( localName.equals( "latitude" ) ) {
            location.setLatitude(CommonHelper.convertStringToDouble(buffer.toString()));
        }
        else if ( localName.equals( "longitude" ) ) {
            location.setLongitude(CommonHelper.convertStringToDouble(buffer.toString()));
        }
        else if ( localName.equals( "name" ) ) {
            location.setName(buffer.toString());
        }
        else if ( localName.equals( "city" ) ) {
            address.setCity(buffer.toString());
        }
        else if ( localName.equals( "country" ) ) {
            address.setCountry(buffer.toString());
        }
        else if ( localName.equals( "county" ) ) {
            address.setCounty(buffer.toString());
        }
        else if ( localName.equals( "phoneNumber" ) ) {
            address.setPhoneNumber(buffer.toString());
        }
        else if ( localName.equals( "postalCode" ) ) {
            address.setPostalCode(CommonHelper.convertStringToInt(buffer.toString()));
        }
        else if ( localName.equals( "state" ) ) {
            address.setState(buffer.toString());
        }
        else if ( localName.equals( "streetLine" ) ) {
            address.setStreetLine(buffer.toString());
        }
        else if ( localName.equals( "streetLine2" ) ) {
            address.setStreetLine2(buffer.toString());
        }



    }

    /**
     * Called during element tags to get the string inside <EXAMPLE>This text is
     * recorded</EXAMPLE>
     *
     * @param start
     * @param length
     *
     * @throws SAXException
     */
    @Override
    public void characters( char[] chars, int start, int length ) throws SAXException
    {
        buffer.append( chars, start, length );
    }

    /**
     *
     * @return
     */
    public HandlerReturnObject<ArrayList<SparkParkingLot>> retrieveHandlerObject()
    {
        return new HandlerReturnObject<ArrayList<SparkParkingLot>>( isValid, message, parkingLots );
    }
}
