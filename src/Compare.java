import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class cmpJDBC extends JDBC{
	/*直接连接模式打开连接、执行SQL、关闭连接2000次*/
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
		//创建一个并发度最长为4的线程池
		//创建四个线程,每个线程执行打开、500次查询、关闭操作
		CountDownLatch latch=new CountDownLatch(4);
		myThread process1=new myThread(latch);
		myThread process2=new myThread(latch);
		myThread process3=new myThread(latch);
		myThread process4=new myThread(latch);
		process1.start();
		process2.start();
		process3.start();
		process4.start();
		try {//等待直到所有任务完成
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
	}
	public void testcase32JDBC() {
		ExecutorService executorService  = Executors.newFixedThreadPool(10);//固定的十线程池
        for (int i = 0; i < 2000; i++) {
            executorService.execute(() -> {
                getConnection();
                searchSQL("select * from student");
                releaseCON();
            });
        }

        executorService.shutdown();//不允许再往线程池中添加任务

        try {//等待直到所有任务完成
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
	/*连接池模式打开连接、执行SQL、关闭连接*/
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
		System.out.println("JDBC测试2000次打开连接、查询、关闭连接耗时：");
		System.out.println((endTime-startTime)+"ms\n");
		
		startTime=System.currentTimeMillis();
		myTestJdbc.testcase2JDBC();
		endTime=System.currentTimeMillis();
		System.out.println("JDBC打开连接，测试2000次查询，后关闭连接耗时：");
		System.out.println((endTime-startTime)+"ms\n");
		
		startTime=System.currentTimeMillis();
		myTestJdbc.testcase3JDBC();
		endTime=System.currentTimeMillis();
		System.out.println("JDBC测试2000次打开连接、查询、关闭连接耗时（4线程）：");
		System.out.println((endTime-startTime)+"ms\n");
	}
	
	public static void testDbcpTwoThousandTimes() {
		long startTime;
		long endTime;
		startTime=System.currentTimeMillis();
		cmpDBCP myTestDbcp=new cmpDBCP();
		myTestDbcp.testcase1DBCP();
		endTime=System.currentTimeMillis();
		System.out.println("DBCP测试2000次打开连接、查询、关闭连接耗时：");
		System.out.println((endTime-startTime)+"ms\n");
		
		startTime=System.currentTimeMillis();
		myTestDbcp.testcase2DBCP();
		endTime=System.currentTimeMillis();
		System.out.println("DBCP打开连接，测试2000次查询，后关闭连接耗时：");
		System.out.println((endTime-startTime)+"ms\n");
		myTestDbcp.printParameter();
		
		myTestDbcp.pool.setMaxIdle(20);/*设置最大空闲等待时间为20*/
		startTime=System.currentTimeMillis();
		myTestDbcp.testcase1DBCP();
		endTime=System.currentTimeMillis();
		System.out.println("最大空闲等待时间为20的情况下，DBCP测试2000次打开连接、查询、关闭连接耗时：");
		System.out.println((endTime-startTime)+"ms\n");
	}
}
