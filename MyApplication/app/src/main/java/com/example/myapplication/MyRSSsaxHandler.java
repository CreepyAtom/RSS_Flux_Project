package com.example.myapplication;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class MyRSSsaxHandler extends DefaultHandler {
    private String url = null ;// l'URL du flux RSS à parser
    // Ensemble de drapeau permettant de savoir où en est le parseur dans le flux XML
    private boolean _inTitle = false ;
    private boolean _inDescription = false ;
    private boolean _inItem = false ;
    private boolean _inDate = false ;
    private boolean _inImageDescription = false;
    private boolean _gotTitle = false;
    private boolean _gotDescription = false;
    private boolean _gotImageDescription = false;
    private boolean _gotDate = false;
    // L'image référencée par l'attribut url du tag <enclosure>
    private Bitmap _image = null ;
    private String _imageURL = null ;
    // Le titre, la description et la date extraits du flux RSS
    private StringBuffer _title = new StringBuffer();
    private StringBuffer _description = new StringBuffer();
    private StringBuffer _imageDescription = new StringBuffer();
    private StringBuffer _date = new StringBuffer();
    private int _numItem = 0; // Le numéro de l'item à extraire du flux RSS
    private int _numItemMax = - 1; // Le nombre total d’items dans le flux RSS
    private static ArrayList<Article> articleList = new ArrayList<Article>();
    private int listIndex = 0;



    public void setUrl(String url){
        this.url= url;
    }



    public void processFeed(){
        Log.d("processFeed", " en cours");
        try {
            _numItem = 0; // A modifier pour visualiser un autre item
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            XMLReader reader = parser.getXMLReader();
            reader.setContentHandler(this);
            InputStream inputStream = new URL(url).openStream();
            reader.parse(new InputSource(inputStream));
            _numItemMax = _numItem;
        }catch(Exception e) {
            Log.e("smb116rssview", "processFeed Exception " + e.getMessage());
        }
    }

    public Bitmap get_image(){
        return _image;
    }

    private Bitmap getBitmap(String imageURL) {
        // récupération d'une image sous format Bitmap à partir d'une url
        try {
            URL url = new URL(imageURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            //  connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Log.d("#### GET BITMAP: ", String.valueOf(url));
            return BitmapFactory.decodeStream(input);
            } catch (IOException e) {
            Log.d("#### GET BITMAP: ", "SHOULDNT BE HERE");
            return null;
            }
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        Log.d("#### START ELEMENT: ", localName);
        if (localName.equalsIgnoreCase("item")){
            _inItem = true;
        }

        if (qName.equalsIgnoreCase("media:content")){
            Log.d("#### IN MEDIA CONTENT: ", "");
            _imageURL = attributes.getValue("url");
            _image = getBitmap(_imageURL);
        }
        if (localName.equalsIgnoreCase("title")){
            _inTitle = true;
        }
        if (qName.equalsIgnoreCase("description")){
            _inDescription = true;
        }
        if (qName.equalsIgnoreCase("media:description")){
            _inImageDescription = true;
        }
        if (localName.equalsIgnoreCase("pubDate")){
            _inDate = true;
        }
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (localName.equalsIgnoreCase("item")){
            _inItem = false;
            Article article = new Article(_image,_imageURL,_title,_description,_imageDescription,_date);
            articleList.add(article);
            listIndex++;
            Log.d("Art: ", article.toString());
        }
    }

    public void characters(char ch[], int start, int length) {
        String chars = new String(ch).substring(start, start + length);
        if (_inItem) {
            if (_inTitle) {
                _title.setLength(0);
                _title.append(chars);
                Log.d("characters : title ", " title  =" + _title);
                _inTitle = false;
                _inDescription = false;
                _inImageDescription = false;
                _inDate = false;
                _gotTitle = true;
            }
            if (_inDescription) {
                _description.setLength(0);
                _description.append(chars);
                Log.d("characters : desc ", " desc  =" + _description);
                _inDescription = false;
                _inImageDescription = false;
                _inTitle = false;
                _inDate = false;
                _gotDescription = true;
            }
            if (_inImageDescription) {
                _imageDescription.setLength(0);
                _imageDescription.append(chars);
                Log.d("characters : desc ", " desc  =" + _imageDescription);
                _inDescription = false;
                _inImageDescription = false;
                _inTitle = false;
                _inDate = false;
                _gotImageDescription = true;
            }
            if (_inDate) {
                _date.setLength(0);
                _date.append(chars);
                Log.d("characters : date ", " date  =" + _date);
                _inDate = false;
                _inTitle = false;
                _inDescription = false;
                _inImageDescription = false;
                _gotDate = true;
            }
        }
    }


    public StringBuffer get_title() {
        return _title;
    }

    public StringBuffer get_date() {
        return _date;
    }

    public StringBuffer get_desc() {
        return _description;
    }

    public StringBuffer get_imageDescription(){
        return _imageDescription;
    }

    public static ArrayList<Article> getArticleList() { return articleList; }


}
