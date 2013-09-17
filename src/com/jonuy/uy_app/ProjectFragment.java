package com.jonuy.uy_app;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ProjectFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_project_slide, null);
		
		return rootView;
	}
}
