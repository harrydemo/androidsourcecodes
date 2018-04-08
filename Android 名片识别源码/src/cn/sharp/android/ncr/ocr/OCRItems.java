package cn.sharp.android.ncr.ocr;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.logging.Logger;

public class OCRItems implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1202859084016129792L;
	private final static String TAG = "OCRItems";
	// private final Logger log = Logger.getLogger(TAG);
	/**
	 * The C OCR engine uses GB2312 character set
	 */
	private final static String INPUT_ENCODE = "EUC_CN";
	/**
	 * The max number of fields that OCR engine can recognize
	 */
	public final static int TOTAL_FIELD = 20;

	public final static int ID_FAMILY_NAME = 0;
	public final static int ID_FAMILY_KANA = 1;
	public final static int ID_GIVEN_NAME = 2;
	public final static int ID_NAME = 3;
	public final static int ID_TELEPHONE = 4;
	public final static int ID_CELLPHONE = 5;
	public final static int ID_PHS = 6;
	public final static int ID_EMAIL = 7;
	public final static int ID_ORGANIZATION = 8;
	public final static int ID_DEPARTMENT = 9;
	public final static int ID_TITLE = 10;
	public final static int ID_POSTCODE = 11;
	public final static int ID_ADDRESS = 12;
	public final static int ID_ADR_REGION = 13;
	public final static int ID_ADR_LOCALITY = 14;
	public final static int ID_ADR_STREET = 15;
	public final static int ID_ADR_INFO = 16;
	public final static int ID_URL = 17;
	public final static int ID_FAX = 18;
	public final static int ID_OTHER = 19;

	public final static String TITLE_FAMILY_NAME = "Family Name";
	public final static String TITLE_FAMILY_KANA = "Family Kana";
	public final static String TITLE_GIVEN_NAME = "Given Name";
	public final static String TITLE_NAME = "Name";
	public final static String TITLE_TELEPHONE = "Telephone";
	public final static String TITLE_CELLPHONE = "Cellphone";
	public final static String TITLE_PHS = "Phs";
	public final static String TITLE_EMAIL = "Email";
	public final static String TITLE_ORGANIZATION = "Organization";
	public final static String TITLE_DEPARTMENT = "Department";
	public final static String TITLE_TITLE = "Title";
	public final static String TITLE_POSTCODE = "Postcode";
	public final static String TITLE_ADDRESS = "Address";
	public final static String TITLE_ADR_REGION = "Adr Region";
	public final static String TITLE_ADR_LOCALITY = "Adr Locality";
	public final static String TITLE_ADR_STREET = "Adr Street";
	public final static String TITLE_ADR_INFO = "Adr Info";
	public final static String TITLE_URL = "Url";
	public final static String TITLE_FAX = "Fax";
	public final static String TITLE_OTHER = "Other";

	public String[] familyName;
	public String[] familyKana;
	public String[] givenName;
	public String[] name;
	public String[] telephone;
	public String[] cellphone;
	public String[] phs;
	public String[] email;
	public String[] organization;
	public String[] department;
	public String[] title;
	public String[] postcode;
	public String[] address;
	public String[] adrRegion;
	public String[] adrLocality;
	public String[] adrStreet;
	public String[] adrInfo;
	public String[] url;
	public String[] fax;
	public String[] other;

	/**
	 * get item values of the specified id
	 * 
	 * @param id
	 *            the item id between 0 and TOTAL_FIELD, eg. FAMILY_NAME,
	 *            FAMILY_KANA...
	 * @return a String array if the item value is not empty, otherwise null. If
	 *         the id do not fall into the range(0 to TOTAL_FIELD include), an
	 *         IllegalArgumentException exception will be thrown out
	 */
	public String[] getItemValue(int id) {
		switch (id) {
		case ID_FAMILY_NAME:
			return familyName;
		case ID_FAMILY_KANA:
			return familyKana;
		case ID_GIVEN_NAME:
			return givenName;
		case ID_NAME:
			return name;
		case ID_TELEPHONE:
			return telephone;
		case ID_CELLPHONE:
			return cellphone;
		case ID_PHS:
			return phs;
		case ID_EMAIL:
			return email;
		case ID_ORGANIZATION:
			return organization;
		case ID_DEPARTMENT:
			return department;
		case ID_TITLE:
			return title;
		case ID_POSTCODE:
			return postcode;
		case ID_ADDRESS:
			return address;
		case ID_ADR_REGION:
			return adrRegion;
		case ID_ADR_LOCALITY:
			return adrLocality;
		case ID_ADR_STREET:
			return adrStreet;
		case ID_ADR_INFO:
			return adrInfo;
		case ID_URL:
			return url;
		case ID_FAX:
			return fax;
		case ID_OTHER:
			return other;
		default:
			throw new IllegalArgumentException("invalid id " + id
					+ ", is too small or too large");
		}
	}

	public String getItemTitle(int id) {
		switch (id) {
		case ID_FAMILY_NAME:
			return TITLE_FAMILY_NAME;
		case ID_FAMILY_KANA:
			return TITLE_FAMILY_KANA;
		case ID_GIVEN_NAME:
			return TITLE_GIVEN_NAME;
		case ID_NAME:
			return TITLE_NAME;
		case ID_TELEPHONE:
			return TITLE_TELEPHONE;
		case ID_CELLPHONE:
			return TITLE_CELLPHONE;
		case ID_PHS:
			return TITLE_PHS;
		case ID_EMAIL:
			return TITLE_EMAIL;
		case ID_ORGANIZATION:
			return TITLE_ORGANIZATION;
		case ID_DEPARTMENT:
			return TITLE_DEPARTMENT;
		case ID_TITLE:
			return TITLE_TITLE;
		case ID_POSTCODE:
			return TITLE_POSTCODE;
		case ID_ADDRESS:
			return TITLE_ADDRESS;
		case ID_ADR_REGION:
			return TITLE_ADR_REGION;
		case ID_ADR_LOCALITY:
			return TITLE_ADR_LOCALITY;
		case ID_ADR_STREET:
			return TITLE_ADR_STREET;
		case ID_ADR_INFO:
			return TITLE_ADR_INFO;
		case ID_URL:
			return TITLE_URL;
		case ID_FAX:
			return TITLE_FAX;
		case ID_OTHER:
			return TITLE_OTHER;
		default:
			throw new IllegalArgumentException("invalid id " + id
					+ ", is too small or too large");
		}
	}

	public OCRItems() {
	}

	public OCRItems(NativeOCRItems nativeItems, int[] fieldLength) {
		if (fieldLength == null || fieldLength.length != TOTAL_FIELD) {
			throw new IllegalArgumentException(
					"size of fieldLength array must be 20");
		}
		if (nativeItems == null) {
			throw new IllegalArgumentException(
					"native items is not allowed null");
		}
		familyName = getUTF8String(nativeItems.familyName, fieldLength[0]);
		familyKana = getUTF8String(nativeItems.familyKana, fieldLength[1]);
		givenName = getUTF8String(nativeItems.givenName, fieldLength[2]);
		name = getUTF8String(nativeItems.name, fieldLength[3]);
		telephone = getUTF8String(nativeItems.telephone, fieldLength[4]);
		cellphone = getUTF8String(nativeItems.cellphone, fieldLength[5]);
		phs = getUTF8String(nativeItems.phs, fieldLength[6]);
		email = getUTF8String(nativeItems.email, fieldLength[7]);
		organization = getUTF8String(nativeItems.organization, fieldLength[8]);
		department = getUTF8String(nativeItems.department, fieldLength[9]);
		title = getUTF8String(nativeItems.title, fieldLength[10]);
		postcode = getUTF8String(nativeItems.postcode, fieldLength[11]);
		address = getUTF8String(nativeItems.address, fieldLength[12]);
		adrRegion = getUTF8String(nativeItems.adrRegion, fieldLength[13]);
		adrLocality = getUTF8String(nativeItems.adrLocality, fieldLength[14]);
		adrStreet = getUTF8String(nativeItems.adrStreet, fieldLength[15]);
		adrInfo = getUTF8String(nativeItems.adrInfo, fieldLength[16]);
		url = getUTF8String(nativeItems.url, fieldLength[17]);
		fax = getUTF8String(nativeItems.fax, fieldLength[18]);
		other = getUTF8String(nativeItems.other, fieldLength[19]);
	}

	public OCRItems(OCRItems old) {
		if (old != null) {
			if (old.familyName != null) {
				familyName = new String[old.familyName.length];
				copyItem(familyName, old.familyName);
			}
			if (old.familyKana != null) {
				familyKana = new String[old.familyKana.length];
				copyItem(familyKana, old.familyKana);
			}
			if (old.givenName != null) {
				givenName = new String[old.givenName.length];
				copyItem(givenName, old.givenName);
			}
			if (old.name != null) {
				name = new String[old.name.length];
				copyItem(name, old.name);
			}
			// familyKana = old.familyKana;
			// givenName = old.givenName;
			// name = old.name;
			telephone = old.telephone;
			cellphone = old.cellphone;
			phs = old.phs;
			email = old.email;
			organization = old.organization;
			department = old.department;
			title = old.title;
			postcode = old.postcode;
			address = old.address;
			adrRegion = old.adrRegion;
			adrLocality = old.adrLocality;
			adrStreet = old.adrStreet;
			adrInfo = old.adrInfo;
			url = old.url;
			fax = old.fax;
			other = old.other;
		}
	}

	private void copyItem(String[] newItem, String[] oldItem) {
		for (int i = 0; i < oldItem.length; i++) {
			newItem[i] = new String(oldItem[i]);
		}
	}

	/**
	 * 返回的是一个数组
	 * 
	 * @param src
	 *            src  一个二维数组
	 * @param length
	 *            长度
	 * @return target 以为数组，或者null
	 */
	private String[] getUTF8String(byte[][] src, int length) {
		if (src == null) {
			// log.warning("src is not allowed null");
			return null;
		} else if (length <= 0) {
			// log.warning("length can only be positive number");
			return null;
		} else if (src.length < length) {
			// log.warning("the size of src " + src.length
			// + " does not match the size of target " + length);
			return null;
		}
		String[] target = new String[length];
		for (int i = 0; i < target.length; i++) {
			try {
				target[i] = new String(src[i], INPUT_ENCODE);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				// log.warning(e.getMessage());
			}
		}
		return target;
	}
}
