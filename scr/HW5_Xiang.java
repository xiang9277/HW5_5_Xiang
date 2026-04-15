/**
 * Project:Lab 5.5
 * Purpose Details: javaBruteForceFreqAnalysis Homework Assignment
 * Course: IST242
 * Author: zizhou xiang
 * Date Developed: 2026/4/14
 * Last Date Changed: 2026/3/1
 * Rev: 1.0
 */
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class HW5_Xiang {
    private static final double[] ARABIC_FREQUENCIES = {
            11.6, // ا
            4.8,  // ب
            3.7,  // ت
            1.1,  // ث
            2.8,  // ج
            2.6,  // ح
            1.1,  // خ
            3.5,  // د
            1.0,  // ذ
            4.7,  // ر
            0.9,  // ز
            6.5,  // س
            3.0,  // ش
            2.9,  // ص
            1.5,  // ض
            1.7,  // ط
            0.7,  // ظ
            3.9,  // ع
            1.0,  // غ
            3.0,  // ف
            2.7,  // ق
            3.6,  // ك
            5.3,  // ل
            3.1,  // م
            7.2,  // ن
            2.5,  // ه
            6.0,  // و
            6.7   // ي
    };

    private static final char[] ARABIC_CHARACTERS = {
            'ا', 'ب', 'ت', 'ث', 'ج', 'ح', 'خ', 'د', 'ذ', 'ر', 'ز',
            'س', 'ش', 'ص', 'ض', 'ط', 'ظ', 'ع', 'غ', 'ف', 'ق', 'ك',
            'ل', 'م', 'ن', 'ه', 'و', 'ي'
    };

    private static final int CAESAR_KEY = 5;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter English plaintext: ");
        String plaintext = scanner.nextLine();


        System.out.print("(Paste anything for API Key): ");
        String fakeApiKey = scanner.nextLine();


        String arabicText = simulateTranslateToArabic(plaintext);
        System.out.println("\n=== Translated Arabic (RTL) ===");
        System.out.println(arabicText);


        String encrypted = caesarEncrypt(arabicText, CAESAR_KEY);
        System.out.println("\n=== Caesar Cipher Encrypted ===");
        System.out.println(encrypted);


        System.out.println("\n=== Arabic Frequency Analysis ===");
        frequencyAnalysis(encrypted);

        scanner.close();
    }


    private static String simulateTranslateToArabic(String text) {
        return "مرحبا بالعالم"; // 固定阿拉伯语：Hello World
    }


    private static String caesarEncrypt(String text, int key) {
        StringBuilder sb = new StringBuilder();
        for (char c : text.toCharArray()) {
            int index = getCharIndex(c);
            if (index != -1) {
                int newIndex = (index + key) % ARABIC_CHARACTERS.length;
                sb.append(ARABIC_CHARACTERS[newIndex]);
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    private static int getCharIndex(char c) {
        for (int i = 0; i < ARABIC_CHARACTERS.length; i++) {
            if (ARABIC_CHARACTERS[i] == c) return i;
        }
        return -1;
    }


    private static void frequencyAnalysis(String text) {
        Map<Character, Integer> countMap = new HashMap<>();
        int total = 0;

        for (char c : text.toCharArray()) {
            if (getCharIndex(c) != -1) {
                countMap.put(c, countMap.getOrDefault(c, 0) + 1);
                total++;
            }
        }

        System.out.println("Total Arabic chars: " + total);
        System.out.println("Char | Count | Actual % | Standard %");
        System.out.println("----------------------------------------");

        for (int i = 0; i < ARABIC_CHARACTERS.length; i++) {
            char c = ARABIC_CHARACTERS[i];
            int cnt = countMap.getOrDefault(c, 0);
            double actual = total > 0 ? (cnt * 100.0) / total : 0;
            double standard = ARABIC_FREQUENCIES[i];

            System.out.printf("%-4c | %-5d | %-8.2f | %.2f%n",
                    c, cnt, actual, standard);
        }
    }
}


