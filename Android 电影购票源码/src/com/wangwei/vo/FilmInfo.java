package com.wangwei.vo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author 作者 E-mail:
 * @version 创建时间：Aug 11, 2010 3:29:13 PM 类说明
 */
public class FilmInfo implements Parcelable {
    private String filmName;
    private String director;
    private String starring;
    private String produce;
    private String summary;
    private String actionTime;
    private String imgUrl;

    public static final Parcelable.Creator<FilmInfo> CREATOR = new Creator<FilmInfo>() {

        @SuppressWarnings("unchecked")
        public FilmInfo createFromParcel(Parcel source) {
            FilmInfo filmInfo = new FilmInfo();
            filmInfo.filmName = source.readString();
            filmInfo.director = source.readString();
            filmInfo.starring = source.readString();
            filmInfo.produce = source.readString();
            filmInfo.summary = source.readString();
            filmInfo.actionTime = source.readString();
            filmInfo.imgUrl = source.readString();
            return filmInfo;
        }

        public FilmInfo[] newArray(int size) {
            return new FilmInfo[size];
        }

    };

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        // 按顺序写 否则数据错位
        dest.writeString(filmName);
        dest.writeString(director);
        dest.writeString(starring);
        dest.writeString(produce);
        dest.writeString(summary);
        dest.writeString(actionTime);
        dest.writeString(imgUrl);
    }


    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getStarring() {
        return starring;
    }

    public void setStarring(String starring) {
        this.starring = starring;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getFilmName() {
        return filmName;
    }

    public void setFilmName(String filmName) {
        this.filmName = filmName;
    }

    public String getProduce() {
        return produce;
    }

    public void setProduce(String produce) {
        this.produce = produce;
    }

    public String getActionTime() {
        return actionTime;
    }

    public void setActionTime(String actionTime) {
        this.actionTime = actionTime;
    }

}
