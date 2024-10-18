import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

public class Main {
    private static final int maxWeight = 20;
    private static Map<int[], Integer> passedVariations;

    public static int[] decimalToBinary(int num, int length)
    {
        int[] binary = new int[length];
        int id = 0;

        while (num > 0) {
            binary[id++] = num % 2;
            num = num / 2;
        }
        return binary;
    }

    public static ArrayList<Integer> feasibilityItems(int[] activeItems, ArrayList<Item> items){
        int value=0;
        int size=0;
        for (int i = 0; i<activeItems.length;i++){
            if (activeItems[i]==1){
                value+=items.get(i).value;
                size+=items.get(i).size;
            }
        }
        return new ArrayList<>(List.of(value,size));
    }

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();


        int[] values = {23, 25, 46, 43, 9, 35, 29, 35, 7, 46, 7, 48, 14, 33, 23, 10, 18, 12, 12, 6, 33, 7, 47, 28, 18, 39, 22, 43, 22, 43};
        int[] size = {2, 8, 8, 7, 7, 9, 4, 2, 8, 2, 7, 4, 5, 3, 7, 5, 6, 6, 4, 8, 4, 5, 2, 3, 3, 4, 6, 4, 7, 9};
        ArrayList<Item> items = new ArrayList<>();
        for (int i=0; i<values.length;i++){
            items.add(new Item(values[i],size[i], i));
        }
        System.out.println((int)Math.pow(2, items.size()));
        System.out.println("Brute Force:");
        passedVariations = new ConcurrentHashMap<>();
        IntStream.range(0, (int)Math.pow(2, items.size()))
                .parallel()
                .forEach(i -> {
                    int[] code = decimalToBinary(i,items.size());
                    ArrayList<Integer> data = feasibilityItems(code,items);
                    if (data.get(1)<=maxWeight){
                        passedVariations.put(code, data.get(0));
                    }
                });
        List<Map.Entry<int[], Integer>> list = new ArrayList<>(passedVariations.entrySet());

        list.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));


        LinkedHashMap<int[], Integer> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<int[], Integer> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        Optional<Integer> maxValue = sortedMap.values().stream().max(Integer::compareTo);
        if (maxValue.isPresent()) {
            for (Map.Entry<int[], Integer> entry : sortedMap.entrySet()) {
                if (entry.getValue().equals(maxValue.get())) {
                    for (Integer i : entry.getKey()) {
                        System.out.print(i + " ");
                    }
                    System.out.println("--- " + entry.getValue());
                }else{
                    break;
                }
            }
        }
        System.out.println();

        long endTime = System.currentTimeMillis();


        System.out.println("Greedy: ");
        List<Item> sortedItemsByDensity = items.stream()
                .sorted(Comparator.comparingDouble(Item::getDensity).reversed())
                .toList();
        int finalValueItemInBag = 0;
        int finalSizeItemBag = 0;
        int[] codeForItemInBag = new int[items.size()];

        for (Item item : sortedItemsByDensity){
            if (finalSizeItemBag+item.size<=maxWeight){
                finalValueItemInBag+=item.value;
                finalSizeItemBag+=item.size;
                codeForItemInBag[item.order]=1;
                if (finalSizeItemBag == maxWeight){
                    break;
                }
            }
        }
        for (Integer i: codeForItemInBag){
            System.out.print(i+" ");
        }
        System.out.println("--- "+finalValueItemInBag);


        System.out.println(endTime-startTime + "ms");
    }
}
