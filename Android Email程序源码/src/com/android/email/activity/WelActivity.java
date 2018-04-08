package com.android.email.activity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.android.email.core.Account;
import com.android.email.core.Preferences;

public class WelActivity extends ListActivity implements OnItemClickListener, OnClickListener{

	private static final String EXTRA_ACCOUNT = "account";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wel);
		ListView listView = getListView();
		listView.setOnItemClickListener(this);
        listView.setItemsCanFocus(false);
        listView.setEmptyView(findViewById(R.id.empty));
        findViewById(R.id.add_new_account).setOnClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View arg1, int position, long arg3) {
		Account account = (Account)parent.getItemAtPosition(position);
		Intent intent = new Intent(this, EmailCpsActivity.class);
        intent.putExtra(EXTRA_ACCOUNT , account);
        startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.add_new_account) {
			Intent intent = new Intent(this, AccountSetupActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
	}
	
	 @Override
	    public void onResume() {
	        super.onResume();
	        refresh();
	    }

	    private void refresh() {
	        Account[] accounts = Preferences.getPreferences(this).getAccounts();
	        getListView().setAdapter(new AccountsAdapter(accounts));
	    }
	
	class AccountsAdapter extends ArrayAdapter<Account> {
        public AccountsAdapter(Account[] accounts) {
            super(WelActivity.this, 0, accounts);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Account account = getItem(position);
            View view;
            if (convertView != null) {
                view = convertView;
            }
            else {
                view = getLayoutInflater().inflate(R.layout.accounts_item, parent, false);
            }
            AccountViewHolder holder = (AccountViewHolder) view.getTag();
            if (holder == null) {
                holder = new AccountViewHolder();
                holder.description = (TextView) view.findViewById(R.id.description);
                holder.email = (TextView) view.findViewById(R.id.email);
                view.setTag(holder);
            }
            holder.description.setText(account.getDescription());
            holder.email.setText(account.getEmail());
            if (account.getEmail().equals(account.getDescription())) {
                holder.email.setVisibility(View.GONE);
            }
            return view;
        }

        class AccountViewHolder {
            public TextView description;
            public TextView email;
        }
    }

}
