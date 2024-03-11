import java.util.*;

public class Main {
    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();

    public static void main(String[] args) {

        List<Thread> threads = new ArrayList<>();

        String[] texts = new String[1000];  //создаем массив

        for (int i = 0; i < texts.length; i++) {                //генерируем строки в массиве
            texts[i] = generateRoute("RLRFR", 100);
        }

        for (String str : texts) {
            Runnable logic = () -> {
                int count = 0;
                for (int i = 0; i < str.length(); i++) { //проходим по каждому символу
                    if (str.charAt(i) == 'R') {
                        count++;
                    } else {                            // если не r, то count заносим в мапу и обнуляем
                        if (count > 0) {
                            synchronized (sizeToFreq) {
                                sizeToFreq.put(count, sizeToFreq.getOrDefault(count, 0) + 1);
                            }
                            count = 0;
                        }
                    }
                }
                if (count > 0) {        // если r конце строки, то его нужно засчитать
                    synchronized (sizeToFreq) {
                        sizeToFreq.put(count, sizeToFreq.getOrDefault(count, 0) + 1);
                    }
                }
            };
            Thread thread = new Thread(logic);
            thread.start();
            threads.add(thread);
        }

        for (Thread thread : threads) {  // Ожидание завершения всех потоков
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for (Map.Entry<Integer, Integer> entry : sizeToFreq.entrySet()) { //выводим мапу
            System.out.println("- " + entry.getKey() + " (" + entry.getValue() + " раз)");
        }
    }

    public static String generateRoute(String letters, int length) { // генерирует строки
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}
