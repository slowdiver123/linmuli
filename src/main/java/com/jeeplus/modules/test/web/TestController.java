/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.test.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.MyBeanUtils;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.persistence.Page;
import com.jeeplus.common.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.modules.test.entity.Test;
import com.jeeplus.modules.test.service.TestService;

/**
 * testController
 * @author test
 * @version 2016-10-31
 */
@Controller
@RequestMapping(value = "${adminPath}/test/test")
public class TestController extends BaseController {

	@Autowired
	private TestService testService;
	
	@ModelAttribute
	public Test get(@RequestParam(required=false) String id) {
		Test entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = testService.get(id);
		}
		if (entity == null){
			entity = new Test();
		}
		return entity;
	}
	
	/**
	 * test列表页面
	 */
	@RequiresPermissions("test:test:list")
	@RequestMapping(value = {"list", ""})
	public String list(Test test, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Test> page = testService.findPage(new Page<Test>(request, response), test); 
		model.addAttribute("page", page);
		return "modules/test/testList";
	}

	/**
	 * 查看，增加，编辑test表单页面
	 */
	@RequiresPermissions(value={"test:test:view","test:test:add","test:test:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Test test, Model model) {
		model.addAttribute("test", test);
		return "modules/test/testForm";
	}

	/**
	 * 保存test
	 */
	@RequiresPermissions(value={"test:test:add","test:test:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public String save(Test test, Model model, RedirectAttributes redirectAttributes) throws Exception{
		if (!beanValidator(model, test)){
			return form(test, model);
		}
		if(!test.getIsNewRecord()){//编辑表单保存
			Test t = testService.get(test.getId());//从数据库取出记录的值
			MyBeanUtils.copyBeanNotNull2Bean(test, t);//将编辑表单中的非NULL值覆盖数据库记录中的值
			testService.save(t);//保存
		}else{//新增表单保存
			testService.save(test);//保存
		}
		addMessage(redirectAttributes, "保存test成功");
		return "redirect:"+Global.getAdminPath()+"/test/test/?repage";
	}
	
	/**
	 * 删除test
	 */
	@RequiresPermissions("test:test:del")
	@RequestMapping(value = "delete")
	public String delete(Test test, RedirectAttributes redirectAttributes) {
		testService.delete(test);
		addMessage(redirectAttributes, "删除test成功");
		return "redirect:"+Global.getAdminPath()+"/test/test/?repage";
	}
	
	/**
	 * 批量删除test
	 */
	@RequiresPermissions("test:test:del")
	@RequestMapping(value = "deleteAll")
	public String deleteAll(String ids, RedirectAttributes redirectAttributes) {
		String idArray[] =ids.split(",");
		for(String id : idArray){
			testService.delete(testService.get(id));
		}
		addMessage(redirectAttributes, "删除test成功");
		return "redirect:"+Global.getAdminPath()+"/test/test/?repage";
	}
	
	/**
	 * 导出excel文件
	 */
	@RequiresPermissions("test:test:export")
    @RequestMapping(value = "export", method=RequestMethod.POST)
    public String exportFile(Test test, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "test"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Test> page = testService.findPage(new Page<Test>(request, response, -1), test);
    		new ExportExcel("test", Test.class).setDataList(page.getList()).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导出test记录失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/test/test/?repage";
    }

	/**
	 * 导入Excel数据

	 */
	@RequiresPermissions("test:test:import")
    @RequestMapping(value = "import", method=RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Test> list = ei.getDataList(Test.class);
			for (Test test : list){
				try{
					testService.save(test);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条test记录。");
			}
			addMessage(redirectAttributes, "已成功导入 "+successNum+" 条test记录"+failureMsg);
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入test失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/test/test/?repage";
    }
	
	/**
	 * 下载导入test数据模板
	 */
	@RequiresPermissions("test:test:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		try {
            String fileName = "test数据导入模板.xlsx";
    		List<Test> list = Lists.newArrayList(); 
    		new ExportExcel("test数据", Test.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			addMessage(redirectAttributes, "导入模板下载失败！失败信息："+e.getMessage());
		}
		return "redirect:"+Global.getAdminPath()+"/test/test/?repage";
    }
	
	
	

}