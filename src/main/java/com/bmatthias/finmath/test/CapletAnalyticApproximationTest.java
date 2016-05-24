package com.bmatthias.finmath.test;

import cern.jet.random.engine.MersenneTwister64;
import net.finmath.functions.AnalyticFormulas;
import net.finmath.functions.NormalDistribution;
import net.finmath.montecarlo.interestrate.products.CapletAnalyticApproximation;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.DoubleUnaryOperator;

public class CapletAnalyticApproximationTest {

    private static MersenneTwister64 mersenneTwister64 = new MersenneTwister64(2037);

    private static double randomDrawSum(double correlation, double[] means, double[] covariances) {
        double stdNormalDist1 = NormalDistribution.inverseCumulativeDistribution(mersenneTwister64.nextDouble());
        double stdNormalDist2 = NormalDistribution.inverseCumulativeDistribution(mersenneTwister64.nextDouble());

        double correlatedNormalDist = correlation * stdNormalDist1 + Math.sqrt(1 - correlation * correlation) * stdNormalDist2;

        double normalDist1 = means[0] - 0.5 * covariances[0] + Math.sqrt(covariances[0]) * stdNormalDist1;
        double normalDist2 = means[1] - 0.5 * covariances[1] + Math.sqrt(covariances[1]) * correlatedNormalDist;

        return Math.exp(normalDist1) + Math.exp(normalDist2);
    }

