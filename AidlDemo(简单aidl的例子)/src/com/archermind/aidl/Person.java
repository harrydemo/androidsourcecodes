package com.archermind.aidl;

import android.os.Parcel;
import android.os.Parcelable;

public class Person implements Parcelable {
	private String name;
	private String descrip;
	private int sex;

	//必须提供一个名为CREATOR的static final属性 该属性需要实现android.os.Parcelable.Creator<T>接口  
	public static final Parcelable.Creator<Person> CREATOR = new Parcelable.Creator<Person>() {

		@Override
		public Person createFromParcel(Parcel source) {
			return new Person(source);
		}

		@Override
		public Person[] newArray(int size) {
			return new Person[size];
		}
	};

	private Person(Parcel source) {
		readFromParcel(source);
	}

	public Person() {

	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(name);
		dest.writeString(descrip);
		dest.writeInt(sex);
	}

	public void readFromParcel(Parcel source) {
		name = source.readString();
		descrip = source.readString();
		sex = source.readInt();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescrip() {
		return descrip;
	}

	public void setDescrip(String descrip) {
		this.descrip = descrip;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}
}
