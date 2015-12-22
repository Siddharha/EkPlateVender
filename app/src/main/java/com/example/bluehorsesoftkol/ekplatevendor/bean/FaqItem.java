package com.example.bluehorsesoftkol.ekplatevendor.bean;

import java.util.ArrayList;

public class FaqItem {

	String question;
	ArrayList<FaqSubItem> subGroupList;
	
	public String getFaqQuestion() {
		return question;
	}
	
	public void setFaqQuestion(String question) {
		this.question = question;
	}

	
	public ArrayList<FaqSubItem> getArrayList() {
		return subGroupList;
	}
	public void setArrayList(ArrayList<FaqSubItem> subGroupList) {
		this.subGroupList = subGroupList;
	}
	
	
}
