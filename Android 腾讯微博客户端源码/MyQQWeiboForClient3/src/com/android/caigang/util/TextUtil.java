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
		faceNameToDrawableId.put("µ÷Æ¤", R.drawable.f000);
		faceNameToDrawableId.put("ßÚÑÀ", R.drawable.f001);
		faceNameToDrawableId.put("¾ªÑÈ", R.drawable.f002);
		faceNameToDrawableId.put("ÄÑ¹ý", R.drawable.f003);
		faceNameToDrawableId.put("¿á", R.drawable.f004);
		faceNameToDrawableId.put("Àäº¹", R.drawable.f005);
		faceNameToDrawableId.put("×¥¿ñ", R.drawable.f006);
		faceNameToDrawableId.put("ÍÂ", R.drawable.f007);
		faceNameToDrawableId.put("ÍµÐ¦", R.drawable.f008);
		faceNameToDrawableId.put("¿É°®", R.drawable.f009);
		faceNameToDrawableId.put("°×ÑÛ", R.drawable.f010);
		faceNameToDrawableId.put("°ÁÂý", R.drawable.f011);
		faceNameToDrawableId.put("Î¢Ð¦", R.drawable.f012);
		faceNameToDrawableId.put("Æ²×ì", R.drawable.f013);
		faceNameToDrawableId.put("É«", R.drawable.f014);
		faceNameToDrawableId.put("·¢´ô", R.drawable.f015);
		faceNameToDrawableId.put("µÃÒâ", R.drawable.f016);
		faceNameToDrawableId.put("Á÷Àá", R.drawable.f017);
		faceNameToDrawableId.put("º¦Ðß", R.drawable.f018);
		faceNameToDrawableId.put("Ðê", R.drawable.f019);
		faceNameToDrawableId.put("À§", R.drawable.f020);
		
		faceNameToDrawableId.put("ÞÏÞÎ", R.drawable.f021);
		faceNameToDrawableId.put("·¢Å­", R.drawable.f022);
		faceNameToDrawableId.put("´ó¿Þ", R.drawable.f023);
		faceNameToDrawableId.put("Á÷º¹", R.drawable.f024);
		faceNameToDrawableId.put("ÔÙ¼û", R.drawable.f025);
		faceNameToDrawableId.put("ÇÃ´ò", R.drawable.f026);
		faceNameToDrawableId.put("²Áº¹", R.drawable.f027);
		faceNameToDrawableId.put("Î¯Çü", R.drawable.f028);
		faceNameToDrawableId.put("ÒÉÎÊ", R.drawable.f029);
		faceNameToDrawableId.put("Ë¯", R.drawable.f030);
		faceNameToDrawableId.put("Ç×Ç×", R.drawable.f031);
		
		faceNameToDrawableId.put("º©Ð¦", R.drawable.f032);
		faceNameToDrawableId.put("µ÷Æ¤", R.drawable.f033);
		faceNameToDrawableId.put("ÒõÏÕ", R.drawable.f034);
		faceNameToDrawableId.put("·Ü¶·", R.drawable.f035);
		faceNameToDrawableId.put("ÓÒºßºß", R.drawable.f036);
		faceNameToDrawableId.put("Óµ±§", R.drawable.f037);
		faceNameToDrawableId.put("»µÐ¦", R.drawable.f038);
		faceNameToDrawableId.put("±ÉÊÓ", R.drawable.f039);
		faceNameToDrawableId.put("ÔÎ", R.drawable.f040);
		faceNameToDrawableId.put("´ó±ø", R.drawable.f041);
		faceNameToDrawableId.put("¿ÉÁ¯", R.drawable.f042);
		
		faceNameToDrawableId.put("¼¢¶ö", R.drawable.f043);
		faceNameToDrawableId.put("ÖäÂî", R.drawable.f044);
		faceNameToDrawableId.put("¿Ù±Ç", R.drawable.f045);
		faceNameToDrawableId.put("¹ÄÕÆ", R.drawable.f046);
		faceNameToDrawableId.put("ôÜ´óÁË", R.drawable.f047);
		faceNameToDrawableId.put("×óºßºß", R.drawable.f048);
		faceNameToDrawableId.put("¹þÇ·", R.drawable.f049);
		faceNameToDrawableId.put("¿ì¿ÞÁË", R.drawable.f050);
		faceNameToDrawableId.put("ÏÅ", R.drawable.f051);
		faceNameToDrawableId.put("±Õ×ì", R.drawable.f052);
		faceNameToDrawableId.put("¾ª¿Ö", R.drawable.f053);
		
		faceNameToDrawableId.put("ÕÛÄ¥", R.drawable.f054);
		faceNameToDrawableId.put("Ê¾°®", R.drawable.f055);
		faceNameToDrawableId.put("°®ÐÄ", R.drawable.f056);
		faceNameToDrawableId.put("ÐÄËé", R.drawable.f057);
		faceNameToDrawableId.put("µ°¸â", R.drawable.f058);
		faceNameToDrawableId.put("ÉÁµç", R.drawable.f059);
		faceNameToDrawableId.put("Õ¨µ¯", R.drawable.f060);
		faceNameToDrawableId.put("µ¶", R.drawable.f061);
		faceNameToDrawableId.put("×ãÇò", R.drawable.f062);
		faceNameToDrawableId.put("Æ°³æ", R.drawable.f063);
		faceNameToDrawableId.put("±ã±ã", R.drawable.f064);
		
		faceNameToDrawableId.put("¿§·È", R.drawable.f065);
		faceNameToDrawableId.put("·¹", R.drawable.f066);
		faceNameToDrawableId.put("Öí", R.drawable.f067);
		faceNameToDrawableId.put("Ãµ¹å", R.drawable.f068);
		faceNameToDrawableId.put("µòÐ»", R.drawable.f069);
		faceNameToDrawableId.put("ÔÂÁÁ", R.drawable.f070);
		faceNameToDrawableId.put("Ì«Ñô", R.drawable.f071);
		faceNameToDrawableId.put("ÀñÎï", R.drawable.f072);
		faceNameToDrawableId.put("Ç¿", R.drawable.f073);
		faceNameToDrawableId.put("Èõ", R.drawable.f074);
		faceNameToDrawableId.put("ÎÕÊÖ", R.drawable.f075);
		
		faceNameToDrawableId.put("Ê¤Àû", R.drawable.f076);
		faceNameToDrawableId.put("±§È­", R.drawable.f077);
		faceNameToDrawableId.put("¹´Òý", R.drawable.f078);
		faceNameToDrawableId.put("È­Í·", R.drawable.f079);
		faceNameToDrawableId.put("²î¾¢", R.drawable.f080);
		faceNameToDrawableId.put("°®Äã", R.drawable.f081);
		faceNameToDrawableId.put("NO", R.drawable.f082);
		faceNameToDrawableId.put("OK", R.drawable.f083);
		faceNameToDrawableId.put("°®Çé", R.drawable.f084);
		faceNameToDrawableId.put("·ÉÎÇ", R.drawable.f085);
		faceNameToDrawableId.put("ÌøÌø", R.drawable.f086);
		
		faceNameToDrawableId.put("·¢¶¶", R.drawable.f087);
		faceNameToDrawableId.put("âæ»ð", R.drawable.f088);
		faceNameToDrawableId.put("×ªÈ¦", R.drawable.f089);
		faceNameToDrawableId.put("¿ÄÍ·", R.drawable.f090);
		faceNameToDrawableId.put("»ØÍ·", R.drawable.f091);
		faceNameToDrawableId.put("ÌøÉþ", R.drawable.f092);
		faceNameToDrawableId.put("»ÓÊÖ", R.drawable.f093);
		faceNameToDrawableId.put("¼¤¶¯", R.drawable.f094);
		faceNameToDrawableId.put("½ÖÎè", R.drawable.f095);
		faceNameToDrawableId.put("Ï×ÎÇ", R.drawable.f096);
		faceNameToDrawableId.put("×óÌ«¼«", R.drawable.f097);
		
		faceNameToDrawableId.put("ÓÒÌ«¼«", R.drawable.f098);
		faceNameToDrawableId.put("²Ëµ¶", R.drawable.f099);
		faceNameToDrawableId.put("Î÷¹Ï", R.drawable.f100);
		faceNameToDrawableId.put("Æ¡¾Æ", R.drawable.f101);
		faceNameToDrawableId.put("÷¼÷Ã", R.drawable.f102);
		faceNameToDrawableId.put("ÀºÇò", R.drawable.f103);
		faceNameToDrawableId.put("Æ¹ÅÒ", R.drawable.f104);
	}
	
	static{
		drawableIdToFaceName.put("h000","µ÷Æ¤");
		drawableIdToFaceName.put("h001","ßÚÑÀ");
		drawableIdToFaceName.put("h002","¾ªÑÈ");
		drawableIdToFaceName.put("h003","ÄÑ¹ý");
		drawableIdToFaceName.put("h004","¿á");
		drawableIdToFaceName.put("h005","Àäº¹");
		drawableIdToFaceName.put("h006","×¥¿ñ");
		drawableIdToFaceName.put("h007","ÍÂ");
		drawableIdToFaceName.put("h008","ÍµÐ¦");
		drawableIdToFaceName.put("h009","¿É°®");
		drawableIdToFaceName.put("h010","°×ÑÛ");
		drawableIdToFaceName.put("h011","°ÁÂý");
		drawableIdToFaceName.put("h012","Î¢Ð¦");
		drawableIdToFaceName.put("h013","Æ²×ì");
		drawableIdToFaceName.put("h014","É«");
		drawableIdToFaceName.put("h015","·¢´ô");
		drawableIdToFaceName.put("h016","µÃÒâ");
		drawableIdToFaceName.put("h017","Á÷Àá");
		drawableIdToFaceName.put("h018","º¦Ðß");
		drawableIdToFaceName.put("h019","Ðê");
		drawableIdToFaceName.put("h020","À§");
		
		drawableIdToFaceName.put("h021","ÞÏÞÎ");
		drawableIdToFaceName.put("h022","·¢Å­");
		drawableIdToFaceName.put("h023","´ó¿Þ");
		drawableIdToFaceName.put("h024","Á÷º¹");
		drawableIdToFaceName.put("h025","ÔÙ¼û");
		drawableIdToFaceName.put("h026","ÇÃ´ò");
		drawableIdToFaceName.put("h027","²Áº¹");
		drawableIdToFaceName.put("h028","Î¯Çü");
		drawableIdToFaceName.put("h029","ÒÉÎÊ");
		drawableIdToFaceName.put("h030","Ë¯");
		drawableIdToFaceName.put("h031","Ç×Ç×");
		
		drawableIdToFaceName.put("h032","º©Ð¦");
		drawableIdToFaceName.put("h033","µ÷Æ¤");
		drawableIdToFaceName.put("h034","ÒõÏÕ");
		drawableIdToFaceName.put("h035","·Ü¶·");
		drawableIdToFaceName.put("h036","ÓÒºßºß");
		drawableIdToFaceName.put("h037","Óµ±§");
		drawableIdToFaceName.put("h038","»µÐ¦");
		drawableIdToFaceName.put("h039","±ÉÊÓ");
		drawableIdToFaceName.put("h040","ÔÎ");
		drawableIdToFaceName.put("h041","´ó±ø");
		drawableIdToFaceName.put("h042","¿ÉÁ¯");
		
		drawableIdToFaceName.put("h043","¼¢¶ö");
		drawableIdToFaceName.put("h044","ÖäÂî");
		drawableIdToFaceName.put("h045","¿Ù±Ç");
		drawableIdToFaceName.put("h046","¹ÄÕÆ");
		drawableIdToFaceName.put("h047","ôÜ´óÁË");
		drawableIdToFaceName.put("h048","×óºßºß");
		drawableIdToFaceName.put("h049","¹þÇ·");
		drawableIdToFaceName.put("h050","¿ì¿ÞÁË");
		drawableIdToFaceName.put("h051","ÏÅ");
		drawableIdToFaceName.put("h052","±Õ×ì");
		drawableIdToFaceName.put("h053","¾ª¿Ö");
		
		drawableIdToFaceName.put("h054","ÕÛÄ¥");
		drawableIdToFaceName.put("h055","Ê¾°®");
		drawableIdToFaceName.put("h056","°®ÐÄ");
		drawableIdToFaceName.put("h057","ÐÄËé");
		drawableIdToFaceName.put("h058","µ°¸â");
		drawableIdToFaceName.put("h059","ÉÁµç");
		drawableIdToFaceName.put("h060","Õ¨µ¯");
		drawableIdToFaceName.put("h061","µ¶");
		drawableIdToFaceName.put("h062","×ãÇò");
		drawableIdToFaceName.put("h063","Æ°³æ");
		drawableIdToFaceName.put("h064","±ã±ã");
		
		drawableIdToFaceName.put("h065","¿§·È");
		drawableIdToFaceName.put("h066","·¹");
		drawableIdToFaceName.put("h067","Öí");
		drawableIdToFaceName.put("h068","Ãµ¹å");
		drawableIdToFaceName.put("h069","µòÐ»");
		drawableIdToFaceName.put("h070","ÔÂÁÁ");
		drawableIdToFaceName.put("h071","Ì«Ñô");
		drawableIdToFaceName.put("h072","ÀñÎï");
		drawableIdToFaceName.put("h073","Ç¿");
		drawableIdToFaceName.put("h074","Èõ");
		drawableIdToFaceName.put("h075","ÎÕÊÖ");
		
		drawableIdToFaceName.put("h076","Ê¤Àû");
		drawableIdToFaceName.put("h077","±§È­");
		drawableIdToFaceName.put("h078","¹´Òý");
		drawableIdToFaceName.put("h079","È­Í·");
		drawableIdToFaceName.put("h080","²î¾¢");
		drawableIdToFaceName.put("h081","°®Äã");
		drawableIdToFaceName.put("h082","NO");
		drawableIdToFaceName.put("h083","OK");
		drawableIdToFaceName.put("h084","°®Çé");
		drawableIdToFaceName.put("h085","·ÉÎÇ");
		drawableIdToFaceName.put("h086","ÌøÌø");
		
		drawableIdToFaceName.put("h087","·¢¶¶");
		drawableIdToFaceName.put("h088","âæ»ð");
		drawableIdToFaceName.put("h089","×ªÈ¦");
		drawableIdToFaceName.put("h090","¿ÄÍ·");
		drawableIdToFaceName.put("h091","»ØÍ·");
		drawableIdToFaceName.put("h092","ÌøÉþ");
		drawableIdToFaceName.put("h093","»ÓÊÖ");
		drawableIdToFaceName.put("h094","¼¤¶¯");
		drawableIdToFaceName.put("h095","½ÖÎè");
		drawableIdToFaceName.put("h096","Ï×ÎÇ");
		drawableIdToFaceName.put("h097","×óÌ«¼«");
		
		drawableIdToFaceName.put("h098","ÓÒÌ«¼«");
		drawableIdToFaceName.put("h099","²Ëµ¶");
		drawableIdToFaceName.put("h100","Î÷¹Ï");
		drawableIdToFaceName.put("h101","Æ¡¾Æ");
		drawableIdToFaceName.put("h102","÷¼÷Ã");
		drawableIdToFaceName.put("h103","ÀºÇò");
		drawableIdToFaceName.put("h104","Æ¹ÅÒ");
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
