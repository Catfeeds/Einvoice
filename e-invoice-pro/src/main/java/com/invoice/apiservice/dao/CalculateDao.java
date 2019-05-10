package com.invoice.apiservice.dao;

import java.util.List;

import com.invoice.bean.db.Catedis;
import com.invoice.bean.db.Category;
import com.invoice.bean.db.Catetax;
import com.invoice.bean.db.Goodsdis;
import com.invoice.bean.db.Goodstax;
import com.invoice.bean.db.Paymode;

/**
 * 小票等单据计算
 * @author Baij
 */
public interface CalculateDao {

	public Goodstax getGoodstaxById(Goodstax p);

	public List<Catetax> getCatetaxById(Catetax p);

	public Category getCategoryById(Category p);

	public Paymode getPaymodeById(Paymode p);

	public Goodsdis getGoodsDis(Goodsdis p);

	public Catedis getCateDis(Catedis p);

}
