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

import android.provider.BaseColumns;

public class ContactColumn implements BaseColumns {
	public ContactColumn() {
	}

	// ����
	public static final String NAME = "name";
	public static final String MOBILE = "mobileNumber";
	public static final String EMAIL = "email";
	public static final String CREATED = "createdDate";
	public static final String MODIFIED = "modifiedDate";
	// �� ����ֵ
	public static final int _ID_COLUMN = 0;
	public static final int NAME_COLUMN = 1;
	public static final int MOBILE_COLUMN = 2;
	public static final int EMAIL_COLUMN = 3;
	public static final int CREATED_COLUMN = 4;
	public static final int MODIFIED_COLUMN = 5;
	// ��ѯ���
	public static final String[] PROJECTION = { _ID,// 0
			NAME,// 1
			MOBILE,// 2
			EMAIL // 3
	};

}
