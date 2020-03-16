import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class cmpJDBC extends JDBC{
	/*ֱ������ģʽ�����ӡ�ִ��SQL���ر�����2000��*/
	public void testcase1JDBC(){
		for(int i=0;i<2000;i++) {
			getConnection();
			searchSQL("select * from student");
			releaseCON();
		}
	}
	public void testcase2JDBC() {
		
		getConnection();
		for(int i=0;i<2000;i++) 
			searchSQL("select * from student");
		releaseCON();
	}
	public void testcaseJDBC(){
		
			getConnection();
			for(int i=0;i<500;i++) {
				searchSQL("select * from student");
			}
			releaseCON();
		
	}
	public void testcase3JDBC() {
		//����һ���������Ϊ4���̳߳�
		//�����ĸ��߳�,ÿ���߳�ִ�д򿪡�500�β�ѯ���رղ���
		CountDownLatch latch=new CountDownLatch(4);
		myThread process1=new myThread(latch);
		myThread process2=new myThread(latch);
		myThread process3=new myThread(latch);
		myThread process4=new myThread(latch);
		process1.start();
		process2.start();
		process3.start();
		process4.start();
		try {//�ȴ�ֱ�������������
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
	}
	public void testcase32JDBC() {
		ExecutorService executorService  = Executors.newFixedThreadPool(10);//�̶���ʮ�̳߳�
        for (int i = 0; i < 2000; i++) {
            executorService.execute(() -> {
                getConnection();
                searchSQL("select * from student");
                releaseCON();
            });
        }

        executorService.shutdown();//�����������̳߳����������

        try {//�ȴ�ֱ�������������
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
	}
}

class myThread extends Thread{
	CountDownLatch latch;
	public myThread(CountDownLatch latch) {
		this.latch=latch;
	}
	public void run() {
		cmpJDBC myjdbc=new cmpJDBC();
		myjdbc.testcaseJDBC();
		latch.countDown();
	}
}

class cmpDBCP extends DBCP{
	/*���ӳ�ģʽ�����ӡ�ִ��SQL���ر�����*/
	public void testcase1DBCP() {
		initDBCP();
		for(int i=0;i<2000;i++) {
			getConnection();
			querySQL("select * from student");
			releaseCON();
		}
	}
	
	public void testcase2DBCP() {
		initDBCP();
		getConnection();
		for(int i=0;i<2000;i++) 
			querySQL("select * from student");
		releaseCON();
	}
}

public class Compare {
	
	public static void main(String[] args) {
		testJdbcTwoThousandTimes();
		testDbcpTwoThousandTimes();
	}
	public static void testJdbcTwoThousandTimes() {
		long startTime;
		long endTime;
		startTime=System.currentTimeMillis();
		cmpJDBC myTestJdbc=new cmpJDBC();
		myTestJdbc.testcase1JDBC();
		endTime=System.currentTimeMillis();
		System.out.println("JDBC����2000�δ����ӡ���ѯ���ر����Ӻ�ʱ��");
		System.out.println((endTime-startTime)+"ms\n");
		
		startTime=System.currentTimeMillis();
		myTestJdbc.testcase2JDBC();
		endTime=System.currentTimeMillis();
		System.out.println("JDBC�����ӣ�����2000�β�ѯ����ر����Ӻ�ʱ��");
		System.out.println((endTime-startTime)+"ms\n");
		
		startTime=System.currentTimeMillis();
		myTestJdbc.testcase3JDBC();
		endTime=System.currentTimeMillis();
		System.out.println("JDBC����2000�δ����ӡ���ѯ���ر����Ӻ�ʱ��4�̣߳���");
		System.out.println((endTime-startTime)+"ms\n");
	}
	
	public static void testDbcpTwoThousandTimes() {
		long startTime;
		long endTime;
		startTime=System.currentTimeMillis();
		cmpDBCP myTestDbcp=new cmpDBCP();
		myTestDbcp.testcase1DBCP();
		endTime=System.currentTimeMillis();
		System.out.println("DBCP����2000�δ����ӡ���ѯ���ر����Ӻ�ʱ��");
		System.out.println((endTime-startTime)+"ms\n");
		
		startTime=System.currentTimeMillis();
		myTestDbcp.testcase2DBCP();
		endTime=System.currentTimeMillis();
		System.out.println("DBCP�����ӣ�����2000�β�ѯ����ر����Ӻ�ʱ��");
		System.out.println((endTime-startTime)+"ms\n");
		myTestDbcp.printParameter();
		
		myTestDbcp.pool.setMaxIdle(20);/*���������еȴ�ʱ��Ϊ20*/
		startTime=System.currentTimeMillis();
		myTestDbcp.testcase1DBCP();
		endTime=System.currentTimeMillis();
		System.out.println("�����еȴ�ʱ��Ϊ20������£�DBCP����2000�δ����ӡ���ѯ���ر����Ӻ�ʱ��");
		System.out.println((endTime-startTime)+"ms\n");
	}
}
