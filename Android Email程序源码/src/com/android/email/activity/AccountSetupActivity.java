package com.android.email.activity;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.email.core.Account;
import com.android.email.core.Email;
import com.android.email.core.EmailAddressValidator;
import com.android.email.core.Preferences;
import com.android.email.core.Utility;

public class AccountSetupActivity extends Activity implements OnClickListener,TextWatcher{
    /** Called when the activity is first created. */
	private final static int DIALOG_NOTE = 1;
	private EmailAddressValidator mEmailValidator = new EmailAddressValidator();
	
//	private Preferences mPrefs;
    private EditText mEmailView;
    private EditText mPasswordView;
    private Button mNextButton;
    private Account mAccount;
    private Provider mProvider;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_setup);
        
//        mPrefs = Preferences.getPreferences(this);
        mEmailView = (EditText)findViewById(R.id.account_email);
        mPasswordView = (EditText)findViewById(R.id.account_password);
        mNextButton = (Button)findViewById(R.id.next);
        
        mNextButton.setOnClickListener(this);
        mEmailView.addTextChangedListener(this);
        mPasswordView.addTextChangedListener(this);
    }
    
    @Override
    public Dialog onCreateDialog(int id) {
        if (id == DIALOG_NOTE) {
            if (mProvider != null && mProvider.note != null) {
                return new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle(android.R.string.dialog_alert_title)
                    .setMessage(mProvider.note)
                    .setPositiveButton(
                            getString(R.string.okay_action),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    finishAutoSetup();
                                }
                            })
                    .setNegativeButton(
                            getString(R.string.cancel_action),
                            null)
                    .create();
            }
        }
        return null;
    }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
        case R.id.next:
            onNext();
            break;
		}
	}
	
	private void finishAutoSetup() {
        String email = mEmailView.getText().toString().trim();
        String password = mPasswordView.getText().toString().trim();
        String[] emailParts = email.split("@");
        String user = emailParts[0];
        String domain = emailParts[1];
        URI incomingUri = null;
        URI outgoingUri = null;
        try {
            String incomingUsername = mProvider.incomingUsernameTemplate;
            incomingUsername = incomingUsername.replaceAll("\\$email", email);
            incomingUsername = incomingUsername.replaceAll("\\$user", user);
            incomingUsername = incomingUsername.replaceAll("\\$domain", domain);

            URI incomingUriTemplate = mProvider.incomingUriTemplate;
            incomingUri = new URI(incomingUriTemplate.getScheme(), incomingUsername + ":"
                    + password, incomingUriTemplate.getHost(), incomingUriTemplate.getPort(), null,
                    null, null);

            String outgoingUsername = mProvider.outgoingUsernameTemplate;
            outgoingUsername = outgoingUsername.replaceAll("\\$email", email);
            outgoingUsername = outgoingUsername.replaceAll("\\$user", user);
            outgoingUsername = outgoingUsername.replaceAll("\\$domain", domain);

            URI outgoingUriTemplate = mProvider.outgoingUriTemplate;
            outgoingUri = new URI(outgoingUriTemplate.getScheme(), outgoingUsername + ":"
                    + password, outgoingUriTemplate.getHost(), outgoingUriTemplate.getPort(), null,
                    null, null);
        } catch (URISyntaxException use) {
            /*
             * If there is some problem with the URI we give up and go on to
             * manual setup.
             */
            onManualSetup();
            return;
        }

        mAccount = new Account(this);
        mAccount.setName(getOwnerName());
        mAccount.setEmail(email);
        mAccount.setStoreUri(incomingUri.toString());
        mAccount.setSenderUri(outgoingUri.toString());
        mAccount.setDraftsFolderName(getString(R.string.special_mailbox_name_drafts));
        mAccount.setTrashFolderName(getString(R.string.special_mailbox_name_trash));
        mAccount.setOutboxFolderName(getString(R.string.special_mailbox_name_outbox));
        mAccount.setSentFolderName(getString(R.string.special_mailbox_name_sent));
        if (incomingUri.toString().startsWith("imap")) {
            // Delete policy must be set explicitly, because IMAP does not provide a UI selection
            // for it. This logic needs to be followed in the auto setup flow as well.
            mAccount.setDeletePolicy(Account.DELETE_POLICY_ON_DELETE);
        }
        AccountCheckSettings.actionCheckSettings(this, mAccount, true, true);
    }
	
	private String getOwnerName() {
        String name = null;
        Account account = Preferences.getPreferences(this).getDefaultAccount();
        if (account != null) {
            name = account.getName();
        }
        return name;
    }
	
	private void onNext() {
        String email = mEmailView.getText().toString().trim();
        String[] emailParts = email.split("@");
        String domain = emailParts[1].trim();
        mProvider = findProviderForDomain(domain);
        if (mProvider == null) {
            /*
             * We don't have default settings for this account, start the manual
             * setup process.
             */
            onManualSetup();
            return;
        }

        if (mProvider.note != null) {
            showDialog(DIALOG_NOTE);
        }
        else {
            finishAutoSetup();
        }
    }
	
	private void onManualSetup() {
        String email = mEmailView.getText().toString().trim();
        String password = mPasswordView.getText().toString().trim();
        String[] emailParts = email.split("@");
        String user = emailParts[0].trim();
        String domain = emailParts[1].trim();
        
        mAccount = new Account(this);
        mAccount.setName(getOwnerName());
        mAccount.setEmail(email);
        try {
            URI uri = new URI("placeholder", user + ":" + password, domain, -1, null, null, null);
            mAccount.setStoreUri(uri.toString());
            mAccount.setSenderUri(uri.toString());
        } catch (URISyntaxException use) {
            // If we can't set up the URL, don't continue - account setup pages will fail too
            Toast.makeText(this, R.string.account_setup_username_password_toast, Toast.LENGTH_LONG)
                    .show();
            mAccount = null;
            return;
        }
        mAccount.setDraftsFolderName(getString(R.string.special_mailbox_name_drafts));
        mAccount.setTrashFolderName(getString(R.string.special_mailbox_name_trash));
        mAccount.setOutboxFolderName(getString(R.string.special_mailbox_name_outbox));
        mAccount.setSentFolderName(getString(R.string.special_mailbox_name_sent));
        AccountSetupAccountType.actionSelectAccountType(this, mAccount, true);
        finish();
    }

	@Override
	public void afterTextChanged(Editable arg0) {
		boolean valid = Utility.requiredFieldValid(mEmailView)
        && Utility.requiredFieldValid(mPasswordView)
        && mEmailValidator.isValid(mEmailView.getText().toString().trim());
		mNextButton.setEnabled(valid);
		/*
		 * Dim the next button's icon to 50% if the button is disabled.
		 * TODO this can probably be done with a stateful drawable. Check into it.
		 * android:state_enabled
		 */
		Utility.setCompoundDrawablesAlpha(mNextButton, mNextButton.isEnabled() ? 255 : 128);
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			android.content.Intent data) {
		 if (resultCode == RESULT_OK) {
	            mAccount.setDescription(mAccount.getEmail());
	            mAccount.save(Preferences.getPreferences(this));
	            Preferences.getPreferences(this).setDefaultAccount(mAccount);
//	            Email.setServicesEnabled(this);
	            AccountSetupNames.actionSetNames(this, mAccount);
	            finish();
	        }
	}

	/**
     * Attempts to get the given attribute as a String resource first, and if it fails
     * returns the attribute as a simple String value.
     * @param xml
     * @param name
     * @return
     */
    private String getXmlAttribute(XmlResourceParser xml, String name) {
        int resId = xml.getAttributeResourceValue(null, name, 0);
        if (resId == 0) {
            return xml.getAttributeValue(null, name);
        }
        else {
            return getString(resId);
        }
    }
    
	/**
     * Search the list of known Email providers looking for one that matches the user's email
     * domain.  We look in providers_product.xml first, followed by the entries in
     * platform providers.xml.  This provides a nominal override capability.
     * 
     * A match is defined as any provider entry for which the "domain" attribute matches.
     * 
     * @param domain The domain portion of the user's email address
     * @return suitable Provider definition, or null if no match found
     */
    private Provider findProviderForDomain(String domain) {
        Provider p = findProviderForDomain(domain, R.xml.providers_product);
        if (p == null) {
            p = findProviderForDomain(domain, R.xml.providers);
        }
        return p;
    }

    /**
     * Search a single resource containing known Email provider definitions.
     *
     * @param domain The domain portion of the user's email address
     * @param resourceId Id of the provider resource to scan
     * @return suitable Provider definition, or null if no match found
     */
    private Provider findProviderForDomain(String domain, int resourceId) {
        try {
            XmlResourceParser xml = getResources().getXml(resourceId);
            int xmlEventType;
            Provider provider = null;
            while ((xmlEventType = xml.next()) != XmlResourceParser.END_DOCUMENT) {
                if (xmlEventType == XmlResourceParser.START_TAG
                        && "provider".equals(xml.getName())
                        && domain.equalsIgnoreCase(getXmlAttribute(xml, "domain"))) {
                    provider = new Provider();
                    provider.id = getXmlAttribute(xml, "id");
                    provider.label = getXmlAttribute(xml, "label");
                    provider.domain = getXmlAttribute(xml, "domain");
                    provider.note = getXmlAttribute(xml, "note");
                }
                else if (xmlEventType == XmlResourceParser.START_TAG
                        && "incoming".equals(xml.getName())
                        && provider != null) {
                    provider.incomingUriTemplate = new URI(getXmlAttribute(xml, "uri"));
                    provider.incomingUsernameTemplate = getXmlAttribute(xml, "username");
                }
                else if (xmlEventType == XmlResourceParser.START_TAG
                        && "outgoing".equals(xml.getName())
                        && provider != null) {
                    provider.outgoingUriTemplate = new URI(getXmlAttribute(xml, "uri"));
                    provider.outgoingUsernameTemplate = getXmlAttribute(xml, "username");
                }
                else if (xmlEventType == XmlResourceParser.END_TAG
                        && "provider".equals(xml.getName())
                        && provider != null) {
                    return provider;
                }
            }
        }
        catch (Exception e) {
            Log.e(Email.LOG_TAG, "Error while trying to load provider settings.", e);
        }
        return null;
    }
	
	static class Provider implements Serializable {
        private static final long serialVersionUID = 8511656164616538989L;

        public String id;

        public String label;

        public String domain;

        public URI incomingUriTemplate;

        public String incomingUsernameTemplate;

        public URI outgoingUriTemplate;

        public String outgoingUsernameTemplate;

        public String note;
    }
}