/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webscrabers;
import java.util.concurrent.Callable;
/**
 *
 * @author Jeppe
 */
public class ApiScrapeCallable  implements Callable<ApiScrape> {  
    
    private ApiScrape apiScrape;

    public ApiScrapeCallable(String apiUrl) {
        apiScrape = new ApiScrape(apiUrl);
    }

    @Override
    public ApiScrape call() throws Exception {
        apiScrape.getApiData();
        return apiScrape;
    }
}
