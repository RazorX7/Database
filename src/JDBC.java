import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBC {
	static Connection dbConn;
	static Statement statement;
	static ResultSet result;
	/*实验JDBC主函数*/
	/*public static void main(String[] args) {
		getConnection();	
		TestJDBC();
		 releaseCON();
	}*/
	/*利用JDBC建立数据库连接*/
	public static void getConnection() {
		String driverName="com.mysql.cj.jdbc.Driver";
		String dbURL="jdbc:mysql://localhost:3306/Lab3?useSSL=false&serverTimezone=GMT%2B8";
		String userName="root";
		String userPwd="user";	
		try {
			Class.forName(driverName);
			dbConn=DriverManager.getConnection(dbURL,userName,userPwd);//建立连接
			// System.out.println("连接数据库成功");
			 if(!dbConn.isClosed())
					statement=dbConn.createStatement();//创建语句			 
		} catch(Exception e) {
			e.printStackTrace();
			//System.out.print("连接失败");
		 }	
	}
	public static ResultSet searchSQL(String sql) {
		ResultSet res=null;
		try {
			res=statement.executeQuery(sql);
		}catch(SQLException e){
			e.printStackTrace();
		}
		return res;
	}//查询功能
	
	public static void executeSQL(String sql) {
		try {
			statement.execute(sql);
		}catch(SQLException e){
			e.printStackTrace();
		}
	}//执行SQL插入、删除、更新语句功能
	
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
	}//根据查询结果展示出来
	public static void releaseCON() {
			try {
				if(!dbConn.isClosed())
					dbConn.close();
			}catch(SQLException e){
				e.printStackTrace();
			}
	}//释放资源，关闭连接
	
	public static void TestJDBC() {
		/*创建学生表*/
		System.out.println("创建学生表:\n");
		executeSQL("Create table Student(\r\n" + 
				"SNO int NOT NULL,\r\n" + 
				"SNAME char(8) NOT NULL,\r\n" + 
				"SEX char(2),\r\n" + 
				"AGE int,\r\n"+
				"DEPTNO int,\r\n" + 
				"primary key(SNO)\r\n" + 
				");");
		/*测试查询功能，查询所有学生*/
		System.out.println("查询学生表:\n");
		String QueryAllStudent="SELECT* FROM Student";
		result=searchSQL(QueryAllStudent);
		ShowRes();
		/*测试插入功能，插入新的数据*/
		System.out.println("插入新数据，插入后表中的数据如下:\n");
		executeSQL("INSERT INTO Student\r\n" + 
				"values\r\n" + 
				"(1001,'喵喵','m',10,20),\r\n" + 
				"(1002,'汪汪','f',10,21), \r\n" + 
				"(1003,'咩咩','m',10,21),\r\n" + 
				"(1004,'哞哞','f',20,21), \r\n" + 
				"(1005,'呱呱','m',20,22),\r\n" + 
				"(1006,'嘎嘎','f',20,22),\r\n" + 
				"(1007,'咕咕哒','f',30,20);");
		result=searchSQL(QueryAllStudent);
		ShowRes();
		/*测试查询功能，查询所有女生*/
		System.out.println("查询所有女生:\n");
		result=searchSQL("SELECT*\r\n" + 
				"FROM Student\r\n" + 
				"WHERE SEX='f';");
		ShowRes();
		/*测试删除功能，将年纪为30的人从表中删除*/
		System.out.println("删除年纪为30的人，结果显示如下:\n");
		executeSQL("DELETE FROM STUDENT WHERE AGE=30;");
		result=searchSQL(QueryAllStudent);
		ShowRes();
		/*测试更新功能，将所有人的年纪加1*/
		System.out.println("将所有人的年纪+1，结果显示如下:\n");
		executeSQL("Update Student set AGE=AGE+1;");
		result=searchSQL(QueryAllStudent);
		ShowRes();
	}
}
