package dk.andsen.RecordEditor;

import android.content.Context;
import android.text.InputType;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import dk.andsen.RecordEditor.types.TableField;
import dk.andsen.asqlitemanager.R;
import dk.andsen.utils.Utils;

/**
 * 
 * @author Andsen Due to the Android input interface Boolean fields can only be
 *         true / false (not null)
 */
public class RecordEditorBuilder {
	static final int DATE_DIALOG_ID = 0;
	static final int TIME_DIALOG_ID = 1;
	private ScrollView sv;
	private TableField[] _fields;
	private Context _cont;

	/**
	 * The base of id's of the edit fields linked to each of the fields
	 */
	public static final int idBase = 1000;
	/**
	 * The base of id's of the LinearLayout that holds info about each record
	 */
	public static final int lineIdBase = 2000;
	/**
	 * If this is set to true empty strings are treated as null
	 */
	private boolean treatEmptyFieldsAsNull = true;
	private int fieldNameWidth = 100;

	/**
	 * 
	 * @param fields
	 * @param cont
	 */
	public RecordEditorBuilder(TableField[] fields, Context cont) {
		_cont = cont;
		_fields = fields;
		sv = new ScrollView(cont);
		sv.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT));
		sv.setPadding(5, 5, 5, 5);
		LinearLayout lmain = new LinearLayout(cont);
		lmain.setOrientation(LinearLayout.VERTICAL);
		lmain.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));
		// Add a linearLayout with name and field to edit for each of the fields
		for (int i = 0; i < fields.length; i++) {
			Utils.logD("Field: " + fields[i].getName());
			if (fields[i].isUpdateable()) {
				LinearLayout ll = new LinearLayout(cont);
				ll.setOrientation(LinearLayout.HORIZONTAL);
				ll.setLayoutParams(new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.FILL_PARENT,
						LinearLayout.LayoutParams.WRAP_CONTENT));
				// Id added to be able to find the line in the layout
				ll.setId(lineIdBase + i);
				TextView tv = new TextView(cont);
				tv.setLayoutParams((new ViewGroup.LayoutParams(
						ViewGroup.LayoutParams.WRAP_CONTENT,
						ViewGroup.LayoutParams.WRAP_CONTENT)));
				if (fields[i].getDisplayName() == null)
					tv.setText(fields[i].getName());
				else
					tv.setText(fields[i].getDisplayName());
				tv.setWidth(fieldNameWidth);
				tv.setPadding(5, 0, 5, 0);
				ll.addView(tv);
				int id = idBase + i;
				/*
				 * Add edit fields to match the fields type
				 * 
				 * TODO in case of referential constraints show data in spinner (needs
				 * database in arguments to do that)
				 */
				switch (fields[i].getType()) {
				case (TableField.TYPE_DATE):
					// change to button with DatePicker
					//DatePicker dp = new DatePicker(cont);
					//dp.setLayoutParams((new LayoutParams(LayoutParams.FILL_PARENT,
					//		LayoutParams.WRAP_CONTENT)));
					//dp.set
					//dp.setTag(fields[i].getValue());
					EditText etd = new EditText(cont);
					etd.setLayoutParams((new LayoutParams(LayoutParams.FILL_PARENT,
							LayoutParams.WRAP_CONTENT)));
					etd.setText(fields[i].getValue());
					etd.setId(id);
					etd.setInputType(InputType.TYPE_CLASS_DATETIME
							| InputType.TYPE_DATETIME_VARIATION_DATE);
					ll.addView(etd);
					break;
				case (TableField.TYPE_DATETIME):
					EditText etdt = new EditText(cont);
					etdt.setLayoutParams((new LayoutParams(LayoutParams.FILL_PARENT,
							LayoutParams.WRAP_CONTENT)));
					etdt.setText(fields[i].getValue());
					etdt.setInputType(InputType.TYPE_CLASS_DATETIME
							| InputType.TYPE_DATETIME_VARIATION_NORMAL);
					etdt.setId(id);
					ll.addView(etdt);
					break;
				case (TableField.TYPE_FLOAT):
					EditText etf = new EditText(cont);
					etf.setLayoutParams((new LayoutParams(LayoutParams.FILL_PARENT,
							LayoutParams.WRAP_CONTENT)));
					etf.setText(fields[i].getValue());
					etf.setInputType(InputType.TYPE_CLASS_NUMBER
							| InputType.TYPE_NUMBER_FLAG_SIGNED
							| InputType.TYPE_NUMBER_FLAG_DECIMAL);
					etf.setId(id);
					ll.addView(etf);
					break;
				case (TableField.TYPE_INTEGER):
					EditText eti = new EditText(cont);
					eti.setLayoutParams((new LayoutParams(LayoutParams.FILL_PARENT,
							LayoutParams.WRAP_CONTENT)));
					eti.setText(fields[i].getValue());
					eti.setInputType(InputType.TYPE_CLASS_NUMBER
							| InputType.TYPE_NUMBER_FLAG_SIGNED);
					eti.setId(id);
					ll.addView(eti);
					break;
				case (TableField.TYPE_TIME):
					//TODO change to time picker
					EditText ett = new EditText(cont);
					ett.setLayoutParams((new LayoutParams(LayoutParams.FILL_PARENT,
							LayoutParams.WRAP_CONTENT)));
					ett.setText(fields[i].getValue());
					ett.setInputType(InputType.TYPE_CLASS_DATETIME
							| InputType.TYPE_DATETIME_VARIATION_TIME);
					ett.setId(id);
					ll.addView(ett);
					break;
				case (TableField.TYPE_BOOLEAN):
					CheckBox etb = new CheckBox(cont);
					etb.setLayoutParams((new LayoutParams(LayoutParams.FILL_PARENT,
							LayoutParams.WRAP_CONTENT)));
					etb.setChecked((fields[i].getValue() == null) ? false : int2boolean(fields[i].getValue()));
					etb.setId(id);
					ll.addView(etb);
					break;
				case (TableField.TYPE_PHONENO):
					EditText etp = new EditText(cont);
					etp.setLayoutParams((new LayoutParams(LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT)));
					etp.setText(fields[i].getValue());
					etp.setHint(fields[i].getHint());
					etp.setId(id);
					etp.setInputType(InputType.TYPE_CLASS_PHONE);
					ll.addView(etp);
					break;
				default: // treat the rest as Strings
					EditText ets = new EditText(cont);
					ets.setLayoutParams((new LayoutParams(LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT)));
					ets.setText(fields[i].getValue());
					ets.setHint(fields[i].getHint());
					ets.setId(id);
					ll.addView(ets);
					break;
				}
				lmain.addView(ll);
			}
		}
		//TODO add OK and cancel buttons here to have them on the scroll view??
		sv.addView(lmain);
	}

	/**
	 * Convert a SQLite 0 / 1 to their boolean values. All but 1 are
	 * treated as false
	 * @param val
	 * @return
	 */
	private boolean int2boolean(String val) {
		if (val.equals("1")) 
			return true;
		return false;
	}

	/**
	 * Build a ScrollView ready to edit a record. The input type are defined by
	 * the data type.
	 * 
	 * @return a ScrollView to edit all the fields of TableField list
	 */
	public ScrollView getScrollView() {
		return sv;
	}

	/**
	 * Check the edited data against the fields definition
	 * 
	 * @param sv
	 *          the ScrollView created by getScrollView containing the edited data
	 * @return a String with validation errors otherwise null
	 */
	public String checkInput(ScrollView sv) {
		String errorMsg = null;
		TableField[] fields = getEditedData(sv);
		for (int i = 0; i < _fields.length; i++) {
			// check for empty / null not null fields
			if (fields[i].getNotNull() != null && fields[i].getNotNull())
				if ((fields[i].getValue() == null) ||
						(fields[i].getValue().equals("") && treatEmptyFieldsAsNull)) {
					if (errorMsg != null)
						errorMsg += "\n" + _fields[i].getDisplayName() + " "
							+ _cont.getText(R.string.MustNotBeEmpty);
					else
						errorMsg = _fields[i].getDisplayName() + " "
							+ _cont.getText(R.string.MustNotBeEmpty);
				}
			// TODO should also check input data types(?) and constraints

		}
		// errorMsg =
		// "No validation yet :-(\nLong errormessages\ndoes works and can be\nused to give a report about\nbroken constraints";
		return errorMsg;
	}

	/**
	 * @param sv
	 *          the ScrollView created by getScrollView after editing
	 * @return a list of TableFields with the value field updated to reflect the
	 *         edited values form the ScrollView
	 */
	public TableField[] getEditedData(ScrollView sv) {
		String res = null;
		for (int i = 0; i < _fields.length; i++) {
			if (_fields[i].isUpdateable()) {
				switch (_fields[i].getType()) {
				case TableField.TYPE_BOOLEAN:
					CheckBox cb = (CheckBox) sv.findViewById(idBase + i);
					Boolean val = cb.isChecked();
					res = val.toString();
					break;
					// Now treated as text
//				case TableField.TYPE_DATE:
//					// TODO Move data from View to TableField list
//					break;
//				case TableField.TYPE_DATETIME:
//					// TODO Move data from View to TableField list
//					break;
//				case TableField.TYPE_TIME:
//					// TODO Move data from View to TableField list
//					break;
				case TableField.TYPE_FLOAT:
					TextView tvFloat = (TextView) sv.findViewById(idBase + i);
					res = tvFloat.getEditableText().toString();
					//if (treatEmptyFieldsAsNull)
						if (res.equals(""))
							res = null;
					break;
				case TableField.TYPE_INTEGER:
					TextView tvInteger = (TextView) sv.findViewById(idBase + i);
					res = tvInteger.getEditableText().toString();
					if (res.equals(""))
						res = null;
					break;
				default: // treat is as a String
					TextView tvString = (TextView) sv.findViewById(idBase + i);
					res = tvString.getEditableText().toString();
					if (treatEmptyFieldsAsNull)
						if (res.equals(""))
							res = null;
					break;
				}
				_fields[i].setValue(res);
			}
		}
		return _fields;
	}

	/**
	 * Set the with of the field name in pixels on the edit dialog
	 * 
	 * @param fieldNameWidth
	 */
	public void setFieldNameWidth(int fieldNameWidth) {
		this.fieldNameWidth = fieldNameWidth;
	}

	/**
	 * @return
	 */
	public int getFieldNameWidth() {
		return fieldNameWidth;
	}

	/**
	 * If this is set to true empty strings are treated as null
	 * 
	 * @param treatEmptyFieldsAsNull
	 */
	public void setTreatEmptyFieldsAsNull(boolean treatEmptyFieldsAsNull) {
		this.treatEmptyFieldsAsNull = treatEmptyFieldsAsNull;
	}

	/**
	 * @return the treatEmptyStringsAsNull
	 */
	public boolean isTreatEmptyFieldsAsNull() {
		return treatEmptyFieldsAsNull;
	}

}
