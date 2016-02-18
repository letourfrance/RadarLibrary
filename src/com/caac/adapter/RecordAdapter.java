package com.caac.adapter;

import java.util.List;

import com.caac.radar.R;
import com.caac.radar.bean.BorrowBook;
import com.caac.radar.bean.RecordBook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class RecordAdapter extends ArrayAdapter<RecordBook> {

	private int resourceId;
	
	
	public RecordAdapter(Context context, int resource, List<RecordBook> objects) {
		super(context, resource, objects);
		resourceId = resource;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		RecordBook books = getItem(position);
		
		View view;
		
		ViewHolder viewHolder;
		
		if(convertView == null){
			view = LayoutInflater.from(getContext()).inflate(resourceId, null);
			viewHolder = new ViewHolder();
			viewHolder.tv_reco_time = (TextView) view.findViewById(R.id.reco_time);
			viewHolder.tv_reco = (TextView) view.findViewById(R.id.reco_con);
			view.setTag(viewHolder);
		}else{
			view = convertView;
			viewHolder = (ViewHolder) view.getTag();
		}
		if(books==null){
			viewHolder.tv_reco.setText("无记录!!!");
		}else{
			viewHolder.tv_reco_time.setText(books.getR_time().getDate()+"");
		    viewHolder.tv_reco.setText(books.getR_peoadd().toString()+" "+books.getR_start().toString());
		}
		
		return view;
		
	}
	
	class ViewHolder{
		
		TextView tv_reco_time;
		TextView tv_reco;
	}
}
