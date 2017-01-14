/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.service.CrudService;
import com.jeeplus.modules.test.entity.Test;
import com.jeeplus.modules.test.dao.TestDao;

/**
 * testService
 * @author test
 * @version 2016-10-31
 */
@Service
@Transactional(readOnly = true)
public class TestService extends CrudService<TestDao, Test> {

	public Test get(String id) {
		return super.get(id);
	}
	
	public List<Test> findList(Test test) {
		return super.findList(test);
	}
	
	public Page<Test> findPage(Page<Test> page, Test test) {
		return super.findPage(page, test);
	}
	
	@Transactional(readOnly = false)
	public void save(Test test) {
		super.save(test);
	}
	
	@Transactional(readOnly = false)
	public void delete(Test test) {
		super.delete(test);
	}
	
	
	
	
}