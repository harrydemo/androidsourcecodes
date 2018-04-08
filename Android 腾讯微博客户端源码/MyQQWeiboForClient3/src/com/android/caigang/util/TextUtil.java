package com.android.caigang.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;

import com.android.caigang.R;

public class TextUtil {
	
	public static Map<String,Integer> faceNameToDrawableId = new HashMap<String, Integer>();
	public static Map<String,String> drawableIdToFaceName = new HashMap<String, String>();
	
	static{
		faceNameToDrawableId.put("��Ƥ", R.drawable.f000);
		faceNameToDrawableId.put("����", R.drawable.f001);
		faceNameToDrawableId.put("����", R.drawable.f002);
		faceNameToDrawableId.put("�ѹ�", R.drawable.f003);
		faceNameToDrawableId.put("��", R.drawable.f004);
		faceNameToDrawableId.put("�亹", R.drawable.f005);
		faceNameToDrawableId.put("ץ��", R.drawable.f006);
		faceNameToDrawableId.put("��", R.drawable.f007);
		faceNameToDrawableId.put("͵Ц", R.drawable.f008);
		faceNameToDrawableId.put("�ɰ�", R.drawable.f009);
		faceNameToDrawableId.put("����", R.drawable.f010);
		faceNameToDrawableId.put("����", R.drawable.f011);
		faceNameToDrawableId.put("΢Ц", R.drawable.f012);
		faceNameToDrawableId.put("Ʋ��", R.drawable.f013);
		faceNameToDrawableId.put("ɫ", R.drawable.f014);
		faceNameToDrawableId.put("����", R.drawable.f015);
		faceNameToDrawableId.put("����", R.drawable.f016);
		faceNameToDrawableId.put("����", R.drawable.f017);
		faceNameToDrawableId.put("����", R.drawable.f018);
		faceNameToDrawableId.put("��", R.drawable.f019);
		faceNameToDrawableId.put("��", R.drawable.f020);
		
		faceNameToDrawableId.put("����", R.drawable.f021);
		faceNameToDrawableId.put("��ŭ", R.drawable.f022);
		faceNameToDrawableId.put("���", R.drawable.f023);
		faceNameToDrawableId.put("����", R.drawable.f024);
		faceNameToDrawableId.put("�ټ�", R.drawable.f025);
		faceNameToDrawableId.put("�ô�", R.drawable.f026);
		faceNameToDrawableId.put("����", R.drawable.f027);
		faceNameToDrawableId.put("ί��", R.drawable.f028);
		faceNameToDrawableId.put("����", R.drawable.f029);
		faceNameToDrawableId.put("˯", R.drawable.f030);
		faceNameToDrawableId.put("����", R.drawable.f031);
		
		faceNameToDrawableId.put("��Ц", R.drawable.f032);
		faceNameToDrawableId.put("��Ƥ", R.drawable.f033);
		faceNameToDrawableId.put("����", R.drawable.f034);
		faceNameToDrawableId.put("�ܶ�", R.drawable.f035);
		faceNameToDrawableId.put("�Һߺ�", R.drawable.f036);
		faceNameToDrawableId.put("ӵ��", R.drawable.f037);
		faceNameToDrawableId.put("��Ц", R.drawable.f038);
		faceNameToDrawableId.put("����", R.drawable.f039);
		faceNameToDrawableId.put("��", R.drawable.f040);
		faceNameToDrawableId.put("���", R.drawable.f041);
		faceNameToDrawableId.put("����", R.drawable.f042);
		
		faceNameToDrawableId.put("����", R.drawable.f043);
		faceNameToDrawableId.put("����", R.drawable.f044);
		faceNameToDrawableId.put("�ٱ�", R.drawable.f045);
		faceNameToDrawableId.put("����", R.drawable.f046);
		faceNameToDrawableId.put("�ܴ���", R.drawable.f047);
		faceNameToDrawableId.put("��ߺ�", R.drawable.f048);
		faceNameToDrawableId.put("��Ƿ", R.drawable.f049);
		faceNameToDrawableId.put("�����", R.drawable.f050);
		faceNameToDrawableId.put("��", R.drawable.f051);
		faceNameToDrawableId.put("����", R.drawable.f052);
		faceNameToDrawableId.put("����", R.drawable.f053);
		
		faceNameToDrawableId.put("��ĥ", R.drawable.f054);
		faceNameToDrawableId.put("ʾ��", R.drawable.f055);
		faceNameToDrawableId.put("����", R.drawable.f056);
		faceNameToDrawableId.put("����", R.drawable.f057);
		faceNameToDrawableId.put("����", R.drawable.f058);
		faceNameToDrawableId.put("����", R.drawable.f059);
		faceNameToDrawableId.put("ը��", R.drawable.f060);
		faceNameToDrawableId.put("��", R.drawable.f061);
		faceNameToDrawableId.put("����", R.drawable.f062);
		faceNameToDrawableId.put("ư��", R.drawable.f063);
		faceNameToDrawableId.put("���", R.drawable.f064);
		
		faceNameToDrawableId.put("����", R.drawable.f065);
		faceNameToDrawableId.put("��", R.drawable.f066);
		faceNameToDrawableId.put("��", R.drawable.f067);
		faceNameToDrawableId.put("õ��", R.drawable.f068);
		faceNameToDrawableId.put("��л", R.drawable.f069);
		faceNameToDrawableId.put("����", R.drawable.f070);
		faceNameToDrawableId.put("̫��", R.drawable.f071);
		faceNameToDrawableId.put("����", R.drawable.f072);
		faceNameToDrawableId.put("ǿ", R.drawable.f073);
		faceNameToDrawableId.put("��", R.drawable.f074);
		faceNameToDrawableId.put("����", R.drawable.f075);
		
		faceNameToDrawableId.put("ʤ��", R.drawable.f076);
		faceNameToDrawableId.put("��ȭ", R.drawable.f077);
		faceNameToDrawableId.put("����", R.drawable.f078);
		faceNameToDrawableId.put("ȭͷ", R.drawable.f079);
		faceNameToDrawableId.put("�", R.drawable.f080);
		faceNameToDrawableId.put("����", R.drawable.f081);
		faceNameToDrawableId.put("NO", R.drawable.f082);
		faceNameToDrawableId.put("OK", R.drawable.f083);
		faceNameToDrawableId.put("����", R.drawable.f084);
		faceNameToDrawableId.put("����", R.drawable.f085);
		faceNameToDrawableId.put("����", R.drawable.f086);
		
		faceNameToDrawableId.put("����", R.drawable.f087);
		faceNameToDrawableId.put("���", R.drawable.f088);
		faceNameToDrawableId.put("תȦ", R.drawable.f089);
		faceNameToDrawableId.put("��ͷ", R.drawable.f090);
		faceNameToDrawableId.put("��ͷ", R.drawable.f091);
		faceNameToDrawableId.put("����", R.drawable.f092);
		faceNameToDrawableId.put("����", R.drawable.f093);
		faceNameToDrawableId.put("����", R.drawable.f094);
		faceNameToDrawableId.put("����", R.drawable.f095);
		faceNameToDrawableId.put("����", R.drawable.f096);
		faceNameToDrawableId.put("��̫��", R.drawable.f097);
		
		faceNameToDrawableId.put("��̫��", R.drawable.f098);
		faceNameToDrawableId.put("�˵�", R.drawable.f099);
		faceNameToDrawableId.put("����", R.drawable.f100);
		faceNameToDrawableId.put("ơ��", R.drawable.f101);
		faceNameToDrawableId.put("����", R.drawable.f102);
		faceNameToDrawableId.put("����", R.drawable.f103);
		faceNameToDrawableId.put("ƹ��", R.drawable.f104);
	}
	
