

class ShamirSSS {
    
    // Function to compute Lagrange interpolation at x = 0 (the secret)
    public static int lagrangeInterpolation(int[][] shares, int k) {
        int secret = 0;
        for (int i = 0; i < k; i++) {
            int xi = shares[i][0]; // x value
            int yi = shares[i][1]; // y value

            double term = yi;
            for (int j = 0; j < k; j++) {
                if (i != j) {
                    int xj = shares[j][0];
                    term *= (0 - xj) * 1.0 / (xi - xj);
                }
            }
            secret += term;
        }
        return (int)Math.round(secret);
    }

    public static void main(String[] args) {
        // Example shares (x,y) in base 10 (can extend to hex parsing)
        int[][] shares = {
            {1, 123}, 
            {2, 281}, 
            {3, 535}, 
            {4, 1005}, 
            {5, 1705}  // Suppose one of these is wrong
        };

        int k = 3;  // Minimum shares required
        int n = shares.length;

        // Try all combinations of k shares out of n to find majority secret
        java.util.HashMap<Integer, Integer> secretCount = new java.util.HashMap<>();

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                for (int l = j + 1; l < n; l++) {
                    int[][] subset = { shares[i], shares[j], shares[l] };
                    int secret = lagrangeInterpolation(subset, k);

                    secretCount.put(secret, secretCount.getOrDefault(secret, 0) + 1);
                }
            }
        }

        // Find the correct secret (appears most often)
        int correctSecret = -1, maxCount = 0;
        for (int s : secretCount.keySet()) {
            if (secretCount.get(s) > maxCount) {
                maxCount = secretCount.get(s);
                correctSecret = s;
            }
        }

        System.out.println("Recovered Secret: " + correctSecret);

        // Identify wrong share
        for (int i = 0; i < n; i++) {
            int[][] testShares = new int[k][2];
            int idx = 0;
            for (int j = 0; j < n; j++) {
                if (i != j && idx < k) {
                    testShares[idx++] = shares[j];
                }
            }
            int secret = lagrangeInterpolation(testShares, k);
            if (secret != correctSecret) {
                System.out.println("Wrong Share: (" + shares[i][0] + "," + shares[i][1] + ")");
                break;
            }
        }
    }
}