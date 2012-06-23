import org.siyapath.job.SiyapathJob;

import java.util.StringTokenizer;

// A sample Job that gets a range of numbers as input and outputs the list of prime numbers in that range

public class SampleSiyapathJob implements SiyapathJob {

    Object inputData, results;
    long startNumber, endNumber;

    @Override
    public void process() {
        if (preProcessData()) {
            results = "";
            for (long num = startNumber; num <= endNumber; num++) {
                if (isPrime(num)) {
                    results = (String) results + num + ",";
                }
            }
//            results = (String) results + "EoR";
        } else {
            results = "Invalid or corrupted inputs";
        }
    }


    @Override
    public void setData(Object inputData) {
        this.inputData = inputData;
    }

    @Override
    public Object getResults() {
        return results;
    }

    private boolean preProcessData() {
        if (inputData == null) return false;
        if (!(inputData instanceof String)) return false;

        String data = (String) inputData;
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

//    /**
//     *  For testing
//     */
//    public static void main(String[] args) {
//        SampleSiyapathJob job = new SampleSiyapathJob();
//        job.setData("20,100");
//        job.process();
//        System.out.println(job.getResults());
//    }
}
