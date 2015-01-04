package org.arong.validator;



public final class ValidationRuleUtil {
	/**
	 * 非空验证
	 * @param field 字段值
	 * @return
	 */
	public static boolean notEmpty(Object field){
		if(field == null)
			return false;
		else if(field instanceof java.lang.String && ((String)field).trim().length() == 0)
			return false;
		return true;
	}
	/**
	 * 验证一个字段值的长度(字符数)是否在指定范围（无边界值）
	 * @param field 字段值
	 * @param min 最小长度
	 * @param max 最大长度
	 * @return
	 */
	public static boolean lengthExcludeRestrict(Object field, Integer min, Integer max){
		if(notEmpty(field) && String.valueOf(field).length() > min && String.valueOf(field).length() < max)
			return true;
		return false;
	}
	/**
	 * 验证一个字段值的长度(字符数)是否在指定范围（含边界值）
	 * @param field 字段值
	 * @param min 最小长度
	 * @param max 最大长度
	 * @return
	 */
	public static boolean lengthIncludeRestrict(Object field, Integer min, Integer max){
		if(notEmpty(field) && String.valueOf(field).length() >= min && String.valueOf(field).length() <= max)
			return true;
		return false;
	}
	/**
	 * 验证一个字符串字段值是否符合邮箱格式
	 * @param field 字段值
	 * @return
	 */
	public static boolean isEmailPattren(String field){
		if(notEmpty(field) && field.trim().matches("\\b(^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@([A-Za-z0-9-])+((\\.com)|(\\.net)|(\\.org)|(\\.info)|(\\.edu)|(\\.mil)|(\\.gov)|(\\.biz)|(\\.ws)|(\\.us)|(\\.tv)|(\\.cc)|(\\.aero)|(\\.arpa)|(\\.coop)|(\\.int)|(\\.jobs)|(\\.museum)|(\\.name)|(\\.pro)|(\\.travel)|(\\.nato)|(\\..{2,3})|(\\..{2,3}\\..{2,3}))$)\\b"))
			return true;
		return false;
	}
	
	/**
	 * 验证一个字符串是否全部是中文
	 * @param field 字段值
	 * @return
	 */
	public static boolean isChineseString(String field){
		if(notEmpty(field) && field.trim().matches("[\u4E00-\uFA29]+"))
			return true;
		return false;
	}
	
	/**
	 * 验证一个int整数是否在指定的范围(含边界值)
	 * @param field 字段值
	 * @param min 最小值
	 * @param max 最大值
	 * @return
	 */
	public static boolean IntegerRangeRestrict(Integer field, Integer min, Integer max){
		if(notEmpty(field) && field >= min && field <= max)
			return true;
		return false;
	}
	
	/**
	 * 验证一个Long整数是否在指定的范围(含边界值)
	 * @param field 字段值
	 * @param min 最小值
	 * @param max 最大值
	 * @return
	 */
	public static boolean LongRangeRestrict(Long field, Long min, Long max){
		if(notEmpty(field) && field >= min && field <= max)
			return true;
		return false;
	}
	public static void main(String[] args) {
		//System.out.println(String.valueOf(null + ""));
		
//		System.out.println(isEmailPattren("120826531@qq.com"));
		
//		System.out.println(isChineseString("12"));
		
//		System.out.println(IntegerRangeRestrict(45, 56, 7899));
		
		System.out.println(LongRangeRestrict(154456656L, 11215L, 448444854515215L));
	}
}
