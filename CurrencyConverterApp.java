import java.io.*;
import java.net.*;
import java.util.*;
import java.text.DecimalFormat;
import org.json.*;

class CurrencyConverter{
    private static final String API_KEY="YOUR_API_KEY_HERE";
    private static final String BASE_URL="https://v6.exchangerate-api.com/v6/"+API_KEY+"/latest/";
    private Scanner scanner;
    private DecimalFormat df;
    private Map<String,String> currencyNames;
    
    public CurrencyConverter(){
        this.scanner=new Scanner(System.in);
        this.df=new DecimalFormat("#,##0.00");
        initializeCurrencyNames();
    }
    
    private void initializeCurrencyNames(){
        currencyNames=new HashMap<>();
        currencyNames.put("USD","US Dollar");
        currencyNames.put("EUR","Euro");
        currencyNames.put("GBP","British Pound");
        currencyNames.put("JPY","Japanese Yen");
        currencyNames.put("AUD","Australian Dollar");
        currencyNames.put("CAD","Canadian Dollar");
        currencyNames.put("CHF","Swiss Franc");
        currencyNames.put("CNY","Chinese Yuan");
        currencyNames.put("SEK","Swedish Krona");
        currencyNames.put("NZD","New Zealand Dollar");
        currencyNames.put("INR","Indian Rupee");
        currencyNames.put("BRL","Brazilian Real");
        currencyNames.put("RUB","Russian Ruble");
        currencyNames.put("KRW","South Korean Won");
        currencyNames.put("SGD","Singapore Dollar");
        currencyNames.put("HKD","Hong Kong Dollar");
        currencyNames.put("NOK","Norwegian Krone");
        currencyNames.put("MXN","Mexican Peso");
        currencyNames.put("ZAR","South African Rand");
        currencyNames.put("TRY","Turkish Lira");
    }
    
    public void displayWelcome(){
        System.out.println("=".repeat(50));
        System.out.println("        REAL-TIME CURRENCY CONVERTER");
        System.out.println("=".repeat(50));
    }
    
    public void displayCurrencies(){
        System.out.println("\nAvailable Currencies:");
        System.out.println("-".repeat(40));
        int count=1;
        for(Map.Entry<String,String> entry:currencyNames.entrySet()){
            System.out.printf("%2d. %s - %s\n",count++,entry.getKey(),entry.getValue());
        }
        System.out.println("-".repeat(40));
    }
    
    public String getCurrencySelection(String type){
        while(true){
            System.out.print("Enter "+type+" currency code (e.g., USD): ");
            String currency=scanner.nextLine().toUpperCase().trim();
            if(currencyNames.containsKey(currency)){
                return currency;
            }
            System.out.println("Invalid currency code! Please try again.");
        }
    }
    
    public double getAmount(){
        while(true){
            try{
                System.out.print("Enter amount to convert: ");
                double amount=Double.parseDouble(scanner.nextLine());
                if(amount>0){
                    return amount;
                }
                System.out.println("Amount must be positive!");
            }catch(NumberFormatException e){
                System.out.println("Invalid amount! Please enter a valid number.");
            }
        }
    }
    
    public double fetchExchangeRate(String fromCurrency,String toCurrency) throws Exception{
        if(fromCurrency.equals(toCurrency)){
            return 1.0;
        }
        String urlString=BASE_URL+fromCurrency;
        URL url=new URL(urlString);
        HttpURLConnection connection=(HttpURLConnection)url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);
        
        int responseCode=connection.getResponseCode();
        if(responseCode!=200){
            throw new Exception("API request failed with code: "+responseCode);
        }
        
        BufferedReader reader=new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response=new StringBuilder();
        String line;
        while((line=reader.readLine())!=null){
            response.append(line);
        }
        reader.close();
        
        JSONObject jsonResponse=new JSONObject(response.toString());
        if(!jsonResponse.getString("result").equals("success")){
            throw new Exception("API returned error: "+jsonResponse.getString("error-type"));
        }
        
