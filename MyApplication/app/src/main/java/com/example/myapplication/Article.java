package com.example.myapplication;

import android.graphics.Bitmap;

public class Article {
    // L'image référencée par l'attribut url du tag <enclosure>
    private Bitmap _image = null ;
    private String _imageURL = null ;
    // Le titre, la description et la date extraits du flux RSS
    private String _title = new String();
    private String _description = new String();
    private String _imageDescription = new String();
    private String _date = new String();


    Article(Bitmap image, String imageURL, StringBuffer title, StringBuffer description, StringBuffer imageDescription, StringBuffer date){
        _date = date.toString();
        _image = image;
        _imageURL = imageURL;
        _title = title.toString();
        _date = date.toString();
        _description = description.toString();
        _imageDescription = imageDescription.toString();
    }

    public Bitmap get_image() {
        return _image;
    }

    public String get_imageURL() {
        return _imageURL;
    }

    public String get_date() {
        return _date;
    }

    public String get_description() {
        return _description;
    }

    public String get_imageDescription() {
        return _imageDescription;
    }

    public String get_title() {
        return _title;
    }

    public String toString() {
        return "Article: " + _title;
    }
}
