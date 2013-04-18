package com.gatech.spark.helper;

import com.gatech.spark.handler.PlaceDetailsXmlHandler;
import com.gatech.spark.handler.PlacesXmlHandler;
import com.gatech.spark.model.Place;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.StringReader;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: tyler
 * Date: 4/3/13
 * Time: 8:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class SaxParser {

    /**
     * @return
     *
     * @throws ParserConfigurationException
     * @throws SAXException
     */
    private XMLReader initializeReader() throws ParserConfigurationException, SAXException
    {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        // create a parser
        SAXParser parser = factory.newSAXParser();
        // create the reader (scanner)
        XMLReader xmlreader = parser.getXMLReader();
        return xmlreader;
    }


    /**
     * @param xml
     *
     * @return
     */
    public HandlerReturnObject<ArrayList<Place>> parsePlacesXmlResponse( String xml )
    {
        try
        {
            XMLReader xmlreader = initializeReader();
            PlacesXmlHandler handler = new PlacesXmlHandler(xmlreader);

            // assign our handler
            xmlreader.setContentHandler( handler );
            // perform the synchronous parse
            xmlreader.parse( new InputSource( new StringReader( xml ) ) );
            return handler.retrieveHandlerObject();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            return new HandlerReturnObject<ArrayList<Place>>( false, "Error Parsing XML.", new ArrayList<Place>() );
        }
    }

    /**
     * @param xml
     *
     * @return
     */
    public HandlerReturnObject<Place> parseDetailedPlaceXmlResponse( String xml )
    {
        try
        {
            XMLReader xmlreader = initializeReader();
            PlaceDetailsXmlHandler handler = new PlaceDetailsXmlHandler(xmlreader);

            // assign our handler
            xmlreader.setContentHandler( handler );
            // perform the synchronous parse
            xmlreader.parse( new InputSource( new StringReader( xml ) ) );
            return handler.retrieveHandlerObject();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            return new HandlerReturnObject<Place>( false, "Error Parsing XML.", new Place() );
        }
    }
}
