package com.archermind.aidl;
import com.archermind.aidl.ITaskCallBack;
import com.archermind.aidl.Person;
interface ITaskBinder {

   void fuc01();
   void fuc02();
   String fuc03(in Person person);
   void registerCallBack(ITaskCallBack cb);
   void unregisterCallBack();
}