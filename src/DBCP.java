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
	/*ʵ��DBCP������*/
/*	public static void main(String[] args) {
		initDBCP();
		getConnection();
		String QueryAllStudent="SELECT* FROM Student";
		result=querySQL(QueryAllStudent);
		ShowRes();
		releaseCON();
	
		//pool.setMaxTotal(20);
		
		//�����ĳ��л�ȡ����
		
	}*/
	public void initDBCP() {
		/*��Java��ʽ���ò���*/
		pool=new BasicDataSource();
		pool.setUsername("root");
		pool.setPassword("user");
		pool.setDriverClassName("com.mysql.cj.jdbc.Driver");
		pool.setUrl("jdbc:mysql://localhost:3306/Lab3?"
				+ "useSSL=false&serverTimezone=GMT%2B8");
	}
	
	/*��������ݿ������*/
	public void getConnection() {
		try {
			conn=pool.getConnection();
			if(!conn.isClosed())
				statement=conn.createStatement();
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	/*����ִ��SQL��ѯ���*/
	public static ResultSet querySQL(String sql) {
		ResultSet res=null;
		try {
			res=statement.executeQuery(sql);
		}catch(SQLException e){
			e.printStackTrace();
		}
		return res;
	}
	/*����ѯ�����ʾ����*/
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
	/*��ӡ���ӳصĲ���*/
	public void printParameter() {
		//��ӡ������ʱ��
		System.out.println(pool.getMaxIdle());
		//��ӡ���׳��쳣֮ǰ���صȴ����ӱ����յ��ʱ��
		System.out.println(pool.getMaxWaitMillis());
		//��ӡ��ʼ��ʱ�м���Connection
		System.out.println(pool.getInitialSize());
		//��ӡ������ж��ٸ�Connection
		System.out.println(pool.getMaxTotal());
		//���óص���ز����������������
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
	}//�ͷ���Դ���ر�����
	
	
}
