package com.staples.pim.base.util;

public class InterruptBlockedThread implements Runnable{
	Thread t;
	int threasholdTime;
	public InterruptBlockedThread(Thread controledThread, int threasholdTime) {

		   //t = new Thread(this);
		   t = controledThread;
		   this.threasholdTime = threasholdTime;
		   
	}
	
   public void run() {
	   long startTime = System.currentTimeMillis();
	   System.out.println(" startTime:"+startTime);
	   long endTime = System.currentTimeMillis();
	   try {       
		   while ((endTime - startTime) < threasholdTime){
			   Thread.sleep(1000);
			   endTime = System.currentTimeMillis();
			   System.out.println(" endTime:"+endTime);
		   }
		   // interrupt the threads
		   if (!t.interrupted()) {
			   t.interrupt();
			   System.out.println(" interrupting monitored thread");
		   }
		   
	   } catch (InterruptedException e) {
		   System.out.print(t.getName() + " interrupted:");
		   System.out.println(e.toString());
	   }
   }

}
