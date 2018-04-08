/**
 * 
 */
package com.nanosheep.bikeroute;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.nanosheep.bikeroute.constants.BikeRouteConsts;
import com.nanosheep.bikeroute.utility.MyHttpClient;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;

import java.net.URLEncoder;
import java.util.regex.Pattern;

/**
 * Activity for sending feedback on a route to CycleStreets.net.
 * 
 * This file is part of BikeRoute.
 * 
 * Copyright (C) 2011  Jonathan Gray
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 * @author jono@nanosheep.net
 * @version Nov 11, 2010
 */
public class Feedback extends Activity {
	private BikeRouteApp app;
	private TextView nameField;
	private TextView emailField;
	private TextView commentField;
	private Button submit;
	private SubmitHandler submitHandler;

	@Override
	public final void onCreate(final Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	app = ((BikeRouteApp) getApplication());
	requestWindowFeature(Window.FEATURE_RIGHT_ICON);
	setContentView(R.layout.feedback);
	setFeatureDrawableResource(Window.FEATURE_RIGHT_ICON, R.drawable.ic_bar_bikeroute);
	
	nameField = (TextView) findViewById(R.id.name_input);
	emailField = (TextView) findViewById(R.id.email_input);
	commentField = (TextView) findViewById(R.id.comment_input);
	submit = (Button) findViewById(R.id.submit_button);
	
	//Handle rotations
	final Object[] data = (Object[]) getLastNonConfigurationInstance();
	if (data != null) {
		nameField.setText((CharSequence) data[0]);
		emailField.setText((CharSequence) data[1]);
		commentField.setText((CharSequence) data[2]);
	}
	//Input validation
	final Validate watcher = new Validate();
	nameField.addTextChangedListener(watcher);
	emailField.addTextChangedListener(watcher);
	commentField.addTextChangedListener(watcher);
	
	//Form submit handler
	submitHandler = new SubmitHandler();
	submit.setOnClickListener(submitHandler);
	}
	
	@Override
	public Object onRetainNonConfigurationInstance() {
		Object[] objs = new Object[3];
		objs[0] = nameField.getText();
		objs[1] = emailField.getText();
		objs[2] = commentField.getText();
	    return objs;
	}
	
