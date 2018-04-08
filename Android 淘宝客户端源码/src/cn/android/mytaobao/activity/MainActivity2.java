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
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
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

public class MainActivity2 extends ListActivity {

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
	public static Map<Integer, Boolean> isSelected;

	private ListView lvProduct = null;
	private MyAdapter adapter = null;

	// ��̬�����˵�
	private Menu myMenu;

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
		adapter = new MyAdapter(context);
		setListAdapter(adapter);
		lvProduct = getListView();
		lvProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (id == -2) // ˵��������Ǹ���
				{
					adapter.more();
				} else if (id != -1) {
					/*
					 * Tool.ShowMessage(context, id + "");
					 * 
					 * CheckBox chkIn = (CheckBox)
					 * view.findViewById(R.id.chkIn); if (chkIn != null) {
					 * chkIn.toggle(); isSelected.put(position - 1,
					 * chkIn.isChecked()); }
					 */
					ViewHolder vHollder = (ViewHolder) view.getTag();
					// ��ÿ�λ�ȡ�����itemʱ�����ڵ�checkbox״̬�ı䣬ͬʱ�޸�map��ֵ��
					if(vHollder.cBox!=null){
					  vHollder.cBox.toggle();
					  isSelected.put(position - 1, vHollder.cBox.isChecked());
					}
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
			if (adapter.isBatchManager()) {
                   initUI(R.layout.productitem);  //������Ʒ���״̬
			} else {
				showDialog(DIALOG_EXIT);
			}
			return true;

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
		private int res_item_layout = R.layout.productitem;

		public MyAdapter(Context context) {
			this.context = context;

		}

		public MyAdapter(Context context, int res_item_layout) {
			this.context = context;
			this.res_item_layout = res_item_layout;
			init();
		}

		/**
		 * ��ʼ���ڲ������ڱ���checkbox״̬�ļ���
		 */
		private void init() {
			isSelected = new HashMap<Integer, Boolean>();
			for (int i = 0; i < products.size(); i++) {
				isSelected.put(i, false);
			}
		}

		/**
		 * ��ȡ���ࣨ��ҳ��ȡ��
		 */
		public void more() {
			// step 1: ��ǰҳ++
			pageIndex++;
			// step 2:��ȡ���������
			List<Product> moreProducts = productManager.getProdcutByPager(
					pageIndex, PAGESIZE);
			if (moreProducts != null) {
				// step 3:��ӵ����еļ�����
				products.addAll(moreProducts);
				// ˢ��ListView
				this.notifyDataSetChanged();
			} else {
				Tool.ShowMessage(context, "�Ѿ������һ����¼�ˣ�");
			}
		}

		/**
		 * ˢ��ListView
		 */
		public void refresh() {
			if (this.isBatchManager())
				init();
			this.notifyDataSetChanged();
		}

		/**
		 * �����ж��Ƿ��Ǵ�����������״̬,true:�ǣ�false:��
		 * 
		 * @return
		 */
		public boolean isBatchManager() {
			return this.res_item_layout == R.layout.productitem2;
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
			ImageView img = null;
			TextView title = null;
			TextView unitPrice = null;

			// ˵���ǵ�һ��
			if (position == 0) {
				convertView = inflater.inflate(R.layout.addproductitem, null);
				ImageView imgAdd = (ImageView) convertView
						.findViewById(R.id.imgAdd);
				imgAdd.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						Tool.ShowMessage(context, "��ӣ�");
					}
				});
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
				holder = new ViewHolder();
				convertView = inflater.inflate(this.res_item_layout, parent,
						false);
				holder.img = (ImageView) convertView
						.findViewById(R.id.imgPhoto);
				holder.title = (TextView) convertView
						.findViewById(R.id.txtName);
				holder.unitPrice = (TextView) convertView
						.findViewById(R.id.txtUnitPrice);
				holder.cBox = (CheckBox) convertView.findViewById(R.id.chkIn);

