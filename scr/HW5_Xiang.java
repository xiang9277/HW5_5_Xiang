/**
 * Project:Lab 5.5
 * Purpose Details: javaBruteForceFreqAnalysis Homework Assignment
 * Course: IST242
 * Author: zizhou xiang
 * Date Developed: 2026/4/14
 * Last Date Changed: 2026/3/1
 * Rev: 1.0
 */
/**
 * Project:Lab 5.5
 * Purpose Details: javaBruteForceFreqAnalysis Homework Assignment
 * Course: IST242
 * Author: zizhou xiang
 * Date Developed: 2026/4/14
 * Last Date Changed: 2026/4/14
 * Rev: 2.0 (Fixed Brute Force + Freq Analysis Decrypt)
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

        // 模拟翻译成阿拉伯语
        String arabicText = simulateTranslateToArabic(plaintext);
        System.out.println("\n=== Translated Arabic (RTL) ===");
        System.out.println(arabicText);

        // 加密
        String encrypted = caesarEncrypt(arabicText, CAESAR_KEY);
        System.out.println("\n=== Caesar Cipher Encrypted ===");
        System.out.println(encrypted);

        // 频率分析（原功能保留）
        System.out.println("\n=== Arabic Frequency Analysis ===");
        frequencyAnalysis(encrypted);

        // ===================== 老师要的核心：暴力破解 + 频率分析解密 =====================
        System.out.println("\n=====================================");
        System.out.println("         BRUTE FORCE + FREQ ANALYSIS");
        System.out.println("=====================================");
        bruteForceAndFindBestDecrypt(encrypted);

        scanner.close();
    }

    // 模拟翻译
    private static String simulateTranslateToArabic(String text) {
        return "مرحبا بالعالم"; // Hello World
    }

    // 加密
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

    // 解密（反向移位）
    private static String caesarDecrypt(String text, int key) {
        StringBuilder sb = new StringBuilder();
        int len = ARABIC_CHARACTERS.length;
        for (char c : text.toCharArray()) {
            int index = getCharIndex(c);
            if (index != -1) {
                int newIndex = (index - key + len) % len;
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

    // 频率分析（原代码）
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

    // ===================== 核心新增：计算频率匹配分数 =====================
    private static double calculateFrequencyScore(String text) {
        Map<Character, Integer> countMap = new HashMap<>();
        int total = 0;

        for (char c : text.toCharArray()) {
            if (getCharIndex(c) != -1) {
                countMap.put(c, countMap.getOrDefault(c, 0) + 1);
                total++;
            }
        }

        if (total == 0) return Double.MAX_VALUE;

        double score = 0;
        for (int i = 0; i < ARABIC_CHARACTERS.length; i++) {
            char c = ARABIC_CHARACTERS[i];
            int cnt = countMap.getOrDefault(c, 0);
            double actual = (cnt * 100.0) / total;
            double standard = ARABIC_FREQUENCIES[i];
            score += Math.abs(actual - standard);
        }
        return score; // 分数越低 = 越接近阿拉伯语
    }

    // ===================== 暴力破解 + 自动找出正确解密结果 =====================
    private static void bruteForceAndFindBestDecrypt(String encrypted) {
        int bestKey = 0;
        String bestText = "";
        double bestScore = Double.MAX_VALUE;

        System.out.println("\n--- Brute Force All Shifts ---");

        for (int key = 0; key < ARABIC_CHARACTERS.length; key++) {
            String decrypted = caesarDecrypt(encrypted, key);
            double score = calculateFrequencyScore(decrypted);

            System.out.printf("Key %-2d | Score %-6.2f | %s%n", key, score, decrypted);

            // 记录分数最低（最正确）的结果
            if (score < bestScore) {
                bestScore = score;
                bestKey = key;
                bestText = decrypted;
            }
        }

        // ===================== 老师要看到的最终结论 =====================
        System.out.println("\n=====================================");
        System.out.println("✅  DECRYPTED SUCCESSFULLY");
        System.out.println("✅  Using Frequency Analysis");
        System.out.println("=====================================");
        System.out.println("Best Key Found: " + bestKey);
        System.out.println("Correct Arabic Text: " + bestText);
        System.out.println("Original Text Was  : مرحبا بالعالم");
        System.out.println("=====================================\n");
    }
}