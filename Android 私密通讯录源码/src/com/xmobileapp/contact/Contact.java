/*
 * [��������] Android ͨѶ¼
 * [����] xmobileapp�Ŷ�
 * [�ο�����] Google Android Samples 
 * [��ԴЭ��] Apache License, Version 2.0 (http://www.apache.org/licenses/LICENSE-2.0)
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

package com.xmobileapp.contact;


import com.xmobileapp.contact.R;
import android.app.ListActivity;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class Contact extends ListActivity {
	private static final String TAG = "Contacts";
	
	private static final int AddContact_ID = Menu.FIRST;
	private static final int EditContact_ID = Menu.FIRST+1;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //���ÿ�ݼ�֧��
        setDefaultKeyMode(DEFAULT_KEYS_SHORTCUT);

        //��ȡ/����Intent�����ڴ�ContactsProvider����ȡͨѶ¼����
        Intent intent = getIntent();
        if (intent.getData() == null) {
            intent.setData(ContactsProvider.CONTENT_URI);
        }
        
        //���ó���֧�֣������������Ĳ˵���
        getListView().setOnCreateContextMenuListener(this);
        
        //ʹ��managedQuery��ȡContactsProvider��Cursor
        Cursor cursor = managedQuery(getIntent().getData(), ContactColumn.PROJECTION, null, null,null);
        //ʹ��SimpleCursorAdapter����Cursor��Adapter�Ա�ʹ�ã����ݱ�ʾ��ʽΪ������ - �ֻ�����
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.contact_list_item, cursor,
                new String[] { ContactColumn.NAME,ContactColumn.MOBILE }, new int[] { R.id.name, R.id.contactinfo });

        //Ϊ��ǰListView����Adapter
        setListAdapter(adapter);
        Log.e(TAG+"onCreate"," is ok");
    }
    
    //Ŀ¼�����Ļص�����
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        
        // ��Ŀ¼�����ӡ���ӡ���ť��Ϊ֮�趨��ݼ���ͼ��
        menu.add(0, AddContact_ID, 0, R.string.menu_add)
        	.setShortcut('3', 'a')
        	.setIcon(android.R.drawable.ic_menu_add);

        return true;
        
    }
    
    //Ŀ¼��ʾ֮ǰ�Ļص�����
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        final boolean haveItems = getListAdapter().getCount() > 0;

        //�����ǰ�б�Ϊ��
        if (haveItems) {
            Uri uri = ContentUris.withAppendedId(getIntent().getData(), getSelectedItemId());

            Intent[] specifics = new Intent[1];
            specifics[0] = new Intent(Intent.ACTION_EDIT, uri);
            MenuItem[] items = new MenuItem[1];

            Intent intent = new Intent(null, uri);
            intent.addCategory(Intent.CATEGORY_ALTERNATIVE);
            menu.addIntentOptions(Menu.CATEGORY_ALTERNATIVE, 0, 0, 
            		null, specifics, intent, 0,items);

            //�����CATEGORY_ALTERNATIVE���͵Ĳ˵���,���༭ѡ������룬��Ϊ֮��ӿ�ݼ�
            if (items[0] != null) {
                items[0].setShortcut('1', 'e');
            }
        } else {
            menu.removeGroup(Menu.CATEGORY_ALTERNATIVE);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case AddContact_ID:
            //�����Ŀ
            startActivity(new Intent(Intent.ACTION_INSERT, getIntent().getData()));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //�����Ĳ˵���������ͨ��������Ŀ���������Ĳ˵�
    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info;
        try {
             info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        } catch (ClassCastException e) {
            return;
        }

        Cursor cursor = (Cursor) getListAdapter().getItem(info.position);
        if (cursor == null) {
            return;
        }

        menu.setHeaderTitle(cursor.getString(1));

        menu.add(0, EditContact_ID, 0, R.string.menu_delete);
    }
    
    //�����Ĳ˵�ѡ��Ļص�����
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info;
        try {
             info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        } catch (ClassCastException e) {
            return false;
        }

        switch (item.getItemId()) {
        	//ѡ��༭��Ŀ
            case EditContact_ID: {
                Uri noteUri = ContentUris.withAppendedId(getIntent().getData(), info.id);
                getContentResolver().delete(noteUri, null, null);
                return true;
            }
        }
        return false;
    }
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Uri uri = ContentUris.withAppendedId(getIntent().getData(), id);
        
        String action = getIntent().getAction();
        if (Intent.ACTION_PICK.equals(action) || Intent.ACTION_GET_CONTENT.equals(action)) {
        	// ���ͨѶ¼�б��Activity�Ǳ�����Activity�����Է���ѡ���ͨѶ��Ϣ
        	// ���磬���ų���ͨ����������ȡĳ�˵ĵ绰����
            setResult(RESULT_OK, new Intent().setData(uri));
        } else {
            //�༭ ��ϵ��
            startActivity(new Intent(Intent.ACTION_EDIT, uri));
        }
    }
    
    
}