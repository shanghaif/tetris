package com.sumavision.tetris.cms.relation;

import java.util.Comparator;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 文章元数据<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年2月26日 下午3:45:09
 */
@Table
@Entity(name = "TETRIS_CMS_COLUMN_RELATION_ARTICLE")
public class ColumnRelationArticlePO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	private Long articleId;
	
	private String articleName;
	
	private String articleRemark;
	
	/** 文章顺序--由小到大排序  */
	private Long articleOrder;
	
	private Long columnId;
	
	private String columnName;
	
	private String columnRemark;

	public Long getArticleId() {
		return articleId;
	}

	public void setArticleId(Long articleId) {
		this.articleId = articleId;
	}

	public Long getColumnId() {
		return columnId;
	}

	public void setColumnId(Long columnId) {
		this.columnId = columnId;
	}

	public String getArticleName() {
		return articleName;
	}

	public void setArticleName(String articleName) {
		this.articleName = articleName;
	}

	public String getArticleRemark() {
		return articleRemark;
	}

	public void setArticleRemark(String articleRemark) {
		this.articleRemark = articleRemark;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getColumnRemark() {
		return columnRemark;
	}

	public void setColumnRemark(String columnRemark) {
		this.columnRemark = columnRemark;
	}

	public Long getArticleOrder() {
		return articleOrder;
	}

	public void setArticleOrder(Long articleOrder) {
		this.articleOrder = articleOrder;
	}
	
	/**
	 * @ClassName: 排序器，从小到大排列<br/> 
	 * @author lvdeyang
	 * @date 2019年2月27日 上午8:36:10 
	 */
	public static final class ArticleOrderComparator implements Comparator<ColumnRelationArticlePO>{
		@Override
		public int compare(ColumnRelationArticlePO o1, ColumnRelationArticlePO o2) {
			
			if(o1.getArticleOrder() > o2.getArticleOrder()){
				return 1;
			}
			if(o1.getArticleOrder() == o2.getArticleOrder()){
				return 0;
			}
			return -1;
		}
	}
}