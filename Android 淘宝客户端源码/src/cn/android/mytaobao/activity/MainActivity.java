package cn.android.mytaobao.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.android.mytaobao.R;
import cn.android.mytaobao.R.layout;
import cn.android.mytaobao.dao.biz.ProductManager;
import cn.android.mytaobao.dao.biz.UserManager;
import cn.android.mytaobao.model.Product;
import cn.android.mytaobao.model.User;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends ListActivity {

	private Context context = null;
	private LayoutInflater inflater = null;
	private User loginUser = null;
	private UserManager userManager = null;

	// ����һЩ�����Ի������͵ı�־
	private static final int DIALOG_EXIT = -1; // �����·��ؽ������ĶԻ���
	private static final int DIALOG_LOGIN = 1; // �����ǵ�¼�Ի���
	private static final int DIALOG_REG = 2; // �����û�ע��Ի���

	// �������ListView��س���
	private static final int PAGESIZE = 5; // ÿ��ȡ������¼

	private int pageIndex = 0; // ���ڱ��浱ǰ�ǵڼ�ҳ,0�����һҳ
	private ProductManager productManager = null;
	private List<Product> products = null;
	private ListView lvProduct = null;
	private MyAdapter adapter = null;

	protected Menu myMenu;
	public static Map<Integer, Boolean> isSelected;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init(); // ϵͳ��ʹ��
		showDialog(DIALOG_LOGIN); // ������¼�Ի���
		// setContentView(R.layout.main);
		// showDialog(DIALOG_REG);

	}

	/**
	 * ��ʹ��ϵͳ����
	 */
	private void init() {
		context = this;
		this.inflater = LayoutInflater.from(this);
		userManager = new UserManager();
		pageIndex = 0;

	}

	/**
	 * ��ʼ��ϵͳ����
	 */
	private void initMainUI() {
		productManager = new ProductManager();
		products = productManager.getProdcutByPager(pageIndex, PAGESIZE);
		lvProduct = getListView();
		// setContentView(R.layout.main);
		adapter = new MyAdapter(context);
		lvProduct.setItemsCanFocus(true);
		setListAdapter(adapter);
		lvProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch ((int) id) {
				case -1: // ���
					Tool.ShowMessage(context, "���!");
					break;
				case -2: // ����
					adapter.more();
					break;
				default:
					// Tool.ShowMessage(context, id + "");
					ViewHolder viewHolder = (ViewHolder) view.getTag();
					// CheckBox chkIn = (CheckBox)
					// view.findViewById(R.id.chkIn);
					if (viewHolder.cBox != null) {
						viewHolder.cBox.toggle();
						Tool.ShowMessage(context, id + "");
						isSelected.put(position - 1,
								viewHolder.cBox.isChecked());
					}
					break;
				}

			}
		});
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog = this.buildDialog(id);
		return dialog;

	}

	/**
	 * ���ݲ�ͬ��ID������ͬ�ĶԻ���
	 * 
	 * @param flag
	 * @return
	 */
	private Dialog buildDialog(int flag) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		switch (flag) {
		case DIALOG_LOGIN:
			createLoginDialog(builder);
			break;
		case DIALOG_EXIT:
			createExitDialog(builder);
			break;
		case DIALOG_REG:
			createRegistDialog(builder);
			break;
		default:
			break;
		}

		return builder.create();
	}

	/**
	 * �����˳�ϵͳѯ��builder����
	 * 
	 * @param builder
	 */
	private void createExitDialog(AlertDialog.Builder builder) {

		builder.setTitle("����").setIcon(R.drawable.flag)
				.setMessage("��ȷ��Ҫ�˳�ϵͳ��")
				.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
					}
				}).setNegativeButton("ȡ��", null);

	}

	/**
	 * ������¼�Ի����builder����
	 * 
	 * @param builder
	 */
	private void createLoginDialog(AlertDialog.Builder builder) {
		View v = inflater.inflate(R.layout.login_ui, null);
		builder.setTitle("��¼").setIcon(R.drawable.flag).setView(v);
		Button btnLogin = (Button) v.findViewById(R.id.btnLogin);
		final EditText edtUserId = (EditText) v.findViewById(R.id.edtUserId);
		final EditText edtPassWord = (EditText) v
				.findViewById(R.id.edtPassWord);
		btnLogin.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				loginUser = userManager.Login(edtUserId.getText().toString(),
						edtPassWord.getText().toString());
				if (loginUser != null) {
					dismissDialog(DIALOG_LOGIN);
					initMainUI();
				} else {
					Tool.ShowMessage(context, "�ʺŻ��������󣬵�¼ʧ��!");
				}
			}

		});

		Button btnExit = (Button) v.findViewById(R.id.btnExit);
		btnExit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		TextView linkReg = (TextView) v.findViewById(R.id.linkReg);
		linkReg.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// Tool.ShowMessage(context, "test");
				showDialog(DIALOG_REG);
			}
		});
	}

	private String getCheckCode() {
		String strInt = " ";
		Integer i = new Integer((int) (Math.random() * 100000000));
		strInt = String.valueOf(i);
		if (strInt.length() != 8) {
			return getCheckCode();
		} else {
			return strInt;
		}
	}

	/**
	 * ����ע���û��Ի����builder����
	 * 
	 * @param builder
	 */
	private void createRegistDialog(AlertDialog.Builder builder) {
		View v = inflater.inflate(R.layout.reg_ui, null);
		builder.setTitle("ע��").setIcon(R.drawable.flag).setView(v);
		Button btnReg = (Button) v.findViewById(R.id.btnRegister);
		final EditText edtUserId = (EditText) v.findViewById(R.id.edtUserId);
		final EditText edtPassWord = (EditText) v
				.findViewById(R.id.edtPassWord);
		final EditText edtChkCode = (EditText) v.findViewById(R.id.edtChkCode);
		final TextView txtChkCode = (TextView) v.findViewById(R.id.txtChkCode);

		txtChkCode.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				((TextView) v).setText(getCheckCode());
			}
		});
		btnReg.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!txtChkCode.getText().toString()
						.equals(edtChkCode.getText().toString())) {
					Tool.ShowMessage(context, "��֤��������������������!");
					String chkcode = getCheckCode();
					txtChkCode.setText(chkcode);
					return;
				}
				User user = new User(edtUserId.getText().toString(),
						edtPassWord.getText().toString());
				User result = userManager.Register(user);
				if (result != null) {
					dismissDialog(DIALOG_REG);
				} else {
					Tool.ShowMessage(context, "ע���û�ʧ��!");
					String chkcode = getCheckCode();
					txtChkCode.setText(chkcode);

				}
			}
		});

		Button btnExit = (Button) v.findViewById(R.id.btnExit);
		btnExit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// finish();
				dismissDialog(DIALOG_REG);
			}
		});

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (adapter != null && adapter.isBatManager()) {
				adapter = new MyAdapter(context, R.layout.productitem);
				setListAdapter(adapter);

				isSelected = new HashMap<Integer, Boolean>();
				for (int i = 0; i < products.size(); i++) {
					isSelected.put(i, false);
				}

				if (myMenu != null) {
					onCreateOptionsMenu(myMenu);
				}
				return true;
			} else {
				showDialog(DIALOG_EXIT);
				return true;
			}
		}

		return false;
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		if (id == DIALOG_REG) {
			String chkcode = getCheckCode();
			TextView tv = (TextView) dialog.findViewById(R.id.txtChkCode);
			tv.setText(chkcode);
		}
	}

	/**
	 * �Զ���Adapter
	 * 
	 * @author jhao
	 * @version 1.0
	 */
	class MyAdapter extends BaseAdapter {
		private Context context;
		private int item_layout_res = R.layout.productitem;

		public MyAdapter(Context context) {
			this.context = context;
		}

		public MyAdapter(Context context, int res) {
			this.context = context;
			this.item_layout_res = res;
		}

		public boolean isBatManager() {
			return this.item_layout_res == R.layout.productitem2;
		}

		public void refresh() {
			notifyDataSetChanged();
		}

		public void more() {
			pageIndex++;
			List<Product> moreProduct = productManager.getProdcutByPager(
					pageIndex, PAGESIZE);
			if (moreProduct != null) {
				products.addAll(moreProduct);
				notifyDataSetChanged();
			} else {
				Tool.ShowMessage(context, "û�����ݿɹ�����!");
			}
		}

		public void changeUI(int item_layout_res) {
			this.item_layout_res = item_layout_res;
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return products.size() + 2;
		}

		@Override
		public Object getItem(int position) {
			return products.get(position - 1);
		}

		@Override
		public long getItemId(int position) {

			// return products.get(position - 1).getId();

			if (position == 0)// ѡ�е�һ��
			{
				return -1;// ���������ǵ�һ��
			} else if (position > 0 && (position < this.getCount() - 1)) {
				return products.get(position - 1).getId();// ����û�ѡ�����м���
				// mUserList.get(index-1).getId();
			} else {
				return -2;// ��ʾ�û�ѡ�����һ��
			}
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// View view ;
			// ˵���ǵ�һ��
			if (position == 0) {
				convertView = inflater.inflate(R.layout.addproductitem, null);
				return convertView;
			}
			// ˵�������һ��
			if (position == this.getCount() - 1) {
				convertView = inflater.inflate(R.layout.moreitemsview, null);
				return convertView;
			}
			ViewHolder holder = null;
			if (convertView == null
					|| convertView.findViewById(R.id.addproduct) != null
					|| convertView.findViewById(R.id.linemore) != null) {
				/*
				 * convertView = inflater.inflate(R.layout.productitem, parent,
				 * false);
				 */
				/*
				 * convertView = inflater.inflate(R.layout.productitem2, parent,
				 * false);
				 */
				holder = new ViewHolder();
				convertView = inflater.inflate(this.item_layout_res, parent,
						false);
				holder.img = (ImageView) convertView
						.findViewById(R.id.imgPhoto);
				holder.title = (TextView) convertView
						.findViewById(R.id.txtName);
				holder.unitPrice = (TextView) convertView
						.findViewById(R.id.txtUnitPrice);
				holder.cBox = convertView.findViewById(R.id.chkIn) == null ? null
						: (CheckBox) convertView.findViewById(R.id.chkIn);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.img.setImageResource(products.get(position - 1).getPhoto());
			holder.title.setText(position + "��"
					+ products.get(position - 1).getName().toString());
			holder.unitPrice.setText(String.valueOf(products.get(position - 1)
					.getUnitPrice()));
			if (holder.cBox != null)
				holder.cBox.setChecked(isSelected.get(position - 1));

			return convertView;
		}

	}

	public final class ViewHolder {
		public ImageView img;
		public TextView title;
		public TextView unitPrice;
		public CheckBox cBox;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		myMenu = menu;
		myMenu.clear();
		if (adapter.isBatManager()) {
			myMenu.add(1, 4, 1, "ȫѡ").setIcon(R.drawable.menu_all_select);
			myMenu.add(1, 5, 2, "ȫ��").setIcon(R.drawable.menu_all_unselect);
			myMenu.add(1, 6, 3, "����ɾ��").setIcon(R.drawable.menu_delete_list);
		} else {
			myMenu.add(1, 1, 1, "��������").setIcon(R.drawable.menu_bat_manage);
			myMenu.add(1, 2, 2, "����").setIcon(R.drawable.ic_menu_search);
			myMenu.add(1, 3, 3, "���۸�����").setIcon(R.drawable.ic_menu_sort);
		}
		return super.onCreateOptionsMenu(myMenu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 1:
			adapter = new MyAdapter(context, R.layout.productitem2);
			setListAdapter(adapter);

			isSelected = new HashMap<Integer, Boolean>();
			for (int i = 0; i < products.size(); i++) {
				isSelected.put(i, false);
			}

			if (myMenu != null) {
				onCreateOptionsMenu(myMenu);
			}
			break;
		case 2:
			break;
		case 3:
			break;
		case 4:
			// Tool.ShowMessage(context, "ȫѡ");
			for (int i = 0; i < products.size(); i++) {
				isSelected.put(i, true);
			}
			adapter.refresh();
			break;
		case 5:
			for (int i = 0; i < products.size(); i++) {
				isSelected.put(i, false);
			}
			adapter.refresh();
			break;
		case 6:
			
			new AlertDialog.Builder(context)
			.setIcon(R.drawable.flag).setTitle("����").setMessage("��ȷ��Ҫɾ����Щ��Ʒ��?")
			.setPositiveButton("ȷ��", new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which) {
					List<Integer> ids = new ArrayList<Integer>();
					for (int i = 0; i < products.size(); i++) {
						if (isSelected.get(i)) {
							ids.add(products.get(i).getId());
						}
					}
					boolean isOk = productManager.DelProduct(ids);
					if (isOk) {
						pageIndex = 0;
						products = productManager
								.getProdcutByPager(pageIndex, PAGESIZE);

						Tool.ShowMessage(context, "ɾ���ɹ�!");
						adapter = new MyAdapter(context, R.layout.productitem);
						setListAdapter(adapter);

						isSelected = new HashMap<Integer, Boolean>();
						for (int i = 0; i < products.size(); i++) {
							isSelected.put(i, false);
						}

						if (myMenu != null) {
							onCreateOptionsMenu(myMenu);
						}

					} else {
						Tool.ShowMessage(context, "ɾ��ʧ��!");
					}

				}}).setNegativeButton("ȡ��", null).create().show();

			
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	/*
	 * @Override public void onBackPressed() { // TODO Auto-generated method
	 * stub Tool.ShowMessage(context, "ok"); super.onBackPressed(); }
	 */

}