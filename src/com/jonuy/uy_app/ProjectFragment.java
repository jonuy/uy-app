package com.jonuy.uy_app;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Fragment representing a single project view.
 * 
 * Reference source: http://developer.android.com/training/animation/screen-slide.html
 */
public class ProjectFragment extends Fragment {

	private static final String ARG_PAGE = "page_number";
	private int mPageNumber;
	
	public static ProjectFragment create(int pageNumber) {
		ProjectFragment frag = new ProjectFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_PAGE, pageNumber);
		frag.setArguments(args);
		
		return frag;
	}
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mPageNumber = getArguments().getInt(ARG_PAGE);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_project_slide, null);
		ImageView iv = (ImageView)rootView.findViewById(R.id.project_image);
		TextView tv = (TextView)rootView.findViewById(R.id.project_text);
		
		switch (mPageNumber) {
		// Do Something android app
		case 0:
			iv.setImageResource(R.drawable.project_ds_logo);
			tv.setText(R.string.cupcake_ipsum);
			break;
		// Pregnancy Text
		case 1:
			iv.setImageResource(R.drawable.project_pregtext_logo);
			tv.setText(R.string.cupcake_ipsum);
			break;
		// KnowItAll
		case 2:
			iv.setImageResource(R.drawable.project_knowitall_logo);
			tv.setText(R.string.cupcake_ipsum2);
			break;
		// Ink Framework
		case 3:
			iv.setImageResource(R.drawable.project_ink_logo);
			tv.setText(R.string.cupcake_ipsum3);
			break;
		// Homefront
		case 4:
			iv.setImageResource(R.drawable.project_homefront_logo);
			tv.setText(R.string.cupcake_ipsum2);
			break;
		// Frontlines: Fuel of War
		case 5:
			iv.setImageResource(R.drawable.project_ffow_logo);
			tv.setText(R.string.cupcake_ipsum);
			break;
		}
		
		return rootView;
	}
}
