package nio;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) {
        new Server().start();
        Client client = new Client();

        int threadCount = 1;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        for (int i = 0; i < 1; i++) {
            executorService.submit(client);
        }
    }

}
