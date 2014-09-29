/**
 * 
 */
package oecp.framework.excel;

import java.util.Collection;

import oecp.framework.excel.enums.Entity2SheetType;

/**
 * 一个Entity2SheetConfig对应一个excel中的sheet
 * 把实体信息放在这个配置中，生成到对应的sheet里面
 * @author YangTao
 * @date 2012-8-9
 * @version 1.0
 */
public class Entity2SheetConfig<E> {

	//sheet标题名称
	private String title;
	//sheet内容头标题
	private String[] headTitles;
	//sheet每列内容对应实体E的字段
	private String[] head2Field;
	//实体数据集合
	private Collection<E> dataset;
	//形成sheet时的数据载体类型
	private Entity2SheetType entity2SheetType;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String[] getHeadTitles() {
		return headTitles;
	}
	public void setHeadTitles(String[] headTitles) {
		this.headTitles = headTitles;
	}
	public Collection<E> getDataset() {
		return dataset;
	}
	public void setDataset(Collection<E> dataset) {
		this.dataset = dataset;
	}
	public String[] getHead2Field() {
		return head2Field;
	}
	public void setHead2Field(String[] head2Field) {
		this.head2Field = head2Field;
	}
	public Entity2SheetType getEntity2SheetType() {
		return entity2SheetType;
	}
	public void setEntity2SheetType(Entity2SheetType entity2SheetType) {
		this.entity2SheetType = entity2SheetType;
	}
	
}
