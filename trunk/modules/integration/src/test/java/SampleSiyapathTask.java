import org.siyapath.task.SiyapathTask;

import java.util.StringTokenizer;

// A sample Job that gets a range of numbers as input and outputs the list of prime numbers in that range

public class SampleSiyapathTask implements SiyapathTask {

    byte[] inputData, results;
    long startNumber, endNumber;

    @Override
    public void process() {
        if (preProcessData()) {
            String resultString = "";
            for (long num = startNumber; num <= endNumber; num++) {
                if (isPrime(num)) {
                    resultString = resultString + num + ",";
                }
            }
            results = resultString.getBytes();
        } else {
            results = "Invalid or corrupted inputs".getBytes();
        }
    }


    @Override
    public void setData(byte[] inputData) {
        this.inputData = inputData;
    }

    @Override
    public byte[] getResults() {
        return results;
    }

    private boolean preProcessData() {
        if (inputData == null) return false;

        String data = new String(inputData);
        StringTokenizer tokenizer = new StringTokenizer(data, ",");
        if (tokenizer.countTokens() != 2) return false;

        startNumber = Integer.valueOf(tokenizer.nextToken());
        endNumber = Integer.valueOf(tokenizer.nextToken());
        return true;
    }


    private boolean isPrime(long num) {
        if (num % 2 == 0) {
            return false;
        }
        for (int n = 3; n <= Math.sqrt(num); n += 2) {
            if (num != n && num % n == 0) {
                return false;
            }
        }
        return true;
    }


    /**
     *  For testing
     */
//    public static void main(String[] args) {
//        SampleSiyapathTask job = new SampleSiyapathTask();
//        job.setData("20,100000");
//        job.process();
//    }
}
