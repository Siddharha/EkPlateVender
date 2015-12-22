package com.example.bluehorsesoftkol.ekplatevendor.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Pref {

	private SharedPreferences spref;
	//private Activity _activity;
	private static final String PREF_FILE = "com.example.bluehorsesoftkol.ekplatevender";
	private Editor _editorSpref;

	public SharedPreferences getSharedPreferencesInstance() {
		return spref;
	}

	public Editor getSharedPreferencesEditorInstance() {
		return _editorSpref;
	}

	@SuppressLint("CommitPrefEdits")
	public Pref(Context _thisContext) {
		// TODO Auto-generated constructor stub
		//this._activity = (Activity)_thisContext;
		spref = _thisContext.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
		_editorSpref = spref.edit();
	}

	public String getSession(String key) {
		String value = spref.getString(key, "");
		return value;
	}

	public int getIntegerSession(String key) {
		int value = spref.getInt(key, Integer.MIN_VALUE);
		return value;
	}

	public void setSession(String key, String value) {
		if (key != null && value != null) {
			_editorSpref.putString(key, value);
			_editorSpref.commit();
		}
	}

	// @author : DTS
	public void setSession(String key, int value) {
		if (key != null) {
			_editorSpref.putInt(key, value);
			_editorSpref.commit();
		}
	}
	
	public void setSession(String key, boolean value) {
		if (key != null) {
			_editorSpref.putBoolean(key, value);
			_editorSpref.commit();
		}
	}
	
	public boolean getBooleanSession(String key){
		boolean value=spref.getBoolean(key, false);
		return value;
	}

	public void saveDate(String date) {
		_editorSpref.putString("date", date);
		_editorSpref.commit();
	}

	public String getDate() {
		return spref.getString("date", "");
	}

    public void saveUserAccessToken(String access_token) {
        _editorSpref.putString("access_token", access_token);
        _editorSpref.commit();
    }

    public String getUserAccessToken() {
        return spref.getString("access_token", "");
    }

    public void saveUserName(String name) {
        _editorSpref.putString("name", name);
        _editorSpref.commit();
    }

    public String getUserName() {
        return spref.getString("name", "");
    }

	public void saveUserEmail(String email) {
		_editorSpref.putString("email", email);
		_editorSpref.commit();
	}

	public String getUserEmail() {
		return spref.getString("email", "");
	}

    public void saveUserMobileNo(String mobile) {
        _editorSpref.putString("mobile", mobile);
        _editorSpref.commit();
    }

    public String getUserMobileNo() {
        return spref.getString("mobile", "");
    }

	public void saveUserFacebook(String facebook) {
		_editorSpref.putString("facebook", facebook);
		_editorSpref.commit();
	}

	public String getUserFacebook() {
		return spref.getString("facebook", "");
	}

	public void saveUserTwitter(String twitter) {
		_editorSpref.putString("twitter", twitter);
		_editorSpref.commit();
	}

	public String getUserTwitter() {
		return spref.getString("twitter", "");
	}

	public void saveUserPininterest(String pininterest) {
		_editorSpref.putString("pininterest", pininterest);
		_editorSpref.commit();
	}

	public String getUserPininterest() {
		return spref.getString("pininterest", "");
	}

	public void saveUserInstagram(String instagram) {
		_editorSpref.putString("instagram", instagram);
		_editorSpref.commit();
	}

	public String getUserInstagram() {
		return spref.getString("instagram", "");
	}

	public void saveUserImagePath(String image_path) {
		_editorSpref.putString("image_path", image_path);
		_editorSpref.commit();
	}

	public String getUserImagePath() {
		return spref.getString("image_path", "");
	}

	public String getUserImagePathFromSdCard() {
		return spref.getString("image_path_sd", "");
	}

	public void saveUserImagePathFromSdCard(String image_path_sd) {
		_editorSpref.putString("image_path_sd", image_path_sd);
		_editorSpref.commit();
	}

    public void saveUserTodaysTarget(String todays_target) {
        _editorSpref.putString("todays_target", todays_target);
        _editorSpref.commit();
    }

    public String getUserTodaysTarget() {
        return spref.getString("todays_target", "");
    }

	public void saveUserCapturedWork(String captured_work) {
		_editorSpref.putString("captured_work", captured_work);
		_editorSpref.commit();
	}

	public String getUserCapturedWork() {
		return spref.getString("captured_work", "");
	}

	public void saveUserPendingWork(String pending_work) {
		_editorSpref.putString("pending_work", pending_work);
		_editorSpref.commit();
	}

	public String getUserPendingWork() {
		return spref.getString("pending_work", "");
	}

	public void saveUserTotalVendor(String total_vendor) {
		_editorSpref.putString("total_vendor", total_vendor);
		_editorSpref.commit();
	}

	public String getUserTotalVendor() {
		return spref.getString("total_vendor", "");
	}

	public void saveUserTotalUploadedVendor(String uploaded_vendor) {
		_editorSpref.putString("uploaded_vendor", uploaded_vendor);
		_editorSpref.commit();
	}

	public String getUserTotalUploadedVendor() {
		return spref.getString("uploaded_vendor", "");
	}

	public void saveUserTotalApprovedVendor(String approved_vendor) {
		_editorSpref.putString("approved_vendor", approved_vendor);
		_editorSpref.commit();
	}

	public String getUserTotalApprovedVendor() {
		return spref.getString("approved_vendor", "");
	}

	public void saveMonth(String month) {
		_editorSpref.putString("month", month);
		_editorSpref.commit();
	}

	public String getMonth() {
		return spref.getString("month", "");
	}

	public void saveMonthTotalDay(String total_day) {
		_editorSpref.putString("total_day", total_day);
		_editorSpref.commit();
	}

	public String getMonthTotalDay() {
		return spref.getString("total_day", "");
	}

	public String getVendorId(){
		return spref.getString("VendorId", "");
	}

	public void saveVendorId(String vendor_id) {
		_editorSpref.putString("VendorId", vendor_id);
		_editorSpref.commit();
	}
}