        JSONObject rates=jsonResponse.getJSONObject("conversion_rates");
        if(!rates.has(toCurrency)){
            throw new Exception("Target currency not found in API response");
        }
        
        return rates.getDouble(toCurrency);
    }
    
    public double fetchExchangeRateOffline(String fromCurrency,String toCurrency){
        Map<String,Double> rates=new HashMap<>();
        rates.put("USD",1.0);
        rates.put("EUR",0.85);
        rates.put("GBP",0.73);
        rates.put("JPY",110.0);
        rates.put("AUD",1.35);
        rates.put("CAD",1.25);
        rates.put("CHF",0.92);
        rates.put("CNY",6.45);
        rates.put("INR",74.5);
        rates.put("BRL",5.2);
        rates.put("RUB",73.5);
        rates.put("KRW",1180.0);
        rates.put("SGD",1.35);
        rates.put("HKD",7.8);
        rates.put("NOK",8.5);
        rates.put("SEK",8.6);
        rates.put("MXN",20.1);
        rates.put("ZAR",14.8);
        rates.put("NZD",1.42);
        rates.put("TRY",8.8);
        
        if(fromCurrency.equals(toCurrency)){
            return 1.0;
        }
        
        double fromRate=rates.getOrDefault(fromCurrency,1.0);
        double toRate=rates.getOrDefault(toCurrency,1.0);
        
        return toRate/fromRate;
    }
    
    public void displayResult(String fromCurrency,String toCurrency,double amount,double exchangeRate,double convertedAmount,boolean isRealTime){
        System.out.println("\n"+"=".repeat(50));
        System.out.println("              CONVERSION RESULT");
        System.out.println("=".repeat(50));
        System.out.println("From: "+currencyNames.get(fromCurrency)+" ("+fromCurrency+")");
        System.out.println("To: "+currencyNames.get(toCurrency)+" ("+toCurrency+")");
        System.out.println("Amount: "+df.format(amount)+" "+fromCurrency);
        System.out.println("Exchange Rate: 1 "+fromCurrency+" = "+df.format(exchangeRate)+" "+toCurrency);
        System.out.println("Converted Amount: "+df.format(convertedAmount)+" "+toCurrency);
        System.out.println("Rate Type: "+(isRealTime?"Real-time":"Offline"));
        System.out.println("=".repeat(50));
    }
    
    public boolean askContinue(){
        System.out.print("\nDo you want to convert another currency? (Y/N): ");
        String response=scanner.nextLine().trim().toUpperCase();
        return response.equals("Y")||response.equals("YES");
    }
    
    public void start(){
        displayWelcome();
        
        do{
            try{
                displayCurrencies();
                String fromCurrency=getCurrencySelection("source");
                String toCurrency=getCurrencySelection("target");
                double amount=getAmount();
                
                System.out.println("\nFetching real-time exchange rates...");
                
                double exchangeRate;
                boolean isRealTime=true;
                
                try{
                    exchangeRate=fetchExchangeRate(fromCurrency,toCurrency);
                }catch(Exception e){
                    System.out.println("Warning: Unable to fetch real-time rates. Using offline rates.");
                    System.out.println("Error: "+e.getMessage());
                    exchangeRate=fetchExchangeRateOffline(fromCurrency,toCurrency);
                    isRealTime=false;
                }
                
                double convertedAmount=amount*exchangeRate;
                displayResult(fromCurrency,toCurrency,amount,exchangeRate,convertedAmount,isRealTime);
                
            }catch(Exception e){
                System.out.println("An error occurred: "+e.getMessage());
                System.out.println("Please try again.");
            }
        }while(askContinue());
        
        System.out.println("\n"+"=".repeat(50));
        System.out.println("    Thank you for using Currency Converter!");
        System.out.println("=".repeat(50));
        scanner.close();
    }
}
public class CurrencyConverterApp{
    public static void main(String[] args){
        CurrencyConverter converter=new CurrencyConverter();
        converter.start();
    }
}
