import java.util.Scanner;

public class FinalSGPAMaster {
    private static final String R = "\u001B[0m", B = "\u001B[1m", C = "\u001B[96m",
            G = "\u001B[92m", Y = "\u001B[93m", Red = "\u001B[91m", P = "\u001B[95m";
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        clear(); header();

        System.out.print(C + "1.Fall  2.Winter  3.Interim\nChoose semester (1-3): " + R);
        int sem = validInt(1, 3);
        String semester = sem == 1 ? "Fall" : sem == 2 ? "Winter" : "Interim";
        System.out.println(G + "→ " + semester + " Semester Selected\n" + R);

        System.out.print(P + "Enter number of LT subjects (3 credits): " + R);
        int lt = validInt(0, 20);
        System.out.print(P + "Enter number of LTP subjects (4 credits): " + R);
        int ltp = validInt(0, 20);
        int totalSubjects = lt + ltp;
        int totalCredits = lt * 3 + ltp * 4;

        if (totalSubjects == 0) {
            System.out.println(Red + "No subjects entered. Exiting..." + R);
            return;
        }

        while (true) {
            menu();
            int choice = validInt(1, 7);
            if (choice == 7) {
                System.out.println(B + G + "\nGood luck! Go get that 9.5+ SGPA & CGPA! \n" + R);
                break;
            }
            switch (choice) {
                case 1 -> bunkMeter();
                case 2 -> dreamSGPA(totalCredits);
                case 3 -> whatIfSimulator();
                case 4 -> internalCalculator(lt > 0, ltp > 0);
                case 5 -> minimumToPass();
                case 6 -> sgpaPredictor(totalSubjects);
            }
        }
        sc.close();
    }

    // 1. Bunk-O-Meter
    private static void bunkMeter() {
    clear();
    System.out.println(B + C + "BUNK-O-METER (75% Minimum Required)\n" + R);

    System.out.print("Classes already held      : ");
    int held = validInt(1, 1000);

    System.out.print("Classes you attended      : ");
    int attended = validInt(0, held);

    System.out.print("Upcoming classes left     : ");
    int upcoming = validInt(0, 500);

    double currentPercent = attended * 100.0 / held;

    int maxBunks = upcoming - (int) Math.ceil(0.75 * (held + upcoming) - attended);

    if (maxBunks < 0) maxBunks = 0;

    System.out.println();
    System.out.printf(Y + "Current attendance  : %.2f%% (%d/%d)\n" + R, currentPercent, attended, held);
    System.out.printf(Y + "Total classes at end: %d\n" + R, held + upcoming);

    if (maxBunks >= upcoming) {
        System.out.println(G + "You can bunk ALL remaining " + upcoming + " classes and still have >75%!" + R);
    } else if (maxBunks > 0) {
        System.out.println(G + "You can safely bunk " + B + maxBunks + R + G + " more classes!" + R);
        System.out.println(G + "You must attend at least " + (upcoming - maxBunks) + " out of remaining " + upcoming + R);
    } else {
        int needToAttend = attended + upcoming;
        int required = (int) Math.ceil(0.75 * (held + upcoming));
        int shortage = required - needToAttend;
        System.out.println(Red + "You are already SHORT by " + B + shortage + R + Red + " classes!" + R);
        System.out.println(Red + "Even attending ALL remaining classes → only " + 
            String.format("%.2f", (attended + upcoming) * 100.0 / (held + upcoming)) + "%" + R);
    }

    pause();
}

    // 2. Dream SGPA Target
    private static void dreamSGPA(int totalCredits) {
    clear();
    System.out.println(B + C + "DREAM SGPA TARGET\n" + R);
    System.out.println(Y + "Internal = 40% (out of 40) | External = 60% (Mid 30% + Term End 30%)\n" + R);

    System.out.print("Your dream SGPA: ");
    double target = validDouble(0, 10);
    if (target > 10) { System.out.println(Red + "Max 10.0!"); pause(); return; }

    System.out.print("Expected average Internal (out of 40): ");
    double internal = validDouble(0, 40);

    double targetPercent = target * 10.0;
    double internalContrib = internal * 1.0;  
    double neededExternal = targetPercent - internalContrib;

    System.out.println();
    System.out.printf(Y + "Internal contributes : %.1f%%\n" + R, internalContrib);

    if (neededExternal <= 0) {
        System.out.println(G + "Already achieved! You can chill in externals!" + R);
    } else if (neededExternal > 60) {
        System.out.println(Red + "Impossible! Max external gives only 60%." + R);
        System.out.printf(Red + "Need at least %.1f/40 in internal\n" + R, targetPercent - 60);
    } else {
        double neededMarks = (neededExternal / 60.0) * 100.0;
        System.out.printf(G + "Need average %.1f/100 in Mid + Term End\n" + R, neededMarks);
        if (neededMarks <= 70) System.out.println(G + "Easy! Just attend!" + R);
        else if (neededMarks <= 90) System.out.println(Y + "Possible with effort!" + R);
        else System.out.println(Red + "Very tough — grind hard!" + R);
    }
    pause();
}

    // 3. What-If Simulator
    private static void whatIfSimulator() {
    clear();
    System.out.println(B + C + "WHAT-IF SGPA SIMULATOR\n" + R);
    System.out.print("Internal marks (out of 40): ");
    double internal = validDouble(0, 40);
    System.out.print("Average Mid Term marks (/50): ");
    double mid = validDouble(0, 50);
    System.out.print("Average Term End marks (/100): ");
    double termEnd = validDouble(0, 100);

    double totalPercent = internal * 1.0 + (mid / 50.0) * 30.0 + termEnd * 0.30;        
    int gp = getStandardGP(totalPercent);
    System.out.println();
    System.out.printf(B + P + "Predicted SGPA → %.2f (%s)\n" + R, (double)gp, getLetter(gp));
    pause();
}

    // 4. Internal Marks Calculator
    private static void internalCalculator(boolean hasLT, boolean hasLTP) {
    clear();
    System.out.println(B + C + "INTERNAL MARKS CALCULATOR (out of 40)\n" + R);

    if (hasLT) {
        System.out.println(Y + "LT Subject (3 credits):" + R);
        double tut = getComp("Tutorial", 10);
        double quiz = getComp("Quiz", 10);
        double assign = getComp("Assignment", 10);
        double group = getComp("Group Activity", 5);
        double att = getComp("Attendance", 5);

        double total = tut + quiz + assign + group + att;
        System.out.printf(G + "LT Internal (VTOP): %.1f/40\n" + R, total);
        System.out.printf(G + "Contributes       : %.2f%% to final grade (40%% weight)\n\n" + R, total * 1.0);
    }

    if (hasLTP) {
        System.out.println(Y + "LTP Subject (4 credits):" + R);
        double ct = getComp("Challenging Task", 10);
        double ca = getComp("Continuous Assessment", 10);
        double tut = getComp("Tutorial", 5);
        double quiz = getComp("Quiz", 5);
        double group = getComp("Group Activity", 5);
        double att = getComp("Attendance", 5);

        double total = ct + ca + tut + quiz + group + att;
        System.out.printf(G + "LTP Internal (VTOP): %.1f/40\n" + R, total);
        System.out.printf(G + "Contributes        : %.2f%% to final grade (40%% weight)\n" + R, total * 1.0);
    }
    pause();
}

    // 5. Minimum to Pass
    private static void minimumToPass() {
    clear();
    System.out.println(B + C + "MINIMUM MARKS TO PASS A SUBJECT\n" + R);
    System.out.println(G + "YOUR COLLEGE PASSING RULES (Must satisfy ALL):" + R);
    System.out.println();
    System.out.println("   1. Term End Exam           ≥ 40/100   ← COMPULSORY");
    System.out.println("   2. Mid Term + Term End     ≥ 60        ← COMPULSORY");
    System.out.println("   3. Internal marks          → Only for grading");
    System.out.println("                                 (No minimum required to pass)");
    System.out.println();
    System.out.println(Y + "SAFE & RECOMMENDED COMBINATIONS (100% PASS):" + R);
    System.out.println(G + "   → Term End = 40+   &   Mid Term ≥ 20/50   → Total 60+ → SAFE!" + R);
    System.out.println(G + "   → Term End = 45+   &   Mid Term ≥ 15/50   → Very safe" + R);
    System.out.println(G + "   → Term End = 50+   &   Mid Term ≥ 10/50   → Relaxed" + R);
    System.out.println();
    System.out.println(Y + "RISKY (Still pass, but dangerous):" + R);
    System.out.println(Red + "   → Term End = 40   &   Mid Term = 20 → Total 60 → Just passes" + R);
    System.out.println(Red + "   → Term End = 39   &   Mid Term = 30 → FAIL (Term End < 40)" + R);
    System.out.println(Red + "   → Term End = 50   &   Mid Term = 9  → FAIL (Total < 60)" + R);
    System.out.println();
    System.out.println(B + G + "Pro Tip: Always aim for Term End ≥ 45 → Almost impossible to fail!" + R);
    pause();
}
    
    // 6. SGPA + CGPA PREDICTOR 
    private static void sgpaPredictor(int n) {
        clear();
        System.out.println(B + C + "SGPA + CGPA PREDICTOR (This Semester)\n" + R);
        System.out.println(Y + "Grading: S(95+) A(85+) B(75+) C(70+) D(60+) E(50+) F(<50)\n" + R);

        int totalGP = 0;
        int aCount = 0;

        for (int i = 1; i <= n; i++) {
            System.out.println(Y + "Subject " + i + ":" + R);

            System.out.print("   Internal RAW marks (out of 40): ");
            double internalRaw = validDouble(0, 40);
            System.out.print("   Mid Term marks (/50): ");
            double midRaw = validDouble(0, 50);
            System.out.print("   Term End marks (/100): ");
            double termEnd = validDouble(0, 100);
            double midPercent = (midRaw / 50.0) * 30.0;
            double termEndPercent = termEnd * 0.30;

            double totalPercent = internalRaw + midPercent + termEndPercent;

            String customGrade = getCustomGrade(totalPercent);
            int gp = getStandardGP(totalPercent);
            totalGP += gp;

            if (customGrade.equals("A")) aCount++;

            System.out.printf("   → Final %%: %.2f%% → Grade: " + colorGrade(customGrade) + "%s" + R + " | GP: %d\n\n", totalPercent, customGrade, gp);
        }

        double sgpa = totalGP * 1.0 / n;
        String prediction = getCGPAPrediction(aCount, n);

        System.out.println(B + G + "FINAL RESULT FOR THIS SEMESTER" + R);
        System.out.println("════════════════════════════════════");
        System.out.printf(B + P + "   Your SGPA: %.2f\n" + R, sgpa);
        System.out.println(prediction);
        System.out.println(G + "   Aim high! Every A brings you closer to 9.5+ CGPA!" + R);
        pause();
    }

    private static String getCustomGrade(double m) {
        if (m >= 95) return "S";
        if (m >= 85) return "A";
        if (m >= 75) return "B";   
        if (m >= 70) return "C";
        if (m >= 60) return "D";
        if (m >= 50) return "E";
        return "F";
    }

    // Standard 10-point GP 
    private static int getStandardGP(double m) {
        if (m >= 90) return 10;
        if (m >= 80) return 9;
        if (m >= 70) return 8;
        if (m >= 60) return 7;
        if (m >= 50) return 6;
        if (m >= 40) return 5;
        return 0;
    }
    
    private static String colorGrade(String g) {
        return switch (g) {
            case "S" -> B + G;        // Bright Green
            case "A" -> G;            // Green
            case "B" -> B + Y;        // Bold Yellow
            case "C" -> Y;            // Yellow
            case "D" -> P;            // Purple
            case "E", "F" -> Red;     // Red
            default -> R;
        };
    }

    // CGPA Prediction based on number of A grades 
    private static String getCGPAPrediction(int aCount, int n) {
        if (aCount == n)     return G + "ALL A → Expected CGPA: 9.7+ → You're a GENIUS!" + R;
        if (aCount >= n-1)   return G + "Almost perfect → CGPA 9.3+" + R;
        if (aCount >= n-2)   return G + "Very strong → CGPA 8.8+" + R;
        if (aCount >= n-3)   return Y + "Good → CGPA 8.3+" + R;
        if (aCount >= n-4)   return Y + "Decent → CGPA 7.8+" + R;
        if (aCount >= n-5)   return P + "Keep pushing → CGPA 7.3+" + R;
        return Red + B + "Work HARDER! More A's needed next semester!" + R;
    }

    // Standard letter grades for What-If
    private static String getLetter(int gp) {
        return switch (gp) {
            case 10 -> "S";   // 90–100 -> S
            case  9 -> "A";   // 85–89  -> A
            case  8 -> "B";   // 75–84  -> B
            case  7 -> "C";   // 70–74  -> C
            case  6 -> "D";   // 60–69  -> D
            case  5 -> "E";   // 50–59  -> E
            default -> "F";   // Below 50 -> F
        };
    }

    private static double getComp(String name, int max) {
        System.out.print("  " + name + " (max " + max + "): ");
        return validDouble(0, max);
    }

    private static int validInt(int min, int max) {
        while (true) {
            try {
                int v = Integer.parseInt(sc.nextLine().trim());
                if (v >= min && v <= max) return v;
                System.out.print(Red + "Enter value between " + min + "-" + max + ": " + R);
            } catch (Exception e) {
                System.out.print(Red + "Invalid input! Try again: " + R);
            }
        }
    }

    private static double validDouble(double min, double max) {
        while (true) {
            try {
                double v = Double.parseDouble(sc.nextLine().trim());
                if (v >= min && v <= max) return v;
                System.out.print(Red + "Enter value between " + min + "-" + max + ": " + R);
            } catch (Exception e) {
                System.out.print(Red + "Invalid input! Try again: " + R);
            }
        }
    }

    private static void header() {
        System.out.println(B + C +
            "╔══════════════════════════════════════════╗\n" +
            "║     VIT Bhopal Students Helper Program   ║\n" +
            "║                by 24BSA10042             ║\n" +
            "╚══════════════════════════════════════════╝\n" + R);
    }

    private static void menu() {
        clear(); header();
        System.out.println(P + "1. Bunk-O-Meter     4. Internal Calculator");
        System.out.println("2. Dream SGPA       5. Minimum to Pass");
        System.out.println("3. What-If          6. SGPA + CGPA Predictor");
        System.out.println("                    7. Exit" + R);
        System.out.print(Y + "\nChoose (1-7): " + R);
    }

    private static void clear() { System.out.print("\033[H\033[2J"); System.out.flush(); }
    private static void pause() { System.out.println(Y + "\nPress Enter to continue..." + R); sc.nextLine(); }
}