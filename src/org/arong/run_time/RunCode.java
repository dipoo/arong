/**
 * 所有用于测试代码效率的类都必须继承此类并将测试代码写入code()方法中
 * 效率测试父类采用模版设计模式实现
 */
package org.arong.run_time;

public abstract class RunCode //3.有抽象方法code()所以必须定义为抽象类
{
	// 1.计算一段代码的运行时间
	public final void getRuntime(String comment) throws Exception{	//5.方法为最终代码final修饰
	    // 获取运行前系统的当前时间  毫秒  一秒 = 1000毫秒
	long start = System.currentTimeMillis(); 
		// 测试代码
		code();		//2.调用需要测试运行时间的方法,
		// 运行结束后获取系统的当前时间
	long end = System.currentTimeMillis(); 
		//结束时间减开始时间等于运行用时
	   System.out.println(comment + "所用时间： " + ( end - start ) + "ms");
	}
	public abstract Object code() throws Exception;//3.不知这方法如何实现,定义成抽象
}