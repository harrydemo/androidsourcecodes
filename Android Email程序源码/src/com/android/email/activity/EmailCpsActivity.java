/*
 * Copyright (C) 2008 The Android Open Source Project
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

package com.android.email.activity;


import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.util.Rfc822Tokenizer;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;
import android.widget.AutoCompleteTextView.Validator;

import com.android.email.core.Account;
import com.android.email.core.Email;
import com.android.email.core.EmailAddressValidator;
import com.android.email.core.internet.MimeBodyPart;
import com.android.email.core.internet.MimeHeader;
import com.android.email.core.internet.MimeMessage;
import com.android.email.core.internet.MimeMultipart;
import com.android.email.core.internet.MimeUtility;
import com.android.email.core.internet.TextBody;
import com.android.email.core.mail.Address;
import com.android.email.core.mail.MessagingException;
import com.android.email.core.mail.Part;
import com.android.email.core.mail.Sender;
import com.android.email.core.mail.Message.RecipientType;

public class EmailCpsActivity extends Activity implements OnClickListener, OnFocusChangeListener {
    private static final String EXTRA_ACCOUNT = "account";

    private static final int MSG_PROGRESS_ON = 1;
    private static final int MSG_PROGRESS_OFF = 2;
    private static final int MSG_UPDATE_TITLE = 3;
    private static final int MSG_SKIPPED_ATTACHMENTS = 4;
    private static final int MSG_SAVED_DRAFT = 5;
    private static final int MSG_DISCARDED_DRAFT = 6;

    private Account mAccount;
    /**
     * Indicates that the source message has been processed at least once and should not
     * be processed on any subsequent loads. This protects us from adding attachments that
     * have already been added from the restore of the view state.
     */

    private MultiAutoCompleteTextView mToView;
    private MultiAutoCompleteTextView mCcView;
    private MultiAutoCompleteTextView mBccView;
    private EditText mSubjectView;
    private EditText mMessageContentView;
    private Button mSendButton;
    private Button mDiscardButton;
    private ProgressDialog progress;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case MSG_PROGRESS_ON:
                    setProgressBarIndeterminateVisibility(true);
                    break;
                case MSG_PROGRESS_OFF:
                    setProgressBarIndeterminateVisibility(false);
                    break;
                case MSG_UPDATE_TITLE:
                    updateTitle();
                    break;
                case MSG_SKIPPED_ATTACHMENTS:
                    Toast.makeText(
                            EmailCpsActivity.this,
                            getString(R.string.message_compose_attachments_skipped_toast),
                            Toast.LENGTH_LONG).show();
                    break;
                case MSG_SAVED_DRAFT:
                    Toast.makeText(
                            EmailCpsActivity.this,
                            getString(R.string.message_saved_toast),
                            Toast.LENGTH_LONG).show();
                    break;
                case MSG_DISCARDED_DRAFT:
                    Toast.makeText(
                            EmailCpsActivity.this,
                            getString(R.string.message_discarded_toast),
                            Toast.LENGTH_LONG).show();
                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    };

    private Validator mAddressValidator;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        setContentView(R.layout.activity_compose);

//        mAddressAdapter = new EmailAddressAdapter(this);
        mAddressValidator = new EmailAddressValidator();

        mToView = (MultiAutoCompleteTextView)findViewById(R.id.to);
        mCcView = (MultiAutoCompleteTextView)findViewById(R.id.cc);
        mBccView = (MultiAutoCompleteTextView)findViewById(R.id.bcc);
        mSubjectView = (EditText)findViewById(R.id.subject);
        mMessageContentView = (EditText)findViewById(R.id.message_content);
        mSendButton = (Button)findViewById(R.id.send);
        mDiscardButton = (Button)findViewById(R.id.discard);

        /** 
         * Implements special address cleanup rules:
         * The first space key entry following an "@" symbol that is followed by any combination
         * of letters and symbols, including one+ dots and zero commas, should insert an extra
         * comma (followed by the space).
         */
        InputFilter recipientFilter = new InputFilter() {

            public CharSequence filter(CharSequence source, int start, int end, Spanned dest,
                    int dstart, int dend) {
                
                // quick check - did they enter a single space?
                if (end-start != 1 || source.charAt(start) != ' ') {
                    return null;
                }
                
                // determine if the characters before the new space fit the pattern
                // follow backwards and see if we find a comma, dot, or @
                int scanBack = dstart;
                boolean dotFound = false;
                while (scanBack > 0) {
                    char c = dest.charAt(--scanBack);
                    switch (c) {
                        case '.':
                            dotFound = true;    // one or more dots are req'd
                            break;
                        case ',':
                            return null;
                        case '@':
                            if (!dotFound) {
                                return null;
                            }
                            // we have found a comma-insert case.  now just do it
                            // in the least expensive way we can.
                            if (source instanceof Spanned) {
                                SpannableStringBuilder sb = new SpannableStringBuilder(",");
                                sb.append(source);
                                return sb;
                            } else {
                                return ", ";
                            }
                        default:
                            // just keep going
                    }
                }
                
                // no termination cases were found, so don't edit the input
                return null;
            }
        };
        InputFilter[] recipientFilters = new InputFilter[] { recipientFilter };

        // NOTE: assumes no other filters are set
        mToView.setFilters(recipientFilters);
        mCcView.setFilters(recipientFilters);
        mBccView.setFilters(recipientFilters);

        /*
         * We set this to invisible by default. Other methods will turn it back on if it's
         * needed.
         */
