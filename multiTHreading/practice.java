// Online Java Compiler
// Use this editor to write, compile and run your Java code online

class MyThread extends Thread {
    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            System.out.println(i);
        }
    }
}

class practice {
    public static void main(String[] args) {

        Thread myThread = new Thread(new MyThread());
        myThread.start();

    }
}