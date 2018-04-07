/**
 * @description : a demo of operation start app .
 * @version 1.0
 * @author kevin qin
 * @date 2010-11-10
 */
package teleca.androidtalk.operation;

import java.util.List;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.widget.Toast;

/**
 * this class start a app by name
 * 
 * @author kevin qin
 *
 */
public class OperationStartApp extends OperationBase implements IOperation {
	private Context context;

	@Override
	public OpResult doOperation(Context context, String opData) {
		if (context != null && opData != null)  {
			this.context = context;
			startAPP(opData);
		}
		return null;
	}

	/**
	 * start an application 
	 * 
	 * @param appName application's name
	 * @return 
	 */
	private void startAPP(String appName) {
		Intent resolveIntent = new Intent();
		resolveIntent.setAction(Intent.ACTION_MAIN);
		resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		PackageManager pm = context.getPackageManager();
		List<ResolveInfo> list = pm.queryIntentActivities(resolveIntent, 0);

		for (ResolveInfo info : list) {
			if (appName.equalsIgnoreCase(info.activityInfo.loadLabel(pm).toString())) {
				Intent intent = new Intent(Intent.ACTION_MAIN);
				intent.addCategory(Intent.CATEGORY_LAUNCHER);
				intent.setComponent(new ComponentName(info.activityInfo.packageName, info.activityInfo.name));

				context.startActivity(intent);
				return;
			}
		}
		//FIXME: we should return this message to invoker.
		Toast.makeText(context, "Not found the matched app!", Toast.LENGTH_SHORT).show();
	}

}
