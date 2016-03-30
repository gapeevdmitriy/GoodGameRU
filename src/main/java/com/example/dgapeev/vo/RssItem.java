package com.example.dgapeev.vo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class RssItem implements Parcelable {
    private String url;
    private String title;
    private Date datePub;
    private String description;


    public RssItem() {
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getDatePub() {
        return datePub;
    }

    public void setDatePub(Date datePub) {
        this.datePub = datePub;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
        dest.writeString(description);
        dest.writeString(title);
        dest.writeLong(datePub.getTime());

    }

    public static final Parcelable.Creator<RssItem> CREATOR = new ClassLoaderCreator<RssItem>() {
        @Override
        public RssItem createFromParcel(Parcel source, ClassLoader loader) {
            return new RssItem(source);
        }

        @Override
        public RssItem createFromParcel(Parcel source) {
            return new RssItem(source);
        }

        @Override
        public RssItem[] newArray(int size) {
            return new RssItem[size];
        }
    };

    private RssItem(Parcel source) {
        url = source.readString();
        description = source.readString();
        title = source.readString();
        datePub = new Date(source.readLong());
    }

}