//        mToView.setAdapter(mAddressAdapter);
        mToView.setTokenizer(new Rfc822Tokenizer());
        mToView.setValidator(mAddressValidator);

//        mCcView.setAdapter(mAddressAdapter);
        mCcView.setTokenizer(new Rfc822Tokenizer());
        mCcView.setValidator(mAddressValidator);

//        mBccView.setAdapter(mAddressAdapter);
        mBccView.setTokenizer(new Rfc822Tokenizer());
        mBccView.setValidator(mAddressValidator);

        mSendButton.setOnClickListener(this);
        mDiscardButton.setOnClickListener(this);

        mSubjectView.setOnFocusChangeListener(this);
        
        Intent intent = getIntent();
        mAccount = (Account) intent.getSerializableExtra(EXTRA_ACCOUNT);
        updateTitle();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void updateTitle() {
        if (mSubjectView.getText().length() == 0) {
            setTitle(R.string.compose_title);
        } else {
            setTitle(mSubjectView.getText().toString());
        }
    }

    public void onFocusChange(View view, boolean focused) {
        if (!focused) {
            updateTitle();
        }
    }

    private Address[] getAddresses(MultiAutoCompleteTextView view) {
        Address[] addresses = Address.parse(view.getText().toString().trim());
        return addresses;
    }

    private MimeMessage createMessage() throws MessagingException {
        MimeMessage message = new MimeMessage();
        message.setSentDate(new Date());
        Address from = new Address(mAccount.getEmail(), mAccount.getName());
        message.setFrom(from);
        message.setRecipients(RecipientType.TO, getAddresses(mToView));
        message.setRecipients(RecipientType.CC, getAddresses(mCcView));
        message.setRecipients(RecipientType.BCC, getAddresses(mBccView));
        message.setSubject(mSubjectView.getText().toString());
        
        String text = mMessageContentView.getText().toString();
        Log.d(Email.LOG_TAG, text);
        TextBody body = new TextBody(text);
        message.setBody(body);
        return message;
    }
    
    private void sendMessage() {
    	progress = ProgressDialog.show(this, "", "sending...");
        /*
         * Create the message from all the data the user has entered.
         */
        final MimeMessage message;
        try {
            message = createMessage();
        }
        catch (MessagingException me) {
            Log.e(Email.LOG_TAG, "Failed to create new message for send or save.", me);
            throw new RuntimeException("Failed to create a new message for send or save.", me);
        }
//        SendMsgTask task = new SendMsgTask();
//        task.execute(message);
        Thread thread = new Thread(new Runnable(){

			@Override
			public void run() {
				try {
		        	Sender sender = Sender.getInstance(mAccount.getSenderUri());
		        	///////////////////////
		        	ArrayList<Part> viewables = new ArrayList<Part>();
	                ArrayList<Part> attachments = new ArrayList<Part>();
	                MimeUtility.collectParts(message, viewables, attachments);

	                StringBuffer sbHtml = new StringBuffer();
	                StringBuffer sbText = new StringBuffer();
	                for (Part viewable : viewables) {
	                    try {
	                        String text = MimeUtility.getTextFromPart(viewable);
	                        /*
	                         * Anything with MIME type text/html will be stored as such. Anything
	                         * else will be stored as text/plain.
	                         */
	                        if (viewable.getMimeType().equalsIgnoreCase("text/html")) {
	                            sbHtml.append(text);
	                        }
	                        else {
	                            sbText.append(text);
	                        }
	                    } catch (Exception e) {
	                        throw new MessagingException("Unable to get text for message part", e);
	                    }
	                }
		        	 ////////////////////////
	                message.setUid("email" + UUID.randomUUID().toString());
	                /////////////////////////
	                message.setHeader(MimeHeader.HEADER_CONTENT_TYPE, "multipart/mixed");
		            MimeMultipart mp = new MimeMultipart();
		            mp.setSubType("mixed");
		            message.setBody(mp);
		            
					String htmlContent = sbHtml.toString();
					String textContent = sbText.toString();

					if (htmlContent != null) {
						TextBody body = new TextBody(htmlContent);
						MimeBodyPart bp = new MimeBodyPart(body, "text/html");
						mp.addBodyPart(bp);
						Log.v(Email.LOG_TAG, htmlContent);
					}

					if (textContent != null) {
						TextBody body = new TextBody(textContent);
						MimeBodyPart bp = new MimeBodyPart(body, "text/plain");
						mp.addBodyPart(bp);
						Log.v(Email.LOG_TAG, textContent);
					}
		            //////////////////////////
		        	
					sender.sendMessage(message);
				} catch (MessagingException e) {
					e.printStackTrace();
				}
				progress.dismiss();
				finish();
			}
		});
        thread.start();
    }
    
    private void onSend() {
        if (getAddresses(mToView).length == 0 &&
                getAddresses(mCcView).length == 0 &&
                getAddresses(mBccView).length == 0) {
            mToView.setError(getString(R.string.message_compose_error_no_recipients));
            Toast.makeText(this, getString(R.string.message_compose_error_no_recipients),
                    Toast.LENGTH_LONG).show();
            return;
        }
        sendMessage();
    }

    private void onDiscard() {
        mHandler.sendEmptyMessage(MSG_DISCARDED_DRAFT);
        finish();
    }
    
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.send:
                onSend();
                break;
            case R.id.discard:
                onDiscard();
                break;
        }
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.send:
                onSend();
                break;
            case R.id.discard:
                onDiscard();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
