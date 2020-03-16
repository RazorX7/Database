import java.sql.Connection;
import java.sql.ResultSet;
//import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.dbcp2.BasicDataSource;

public class DBCP {
	static Connection conn=null;
	static Statement statement;
	public BasicDataSource pool;
	static ResultSet result;
	/*实验DBCP主函数*/
/*	public static void main(String[] args) {
		initDBCP();
		getConnection();
		String QueryAllStudent="SELECT* FROM Student";
		result=querySQL(QueryAllStudent);
		ShowRes();
		releaseCON();
	
		//pool.setMaxTotal(20);
		
		//从它的池中获取连接
		
	}*/
	public void initDBCP() {
		/*纯Java方式设置参数*/
		pool=new BasicDataSource();
		pool.setUsername("root");
		pool.setPassword("user");
		pool.setDriverClassName("com.mysql.cj.jdbc.Driver");
		pool.setUrl("jdbc:mysql://localhost:3306/Lab3?"
				+ "useSSL=false&serverTimezone=GMT%2B8");
	}
	
	/*获得与数据库的连接*/
	public void getConnection() {
		try {
			conn=pool.getConnection();
			if(!conn.isClosed())
				statement=conn.createStatement();
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	/*尝试执行SQL查询语句*/
	public static ResultSet querySQL(String sql) {
		ResultSet res=null;
		try {
			res=statement.executeQuery(sql);
		}catch(SQLException e){
			e.printStackTrace();
		}
		return res;
	}
	/*将查询结果显示出来*/
	public static void ShowRes() {
		try {
			while(result.next()) {
				System.out.println("SNO:"+result.getString(1)+
				"SNAME:"+result.getString(2)+
				"SEX:"+result.getString(3)+
				"AGE:"+result.getString(4)+
				"DEPTNO"+result.getString(5));
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	/*打印连接池的参数*/
	public void printParameter() {
		//打印最大空闲时间
		System.out.println(pool.getMaxIdle());
		//打印在抛出异常之前，池等待连接被回收的最长时间
		System.out.println(pool.getMaxWaitMillis());
		//打印初始化时有几个Connection
		System.out.println(pool.getInitialSize());
		//打印最多能有多少个Connection
		System.out.println(pool.getMaxTotal());
		//设置池的相关参数，如最大连接数
	}
	public static void releaseCON() {
		try {
			if(result!=null) 
				result.close();
		}catch(SQLException e){
				e.printStackTrace();
		}finally {
			try {
				if(statement!=null) 
					statement.close();
			}catch(SQLException e){
				e.printStackTrace();
			}finally {
				try {
					if(conn!=null)			
						conn.close();
				}catch(SQLException e){
					e.printStackTrace();
				}
			}
		}
	}//释放资源，关闭连接
	
	
}
