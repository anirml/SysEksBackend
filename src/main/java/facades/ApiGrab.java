
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/*
public class ApiGrab {


public Map<String, Double> getAllDataInParalelWithQueue() throws ProtocolException, IOException, InterruptedException, ExecutionException {
        Map<String, Double> results = new HashMap();
        Queue<Future<CoinPair>> queue = new ArrayBlockingQueue(COINPAIRS.length);

        ExecutorService workingJack = Executors.newCachedThreadPool();
        for (String coinPair : COINPAIRS) {
            Future<CoinPair> future = workingJack.submit(() -> {
                JsonObject jsonObject = new JsonParser().parse(getBinanceData(coinPair)).getAsJsonObject();
                CoinPair cp = new CoinPair(coinPair, jsonObject.get("price").getAsDouble());
                return cp;
            });
            queue.add(future);
        }
        while (!queue.isEmpty()) {
            Future<CoinPair> cp = queue.poll();
            if (cp.isDone()) {
                results.put(cp.get().getName(), cp.get().getPrice());
            } else {
                queue.add(cp);
            }
        }
        return results;
    }

    public String getBinanceData() throws MalformedURLException, ProtocolException, IOException {
        String fullUrl = "https://emilgth.dk/margamondo/api/flights/all";
        //String fullUrl = URL + coinPair;
        URL url = new URL(fullUrl);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Accept", "application/json;charset=UTF-8");
        try (Scanner scan = new Scanner(con.getInputStream())) {
            String jsonStr = "";
            while (scan.hasNext()) {
                jsonStr += scan.nextLine();
            }
            return jsonStr;
        }
    } 
}  */