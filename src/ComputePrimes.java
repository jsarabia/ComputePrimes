import java.util.LinkedList;
import java.util.concurrent.Semaphore;

/**
 * Created by Joe on 9/20/2015.
 */
public class ComputePrimes {

    public static int counter = 1;
    public static int numPrimesFound = 0;
    public static int sum = 0;
    public static LinkedList<Integer> top10 = new LinkedList<>();

    public static boolean computePrime(int x){
        if (x == 1){
            return false;
        }
        if (x <= 3){
            return true;
        }
        for(int i = 4; i < x; i++){
            if (x % i == 0){
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args){

        Semaphore addPrimeLock = new Semaphore(1);
        Semaphore counterLock = new Semaphore(1);
        Thread[] threads =  new Thread[8];

        System.out.println("Initializing threads...");
        for(int i = 0; i < 8; i++){

            Thread temp = new Thread(new Runnable() {
                @Override
                public void run() {
                    boolean isFinished = false;
                    int numberToTry;
                    while(!isFinished){
                        try {
                            counterLock.acquire();
                            numberToTry = counter;
                            counter++;
                            System.out.println("Trying number: " + numberToTry);
                            counterLock.release();
                            if(numberToTry > Math.pow(10,8)){
                                break;
                            }
                            boolean isPrime = computePrime(numberToTry);
                            if(isPrime){
                                addPrimeLock.acquire();
                                numPrimesFound++;
                                top10.addLast(numberToTry);
                                if(top10.size() > 10){
                                    top10.removeFirst();
                                }
                                sum+= numberToTry;
                                addPrimeLock.release();
                            }

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            threads[i] = temp;
        }
        System.out.println("Beginning Execution...");
        long startTime = System.currentTimeMillis();
        for(Thread x : threads){
            x.start();
        }
        for(Thread x : threads){
            try {
                x.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Total execution time: " + (System.currentTimeMillis() - startTime));
        System.out.println("Total number of primes found: " + numPrimesFound);
        System.out.println("Sum of all primes found: " + sum);
        System.out.println("Top ten maximum primes, listed in order from lowest to highest: ");
        for(int i = 0; i < top10.size(); i++){
            System.out.println(top10.remove());
        }
    }
}
