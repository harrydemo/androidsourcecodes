/*
 * [程序名称] Android 音乐播放器
 * [参考资料] http://code.google.com/p/rockon-android/ 
 * [开源协议] Apache License, Version 2.0 (http://www.apache.org/licenses/LICENSE-2.0)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xmobileapp.rockplayer;

import android.util.Log;

class RockOnAppWidgetProviderWrapper {
   private RockOnAppWidgetProvider mInstance;
   
   /* class initialization fails when this throws an exception */
   static {
       try {
    	   Log.i("WIDGET-COMPAT", "checking for class AppWidgetProvider");
           Class.forName("android.appwidget.AppWidgetManager");
       } catch (Exception ex) {
           throw new RuntimeException(ex);
       }
   }

   /* calling here forces class initialization */
   public static void checkAvailable() {}

//   public static void setGlobalDiv(int div) {
//       NewClass.setGlobalDiv(div);
//   }

   
   public RockOnAppWidgetProviderWrapper() {
       mInstance = RockOnAppWidgetProvider.getInstance();
   }
   
   void performUpdate(PlayerService service, int[] appWidgetIds) {
	   mInstance.performUpdate(service, appWidgetIds);
   }
   
   void notifyChange(PlayerService service, String what) {
	   mInstance.notifyChange(service, what);
   }

}