package cn.ingenic.gabriel.filmwind;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.widget.Checkable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.BaseAdapter;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;

import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class GabrielAdapter extends BaseAdapter implements View.OnClickListener {
    private int[] mTo;
    private String[] mFrom;
    private Handler mHandler;
    private List<? extends Map<String, ?>> mData;
    private int mResource;
    private LayoutInflater mInflater;
    private final WeakHashMap<View, View[]> mHolders = new WeakHashMap<View, View[]>();

    public GabrielAdapter(Context context, Handler handler, List<? extends Map<String, ?>> data,
            int resource, String[] from, int[] to) {
    	mHandler = handler; mData = data;
        mResource = resource;
        mFrom = from; mTo = to;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() { return mData.size(); }

    public Object getItem(int position) { return mData.get(position); }

    public long getItemId(int position) { return position; }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        return createViewFromResource(position, convertView, parent, mResource);
    }

    private View createViewFromResource(int position, View convertView, ViewGroup parent, int resource) {
        View v;
        if (convertView == null) {
            v = mInflater.inflate(resource, parent, false);
            final int[] to = mTo;
            final int count = to.length;
            final View[] holder = new View[count];
            for (int i = 0; i < count; i++) {
                holder[i] = v.findViewById(to[i]);
            }
            mHolders.put(v, holder);
        } else {
            v = convertView;
        }
        bindView(position, v);
        return v;
    }

    private void bindView(int position, View view) {
        final Map<String, ?> dataSet = mData.get(position);
        if (dataSet == null) return;
        final View[] holder = mHolders.get(view);
        final String[] from = mFrom;
        final int[] to = mTo;
        final int count = to.length;

        for (int i = 0; i < count; i++) {
            final View v = holder[i];
            if (v != null) {
                final Object data = dataSet.get(from[i]);
                String text = data == null ? "" : data.toString();
                if (text == null) text = "";

                if (v instanceof Checkable) {
                    if (data instanceof Boolean) {
                        ((Checkable) v).setChecked((Boolean) data);
                    } else {
                        throw new IllegalStateException(v.getClass().getName() + " should be Boolean, not " + data.getClass());
                    }
                } else if (v instanceof TextView) {
                    // Note: keep the instanceof TextView check at the bottom of these
                    // ifs since a lot of views are TextViews (e.g. CheckBoxes).
                    setViewText((TextView) v, text);
                } else if(v instanceof ImageButton) {
                	if (data instanceof Integer) {
                		setImageButton((ImageButton) v, (Integer) data, position);
                	} else {
                		setImageButton((ImageButton) v, text, position);
                	} 
                } else if (v instanceof ImageView) {
                    if (data instanceof Integer) {
                        setViewImage((ImageView) v, (Integer) data);                            
                    } else {
                        setViewImage((ImageView) v, text);
                    }
                } else {
                    throw new IllegalStateException(v.getClass().getName() + " is not a " +
                            " view that can be bounds by this GabrielAdapter");
                }
            }
        }
    }

    private void setImageButton(ImageButton v, int id, int position) {
    	v.setImageResource(id);
    	v.setOnClickListener(this);
    	v.setTag(position);
    }
    public void setImageButton(ImageView v, String value, int position) {
        try {
            v.setImageResource(Integer.parseInt(value));
        } catch (NumberFormatException nfe) {
            v.setImageURI(Uri.parse(value));
        } finally {
        	v.setOnClickListener(this);
        	v.setTag(position);
        }
    }

    public void setViewImage(ImageView v, int value) {
        v.setImageResource(value);
    }

    public void setViewImage(ImageView v, String value) {
        try {
            v.setImageResource(Integer.parseInt(value));
        } catch (NumberFormatException nfe) {
            v.setImageURI(Uri.parse(value));
        }
    }

    public void setViewText(TextView v, String text) {
        v.setText(text);
    }

	public void onClick(View view) {
		// TODO Auto-generated method stub
		if(DBG) Log.d(TAG, "onClick: tag: " + view.getTag());
		Message msg = mHandler.obtainMessage();
		msg.what = Parameters.ADAPTER_BUTTON_CLICK_MSG;
		msg.arg1 = (Integer) view.getTag();
		mHandler.sendMessage(msg);
	}
	
	private static final String TAG = "Wind";
	private boolean DBG = Parameters.DEBUG;
}
