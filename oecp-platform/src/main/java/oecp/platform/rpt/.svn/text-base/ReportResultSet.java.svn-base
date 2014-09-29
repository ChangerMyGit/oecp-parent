/**
 * oecp-platform - UIViewParser.java
 * copyright 2009-2012 OECP www.oecp.cn
 * 创建人:slx	创建时间:2011-11-7上午10:41:16		版本:v1
 * ============================================
 * 修改人：			修改时间:					版本:
 * 修改原因:
 *
 * ============================================
 */

package oecp.platform.rpt;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import oecp.framework.dao.QueryResult;
import oecp.framework.util.FastJsonUtils;
import oecp.framework.vo.base.DataVO;
import oecp.framework.web.JsonResult;

/**
 * 报表查询得到的数据结果
 * 
 * @author slx
 * @date 2012-4-27 下午2:11:27
 * @version 1.0
 * 
 */
public class ReportResultSet implements Serializable {
	private static final long serialVersionUID = 1L;
	/** 表格数据 **/
	private HashMap<String, QueryResult<? extends DataVO>> grids;
	/** 图表数据 **/
	private HashMap<String, List<?>> charts;
	
	public void gridRename(String oldname,String newname){
		changeKey(grids,oldname,newname);
	}
	public void chartRename(String oldname,String newname){
		changeKey(charts,oldname,newname);
	}
	
	private void changeKey(HashMap map,String oldkey,String newkey){
		if(oldkey.equals(newkey)){
			return ;
		}
		Object obj = map.get(oldkey);
		map.remove(oldkey);
		map.put(newkey, obj);
	}
	
	public ReportResultSet() {
		grids = new HashMap<String, QueryResult<? extends DataVO>>();
		charts = new HashMap<String, List<?>>();
	}

	public HashMap<String, QueryResult<? extends DataVO>> getGrids() {
		return grids;
	}

	public void setGrids(HashMap<String, QueryResult<? extends DataVO>> grids) {
		this.grids = grids;
	}

	public HashMap<String, List<?>> getCharts() {
		return charts;
	}

	public void setCharts(HashMap<String, List<?>> charts) {
		this.charts = charts;
	}
}