	@Override
	public Dialog onCreateDialog(final int id) {
		AlertDialog.Builder builder;
		ProgressDialog pDialog;
		Dialog dialog;
		switch(id) {
		case R.id.send:
			pDialog = new ProgressDialog(this);
			pDialog.setCancelable(true);
			pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pDialog.setMessage(getText(R.string.send_msg));
			pDialog.setOnDismissListener(new OnDismissListener() {
				@Override
				public void onDismiss(final DialogInterface arg0) {
					removeDialog(R.id.send);
				}
			});
			pDialog.setOnCancelListener(new OnCancelListener() {

				@Override
				public void onCancel(final DialogInterface arg0) {
						submitHandler.cancel(true);
				}
				
			});
			dialog = pDialog;
			break;
		case R.id.thanks:
			builder = new AlertDialog.Builder(this);
			builder.setMessage(getText(R.string.thanks_message)).setCancelable(
					true).setPositiveButton(getString(R.string.ok),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(final DialogInterface dialog,
								final int id) {
							dialog.dismiss();
							finish();
						}
					});
			dialog = builder.create();
			break;
		case R.id.feedback_fail:
			builder = new AlertDialog.Builder(this);
			builder.setMessage(getText(R.string.feedback_fail_message)).setCancelable(
					true).setPositiveButton(getString(R.string.ok),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(final DialogInterface dialog,
								final int id) {
							dialog.dismiss();
						}
					});
			dialog = builder.create();
			break;
		default:
			dialog = null;
		}
		return dialog;
	}
	
	private class SubmitHandler extends AsyncTask<Void, Void, Integer> implements OnClickListener {
		private static final int OK = 1;

		/* (non-Javadoc)
		 * @see android.view.View.OnClickListener#onClick(android.view.View)
		 */
		@Override
		public void onClick(View arg0) {
			this.execute();
		}
		
		@Override
		protected void onPreExecute() {
			Feedback.this.showDialog(R.id.send);
		}

		/**
		 * Fire a thread to submit feedback.
		 * 
		 * @param itineraryId
		 * @param name
		 * @param email
		 * @param comment
		 */
		private int doSubmit(final int itineraryId, final String name, final String email,
				final String comment) {
			int result = OK;
			String reqString = getString(R.string.feedback_api);
			reqString += "itinerary=" + itineraryId;
			reqString += "&comments=" + URLEncoder.encode(comment);
			reqString += "&name=" + URLEncoder.encode(name);
			reqString += "&email=" + URLEncoder.encode(email);
			HttpUriRequest request = new HttpPut(reqString);
			request.addHeader("User-Agent", BikeRouteConsts.AGENT);
	        try {
				final HttpResponse response = new MyHttpClient(Feedback.this).execute(request);
			} catch (Exception e) {
				result = -1;
			}
			return result;
		}

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected Integer doInBackground(Void... arg0) {
			return doSubmit(app.getRoute().getItineraryId(), nameField.getText().toString(), 
					emailField.getText().toString(), commentField.getText().toString());
		}
		
		@Override
        protected void onPostExecute(final Integer msg) {
			Feedback.this.dismissDialog(R.id.send);
			if (msg == OK) {
				Feedback.this.showDialog(R.id.thanks);
			} else {
				Feedback.this.showDialog(R.id.feedback_fail);
			}
        }
        
        @Override
        protected void onCancelled() {
        }
		
	}
	
	private class Validate implements TextWatcher {
		Pattern p;
		
		public Validate() {
			p = Pattern.compile("((([ \\t]*[a-zA-Z0-9\\!\\#\\$\\%\\&\\'\\*\\+\\-\\/\\=\\?\\^\\_\\`\\{\\|\\}\\~]+[ \\t]*)|(\\\"([ \\t]*([\\x01-\\x08\\x0B\\x0C\\x0E-\\x1F\\x7F\\x21\\x23-\\x5B\\x5D-\\x7E]|(\\\\[\\x01-\\x09\\x0B\\x0C\\x0E-\\x7F])))*[ \\t]*\\\"))+)?[ \\t]*<(([ \\t]*([a-zA-Z0-9\\!\\#\\$\\%\\&\\'\\*\\+\\-\\/\\=\\?\\^\\_\\`\\{\\|\\}\\~]+(\\.[a-zA-Z0-9\\!\\#\\$\\%\\&\\'\\*\\+\\-\\/\\=\\?\\^\\_\\`\\{\\|\\}\\~]+)*)[ \\t]*)|(\\\"([ \\t]*([\\x01-\\x08\\x0B\\x0C\\x0E-\\x1F\\x7F\\x21\\x23-\\x5B\\x5D-\\x7E]|(\\\\[\\x01-\\x09\\x0B\\x0C\\x0E-\\x7F])))*[ \\t]*\\\"))@([ \\t]*([a-zA-Z0-9\\!\\#\\$\\%\\&\\'\\*\\+\\-\\/\\=\\?\\^\\_\\`\\{\\|\\}\\~]+(\\.[a-zA-Z0-9\\!\\#\\$\\%\\&\\'\\*\\+\\-\\/\\=\\?\\^\\_\\`\\{\\|\\}\\~]+)*)[ \\t]*|\\[([ \\t]*[\\x01-\\x08\\x0B\\x0C\\x0E-\\x1F\\x7F\\x21-\\x5A\\x5E-\\x7E]|(\\\\[\\x01-\\x09\\x0B\\x0C\\x0E-\\x7F])+)*[ \\t]*\\])>|(([ \\t]*([a-zA-Z0-9\\!\\#\\$\\%\\&\\'\\*\\+\\-\\/\\=\\?\\^\\_\\`\\{\\|\\}\\~]+(\\.[a-zA-Z0-9\\!\\#\\$\\%\\&\\'\\*\\+\\-\\/\\=\\?\\^\\_\\`\\{\\|\\}\\~]+)*)[ \\t]*)|(\\\"([ \\t]*([\\x01-\\x08\\x0B\\x0C\\x0E-\\x1F\\x7F\\x21\\x23-\\x5B\\x5D-\\x7E]|(\\\\[\\x01-\\x09\\x0B\\x0C\\x0E-\\x7F])))*[ \\t]*\\\"))@([ \\t]*([a-zA-Z0-9\\!\\#\\$\\%\\&\\'\\*\\+\\-\\/\\=\\?\\^\\_\\`\\{\\|\\}\\~]+(\\.[a-zA-Z0-9\\!\\#\\$\\%\\&\\'\\*\\+\\-\\/\\=\\?\\^\\_\\`\\{\\|\\}\\~]+)*)[ \\t]*|\\[([ \\t]*[\\x01-\\x08\\x0B\\x0C\\x0E-\\x1F\\x7F\\x21-\\x5A\\x5E-\\x7E]|(\\\\[\\x01-\\x09\\x0B\\x0C\\x0E-\\x7F])+)*[ \\t]*\\])");
		}
		
		/* (non-Javadoc)
		 * @see android.text.TextWatcher#afterTextChanged(android.text.Editable)
		 */
		@Override
		public void afterTextChanged(Editable arg0) {
			if ((commentField.getText().length() != 0) && isValid(emailField.getText())) {
				submit.setEnabled(true);
			} else {
				submit.setEnabled(false);
			}
		}

		/**
		 * @param text
		 * @return
		 */
		private boolean isValid(CharSequence text) {
			return p.matcher(text).matches();
		}

		/* (non-Javadoc)
		 * @see android.text.TextWatcher#beforeTextChanged(java.lang.CharSequence, int, int, int)
		 */
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}

		/* (non-Javadoc)
		 * @see android.text.TextWatcher#onTextChanged(java.lang.CharSequence, int, int, int)
		 */
		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
