package org.arong.util.excel;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author cyy
 * @date [Wed May 14 15:48:56 CST 2014]
 * @desc 功能:本类用于定义 excel 文件的标题列
 */
public class ExcelTitle implements Serializable {

	private static final long serialVersionUID = 1L;
      
    /** 
    * 域 编码
    */
    protected String code;
      
    /** 
    * 域 名称
    */
    protected String name;
      
    /** 
    * 域 对应数据字典转换类
    */
    protected String changer;
      
    /** 
    * 域  宽度 默认300
    */
    protected String width;
      
    /** 
    * 域 其他样式
    */
    protected String style;
    
    // 数据字典转换集合
    List<Map> changerList;
    

	public ExcelTitle(){
    	
    }
    
    public ExcelTitle(String code,String name){
    	this.code=code;
    	this.name=name;
    }
    
    public ExcelTitle(String code,String name,String changer){
    	this.code=code;
    	this.name=name;
    	this.changer=changer;
    }
    
    public ExcelTitle(String code,String name,List changerList){
    	this.code=code;
    	this.name=name;
    	this.changerList=changerList;
    }
    
    public ExcelTitle(String code,String name,String changer,String width,String style){
    	this.code=code;
    	this.name=name;
    	this.changer=changer;
    	this.width=width;
    	this.style=style;
    }
    
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getChanger() {
		return changer;
	}

	public void setChanger(String changer) {
		this.changer = changer;
	}

	public String getWidth() {
		if(width==null)
			width=String.valueOf(this.getName().length()*5);
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}
   
    public List<Map> getChangerList() {
		return changerList;
	}

	public void setChangerList(List<Map> changerList) {
		this.changerList = changerList;
	}

	
//	@Override
//   public boolean equals(Object o){
//	    
//		
//		return false;
//   }
 
}