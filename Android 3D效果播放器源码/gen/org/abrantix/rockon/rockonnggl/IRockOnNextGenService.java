/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: D:\\Android Դ��\\3Dmusic\\fabrantes-rockonnggl-b8c8297\\src\\org\\abrantix\\rockon\\rockonnggl\\IRockOnNextGenService.aidl
 */
package org.abrantix.rockon.rockonnggl;
public interface IRockOnNextGenService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements org.abrantix.rockon.rockonnggl.IRockOnNextGenService
{
private static final java.lang.String DESCRIPTOR = "org.abrantix.rockon.rockonnggl.IRockOnNextGenService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an org.abrantix.rockon.rockonnggl.IRockOnNextGenService interface,
 * generating a proxy if needed.
 */
public static org.abrantix.rockon.rockonnggl.IRockOnNextGenService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = (android.os.IInterface)obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof org.abrantix.rockon.rockonnggl.IRockOnNextGenService))) {
return ((org.abrantix.rockon.rockonnggl.IRockOnNextGenService)iin);
}
return new org.abrantix.rockon.rockonnggl.IRockOnNextGenService.Stub.Proxy(obj);
}
public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_openFile:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
boolean _arg1;
_arg1 = (0!=data.readInt());
this.openFile(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_openFileAsync:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.openFileAsync(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_open:
{
data.enforceInterface(DESCRIPTOR);
long[] _arg0;
_arg0 = data.createLongArray();
int _arg1;
_arg1 = data.readInt();
this.open(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_getQueuePosition:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.getQueuePosition();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_isPlaying:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.isPlaying();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_stop:
{
data.enforceInterface(DESCRIPTOR);
this.stop();
reply.writeNoException();
return true;
}
case TRANSACTION_pause:
{
data.enforceInterface(DESCRIPTOR);
this.pause();
reply.writeNoException();
return true;
}
case TRANSACTION_play:
{
data.enforceInterface(DESCRIPTOR);
this.play();
reply.writeNoException();
return true;
}
case TRANSACTION_prev:
{
data.enforceInterface(DESCRIPTOR);
this.prev();
reply.writeNoException();
return true;
}
case TRANSACTION_next:
{
data.enforceInterface(DESCRIPTOR);
this.next();
reply.writeNoException();
return true;
}
case TRANSACTION_duration:
{
data.enforceInterface(DESCRIPTOR);
long _result = this.duration();
reply.writeNoException();
reply.writeLong(_result);
return true;
}
case TRANSACTION_position:
{
data.enforceInterface(DESCRIPTOR);
long _result = this.position();
reply.writeNoException();
reply.writeLong(_result);
return true;
}
case TRANSACTION_seek:
{
data.enforceInterface(DESCRIPTOR);
long _arg0;
_arg0 = data.readLong();
long _result = this.seek(_arg0);
reply.writeNoException();
reply.writeLong(_result);
return true;
}
case TRANSACTION_getTrackName:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _result = this.getTrackName();
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_getAlbumName:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _result = this.getAlbumName();
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_getAlbumId:
{
data.enforceInterface(DESCRIPTOR);
long _result = this.getAlbumId();
reply.writeNoException();
reply.writeLong(_result);
return true;
}
case TRANSACTION_getArtistName:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _result = this.getArtistName();
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_getArtistId:
{
data.enforceInterface(DESCRIPTOR);
long _result = this.getArtistId();
reply.writeNoException();
reply.writeLong(_result);
return true;
}
case TRANSACTION_enqueue:
{
data.enforceInterface(DESCRIPTOR);
long[] _arg0;
_arg0 = data.createLongArray();
int _arg1;
_arg1 = data.readInt();
this.enqueue(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_getQueue:
{
data.enforceInterface(DESCRIPTOR);
long[] _result = this.getQueue();
reply.writeNoException();
reply.writeLongArray(_result);
return true;
}
case TRANSACTION_getOutstandingQueue:
{
data.enforceInterface(DESCRIPTOR);
long[] _result = this.getOutstandingQueue();
reply.writeNoException();
reply.writeLongArray(_result);
return true;
}
case TRANSACTION_moveQueueItem:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
int _arg1;
_arg1 = data.readInt();
this.moveQueueItem(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_setQueuePosition:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.setQueuePosition(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_getPath:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _result = this.getPath();
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_getAudioId:
{
data.enforceInterface(DESCRIPTOR);
long _result = this.getAudioId();
reply.writeNoException();
reply.writeLong(_result);
return true;
}
case TRANSACTION_setPlaylistId:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.setPlaylistId(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_getPlaylistId:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.getPlaylistId();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_setShuffleMode:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.setShuffleMode(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_getShuffleMode:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.getShuffleMode();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_setRockOnShuffleMode:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.setRockOnShuffleMode(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_getRockOnShuffleMode:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.getRockOnShuffleMode();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_removeTracks:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
int _arg1;
_arg1 = data.readInt();
int _result = this.removeTracks(_arg0, _arg1);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_removeTrack:
{
data.enforceInterface(DESCRIPTOR);
long _arg0;
_arg0 = data.readLong();
int _result = this.removeTrack(_arg0);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_setRepeatMode:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.setRepeatMode(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_getRepeatMode:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.getRepeatMode();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_setRockOnRepeatMode:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.setRockOnRepeatMode(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_getRockOnRepeatMode:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.getRockOnRepeatMode();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_getMediaMountedCount:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.getMediaMountedCount();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_setScrobbler:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.setScrobbler(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_setLockScreen:
{
data.enforceInterface(DESCRIPTOR);
boolean _arg0;
_arg0 = (0!=data.readInt());
this.setLockScreen(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_registerScreenOnReceiver:
{
data.enforceInterface(DESCRIPTOR);
this.registerScreenOnReceiver();
reply.writeNoException();
return true;
}
case TRANSACTION_unregisterScreenOnReceiver:
{
data.enforceInterface(DESCRIPTOR);
this.unregisterScreenOnReceiver();
reply.writeNoException();
return true;
}
case TRANSACTION_prepareForCrash:
{
data.enforceInterface(DESCRIPTOR);
this.prepareForCrash();
reply.writeNoException();
return true;
}
case TRANSACTION_isEqEnabled:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.isEqEnabled();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_enableEq:
{
data.enforceInterface(DESCRIPTOR);
this.enableEq();
reply.writeNoException();
return true;
}
case TRANSACTION_disableEq:
{
data.enforceInterface(DESCRIPTOR);
this.disableEq();
reply.writeNoException();
return true;
}
case TRANSACTION_getEqBandHz:
{
data.enforceInterface(DESCRIPTOR);
int[] _result = this.getEqBandHz();
reply.writeNoException();
reply.writeIntArray(_result);
return true;
}
case TRANSACTION_getEqBandLevels:
{
data.enforceInterface(DESCRIPTOR);
int[] _result = this.getEqBandLevels();
reply.writeNoException();
reply.writeIntArray(_result);
return true;
}
case TRANSACTION_getEqCurrentPreset:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.getEqCurrentPreset();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_getEqPresetNames:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String[] _result = this.getEqPresetNames();
reply.writeNoException();
reply.writeStringArray(_result);
return true;
}
case TRANSACTION_getEqLevelRange:
{
data.enforceInterface(DESCRIPTOR);
int[] _result = this.getEqLevelRange();
reply.writeNoException();
reply.writeIntArray(_result);
return true;
}
case TRANSACTION_getEqNumBands:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.getEqNumBands();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_setEqBandLevel:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
int _arg1;
_arg1 = data.readInt();
this.setEqBandLevel(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_setEqPreset:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.setEqPreset(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_trackPage:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.trackPage(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_trackEvent:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
java.lang.String _arg2;
_arg2 = data.readString();
int _arg3;
_arg3 = data.readInt();
this.trackEvent(_arg0, _arg1, _arg2, _arg3);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements org.abrantix.rockon.rockonnggl.IRockOnNextGenService
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
public void openFile(java.lang.String path, boolean oneShot) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(path);
_data.writeInt(((oneShot)?(1):(0)));
mRemote.transact(Stub.TRANSACTION_openFile, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public void openFileAsync(java.lang.String path) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(path);
mRemote.transact(Stub.TRANSACTION_openFileAsync, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public void open(long[] list, int position) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeLongArray(list);
_data.writeInt(position);
mRemote.transact(Stub.TRANSACTION_open, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public int getQueuePosition() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getQueuePosition, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public boolean isPlaying() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_isPlaying, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public void stop() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_stop, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public void pause() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_pause, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public void play() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_play, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public void prev() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_prev, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public void next() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_next, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public long duration() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
long _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_duration, _data, _reply, 0);
_reply.readException();
_result = _reply.readLong();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public long position() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
long _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_position, _data, _reply, 0);
_reply.readException();
_result = _reply.readLong();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public long seek(long pos) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
long _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeLong(pos);
mRemote.transact(Stub.TRANSACTION_seek, _data, _reply, 0);
_reply.readException();
_result = _reply.readLong();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public java.lang.String getTrackName() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getTrackName, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public java.lang.String getAlbumName() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getAlbumName, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public long getAlbumId() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
long _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getAlbumId, _data, _reply, 0);
_reply.readException();
_result = _reply.readLong();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public java.lang.String getArtistName() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getArtistName, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public long getArtistId() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
long _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getArtistId, _data, _reply, 0);
_reply.readException();
_result = _reply.readLong();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public void enqueue(long[] list, int action) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeLongArray(list);
_data.writeInt(action);
mRemote.transact(Stub.TRANSACTION_enqueue, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public long[] getQueue() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
long[] _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getQueue, _data, _reply, 0);
_reply.readException();
_result = _reply.createLongArray();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public long[] getOutstandingQueue() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
long[] _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getOutstandingQueue, _data, _reply, 0);
_reply.readException();
_result = _reply.createLongArray();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public void moveQueueItem(int from, int to) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(from);
_data.writeInt(to);
mRemote.transact(Stub.TRANSACTION_moveQueueItem, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public void setQueuePosition(int index) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(index);
mRemote.transact(Stub.TRANSACTION_setQueuePosition, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public java.lang.String getPath() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getPath, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public long getAudioId() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
long _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getAudioId, _data, _reply, 0);
_reply.readException();
_result = _reply.readLong();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public void setPlaylistId(int playlistId) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(playlistId);
mRemote.transact(Stub.TRANSACTION_setPlaylistId, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public int getPlaylistId() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getPlaylistId, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public void setShuffleMode(int shufflemode) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(shufflemode);
mRemote.transact(Stub.TRANSACTION_setShuffleMode, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public int getShuffleMode() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getShuffleMode, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public void setRockOnShuffleMode(int shufflemode) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(shufflemode);
mRemote.transact(Stub.TRANSACTION_setRockOnShuffleMode, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public int getRockOnShuffleMode() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getRockOnShuffleMode, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public int removeTracks(int first, int last) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(first);
_data.writeInt(last);
mRemote.transact(Stub.TRANSACTION_removeTracks, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public int removeTrack(long id) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeLong(id);
mRemote.transact(Stub.TRANSACTION_removeTrack, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public void setRepeatMode(int repeatmode) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(repeatmode);
mRemote.transact(Stub.TRANSACTION_setRepeatMode, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public int getRepeatMode() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getRepeatMode, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public void setRockOnRepeatMode(int repeatmode) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(repeatmode);
mRemote.transact(Stub.TRANSACTION_setRockOnRepeatMode, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public int getRockOnRepeatMode() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getRockOnRepeatMode, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public int getMediaMountedCount() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getMediaMountedCount, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public void setScrobbler(java.lang.String scrobblerName) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(scrobblerName);
mRemote.transact(Stub.TRANSACTION_setScrobbler, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public void setLockScreen(boolean lock) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(((lock)?(1):(0)));
mRemote.transact(Stub.TRANSACTION_setLockScreen, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public void registerScreenOnReceiver() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_registerScreenOnReceiver, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public void unregisterScreenOnReceiver() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_unregisterScreenOnReceiver, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public void prepareForCrash() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_prepareForCrash, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public boolean isEqEnabled() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_isEqEnabled, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public void enableEq() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_enableEq, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public void disableEq() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_disableEq, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public int[] getEqBandHz() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int[] _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getEqBandHz, _data, _reply, 0);
_reply.readException();
_result = _reply.createIntArray();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public int[] getEqBandLevels() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int[] _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getEqBandLevels, _data, _reply, 0);
_reply.readException();
_result = _reply.createIntArray();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public int getEqCurrentPreset() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getEqCurrentPreset, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public java.lang.String[] getEqPresetNames() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String[] _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getEqPresetNames, _data, _reply, 0);
_reply.readException();
_result = _reply.createStringArray();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public int[] getEqLevelRange() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int[] _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getEqLevelRange, _data, _reply, 0);
_reply.readException();
_result = _reply.createIntArray();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public int getEqNumBands() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getEqNumBands, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public void setEqBandLevel(int bandIdx, int level) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(bandIdx);
_data.writeInt(level);
mRemote.transact(Stub.TRANSACTION_setEqBandLevel, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public void setEqPreset(int presetIdx) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(presetIdx);
mRemote.transact(Stub.TRANSACTION_setEqPreset, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public void trackPage(java.lang.String pageName) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(pageName);
mRemote.transact(Stub.TRANSACTION_trackPage, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public void trackEvent(java.lang.String cat, java.lang.String action, java.lang.String label, int val) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(cat);
_data.writeString(action);
_data.writeString(label);
_data.writeInt(val);
mRemote.transact(Stub.TRANSACTION_trackEvent, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_openFile = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_openFileAsync = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_open = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_getQueuePosition = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_isPlaying = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_stop = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
static final int TRANSACTION_pause = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
static final int TRANSACTION_play = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
static final int TRANSACTION_prev = (android.os.IBinder.FIRST_CALL_TRANSACTION + 8);
static final int TRANSACTION_next = (android.os.IBinder.FIRST_CALL_TRANSACTION + 9);
static final int TRANSACTION_duration = (android.os.IBinder.FIRST_CALL_TRANSACTION + 10);
static final int TRANSACTION_position = (android.os.IBinder.FIRST_CALL_TRANSACTION + 11);
static final int TRANSACTION_seek = (android.os.IBinder.FIRST_CALL_TRANSACTION + 12);
static final int TRANSACTION_getTrackName = (android.os.IBinder.FIRST_CALL_TRANSACTION + 13);
static final int TRANSACTION_getAlbumName = (android.os.IBinder.FIRST_CALL_TRANSACTION + 14);
static final int TRANSACTION_getAlbumId = (android.os.IBinder.FIRST_CALL_TRANSACTION + 15);
static final int TRANSACTION_getArtistName = (android.os.IBinder.FIRST_CALL_TRANSACTION + 16);
static final int TRANSACTION_getArtistId = (android.os.IBinder.FIRST_CALL_TRANSACTION + 17);
static final int TRANSACTION_enqueue = (android.os.IBinder.FIRST_CALL_TRANSACTION + 18);
static final int TRANSACTION_getQueue = (android.os.IBinder.FIRST_CALL_TRANSACTION + 19);
static final int TRANSACTION_getOutstandingQueue = (android.os.IBinder.FIRST_CALL_TRANSACTION + 20);
static final int TRANSACTION_moveQueueItem = (android.os.IBinder.FIRST_CALL_TRANSACTION + 21);
static final int TRANSACTION_setQueuePosition = (android.os.IBinder.FIRST_CALL_TRANSACTION + 22);
static final int TRANSACTION_getPath = (android.os.IBinder.FIRST_CALL_TRANSACTION + 23);
static final int TRANSACTION_getAudioId = (android.os.IBinder.FIRST_CALL_TRANSACTION + 24);
static final int TRANSACTION_setPlaylistId = (android.os.IBinder.FIRST_CALL_TRANSACTION + 25);
static final int TRANSACTION_getPlaylistId = (android.os.IBinder.FIRST_CALL_TRANSACTION + 26);
static final int TRANSACTION_setShuffleMode = (android.os.IBinder.FIRST_CALL_TRANSACTION + 27);
static final int TRANSACTION_getShuffleMode = (android.os.IBinder.FIRST_CALL_TRANSACTION + 28);
static final int TRANSACTION_setRockOnShuffleMode = (android.os.IBinder.FIRST_CALL_TRANSACTION + 29);
static final int TRANSACTION_getRockOnShuffleMode = (android.os.IBinder.FIRST_CALL_TRANSACTION + 30);
static final int TRANSACTION_removeTracks = (android.os.IBinder.FIRST_CALL_TRANSACTION + 31);
static final int TRANSACTION_removeTrack = (android.os.IBinder.FIRST_CALL_TRANSACTION + 32);
static final int TRANSACTION_setRepeatMode = (android.os.IBinder.FIRST_CALL_TRANSACTION + 33);
static final int TRANSACTION_getRepeatMode = (android.os.IBinder.FIRST_CALL_TRANSACTION + 34);
static final int TRANSACTION_setRockOnRepeatMode = (android.os.IBinder.FIRST_CALL_TRANSACTION + 35);
static final int TRANSACTION_getRockOnRepeatMode = (android.os.IBinder.FIRST_CALL_TRANSACTION + 36);
static final int TRANSACTION_getMediaMountedCount = (android.os.IBinder.FIRST_CALL_TRANSACTION + 37);
static final int TRANSACTION_setScrobbler = (android.os.IBinder.FIRST_CALL_TRANSACTION + 38);
static final int TRANSACTION_setLockScreen = (android.os.IBinder.FIRST_CALL_TRANSACTION + 39);
static final int TRANSACTION_registerScreenOnReceiver = (android.os.IBinder.FIRST_CALL_TRANSACTION + 40);
static final int TRANSACTION_unregisterScreenOnReceiver = (android.os.IBinder.FIRST_CALL_TRANSACTION + 41);
static final int TRANSACTION_prepareForCrash = (android.os.IBinder.FIRST_CALL_TRANSACTION + 42);
static final int TRANSACTION_isEqEnabled = (android.os.IBinder.FIRST_CALL_TRANSACTION + 43);
static final int TRANSACTION_enableEq = (android.os.IBinder.FIRST_CALL_TRANSACTION + 44);
static final int TRANSACTION_disableEq = (android.os.IBinder.FIRST_CALL_TRANSACTION + 45);
static final int TRANSACTION_getEqBandHz = (android.os.IBinder.FIRST_CALL_TRANSACTION + 46);
static final int TRANSACTION_getEqBandLevels = (android.os.IBinder.FIRST_CALL_TRANSACTION + 47);
static final int TRANSACTION_getEqCurrentPreset = (android.os.IBinder.FIRST_CALL_TRANSACTION + 48);
static final int TRANSACTION_getEqPresetNames = (android.os.IBinder.FIRST_CALL_TRANSACTION + 49);
static final int TRANSACTION_getEqLevelRange = (android.os.IBinder.FIRST_CALL_TRANSACTION + 50);
static final int TRANSACTION_getEqNumBands = (android.os.IBinder.FIRST_CALL_TRANSACTION + 51);
static final int TRANSACTION_setEqBandLevel = (android.os.IBinder.FIRST_CALL_TRANSACTION + 52);
static final int TRANSACTION_setEqPreset = (android.os.IBinder.FIRST_CALL_TRANSACTION + 53);
static final int TRANSACTION_trackPage = (android.os.IBinder.FIRST_CALL_TRANSACTION + 54);
static final int TRANSACTION_trackEvent = (android.os.IBinder.FIRST_CALL_TRANSACTION + 55);
}
public void openFile(java.lang.String path, boolean oneShot) throws android.os.RemoteException;
public void openFileAsync(java.lang.String path) throws android.os.RemoteException;
public void open(long[] list, int position) throws android.os.RemoteException;
public int getQueuePosition() throws android.os.RemoteException;
public boolean isPlaying() throws android.os.RemoteException;
public void stop() throws android.os.RemoteException;
public void pause() throws android.os.RemoteException;
public void play() throws android.os.RemoteException;
public void prev() throws android.os.RemoteException;
public void next() throws android.os.RemoteException;
public long duration() throws android.os.RemoteException;
public long position() throws android.os.RemoteException;
public long seek(long pos) throws android.os.RemoteException;
public java.lang.String getTrackName() throws android.os.RemoteException;
public java.lang.String getAlbumName() throws android.os.RemoteException;
public long getAlbumId() throws android.os.RemoteException;
public java.lang.String getArtistName() throws android.os.RemoteException;
public long getArtistId() throws android.os.RemoteException;
public void enqueue(long[] list, int action) throws android.os.RemoteException;
public long[] getQueue() throws android.os.RemoteException;
public long[] getOutstandingQueue() throws android.os.RemoteException;
public void moveQueueItem(int from, int to) throws android.os.RemoteException;
public void setQueuePosition(int index) throws android.os.RemoteException;
public java.lang.String getPath() throws android.os.RemoteException;
public long getAudioId() throws android.os.RemoteException;
public void setPlaylistId(int playlistId) throws android.os.RemoteException;
public int getPlaylistId() throws android.os.RemoteException;
public void setShuffleMode(int shufflemode) throws android.os.RemoteException;
public int getShuffleMode() throws android.os.RemoteException;
public void setRockOnShuffleMode(int shufflemode) throws android.os.RemoteException;
public int getRockOnShuffleMode() throws android.os.RemoteException;
public int removeTracks(int first, int last) throws android.os.RemoteException;
public int removeTrack(long id) throws android.os.RemoteException;
public void setRepeatMode(int repeatmode) throws android.os.RemoteException;
public int getRepeatMode() throws android.os.RemoteException;
public void setRockOnRepeatMode(int repeatmode) throws android.os.RemoteException;
public int getRockOnRepeatMode() throws android.os.RemoteException;
public int getMediaMountedCount() throws android.os.RemoteException;
public void setScrobbler(java.lang.String scrobblerName) throws android.os.RemoteException;
public void setLockScreen(boolean lock) throws android.os.RemoteException;
public void registerScreenOnReceiver() throws android.os.RemoteException;
public void unregisterScreenOnReceiver() throws android.os.RemoteException;
public void prepareForCrash() throws android.os.RemoteException;
public boolean isEqEnabled() throws android.os.RemoteException;
public void enableEq() throws android.os.RemoteException;
public void disableEq() throws android.os.RemoteException;
public int[] getEqBandHz() throws android.os.RemoteException;
public int[] getEqBandLevels() throws android.os.RemoteException;
public int getEqCurrentPreset() throws android.os.RemoteException;
public java.lang.String[] getEqPresetNames() throws android.os.RemoteException;
public int[] getEqLevelRange() throws android.os.RemoteException;
public int getEqNumBands() throws android.os.RemoteException;
public void setEqBandLevel(int bandIdx, int level) throws android.os.RemoteException;
public void setEqPreset(int presetIdx) throws android.os.RemoteException;
public void trackPage(java.lang.String pageName) throws android.os.RemoteException;
public void trackEvent(java.lang.String cat, java.lang.String action, java.lang.String label, int val) throws android.os.RemoteException;
}
