/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import webscrabers.ApiScrape;
import webscrabers.ApiScrapeCallable;

/**
 *
 * @author Jeppe
 */
public class ApiScrapeFacade {
    
    
    
     public List<String> runParralel() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        List<String> urls = new ArrayList<>();
        urls.add("https://swapi.co/api/people/");
        urls.add("https://swapi.co/api/planets/");
        urls.add("https://swapi.co/api/starships/");
        urls.add("https://swapi.co/api/films/");
        urls.add("https://swapi.co/api/species/");
        List<Future<ApiScrape>> futures = new ArrayList<>();
        for (String url : urls) {
            Callable<ApiScrape> getApiCallable = new ApiScrapeCallable(url);
            Future<ApiScrape> future = executorService.submit(getApiCallable);
            futures.add(future);
        }
        List<String> apiData = new ArrayList<>();
        for (Future<ApiScrape> future : futures) {
            ApiScrape apiscrape = future.get();
            apiData.add(apiscrape.getResult());
        }
        executorService.shutdown();
        return apiData;
    }
}
