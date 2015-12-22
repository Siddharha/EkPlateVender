package com.example.bluehorsesoftkol.ekplatevendor.adapters;

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.bluehorsesoftkol.ekplatevendor.R;
import com.example.bluehorsesoftkol.ekplatevendor.bean.FaqSubItem;
import com.example.bluehorsesoftkol.ekplatevendor.bean.FaqItem;

public class FaqAdapter extends BaseExpandableListAdapter {

	private Context context;
	private ArrayList<FaqItem> faqItems;
	private LayoutInflater inflater;

	public FaqAdapter(Context context,
					  ArrayList<FaqItem> faqItems) {
		this.context = context;
		this.faqItems = faqItems;
		inflater = (LayoutInflater) this.context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return faqItems.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return faqItems.get(groupPosition).getArrayList().size();
	}

	@Override
	public FaqItem getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return faqItems.get(groupPosition);
	}

	@Override
	public FaqSubItem getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return faqItems.get(groupPosition).getArrayList()
				.get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return (groupPosition * childPosition);
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		View groupView = convertView;

		if (groupView == null) {

			groupView = inflater.inflate(R.layout.faq_question, null);
			ViewGroupHolder holder = new ViewGroupHolder();
			holder.tvFaqQuestion = (TextView) groupView
					.findViewById(R.id.tvFaqQuestion);
			groupView.setTag(holder);
		}

		ViewGroupHolder groupHolder = (ViewGroupHolder) groupView.getTag();
		groupHolder.tvFaqQuestion.setText(getGroup(groupPosition).getFaqQuestion());
		return groupView;
	}

	@Override
	public View getChildView(final int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		View childView = convertView;

		childView = inflater.inflate(R.layout.faq_answer, null);
		final ViewChildHolder childHolder = new ViewChildHolder();
		childHolder.tvFaqAnswer = (TextView) childView
				.findViewById(R.id.tvFaqAnswer);

		childView.setTag(childHolder);
		ViewChildHolder newChildHolder = (ViewChildHolder) childView.getTag();
		newChildHolder.tvFaqAnswer.setText(getChild(groupPosition,
				childPosition).getFaqAnswer());

		return childView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return false;
	}

	class ViewGroupHolder {
		TextView tvFaqQuestion;
		ImageView indicator;
	}

	class ViewChildHolder {
		TextView tvFaqAnswer;
	}

}
