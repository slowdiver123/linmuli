/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.entity;

import org.hibernate.validator.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.Max;

import com.jeeplus.common.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * testEntity
 * @author test
 * @version 2016-10-31
 */
public class Test extends DataEntity<Test> {
	
	private static final long serialVersionUID = 1L;
	private String zhb;		// gggg
	private String yyy;		// yy
	
	public Test() {
		super();
	}

	public Test(String id){
		super(id);
	}

	@Email(message="gggg必须为合法邮箱")
	@ExcelField(title="gggg", align=2, sort=7)
	public String getZhb() {
		return zhb;
	}

	public void setZhb(String zhb) {
		this.zhb = zhb;
	}
	
	@Min(value=4,message="yy的最小值不能小于4")
	@Max(value=70,message="yy的最大值不能超过70")
	@ExcelField(title="yy", align=2, sort=8)
	public String getYyy() {
		return yyy;
	}

	public void setYyy(String yyy) {
		this.yyy = yyy;
	}
	
}