				convertView.setTag(holder);

			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (holder.cBox != null)
				holder.cBox.setChecked(isSelected.get(position - 1));

			holder.img.setImageResource(products.get(position - 1).getPhoto());
			holder.title.setText(position + "��"
					+ products.get(position - 1).getName().toString());
			holder.unitPrice.setText(String.valueOf(products.get(position - 1)
					.getUnitPrice()));

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
		super.onCreateOptionsMenu(menu);
		myMenu = menu;
		myMenu.clear();
		if (adapter.isBatchManager()) {
			myMenu.add(1, 4, 1, "ȫ ѡ").setIcon(R.drawable.menu_all_select);
			myMenu.add(1, 5, 2, "ȫ ��").setIcon(R.drawable.menu_all_unselect);
			myMenu.add(1, 6, 3, "����ɾ��").setIcon(R.drawable.menu_delete_list);
		} else {
			myMenu.add(1, 1, 1, "��������").setIcon(R.drawable.menu_bat_manage);
			myMenu.add(1, 2, 2, "�� ��").setIcon(R.drawable.ic_menu_search);
			myMenu.add(1, 3, 3, "���۸�����").setIcon(R.drawable.ic_menu_sort);
		}
		return true;
	}

	/**
	 * ȫѡ��ȫ��
	 * 
	 * @param isok
	 *            true:ȫѡ��false:ȫ��
	 */
	private void isSelectAll(boolean isok) {

		isSelected = new HashMap<Integer, Boolean>();
		for (int i = 0; i < products.size(); i++) {
			isSelected.put(i, isok);
		}

	}

	/**
	 * ����ɾ��
	 */
	private boolean deleteList() {
		List<Integer> ids = new ArrayList<Integer>();
		for (int i = 0; i < products.size(); i++) {
			if (isSelected.get(i)) {
				ids.add(products.get(i).getId());
			}
		}
		return productManager.DelProduct(ids);
	}

	private void initUI(int res_item_layout) {
		pageIndex = 0;
		adapter = new MyAdapter(context, res_item_layout);
		setListAdapter(adapter);
		if (myMenu != null) {
			onCreateOptionsMenu(myMenu);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 1: // ��������
			adapter = new MyAdapter(context, R.layout.productitem2);
			setListAdapter(adapter);
			if (myMenu != null) {
				onCreateOptionsMenu(myMenu);
			}
			break;
		case 2:
			Tool.ShowMessage(context, "�� ��");
			break;
		case 3:
			Tool.ShowMessage(context, "���۸�����");
			break;
		case 4: // ȫѡ
			// Tool.ShowMessage(context, "ȫѡ");
			isSelectAll(true);
			adapter.refresh();
			break;
		case 5: // ȫ��
			// Tool.ShowMessage(context, "ȫ��");
			isSelectAll(false);
			adapter.refresh();
			break;
		case 6: // ����ɾ��
			if (!hasSelected()) {
				Tool.ShowMessage(context, "��ѡ��Ҫɾ����!!");
				break;
			}

			new AlertDialog.Builder(context)
					.setIcon(R.drawable.flag)
					.setTitle("����")
					.setMessage("��ȷ��Ҫɾ��ѡ����Ʒ��?")
					.setPositiveButton("ȷ��",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									boolean isok = deleteList();
									if (isok) {
										pageIndex = 0;
										products = productManager
												.getProdcutByPager(pageIndex,
														PAGESIZE);
										adapter.refresh();
										Tool.ShowMessage(context, "����ɾ���ɹ�!");
										initUI(R.layout.productitem);
									} else {
										Tool.ShowMessage(context, "����ɾ��ʧ��!");
									}
								}
							}).setNegativeButton("ȡ��", null).create().show();

			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private boolean hasSelected() {
		boolean _isSelected = false;
		for (int i = 0; i < products.size(); i++) {
			if (isSelected.get(i)) {
				_isSelected = true;
				break;
			}
		}
		return _isSelected;
	}

}