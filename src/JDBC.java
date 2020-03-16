import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBC {
	static Connection dbConn;
	static Statement statement;
	static ResultSet result;
	/*ʵ��JDBC������*/
	/*public static void main(String[] args) {
		getConnection();	
		TestJDBC();
		 releaseCON();
	}*/
	/*����JDBC�������ݿ�����*/
	public static void getConnection() {
		String driverName="com.mysql.cj.jdbc.Driver";
		String dbURL="jdbc:mysql://localhost:3306/Lab3?useSSL=false&serverTimezone=GMT%2B8";
		String userName="root";
		String userPwd="user";	
		try {
			Class.forName(driverName);
			dbConn=DriverManager.getConnection(dbURL,userName,userPwd);//��������
			// System.out.println("�������ݿ�ɹ�");
			 if(!dbConn.isClosed())
					statement=dbConn.createStatement();//�������			 
		} catch(Exception e) {
			e.printStackTrace();
			//System.out.print("����ʧ��");
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
	}//��ѯ����
	
	public static void executeSQL(String sql) {
		try {
			statement.execute(sql);
		}catch(SQLException e){
			e.printStackTrace();
		}
	}//ִ��SQL���롢ɾ����������书��
	
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
	}//���ݲ�ѯ���չʾ����
	public static void releaseCON() {
			try {
				if(!dbConn.isClosed())
					dbConn.close();
			}catch(SQLException e){
				e.printStackTrace();
			}
	}//�ͷ���Դ���ر�����
	
	public static void TestJDBC() {
		/*����ѧ����*/
		System.out.println("����ѧ����:\n");
		executeSQL("Create table Student(\r\n" + 
				"SNO int NOT NULL,\r\n" + 
				"SNAME char(8) NOT NULL,\r\n" + 
				"SEX char(2),\r\n" + 
				"AGE int,\r\n"+
				"DEPTNO int,\r\n" + 
				"primary key(SNO)\r\n" + 
				");");
		/*���Բ�ѯ���ܣ���ѯ����ѧ��*/
		System.out.println("��ѯѧ����:\n");
		String QueryAllStudent="SELECT* FROM Student";
		result=searchSQL(QueryAllStudent);
		ShowRes();
		/*���Բ��빦�ܣ������µ�����*/
		System.out.println("���������ݣ��������е���������:\n");
		executeSQL("INSERT INTO Student\r\n" + 
				"values\r\n" + 
				"(1001,'����','m',10,20),\r\n" + 
				"(1002,'����','f',10,21), \r\n" + 
				"(1003,'����','m',10,21),\r\n" + 
				"(1004,'����','f',20,21), \r\n" + 
				"(1005,'����','m',20,22),\r\n" + 
				"(1006,'�¸�','f',20,22),\r\n" + 
				"(1007,'������','f',30,20);");
		result=searchSQL(QueryAllStudent);
		ShowRes();
		/*���Բ�ѯ���ܣ���ѯ����Ů��*/
		System.out.println("��ѯ����Ů��:\n");
		result=searchSQL("SELECT*\r\n" + 
				"FROM Student\r\n" + 
				"WHERE SEX='f';");
		ShowRes();
		/*����ɾ�����ܣ������Ϊ30���˴ӱ���ɾ��*/
		System.out.println("ɾ�����Ϊ30���ˣ������ʾ����:\n");
		executeSQL("DELETE FROM STUDENT WHERE AGE=30;");
		result=searchSQL(QueryAllStudent);
		ShowRes();
		/*���Ը��¹��ܣ��������˵���ͼ�1*/
		System.out.println("�������˵����+1�������ʾ����:\n");
		executeSQL("Update Student set AGE=AGE+1;");
		result=searchSQL(QueryAllStudent);
		ShowRes();
	}
}
