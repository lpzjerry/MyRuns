package com.dartmouth.cs.myruns2;

public class WekaClassifier {

    public static double classify(double max) throws Exception {
        Object[] i = new Object[65];
        i[64] = max;
        double p = Double.NaN;
        p = WekaClassifier.N6faadf760(i);
        return p;
    }

    private static double N6faadf760(Object[] i) {
        double p = Double.NaN;
        if (i[64] == null) {
            p = 0;
        } else if ((Double) i[64] <= 0.161521) {
            p = 0;
        } else {
            p = WekaClassifier.N49e7c46d1(i);
        }
        return p;
    }

    private static double N49e7c46d1(Object[] i) {
        double p = Double.NaN;
        if (i[64] == null) {
            p = 1;
        } else if ((Double) i[64] <= 12.634823) {
            p = 1;
        } else {
            p = 2;
        }
        return p;
    }
}