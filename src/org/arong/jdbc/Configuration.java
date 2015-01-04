package org.arong.jdbc;

public final class Configuration {
	public static final String CONFIG_FILE_NAME = "arong-db.properties";
	public static final String CONFIG_DRIVER_KEY = "driver";
	public static final String CONFIG_URL_KEY = "url";
	public static final String CONFIG_USER_KEY = "user";
	public static final String CONFIG_PASSWORD_KEY = "password";
	public static final String CONFIG_MAXACTIVE_KEY = "maxActive";
	public static final String CONFIG_MAXIDLE_KEY = "maxIdle";
	public static final String CONFIG_MINIDLE_KEY = "minIdle";
	public static final String CONFIG_INITIALSIZE_KEY = "initialSize";
	public static final String CONFIG_MAXWAIT_KEY = "maxWait";
	public static final String CONFIG_DATASOURCE_KEY = "dataSource";
	
	/**
	 * 获取配置jdbcUtil配置说明信息
	 */
	public static void printConfigIntru() {
		System.out.println("一、在工程的src目录下创建一个名为：" + CONFIG_FILE_NAME
				+ "的数据库配置文件。");
		System.out.println("  必需属性：");
		System.out.println("       1." + CONFIG_DRIVER_KEY + "：数据库驱动");
		System.out.println("       2." + CONFIG_URL_KEY + "：数据库url地址");
		System.out.println("       3." + CONFIG_USER_KEY + "：数据库用户名称");
		System.out.println("       4." + CONFIG_PASSWORD_KEY + "：数据库用户密码");
		System.out.println("  可选属性：");
		System.out.println("       5." + CONFIG_DATASOURCE_KEY + "：数据源名称");
		System.out.println("       6." + CONFIG_MAXACTIVE_KEY + "：数据源最大连接数");
		System.out.println("       7." + CONFIG_MAXIDLE_KEY + "：数据源最大峰值");
		System.out.println("       8." + CONFIG_MINIDLE_KEY + "：数据源最小峰值");
		System.out.println("       9." + CONFIG_INITIALSIZE_KEY + "：数据源初始值");
		System.out.println("       10." + CONFIG_MAXWAIT_KEY + "：最大等待时间");
		System.out.println("二、默认使用内置的数据源，也可以通过指定" + CONFIG_DATASOURCE_KEY + "来使用第三方数据源");
		System.out.println("               当" + CONFIG_DATASOURCE_KEY
				+ "=c3p0,则使用c3p0连接池；");
		System.out.println("               当" + CONFIG_DATASOURCE_KEY
				+ "=dbcp，则使用dbcp连接池。");
	}
}
