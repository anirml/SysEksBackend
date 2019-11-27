package facades;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dto.FlightDTO;
import facades.FlightFacade;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;


public class ApiGrabFacade {

    private static ApiGrabFacade instance;
    private static EntityManagerFactory emf;
    
    private static List<String> urls;
    
     private ApiGrabFacade(){}
     
    /**
     * 
     * @param _emf
     * @return an instance of this facade class.
     */
    public static ApiGrabFacade getApiScrapeFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            getURLS();
            instance = new ApiGrabFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }   
    
    
    private static List<String> getURLS(){
        ArrayList urls = new ArrayList<>();
        urls.add("https://emilgth.dk/margamondo/api/flights/all"); 
        urls.add("https://jjugroup.ga/SysEksBackend/api/flight/all"); 
        return urls;
    }

public List<FlightDTO> getAllApiDataSequentially() throws ProtocolException, IOException, ParseException{
    Long tempId = new Long(1);
    List<FlightDTO> data = new ArrayList<>();
    for(String apiUrl : getURLS()){
        JsonArray ja = new JsonParser().parse(getApiData(apiUrl)).getAsJsonArray();
        for(JsonElement je : ja){
            
            String link;
            if(je.getAsJsonObject().get("link")==null){
                link="www.sas.com";
            }
            else{
                link=je.getAsJsonObject().get("link").getAsString();
            }
            FlightDTO fdto = new FlightDTO(tempId,
                    dateFormatter(je.getAsJsonObject().get("departureTime").getAsString()),
                    je.getAsJsonObject().get("flightDuration").getAsLong(),
                    je.getAsJsonObject().get("departureAirportName").getAsString(),
                    je.getAsJsonObject().get("departureAirportCode").getAsString(),
                    je.getAsJsonObject().get("arrivalAirportName").getAsString(),
                    je.getAsJsonObject().get("arrivalAirportCode").getAsString(),
                    je.getAsJsonObject().get("airline").getAsString(),
                    je.getAsJsonObject().get("price").getAsDouble(),
                    link                           
            );
            tempId++;

           data.add(fdto);
          
        }
    }
    
    System.out.println(data);
    return data;
}    
    
/*    
public List<String> getAllDataInParalelWithQueue() throws ProtocolException, IOException, InterruptedException, ExecutionException {
        List<String> data = new ArrayList<>();
        Queue<Future<String>> queue = new ArrayBlockingQueue(urls.size());

        ExecutorService workingJack = Executors.newCachedThreadPool();
        for (String coinPair : COINPAIRS) {
            Future<CoinPair> future = workingJack.submit(() -> {
                JsonObject jsonObject = new JsonParser().parse(getApiData(apiUrl)).getAsJsonObject();
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
*/
    public String getApiData(String apiUrl) throws MalformedURLException, ProtocolException, IOException {
        URL url = new URL(apiUrl);
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
    
     public Date dateFormatter(String dateStr) throws ParseException{
    SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy HH:mm:ss a");
    Date date = sdf.parse(dateStr);

    return date;
    }
}  