    public static void main(String[] args) throws Exception {
        try (FileWriter writerAll = new FileWriter("approximations_all.csv", true)) {
            String tableHeaderAll = "i\tj\tk\tmean1\tmean2\tvariance1\tvariance2\tcorrelation\t" +
                    "probabilitySum\tprobabilitySimApprox\tprobabilityBlackApprox\t" +
                    "probabilitySimLevy\tprobabilitySimJu\tprobabilityBlackLevy\tprobabilityBlackJu\t" +
                    "normalizedErrorSimApprox\tnormalizedErrorBlackApprox\t" +
                    "normalizedErrorSimLevy\tnormalizedErrorSimJu\t" +
                    "normalizedErrorBlackLevy\tnormalizedErrorBlackJu";
            System.out.println(tableHeaderAll);
            writerAll.write(tableHeaderAll);

            double numberOfSimulations = 1E8;
            for(int i = 0; i < 10; i++) {
                try (FileWriter writerFixedMean = new FileWriter("approximations_fixed_mean_" + i + ".csv", true)) {
                    writerFixedMean.write(tableHeaderAll);
                    for(int j = 0; j < 10; j++) {
                        try (FileWriter writerFixedVola = new FileWriter("approximations_fixed_vola_" + j + ".csv", true)) {
                            if(i == 0) writerFixedVola.write(tableHeaderAll);
                            for(int k = 0; k < 10; k++) {
                                try (FileWriter writerFixedCorrelation = new FileWriter("approximations_fixed_correlation_" + k + ".csv", true)) {
                                    if(j == 0 && i == 0) writerFixedCorrelation.write(tableHeaderAll);
                                    double correlation = 0.1 * k;
                                    double[] means = new double[] { -1.0 - 1.2 * i, -2.0 - 0.6 * i};
                                    double[] covariances = new double[] { 0.1 + 0.5 * j, 0.6 * j, Math.sqrt(0.1 + 0.5 * j) * Math.sqrt(0.6 * j) * correlation };

                                    double forward = Math.exp(means[0]);
                                    double spread = Math.exp(means[1]);
                                    double libor = forward + spread;
                                    double strike = libor;
                                    double[] moments = new double[] {
                                            Math.log(libor),
                                            (forward * forward * covariances[0] + spread * spread * covariances[1] + 2.0 * forward * spread * covariances[2]) / (libor * libor)
                                    };

                                    double[][] covarianceMatrix = new double[][]{
                                            { covariances[0], covariances[2] }, { covariances[2], covariances[1] }
                                    };
                                    double[] expMeans = new double[] { Math.exp(means[0]), Math.exp(means[1]) };
                                    double[] momentsLevy = CapletAnalyticApproximation.getMomentsLevy(expMeans, covarianceMatrix);

                                    double volaSimple = Math.sqrt(moments[1]);
                                    DoubleUnaryOperator randomDrawSimple = (double normalDist) -> {
                                        double normalDist1 = moments[0] - 0.5 * moments[1] + volaSimple * normalDist;
                                        return Math.exp(normalDist1);
                                    };
                                    double volaLevy = Math.sqrt(momentsLevy[1]);
                                    DoubleUnaryOperator randomDrawLevy = (double normalDist) -> {
                                        double normalDist1 = momentsLevy[0] + volaLevy * normalDist;
                                        return Math.exp(normalDist1);
                                    };

                                    double probabilitySimApprox = 0.0, probabilitySum = 0.0,  probabilitySimLevy = 0.0;
                                    for(int n = 0; n < numberOfSimulations; n++) {
                                        double normalDist = NormalDistribution.inverseCumulativeDistribution(mersenneTwister64.nextDouble());
                                        probabilitySum += Math.max(randomDrawSum(correlation, means, covariances) - strike, 0.0);
                                        probabilitySimApprox += Math.max(randomDrawSimple.applyAsDouble(normalDist) - strike, 0.0);
                                        probabilitySimLevy += Math.max(randomDrawLevy.applyAsDouble(normalDist) - strike, 0.0);
                                    }

                                    probabilitySum /= numberOfSimulations;
                                    probabilitySimApprox /= numberOfSimulations;
                                    double probabilityBlackApprox = AnalyticFormulas.blackModelCapletValue(libor, Math.sqrt(moments[1]), 1.0, strike, 1.0, 1.0);

                                    double adjustment = CapletAnalyticApproximation.getTaylorExpansionByJuAdjustment(expMeans, momentsLevy, covarianceMatrix, strike);
                                    probabilitySimLevy /= numberOfSimulations;
                                    double probabilitySimJu = probabilitySimLevy + adjustment;
                                    double probabilityBlackLevy = CapletAnalyticApproximation.optionValue(libor, strike, momentsLevy[0], momentsLevy[1], 1.0);
                                    double probabilityBlackJu = CapletAnalyticApproximation.optionValue(libor, strike, momentsLevy[0], momentsLevy[1], 1.0) + adjustment;

                                    double normalizedErrorSimApprox = 2.0 * Math.abs(probabilitySum - probabilitySimApprox) / Math.abs(probabilitySum + probabilitySimApprox);
                                    double normalizedErrorBlackApprox = 2.0 * Math.abs(probabilitySum - probabilityBlackApprox) / Math.abs(probabilitySum + probabilityBlackApprox);
                                    double normalizedErrorSimLevy = 2.0 * Math.abs(probabilitySum - probabilitySimLevy) / Math.abs(probabilitySum + probabilitySimLevy);
                                    double normalizedErrorSimJu = 2.0 * Math.abs(probabilitySum - probabilitySimJu) / Math.abs(probabilitySum + probabilitySimJu);
                                    double normalizedErrorBlackLevy = 2.0 * Math.abs(probabilitySum - probabilityBlackLevy) / Math.abs(probabilitySum + probabilityBlackLevy);
                                    double normalizedErrorBlackJu = 2.0 * Math.abs(probabilitySum - probabilityBlackJu) / Math.abs(probabilitySum + probabilityBlackJu);

                                    String resultString = i + "\t" + j + "\t" + k + "\t" + means[0] + "\t" + means[1] + "\t" + covariances[0] + "\t" + covariances[1] + "\t" + correlation +
                                            "\t" + probabilitySum + "\t" + probabilitySimApprox + "\t" + probabilityBlackApprox + "\t" + probabilitySimLevy + "\t" + probabilitySimJu + "\t" +
                                            probabilityBlackLevy + "\t" + probabilityBlackJu +
                                            "\t" + normalizedErrorSimApprox + "\t" + normalizedErrorBlackApprox + "\t" + normalizedErrorSimLevy + "\t" + normalizedErrorSimJu + "\t" +
                                            normalizedErrorBlackLevy + "\t" + normalizedErrorBlackJu;
                                    System.out.println(resultString);

                                    writerAll.write("\n" + resultString);
                                    writerFixedVola.write("\n" + resultString);
                                    writerFixedMean.write("\n" + resultString);
                                    writerFixedCorrelation.write("\n" + resultString);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}