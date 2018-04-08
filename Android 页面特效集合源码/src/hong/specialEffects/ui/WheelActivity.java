package hong.specialEffects.ui;

import hong.specialEffects.R;
import hong.specialEffects.wheel.OnWheelChangedListener;
import hong.specialEffects.wheel.OnWheelScrollListener;
import hong.specialEffects.wheel.WheelView;
import hong.specialEffects.wheel.adapter.AbstractWheelTextAdapter;
import hong.specialEffects.wheel.adapter.ArrayWheelAdapter;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class WheelActivity extends Activity {
    // Scrolling flag
    private boolean scrolling = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.wheel);
                
        final WheelView country = (WheelView) findViewById(R.id.country);
        country.setVisibleItems(3);
        country.setViewAdapter(new CountryAdapter(this));
        //country.setCyclic(true);
        
        final String cities[][] = new String[][] {
                        new String[] {"����", "����", "����", "�޷�", "����"},
                        new String[] {"����", "��³ľ��", "����", "����", "�Ϸ�"},
                        new String[] {"����", "���", "����", "����"},
                        new String[] {"�ɶ�", "����", "����", "����","����","�Ϻ�","����","ŦԼ"},
                        new String[] {"��ʢ��", "Ī˹��", "�׶�", "ʥ�˵ñ�", "��̫��"},
                        new String[] {"���", "���ͺ���", "̨��", "���", "��ɽ"},
                        new String[] {"�ɶ�", "����", "����", "����","����","�Ϻ�","����","ŦԼ"},
                        };
        
        final WheelView city= (WheelView) findViewById(R.id.city);
        city.setVisibleItems(5);
        country.addChangingListener(new OnWheelChangedListener() {
                        @Override
						public void onChanged(WheelView wheel, int oldValue, int newValue) {
                            if (!scrolling) {
                                updateCities(city, cities, newValue);
                                Log.i("Change",newValue+"");
                            }
                        }
                });
        
        country.addScrollingListener( new OnWheelScrollListener() {
            @Override
			public void onScrollingStarted(WheelView wheel) {
                scrolling = true;
            }
            @Override
			public void onScrollingFinished(WheelView wheel) {
                scrolling = false;
                Log.i("onScrollingFinished",wheel.getCurrentItem()+"");
                updateCities(city, cities, country.getCurrentItem());
            }
        });

        country.setCurrentItem(1);
        
    }
    
    /**
     * Updates the city wheel
     */
    private void updateCities(WheelView city, String cities[][], int index) {
        ArrayWheelAdapter<String> adapter =
            new ArrayWheelAdapter<String>(this, cities[index]);
        adapter.setTextSize(20);
        city.setViewAdapter(adapter);
        city.setCurrentItem(cities[index].length / 2);        
    }
    
    /**
     * Adapter for countries
     */
    private class CountryAdapter extends AbstractWheelTextAdapter {
        // Countries names
        private String countries[] =
            new String[] {"�й�", "�й�", "�й�", "�й�","�й�","�й�","�й�"};
        // Countries flags
        private int flags[] =
            new int[] {R.drawable.usa,
        		R.drawable.usa,
        		R.drawable.usa,
        		R.drawable.usa, 
        		R.drawable.canada, 
        		R.drawable.ukraine,
        		R.drawable.france};
        
        /**
         * Constructor
         */
        protected CountryAdapter(Context context) {
            super(context, R.layout.country_layout, NO_RESOURCE);
            
           // setItemTextResource(R.id.country_name);
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            View view = super.getItem(index, cachedView, parent);
           
            ImageView img = (ImageView) view.findViewById(R.id.flag);
            img.setImageResource(flags[index]);
            return view;
        }
        
        @Override
        public int getItemsCount() {
            return countries.length;
        }
        
        @Override
        protected CharSequence getItemText(int index) {
            return countries[index];
        }
    }
}