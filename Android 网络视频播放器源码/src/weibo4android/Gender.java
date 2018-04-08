// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   Gender.java

package weibo4android;

public final class Gender
//extends Enum
{

	private static final Gender ENUM$VALUES[];
	public static final Gender FEMALE;
	public static final Gender MALE;

	private Gender(String s, int i)
	{
		//super(s, i);
	}

	public static String valueOf(Gender gender)
	{
		String s = "";
		/*if (gender.ordinal() == 0)
			s = "m";
		else
			s = "f";*/
		return s;
	}

	public static Gender valueOf(String s)
	{
		//return (Gender)Enum.valueOf(weibo4android/Gender, s);
		return null;
	}

	public static Gender[] values()
	{
		Gender agender[] = ENUM$VALUES;
		int i = agender.length;
		Gender agender1[] = new Gender[i];
		System.arraycopy(agender, 0, agender1, 0, i);
		return agender1;
	}

	static 
	{
		MALE = new Gender("MALE", 0);
		FEMALE = new Gender("FEMALE", 1);
		Gender agender[] = new Gender[2];
		Gender gender = MALE;
		agender[0] = gender;
		Gender gender1 = FEMALE;
		agender[1] = gender1;
		ENUM$VALUES = agender;
	}
}
