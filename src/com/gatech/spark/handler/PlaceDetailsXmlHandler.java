package com.gatech.spark.handler;

import com.gatech.spark.helper.CommonHelper;
import com.gatech.spark.helper.HandlerReturnObject;
import com.gatech.spark.model.Place;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: tyler
 * Date: 4/14/13
 * Time: 9:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class PlaceDetailsXmlHandler extends DefaultHandler
{
    private StringBuffer      buffer   = new StringBuffer();
    private boolean           isValid  = false;
    private String            message  = "";
    private Place place = new Place();


    private DefaultHandler    parent;
    private XMLReader reader;

    public PlaceDetailsXmlHandler( XMLReader reader )
    {
        this( null, reader );
    }

    public PlaceDetailsXmlHandler( DefaultHandler parent, XMLReader reader )
    {
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
        if ( localName.equals( "PlaceDetailsResponse" ) ) {
            place = new Place();
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
        if ( localName.equals( "status" ) )
        {
            isValid = CommonHelper.convertStringToBoolean(buffer.toString());
            message = buffer.toString();
        }
        else if ( localName.equals( "name" ) )
        {
            place.setName(buffer.toString());
        }
        else if ( localName.equals( "type" ) )
        {
            place.addType(buffer.toString());
        }
        else if ( localName.equals( "formatted_address" ) )
        {
            place.setFormattedAddress(buffer.toString());
        }
        else if ( localName.equals( "formatted_phone_number" ) )
        {
            place.setPhoneNumber(buffer.toString());
        }
        else if ( localName.equals( "vicinity" ) )
        {
            place.setVicinity(buffer.toString());
        }
        //TODO use the proper sub xml handler for locations
        else if ( localName.equals( "lat" ) )
        {
            place.setLatitude(CommonHelper.convertStringToDouble(buffer.toString()));
        }
        else if ( localName.equals( "lng" ) )
        {
            place.setLongitude(CommonHelper.convertStringToDouble(buffer.toString()));
        }
        else if ( localName.equals( "rating" ) )
        {
            place.setRating(CommonHelper.convertStringToDouble(buffer.toString()));
        }
        else if ( localName.equals( "icon" ) )
        {
            place.setIconLink(buffer.toString());
        }
        else if ( localName.equals( "reference" ) )
        {
            place.setReference(buffer.toString());
        }
        else if ( localName.equals( "id" ) )
        {
            place.setId(buffer.toString());
        }
        else if ( localName.equals( "open_now" ) )
        {
            place.setOpenNow(CommonHelper.convertStringToBoolean(buffer.toString()));
        }
        else if ( localName.equals( "price_level" ) )
        {
            place.setPriceLevel(CommonHelper.convertStringToInt(buffer.toString()));
        }
        else if ( localName.equals( "photo_reference" ) )
        {
            place.getPhoto().setPhotoReference(buffer.toString());
        }
        else if ( localName.equals( "website" ) )
        {
            place.setWebsite(buffer.toString());
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
    public HandlerReturnObject<Place> retrieveHandlerObject()
    {
        return new HandlerReturnObject<Place>( isValid, message, place );
    }
}
