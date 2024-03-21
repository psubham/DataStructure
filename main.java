import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
//Currency conversion
public class Main {
    enum currency {
        INR("0"),
        USD("1"),
        EUR("2"),
        CFA("3"),
        SSD("4");

        private final String value;

        private currency(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public static void main(String[] args) {
        System.out.println("Try programiz.pro");
        List<List<String>> currAsk = new ArrayList<>();
        currAsk.add(new ArrayList<String>(){{add("INR"); add("USD");}});
        currAsk.add(new ArrayList<String>(){{add("INR"); add("EUR");}});
        currAsk.add(new ArrayList<String>(){{add("EUR"); add("USD");}});
        currAsk.add(new ArrayList<String>(){{add("USD"); add("EUR");}});
        currAsk.add(new ArrayList<String>(){{add("INR"); add("CFA");}});
        currAsk.add(new ArrayList<String>(){{add("CFA"); add("USD");}});
        currAsk.add(new ArrayList<String>(){{add("CFA"); add("SSD");}});
        
        String[][] curr = {
            {"0", "1", "0.90"},
            {"1", "0", "82.3"},
            {"1", "2", "1.25"},
            {"2", "3", "0.5"}
        };
        
        HashMap<String, HashMap<String, Double>> currMap = new HashMap<>();
        preProcessMap(curr, currMap);

        List<String> proceed = new ArrayList<>();
        processCurr(curr, currAsk, currMap, proceed);
    }
    
    private static void processCurr(String[][] curr, List<List<String>> currAsk,
    HashMap<String, HashMap<String, Double>> currMap, List<String> proceed) {
                 System.out.println(""+currMap);
         System.out.println(""+currAsk);

        for (List<String> ask : currAsk) {
            proceed = new ArrayList<String>();
            Double conversion = findCurr(currMap, ask, proceed);
            System.out.println(String.format("Conversion rate from %s to %s: %f", ask.get(0), ask.get(1), conversion));
        }
    }

    private static void preProcessMap(String[][]  curr, HashMap<String, HashMap<String, Double>> currMap) {
        for(int i = 0; i < curr.length; i++ ){
            String source = curr[i][0];
            String target = curr[i][1];
            double conversionRate = Double.parseDouble(curr[i][2]);

            HashMap<String, Double> sourceMap = currMap.getOrDefault(source, new HashMap<>());
            sourceMap.put(target, conversionRate);
            currMap.put(source, sourceMap);

            HashMap<String, Double> targetMap = currMap.getOrDefault(target, new HashMap<>());
            targetMap.put(source, 1.0 / conversionRate);
            currMap.put(target, targetMap);
        }
    }
    
    private static Double findCurr(HashMap<String, HashMap<String, Double>> currMap,
    List<String> currAsk, List<String> proceed){
        currency cur1 = (currency.valueOf( currAsk.get(0)));
        
      //  (currAsk.get(0)).getValue();
        currency cur2 = (currency.valueOf(currAsk.get(1)));
        
         System.out.println(cur1 +"+"+ cur2);
        return SearchCurr(cur1.getValue(), cur2.getValue(), 1.0, currMap, proceed);
    }
    
    private static Double SearchCurr(String sour, String dest, Double curr, 
    HashMap<String, HashMap<String, Double>> currMap, List<String> proceed){
        HashMap<String, Double> child = currMap.getOrDefault(sour, new HashMap<String, Double>());
        double res = -1.0;
        proceed.add(sour);
        if(child.containsKey(dest))
            return curr * child.get(dest);
        for (String nextCurrency : child.keySet()) {
            if (!proceed.contains(nextCurrency))
                res = Math.max(res, SearchCurr(nextCurrency, dest, curr * child.get(nextCurrency), currMap, proceed));
        }
        return res <= 0 ? res : res * curr;
    } 
}
