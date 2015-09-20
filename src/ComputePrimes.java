import java.lang.reflect.Array;

/**
 * Created by Joe on 9/20/2015.
 */
public class ComputePrimes {

    public static int numbersToCheck = (int)Math.pow(10,8);
    public static int numThreads = 8;

    //need to include the last index as addressable
    public static boolean[] sieveTable = new boolean[numbersToCheck+1];
    public static int lastNumberNeeded = (int)Math.ceil(Math.sqrt(numbersToCheck));


    public static void main(String[] args){

        sieveTable[0] = true;
        sieveTable[1] = true;
        Thread[] threads =  new Thread[numThreads];
        System.out.println("Initializing threads...");
        for(int i = 0; i < numThreads; i++){

            final int index = i;
            Thread temp = new Thread(new Runnable() {
                @Override
                public void run() {
                    final int tid = index;
                    int startIndex = Math.max((numbersToCheck / 8) * tid, 1);
                    int endIndex = Math.min((numbersToCheck / 8) * (tid+1), numbersToCheck);
                    for(int numberToCheck = 2; numberToCheck <= lastNumberNeeded; numberToCheck++){

                        System.out.println("number to Check is " + numberToCheck);

                        if(sieveTable[numberToCheck] == true) {
                            continue;
                        }

                        int offset = 0;
                        for(int i = startIndex; i <= endIndex; i++){
                            if(i % numberToCheck == 0){
                                offset = i;
                                break;
                            }
                        }

                        for(int i = offset; i <= endIndex; i += numberToCheck){
                            if(i == numberToCheck) {
                                continue;
                            }
                            sieveTable[i] = true;
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
        long sum = 0;
        int numPrimes = 0;
        for(int i = 0; i < sieveTable.length; i++){
            if(sieveTable[i] == false){
                sum+=i;
                numPrimes++;
            }
        }
        System.out.println("Total number of primes found: " + numPrimes);
        System.out.println("Sum of all primes found: " + sum);
        //System.out.println("Top ten maximum primes, listed in order from lowest to highest: ");
        //for(int i = 0; i < top10.size(); i++){
        //    System.out.println(top10.remove());
        //}
    }
}
