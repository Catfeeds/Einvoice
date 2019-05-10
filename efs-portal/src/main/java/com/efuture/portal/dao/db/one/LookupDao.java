package com.efuture.portal.dao.db.one;

import java.util.List;

import com.efuture.portal.beans.Option;

public interface LookupDao {

	List<Option> getLookupSelect(int lookupid);

}
