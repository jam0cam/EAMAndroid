package com.eam.android.presenter;

import com.eam.android.utils.UrlValidator;
import com.eam.android.view.IWelcomeView;

public class WelcomePresenter {
	
	private IWelcomeView view;
	
	public WelcomePresenter(IWelcomeView view) {
		this.view = view;
	}
	
	/**
	 * Validates the entered URL for EAM validity
	 */
	public boolean submit() {
		return UrlValidator.isValidEamURL(view.getUrl());
	}
}
