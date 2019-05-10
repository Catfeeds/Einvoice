package com.invoice.uiservice.dao;

import java.util.List;

import com.invoice.bean.ui.Option;

public interface LookupDao {

	List<Option> getLookupSelect(String lookupid);

}
