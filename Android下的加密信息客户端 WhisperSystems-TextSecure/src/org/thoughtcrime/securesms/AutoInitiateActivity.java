/** 
 * Copyright (C) 2011 Whisper Systems
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.thoughtcrime.securesms;

import org.thoughtcrime.securesms.crypto.KeyExchangeInitiator;
import org.thoughtcrime.securesms.crypto.MasterSecret;
import org.thoughtcrime.securesms.database.LocalKeyRecord;
import org.thoughtcrime.securesms.database.RemoteKeyRecord;
import org.thoughtcrime.securesms.recipients.Recipient;
import org.thoughtcrime.securesms.util.MemoryCleaner;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * Activity which prompts the user to initiate a secure
 * session.  Initiated by whitespace tag detection from
 * the remote endpoint.
 * 
 * @author Moxie Marlinspike
 *
 */
public class AutoInitiateActivity extends Activity {

  private long threadId;
  private Recipient recipient;
  private MasterSecret masterSecret;
	
  @Override
  public void onCreate(Bundle icicle) {
    super.onCreate(icicle);
    setContentView(R.layout.auto_initiate_activity);
		
    initializeResources();
  }
  
  @Override
  public void onDestroy() {
    MemoryCleaner.clean(masterSecret);
    super.onDestroy();
  }

  private void initializeResources() {
    this.threadId     = this.getIntent().getLongExtra("threadId", -1);
    this.recipient    = (Recipient)this.getIntent().getParcelableExtra("recipient");
    this.masterSecret = (MasterSecret)this.getIntent().getParcelableExtra("masterSecret");
		
    ((Button)findViewById(R.id.initiate_button)).setOnClickListener(new OkListener());		
    ((Button)findViewById(R.id.cancel_button)).setOnClickListener(new CancelListener());
  }
	
  private void initiateKeyExchange() {
    KeyExchangeInitiator.initiate(this, masterSecret, recipient, true);
    finish();
  }
	
  private class OkListener implements View.OnClickListener {
    public void onClick(View v) {
      initiateKeyExchange();
    }
  }
	
  private class CancelListener implements View.OnClickListener {
    public void onClick(View v) {
      Log.w("AutoInitiateActivity", "Exempting threadID: " + threadId);
      exemptThread(AutoInitiateActivity.this, threadId);
      AutoInitiateActivity.this.finish();
    }
  }
	
  public static void exemptThread(Context context, long threadId) {
    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
    sp.edit().putBoolean("pref_thread_auto_init_exempt_" + threadId, true).commit();		
  }
	
  public static boolean isValidAutoInitiateSituation(Context context, MasterSecret masterSecret, 
						     Recipient recipient, String message, long threadId) 
  {
    return 
      isMessageTagged(message)             &&
      isThreadQualified(context, threadId) &&
      isExchangeQualified(context, masterSecret, recipient);
  }

  private static boolean isMessageTagged(String message) {
    return message.endsWith("             ");
  }

  private static boolean isThreadQualified(Context context, long threadId) {
    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
    return !sp.getBoolean("pref_thread_auto_init_exempt_" + threadId, false);
  }
	
  private static boolean isExchangeQualified(Context context, MasterSecret masterSecret, Recipient recipient) {
    return 
      (new RemoteKeyRecord(context,recipient).getCurrentRemoteKey() == null) &&
      (new LocalKeyRecord(context, masterSecret, recipient).getCurrentKeyPair() == null);
  }
	
}
