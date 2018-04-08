/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: D:\\Android ิดย๋\\androidapp\\androidapp\\5_rockplayer\\rockplayer\\src\\com\\xmobileapp\\rockplayer\\PlayerServiceInterface.aidl
 */
package com.xmobileapp.rockplayer;
import java.lang.String;
import android.os.RemoteException;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Binder;
import android.os.Parcel;
public interface PlayerServiceInterface extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.xmobileapp.rockplayer.PlayerServiceInterface
{
private static final java.lang.String DESCRIPTOR = "com.xmobileapp.rockplayer.PlayerServiceInterface";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an PlayerServiceInterface interface,
 * generating a proxy if needed.
 */
public static com.xmobileapp.rockplayer.PlayerServiceInterface asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = (android.os.IInterface)obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.xmobileapp.rockplayer.PlayerServiceInterface))) {
return ((com.xmobileapp.rockplayer.PlayerServiceInterface)iin);
}
return new com.xmobileapp.rockplayer.PlayerServiceInterface.Stub.Proxy(obj);
}
public android.os.IBinder asBinder()
{
return this;
}
public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_play:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
int _arg1;
_arg1 = data.readInt();
this.play(_arg0, _arg1);
reply.writeNoException();
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
case TRANSACTION_isPaused:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.isPaused();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_resume:
{
data.enforceInterface(DESCRIPTOR);
this.resume();
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
case TRANSACTION_playNext:
{
data.enforceInterface(DESCRIPTOR);
this.playNext();
reply.writeNoException();
return true;
}
case TRANSACTION_stop:
{
data.enforceInterface(DESCRIPTOR);
this.stop();
reply.writeNoException();
return true;
}
case TRANSACTION_getPlayingPosition:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.getPlayingPosition();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_getDuration:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.getDuration();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_setShuffle:
{
data.enforceInterface(DESCRIPTOR);
boolean _arg0;
_arg0 = (0!=data.readInt());
this.setShuffle(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_getAlbumCursorPosition:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.getAlbumCursorPosition();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_getSongCursorPosition:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.getSongCursorPosition();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_setAlbumCursorPosition:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
boolean _result = this.setAlbumCursorPosition(_arg0);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_setSongCursorPosition:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
boolean _result = this.setSongCursorPosition(_arg0);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_forward:
{
data.enforceInterface(DESCRIPTOR);
this.forward();
reply.writeNoException();
return true;
}
case TRANSACTION_reverse:
{
data.enforceInterface(DESCRIPTOR);
this.reverse();
reply.writeNoException();
return true;
}
case TRANSACTION_seekTo:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.seekTo(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_setPlaylist:
{
data.enforceInterface(DESCRIPTOR);
long _arg0;
_arg0 = data.readLong();
this.setPlaylist(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_setRecentPeriod:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
this.setRecentPeriod(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_resetAlbumCursor:
{
data.enforceInterface(DESCRIPTOR);
this.resetAlbumCursor();
reply.writeNoException();
return true;
}
case TRANSACTION_reloadCursors:
{
data.enforceInterface(DESCRIPTOR);
this.reloadCursors();
reply.writeNoException();
return true;
}
case TRANSACTION_destroy:
{
data.enforceInterface(DESCRIPTOR);
this.destroy();
reply.writeNoException();
return true;
}
case TRANSACTION_setScrobbleDroid:
{
data.enforceInterface(DESCRIPTOR);
boolean _arg0;
_arg0 = (0!=data.readInt());
this.setScrobbleDroid(_arg0);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.xmobileapp.rockplayer.PlayerServiceInterface
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
public void play(int albumCursorPosition, int songCursorPosition) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(albumCursorPosition);
_data.writeInt(songCursorPosition);
mRemote.transact(Stub.TRANSACTION_play, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
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
public boolean isPaused() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_isPaused, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public void resume() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_resume, _data, _reply, 0);
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
public void playNext() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_playNext, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
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
public int getPlayingPosition() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getPlayingPosition, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public int getDuration() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getDuration, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public void setShuffle(boolean shuffle) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(((shuffle)?(1):(0)));
mRemote.transact(Stub.TRANSACTION_setShuffle, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public int getAlbumCursorPosition() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getAlbumCursorPosition, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public int getSongCursorPosition() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getSongCursorPosition, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public boolean setAlbumCursorPosition(int position) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(position);
mRemote.transact(Stub.TRANSACTION_setAlbumCursorPosition, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public boolean setSongCursorPosition(int position) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(position);
mRemote.transact(Stub.TRANSACTION_setSongCursorPosition, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
public void forward() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_forward, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public void reverse() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_reverse, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public void seekTo(int msec) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(msec);
mRemote.transact(Stub.TRANSACTION_seekTo, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public void setPlaylist(long playlist) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeLong(playlist);
mRemote.transact(Stub.TRANSACTION_setPlaylist, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public void setRecentPeriod(int period) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(period);
mRemote.transact(Stub.TRANSACTION_setRecentPeriod, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public void resetAlbumCursor() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_resetAlbumCursor, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public void reloadCursors() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_reloadCursors, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public void destroy() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_destroy, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public void setScrobbleDroid(boolean val) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(((val)?(1):(0)));
mRemote.transact(Stub.TRANSACTION_setScrobbleDroid, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_play = (IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_isPlaying = (IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_isPaused = (IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_resume = (IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_pause = (IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_playNext = (IBinder.FIRST_CALL_TRANSACTION + 5);
static final int TRANSACTION_stop = (IBinder.FIRST_CALL_TRANSACTION + 6);
static final int TRANSACTION_getPlayingPosition = (IBinder.FIRST_CALL_TRANSACTION + 7);
static final int TRANSACTION_getDuration = (IBinder.FIRST_CALL_TRANSACTION + 8);
static final int TRANSACTION_setShuffle = (IBinder.FIRST_CALL_TRANSACTION + 9);
static final int TRANSACTION_getAlbumCursorPosition = (IBinder.FIRST_CALL_TRANSACTION + 10);
static final int TRANSACTION_getSongCursorPosition = (IBinder.FIRST_CALL_TRANSACTION + 11);
static final int TRANSACTION_setAlbumCursorPosition = (IBinder.FIRST_CALL_TRANSACTION + 12);
static final int TRANSACTION_setSongCursorPosition = (IBinder.FIRST_CALL_TRANSACTION + 13);
static final int TRANSACTION_forward = (IBinder.FIRST_CALL_TRANSACTION + 14);
static final int TRANSACTION_reverse = (IBinder.FIRST_CALL_TRANSACTION + 15);
static final int TRANSACTION_seekTo = (IBinder.FIRST_CALL_TRANSACTION + 16);
static final int TRANSACTION_setPlaylist = (IBinder.FIRST_CALL_TRANSACTION + 17);
static final int TRANSACTION_setRecentPeriod = (IBinder.FIRST_CALL_TRANSACTION + 18);
static final int TRANSACTION_resetAlbumCursor = (IBinder.FIRST_CALL_TRANSACTION + 19);
static final int TRANSACTION_reloadCursors = (IBinder.FIRST_CALL_TRANSACTION + 20);
static final int TRANSACTION_destroy = (IBinder.FIRST_CALL_TRANSACTION + 21);
static final int TRANSACTION_setScrobbleDroid = (IBinder.FIRST_CALL_TRANSACTION + 22);
}
public void play(int albumCursorPosition, int songCursorPosition) throws android.os.RemoteException;
public boolean isPlaying() throws android.os.RemoteException;
public boolean isPaused() throws android.os.RemoteException;
public void resume() throws android.os.RemoteException;
public void pause() throws android.os.RemoteException;
public void playNext() throws android.os.RemoteException;
public void stop() throws android.os.RemoteException;
public int getPlayingPosition() throws android.os.RemoteException;
public int getDuration() throws android.os.RemoteException;
public void setShuffle(boolean shuffle) throws android.os.RemoteException;
public int getAlbumCursorPosition() throws android.os.RemoteException;
public int getSongCursorPosition() throws android.os.RemoteException;
public boolean setAlbumCursorPosition(int position) throws android.os.RemoteException;
public boolean setSongCursorPosition(int position) throws android.os.RemoteException;
public void forward() throws android.os.RemoteException;
public void reverse() throws android.os.RemoteException;
public void seekTo(int msec) throws android.os.RemoteException;
public void setPlaylist(long playlist) throws android.os.RemoteException;
public void setRecentPeriod(int period) throws android.os.RemoteException;
public void resetAlbumCursor() throws android.os.RemoteException;
public void reloadCursors() throws android.os.RemoteException;
public void destroy() throws android.os.RemoteException;
public void setScrobbleDroid(boolean val) throws android.os.RemoteException;
}