	static{
		drawableIdToFaceName.put("h000","��Ƥ");
		drawableIdToFaceName.put("h001","����");
		drawableIdToFaceName.put("h002","����");
		drawableIdToFaceName.put("h003","�ѹ�");
		drawableIdToFaceName.put("h004","��");
		drawableIdToFaceName.put("h005","�亹");
		drawableIdToFaceName.put("h006","ץ��");
		drawableIdToFaceName.put("h007","��");
		drawableIdToFaceName.put("h008","͵Ц");
		drawableIdToFaceName.put("h009","�ɰ�");
		drawableIdToFaceName.put("h010","����");
		drawableIdToFaceName.put("h011","����");
		drawableIdToFaceName.put("h012","΢Ц");
		drawableIdToFaceName.put("h013","Ʋ��");
		drawableIdToFaceName.put("h014","ɫ");
		drawableIdToFaceName.put("h015","����");
		drawableIdToFaceName.put("h016","����");
		drawableIdToFaceName.put("h017","����");
		drawableIdToFaceName.put("h018","����");
		drawableIdToFaceName.put("h019","��");
		drawableIdToFaceName.put("h020","��");
		
		drawableIdToFaceName.put("h021","����");
		drawableIdToFaceName.put("h022","��ŭ");
		drawableIdToFaceName.put("h023","���");
		drawableIdToFaceName.put("h024","����");
		drawableIdToFaceName.put("h025","�ټ�");
		drawableIdToFaceName.put("h026","�ô�");
		drawableIdToFaceName.put("h027","����");
		drawableIdToFaceName.put("h028","ί��");
		drawableIdToFaceName.put("h029","����");
		drawableIdToFaceName.put("h030","˯");
		drawableIdToFaceName.put("h031","����");
		
		drawableIdToFaceName.put("h032","��Ц");
		drawableIdToFaceName.put("h033","��Ƥ");
		drawableIdToFaceName.put("h034","����");
		drawableIdToFaceName.put("h035","�ܶ�");
		drawableIdToFaceName.put("h036","�Һߺ�");
		drawableIdToFaceName.put("h037","ӵ��");
		drawableIdToFaceName.put("h038","��Ц");
		drawableIdToFaceName.put("h039","����");
		drawableIdToFaceName.put("h040","��");
		drawableIdToFaceName.put("h041","���");
		drawableIdToFaceName.put("h042","����");
		
		drawableIdToFaceName.put("h043","����");
		drawableIdToFaceName.put("h044","����");
		drawableIdToFaceName.put("h045","�ٱ�");
		drawableIdToFaceName.put("h046","����");
		drawableIdToFaceName.put("h047","�ܴ���");
		drawableIdToFaceName.put("h048","��ߺ�");
		drawableIdToFaceName.put("h049","��Ƿ");
		drawableIdToFaceName.put("h050","�����");
		drawableIdToFaceName.put("h051","��");
		drawableIdToFaceName.put("h052","����");
		drawableIdToFaceName.put("h053","����");
		
		drawableIdToFaceName.put("h054","��ĥ");
		drawableIdToFaceName.put("h055","ʾ��");
		drawableIdToFaceName.put("h056","����");
		drawableIdToFaceName.put("h057","����");
		drawableIdToFaceName.put("h058","����");
		drawableIdToFaceName.put("h059","����");
		drawableIdToFaceName.put("h060","ը��");
		drawableIdToFaceName.put("h061","��");
		drawableIdToFaceName.put("h062","����");
		drawableIdToFaceName.put("h063","ư��");
		drawableIdToFaceName.put("h064","���");
		
		drawableIdToFaceName.put("h065","����");
		drawableIdToFaceName.put("h066","��");
		drawableIdToFaceName.put("h067","��");
		drawableIdToFaceName.put("h068","õ��");
		drawableIdToFaceName.put("h069","��л");
		drawableIdToFaceName.put("h070","����");
		drawableIdToFaceName.put("h071","̫��");
		drawableIdToFaceName.put("h072","����");
		drawableIdToFaceName.put("h073","ǿ");
		drawableIdToFaceName.put("h074","��");
		drawableIdToFaceName.put("h075","����");
		
		drawableIdToFaceName.put("h076","ʤ��");
		drawableIdToFaceName.put("h077","��ȭ");
		drawableIdToFaceName.put("h078","����");
		drawableIdToFaceName.put("h079","ȭͷ");
		drawableIdToFaceName.put("h080","�");
		drawableIdToFaceName.put("h081","����");
		drawableIdToFaceName.put("h082","NO");
		drawableIdToFaceName.put("h083","OK");
		drawableIdToFaceName.put("h084","����");
		drawableIdToFaceName.put("h085","����");
		drawableIdToFaceName.put("h086","����");
		
		drawableIdToFaceName.put("h087","����");
		drawableIdToFaceName.put("h088","���");
		drawableIdToFaceName.put("h089","תȦ");
		drawableIdToFaceName.put("h090","��ͷ");
		drawableIdToFaceName.put("h091","��ͷ");
		drawableIdToFaceName.put("h092","����");
		drawableIdToFaceName.put("h093","����");
		drawableIdToFaceName.put("h094","����");
		drawableIdToFaceName.put("h095","����");
		drawableIdToFaceName.put("h096","����");
		drawableIdToFaceName.put("h097","��̫��");
		
		drawableIdToFaceName.put("h098","��̫��");
		drawableIdToFaceName.put("h099","�˵�");
		drawableIdToFaceName.put("h100","����");
		drawableIdToFaceName.put("h101","ơ��");
		drawableIdToFaceName.put("h102","����");
		drawableIdToFaceName.put("h103","����");
		drawableIdToFaceName.put("h104","ƹ��");
	}
	
	
	public static SpannableString decorateFaceInStr(SpannableString spannable,List<Map<String,Object>> list,Resources resources){
		int size = list.size();
		Drawable drawable = null;
		if(list!=null&&list.size()>0){
			for(int i=0;i<size;i++){
				Map<String,Object> map = list.get(i);
				drawable = resources.getDrawable(faceNameToDrawableId.get(map.get("faceName")));
				drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());
				ImageSpan span = new ImageSpan(drawable,ImageSpan.ALIGN_BASELINE);
				spannable.setSpan(span, (Integer)map.get("startIndex"), (Integer)map.get("endIndex"), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
		}
		return spannable;
	}
	public static SpannableString decorateVipInStr(SpannableString spannable,List<Map<String,Object>> list,Resources resources){
		int size = list.size();
		Drawable drawable = null;
		if(list!=null&&list.size()>0){
			for(int i=0;i<size;i++){
				Map<String,Object> map = list.get(i);
				drawable = resources.getDrawable(R.drawable.vip);
				drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());
				ImageSpan span = new ImageSpan(drawable,ImageSpan.ALIGN_BASELINE);
				spannable.setSpan(span, (Integer)map.get("startIndex"), (Integer)map.get("endIndex"), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
		}
		return spannable;
	}
	
	public static SpannableString decorateTopicInStr(SpannableString spannable,List<Map<String,Object>> list,Resources resources){
		int size = list.size();
		Drawable drawable = null;
		CharacterStyle foregroundColorSpan=new ForegroundColorSpan(Color.argb(255, 33, 92, 110));
		if(list!=null&&list.size()>0){
			for(int i=0;i<size;i++){
				Map<String,Object> map = list.get(i);
				spannable.setSpan(foregroundColorSpan, (Integer)map.get("startIndex"), (Integer)map.get("endIndex"), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
		}
		return spannable;
	}
	
	public static SpannableString decorateRefersInStr(SpannableString spannable,List<Map<String,Object>> list,Resources resources){
		int size = list.size();
		Drawable drawable = null;
		CharacterStyle foregroundColorSpan=new ForegroundColorSpan(Color.argb(255, 33, 92, 110));
		if(list!=null&&list.size()>0){
			for(int i=0;i<size;i++){
				Map<String,Object> map = list.get(i);
				spannable.setSpan(foregroundColorSpan, (Integer)map.get("startIndex"), (Integer)map.get("endIndex"), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
		}
		return spannable;
	}
}
