package com.kris.search;

import java.util.Random;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class SearchFlyActivity extends Activity implements OnClickListener {  
    public static final String[] keywords = { "QQ", "Sodino", "APK", "GFW", "Ǧ��",//  
        "����", "���澫��", "MacBook Pro", "ƽ�����", "��ʫ����",//  
        "����ŷ TR-100", "�ʼǱ�", "SPY Mouse", "Thinkpad E40", "�������",//  
        "�ڴ�����", "��ͼ", "����", "����", "����",//  
        "ͨѶ¼", "������", "CSDN leak", "��ȫ", "3D",//  
        "��Ů", "����", "4743G", "����", "����",//  
        "ŷ��", "�����", "��ŭ��С��", "mmShow", "���׹�����",//  
        "iciba", "��ˮ��ϵ", "����App", "������", "365����",//  
        "����ʶ��", "Chrome", "Safari", "�й���Siri", "A5������",//  
        "iPhone4S", "Ħ�� ME525", "���� M9", "�῵ S2500" };  
private KeywordsFlow keywordsFlow;  
private Button btnIn, btnOut;  

public void onCreate(Bundle savedInstanceState) {  
    super.onCreate(savedInstanceState);  
    setContentView(R.layout.main);  
    btnIn = (Button) findViewById(R.id.button1);  
    btnOut = (Button) findViewById(R.id.button2);  
    btnIn.setOnClickListener(this);  
    btnOut.setOnClickListener(this);  
    keywordsFlow = (KeywordsFlow) findViewById(R.id.frameLayout1);  
    keywordsFlow.setDuration(800l);  
    keywordsFlow.setOnItemClickListener(this);  
    // ���  
    feedKeywordsFlow(keywordsFlow, keywords);  
    keywordsFlow.go2Show(KeywordsFlow.ANIMATION_IN);  
}  

private static void feedKeywordsFlow(KeywordsFlow keywordsFlow, String[] arr) {  
    Random random = new Random();  
    for (int i = 0; i < KeywordsFlow.MAX; i++) {  
        int ran = random.nextInt(arr.length);  
        String tmp = arr[ran];  
        keywordsFlow.feedKeyword(tmp);  
    }  
}  

@Override  
public void onClick(View v) {  
    if (v == btnIn) {  
        keywordsFlow.rubKeywords();  
        // keywordsFlow.rubAllViews();  
        feedKeywordsFlow(keywordsFlow, keywords);  
        keywordsFlow.go2Show(KeywordsFlow.ANIMATION_IN);  
    } else if (v == btnOut) {  
        keywordsFlow.rubKeywords();  
        // keywordsFlow.rubAllViews();  
        feedKeywordsFlow(keywordsFlow, keywords);  
        keywordsFlow.go2Show(KeywordsFlow.ANIMATION_OUT);  
    } else if (v instanceof TextView) {  
        String keyword = ((TextView) v).getText().toString();  
//        Intent intent = new Intent();  
//        intent.setAction(Intent.ACTION_VIEW);  
//        intent.addCategory(Intent.CATEGORY_DEFAULT);  
//        intent.setData(Uri.parse("http://www.google.com.hk/#q=" + keyword));  
//        startActivity(intent);
        Log.e("Search", keyword);
    }  
}  
}