package com.invoice.apiservice.dao;

import java.util.List;
import java.util.Map;

import com.invoice.bean.db.Invque;
import com.invoice.bean.db.InvqueList;
import com.invoice.bean.db.InvqueListDetail;

public interface InvqueDao {

	void insertInvque(Invque que);

	void insertInvqueList(InvqueList quelist);
	
	List<Invque> getInvque(Map<String, Object> p);
	
	List<Invque> getInvques(Map<String, Object> p);
	
	List<Invque> getInvquesNoFphm(Map<String, Object> p);
	
	List<InvqueList> getInvqueList(Map<String, Object> p);
	
	List<InvqueList> gethcInvqueList(Map<String, Object> p);
	
	int getInvqueCount(Map<String, Object> p);

	void updateTo40(Invque que);
	
	void updateToFp(Invque que);
	
	void updateTo50(Invque que);
	
	void updateTo50ByFp(Invque que);
	
	void updateTo30(Invque que);
	
	void updateTo99(Invque que);
	
	void updatepdf(Invque que);
	
	void updateinvoicetimes(Invque que);
	
	void insertInvqueListDetail(List<InvqueListDetail> listDetail);
	
	List<Invque> getInvquelistForSheetid(Map<String, Object> p);
	
	int getInvquelistForSheetidcount(Map<String, Object> p);
	
	List<Invque> getInvqueForsheetid(Map<String, Object> p);
	
	Invque getInvqueHead(Invque que);
	
	void updateForCallGD(Invque que);
	
	void updateForCardGD(Invque que);
	
	List<Invque> getInvqueCancelCard(Map<String, String> p);
	
	String getInvqueIqmemo(Map<String, String> p);
}
