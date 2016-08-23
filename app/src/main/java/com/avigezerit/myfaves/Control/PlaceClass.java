package com.avigezerit.myfaves.Control;

 /* * * * * * * * * * * * * LOCATION POJO * * * * * * * * * * * * */

public class PlaceClass {

    private int _id;
    private String mName;
    private double mLati;
    private double mLongi;
    private String mAddress;
    private int mImage;
    private boolean isFav;

    public PlaceClass() {
    }

    public PlaceClass(String mName, double mLati, double mLongi) {
        this.mName = mName;
        this.mLati = mLati;
        this.mLongi = mLongi;
    }

    public boolean isFav() {
        return isFav;
    }

    public void setFav(boolean fav) {
        isFav = fav;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    public int getId() {
        return _id;
    }

    public void setId(int _id) {
        this._id = _id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public double getLati() {
        return mLati;
    }

    public double getLongi() {
        return mLongi;
    }

    public int getImage() {
        return mImage;
    }

    public void setImage(int mImage) {
        this.mImage = mImage;
    }

    public void setLati(double mLati) {
        this.mLati = mLati;
    }

    public void setLongi(double mLongi) {
        this.mLongi = mLongi;
    }
}
