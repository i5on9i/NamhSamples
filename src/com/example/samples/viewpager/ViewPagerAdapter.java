package com.example.samples.viewpager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.samples.R;

public class ViewPagerAdapter extends PagerAdapter {
	// Declare Variables
	Context context;
	String[] rank;
	String[] country;
	String[] population;
	int[] flag;
	LayoutInflater inflater;

	public ViewPagerAdapter(Context context, String[] rank, String[] country,
			String[] population, int[] flag) {
		this.context = context;
		this.rank = rank;
		this.country = country;
		this.population = population;
		this.flag = flag;
	}

	@Override
	public int getCount() {
		return rank.length;
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == ((LinearLayout) object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		
		// Declare Variables
		TextView txtrank;
		TextView txtcountry;
		TextView txtpopulation;
		ImageView imgflag;

		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View itemView = inflater.inflate(R.layout.viewpager_item, container,
				false);


		// Add viewpager_item.xmlml to ViewPager
		((ViewPager) container).addView(itemView);



		return itemView;
	}

    @Override
    public float getPageWidth(final int position) {
        // this will have 3 pages in a single view
        return 0.7f;
    }

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// Remove viewpager_item_1_1.xml from ViewPager
		((ViewPager) container).removeView((LinearLayout) object);

	}
}
