/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: C:\\Users\\Adminstrator\\Desktop\\AidlDemo\\src\\com\\archermind\\aidl\\ITaskBinder.aidl
 */
package com.archermind.aidl;

public interface ITaskBinder extends android.os.IInterface {
	/** Local-side IPC implementation stub class. */
	public static abstract class Stub extends android.os.Binder implements
			com.archermind.aidl.ITaskBinder {
		private static final java.lang.String DESCRIPTOR = "com.archermind.aidl.ITaskBinder";

		/** Construct the stub at attach it to the interface. */
		public Stub() {
			this.attachInterface(this, DESCRIPTOR);
		}

		/**
		 * Cast an IBinder object into an com.archermind.aidl.ITaskBinder interface,
		 * generating a proxy if needed.
		 */
		public static com.archermind.aidl.ITaskBinder asInterface(
				android.os.IBinder obj) {
			if ((obj == null)) {
				return null;
			}
			android.os.IInterface iin = (android.os.IInterface) obj
					.queryLocalInterface(DESCRIPTOR);
			if (((iin != null) && (iin instanceof com.archermind.aidl.ITaskBinder))) {
				return ((com.archermind.aidl.ITaskBinder) iin);
			}
			return new com.archermind.aidl.ITaskBinder.Stub.Proxy(obj);
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
			case TRANSACTION_fuc01: {
				data.enforceInterface(DESCRIPTOR);
				this.fuc01();
				reply.writeNoException();
				return true;
			}
			case TRANSACTION_fuc02: {
				data.enforceInterface(DESCRIPTOR);
				this.fuc02();
				reply.writeNoException();
				return true;
			}
			case TRANSACTION_fuc03: {
				data.enforceInterface(DESCRIPTOR);
				com.archermind.aidl.Person _arg0;
				if ((0 != data.readInt())) {
					_arg0 = com.archermind.aidl.Person.CREATOR
							.createFromParcel(data);
				} else {
					_arg0 = null;
				}
				java.lang.String _result = this.fuc03(_arg0);
				reply.writeNoException();
				reply.writeString(_result);
				return true;
			}
			case TRANSACTION_registerCallBack: {
				data.enforceInterface(DESCRIPTOR);
				com.archermind.aidl.ITaskCallBack _arg0;
				_arg0 = com.archermind.aidl.ITaskCallBack.Stub.asInterface(data
						.readStrongBinder());
				this.registerCallBack(_arg0);
				reply.writeNoException();
				return true;
			}
			case TRANSACTION_unregisterCallBack: {
				data.enforceInterface(DESCRIPTOR);
				this.unregisterCallBack();
				reply.writeNoException();
				return true;
			}
			}
			return super.onTransact(code, data, reply, flags);
		}

		private static class Proxy implements com.archermind.aidl.ITaskBinder {
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

			public void fuc01() throws android.os.RemoteException {
				android.os.Parcel _data = android.os.Parcel.obtain();
				android.os.Parcel _reply = android.os.Parcel.obtain();
				try {
					_data.writeInterfaceToken(DESCRIPTOR);
					mRemote.transact(Stub.TRANSACTION_fuc01, _data, _reply, 0);
					_reply.readException();
				} finally {
					_reply.recycle();
					_data.recycle();
				}
			}

			public void fuc02() throws android.os.RemoteException {
				android.os.Parcel _data = android.os.Parcel.obtain();
				android.os.Parcel _reply = android.os.Parcel.obtain();
				try {
					_data.writeInterfaceToken(DESCRIPTOR);
					mRemote.transact(Stub.TRANSACTION_fuc02, _data, _reply, 0);
					_reply.readException();
				} finally {
					_reply.recycle();
					_data.recycle();
				}
			}

			public java.lang.String fuc03(com.archermind.aidl.Person person)
					throws android.os.RemoteException {
				android.os.Parcel _data = android.os.Parcel.obtain();
				android.os.Parcel _reply = android.os.Parcel.obtain();
				java.lang.String _result;
				try {
					_data.writeInterfaceToken(DESCRIPTOR);
					if ((person != null)) {
						_data.writeInt(1);
						person.writeToParcel(_data, 0);
					} else {
						_data.writeInt(0);
					}
					mRemote.transact(Stub.TRANSACTION_fuc03, _data, _reply, 0);
					_reply.readException();
					_result = _reply.readString();
				} finally {
					_reply.recycle();
					_data.recycle();
				}
				return _result;
			}

			public void registerCallBack(com.archermind.aidl.ITaskCallBack cb)
					throws android.os.RemoteException {
				android.os.Parcel _data = android.os.Parcel.obtain();
				android.os.Parcel _reply = android.os.Parcel.obtain();
				try {
					_data.writeInterfaceToken(DESCRIPTOR);
					_data.writeStrongBinder((((cb != null)) ? (cb.asBinder())
							: (null)));
					mRemote.transact(Stub.TRANSACTION_registerCallBack, _data,
							_reply, 0);
					_reply.readException();
				} finally {
					_reply.recycle();
					_data.recycle();
				}
			}

			public void unregisterCallBack() throws android.os.RemoteException {
				android.os.Parcel _data = android.os.Parcel.obtain();
				android.os.Parcel _reply = android.os.Parcel.obtain();
				try {
					_data.writeInterfaceToken(DESCRIPTOR);
					mRemote.transact(Stub.TRANSACTION_unregisterCallBack,
							_data, _reply, 0);
					_reply.readException();
				} finally {
					_reply.recycle();
					_data.recycle();
				}
			}
		}

		static final int TRANSACTION_fuc01 = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
		static final int TRANSACTION_fuc02 = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
		static final int TRANSACTION_fuc03 = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
		static final int TRANSACTION_registerCallBack = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
		static final int TRANSACTION_unregisterCallBack = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
	}

	public void fuc01() throws android.os.RemoteException;

	public void fuc02() throws android.os.RemoteException;

	public java.lang.String fuc03(com.archermind.aidl.Person person)
			throws android.os.RemoteException;

	public void registerCallBack(com.archermind.aidl.ITaskCallBack cb)
			throws android.os.RemoteException;

	public void unregisterCallBack() throws android.os.RemoteException;
}
