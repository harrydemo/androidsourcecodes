/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: C:\\Users\\Adminstrator\\Desktop\\AidlDemo\\src\\com\\archermind\\aidl\\ITaskCallBack.aidl
 */
package com.archermind.aidl;

public interface ITaskCallBack extends android.os.IInterface {
	/** Local-side IPC implementation stub class. */
	public static abstract class Stub extends android.os.Binder implements
			com.archermind.aidl.ITaskCallBack {
		private static final java.lang.String DESCRIPTOR = "com.archermind.aidl.ITaskCallBack";

		/** Construct the stub at attach it to the interface. */
		public Stub() {
			this.attachInterface(this, DESCRIPTOR);
		}

		/**
		 * Cast an IBinder object into an com.archermind.aidl.ITaskCallBack interface,
		 * generating a proxy if needed.
		 */
		public static com.archermind.aidl.ITaskCallBack asInterface(
				android.os.IBinder obj) {
			if ((obj == null)) {
				return null;
			}
			android.os.IInterface iin = (android.os.IInterface) obj
					.queryLocalInterface(DESCRIPTOR);
			if (((iin != null) && (iin instanceof com.archermind.aidl.ITaskCallBack))) {
				return ((com.archermind.aidl.ITaskCallBack) iin);
			}
			return new com.archermind.aidl.ITaskCallBack.Stub.Proxy(obj);
		}

		public android.os.IBinder asBinder() {
			return this;
		}

		@Override
		public boolean onTransact(int code, android.os.Parcel data,
				android.os.Parcel reply, int flags)
				throws android.os.RemoteException {
			switch (code) {
			case INTERFACE_TRANSACTION: {
				reply.writeString(DESCRIPTOR);
				return true;
			}
			case TRANSACTION_onActionBack: {
				data.enforceInterface(DESCRIPTOR);
				java.lang.String _arg0;
				_arg0 = data.readString();
				this.onActionBack(_arg0);
				reply.writeNoException();
				return true;
			}
			}
			return super.onTransact(code, data, reply, flags);
		}

		private static class Proxy implements com.archermind.aidl.ITaskCallBack {
			private android.os.IBinder mRemote;

			Proxy(android.os.IBinder remote) {
				mRemote = remote;
			}

			public android.os.IBinder asBinder() {
				return mRemote;
			}

			public java.lang.String getInterfaceDescriptor() {
				return DESCRIPTOR;
			}

			public void onActionBack(java.lang.String str)
					throws android.os.RemoteException {
				android.os.Parcel _data = android.os.Parcel.obtain();
				android.os.Parcel _reply = android.os.Parcel.obtain();
				try {
					_data.writeInterfaceToken(DESCRIPTOR);
					_data.writeString(str);
					mRemote.transact(Stub.TRANSACTION_onActionBack, _data,
							_reply, 0);
					_reply.readException();
				} finally {
					_reply.recycle();
					_data.recycle();
				}
			}
		}

		static final int TRANSACTION_onActionBack = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
	}

	public void onActionBack(java.lang.String str)
			throws android.os.RemoteException;
}
