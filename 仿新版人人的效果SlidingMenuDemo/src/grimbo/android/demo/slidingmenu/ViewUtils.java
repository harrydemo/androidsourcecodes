package grimbo.android.demo.slidingmenu;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Utility methods for Views.
 */
public class ViewUtils {
    private ViewUtils() {
    }


    public static void initListView(Context context, ListView listView, String prefix, int numItems, int layout) {
        String[] arr = new String[numItems];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = prefix + (i + 1);
        }
        listView.setAdapter(new ArrayAdapter<String>(context, layout, arr));
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Context context = view.getContext();
                String msg = "item[" + position + "]=" + parent.getItemAtPosition(position);
            }
        });
    }
}
