import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class BaiTapB7Thread {
   static class MyThread implements Runnable {
        private String threadName;

        public MyThread(String name) {
            this.threadName = name;
        }

        @Override
        public void run() {
            for (int i = 1; i <= 10; i++) {
                System.out.println(threadName + " - Số: " + i);
                try {
                    Thread.sleep(500); // Dừng 500 milliseconds
                } catch (InterruptedException e) {
                    System.out.println(threadName + " bị gián đoạn.");
                }
            }
        }

        public static void main(String[] args) {
            Thread thread1 = new Thread(new MyThread("Thread1"));
            Thread thread2 = new Thread(new MyThread("Thread2"));

            thread1.setPriority(Thread.MAX_PRIORITY); // Đặt ưu tiên cao nhất
            thread2.setPriority(Thread.MIN_PRIORITY); // Đặt ưu tiên thấp nhất

            thread1.start();
            thread2.start();
        }
    }

    static class OddthreadAndEvenThread {
       static class OddThread extends Thread{
           public void run(){
               for (int i = 1; i <= 10; i +=2) {
                   System.out.println("OddThread - Số lẻ: " + i);
                   try {
                       Thread.sleep(10); // Dừng 500 milliseconds
                   } catch (InterruptedException e) {
                       System.out.println("OddThread bị gián đoạn.");
                   }
               }
           }
       }
       static class EvenThread extends Thread{
           public void run(){
               for (int i = 2; i <= 10; i+=2) {
                   System.out.println("EvenThread - Số chẵn: " + i);
                   try {
                       Thread.sleep(15);
                   } catch (InterruptedException e) {
                       System.out.println("EvenThread bị gián đoạn.");
                   }
               }
           }
       }
      static class TestThread {
           public static void main(String[] args){
               OddThread oddThread= new OddThread();
               EvenThread evenThread=new EvenThread();

               oddThread.start();
               try {
                   oddThread.join();
               }catch (InterruptedException e){
                   System.out.println("OddThread bị gián đoạn.");
               }
               evenThread.start();
           }
       }
    }


}
