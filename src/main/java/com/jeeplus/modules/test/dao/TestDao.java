/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.dao;

import com.jeeplus.common.persistence.CrudDao;
import com.jeeplus.common.persistence.annotation.MyBatisDao;
import com.jeeplus.modules.test.entity.Test;

/**
 * testDAO接口
 * @author test
 * @version 2016-10-31
 */
@MyBatisDao
public interface TestDao extends CrudDao<Test> {

	
}