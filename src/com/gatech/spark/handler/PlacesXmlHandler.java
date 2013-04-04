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
 * Date: 4/3/13
 * Time: 8:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class PlacesXmlHandler extends DefaultHandler
{
    private StringBuffer      buffer   = new StringBuffer();
    private boolean           isValid  = false;
    private String            message  = "";
    private ArrayList<Place> places = new ArrayList<Place>();

    private PlaceXmlHandler  placeHandler;
    private DefaultHandler    parent;
    private XMLReader reader;

    public PlacesXmlHandler( XMLReader reader )
    {
        this( null, reader );
    }

    public PlacesXmlHandler( DefaultHandler parent, XMLReader reader )
    {
        places = new ArrayList<Place>();
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
     * @throws SAXException
     */
    @Override
    public void startElement( String uri, String localName, String qName, Attributes attributes ) throws SAXException
    {
        buffer.setLength( 0 );
        if ( localName.equals( "result" ) )
        {
            placeHandler = new PlaceXmlHandler( this, reader, places );
            reader.setContentHandler( placeHandler );
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
        else if ( localName.equals( "PlaceSearchResponse" ) )
        {
            if ( parent != null )
            {
                reader.setContentHandler( parent );
            }
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
    public HandlerReturnObject<ArrayList<Place>> retrieveHandlerObject()
    {
        return new HandlerReturnObject<ArrayList<Place>>( isValid, message, places );
    }
}
