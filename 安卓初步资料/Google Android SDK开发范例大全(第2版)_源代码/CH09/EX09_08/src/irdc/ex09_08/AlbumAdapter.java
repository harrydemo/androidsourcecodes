package irdc.ex09_08;

/* import���class */
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/* �Զ����Adapter���̳�android.widget.BaseAdapter */
public class AlbumAdapter extends BaseAdapter
{
  private LayoutInflater mInflater;
  private List<String[]> items;

  public AlbumAdapter(Context context,List<String[]> it)
  {
    mInflater = LayoutInflater.from(context);
    items = it;
  }
  
  /* ��̳�BaseAdapter������д����method */
  @Override
  public int getCount()
  {
    return items.size();
  }

  @Override
  public Object getItem(int position)
  {
    return items.get(position);
  }
  
  @Override
  public long getItemId(int position)
  {
    return position;
  }
  
  @Override
  public View getView(int position,View conView,ViewGroup par)
  {
    ViewHolder holder;
    
    if(conView == null)
    {
      /* ʹ���Զ����album��ΪLayout */
      conView = mInflater.inflate(R.layout.album, null);
      /* ��ʼ��holder��text��image */
      holder = new ViewHolder();
      holder.text = (TextView) conView.findViewById(R.id.myText);
      holder.image = (ImageView)conView.findViewById(R.id.myImage);
      conView.setTag(holder);
    }
    else
    {
      holder = (ViewHolder) conView.getTag();
    }
    /* �趨�ಾ���� */
    String[] tmpS=(String[])items.get(position);
    holder.text.setText(tmpS[2]);
    /* �趨�ಾ��Ƭ */
    URL url;
    try
    {
      url = new URL(tmpS[1]);
      URLConnection conn = url.openConnection(); 
      conn.connect(); 
      Bitmap bm = BitmapFactory.decodeStream(conn.getInputStream());
      holder.image.setImageBitmap(bm);
    } catch (Exception e)
    {
      e.printStackTrace();
    } 
    return conView;
  }
  
  private class ViewHolder
  {
    /* text ���ಾ����
     * image���ಾ��Ƭ */
    TextView text;
    ImageView image;
  }
}

