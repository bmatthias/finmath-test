/*
 * (c) Copyright Christian P. Fries, Germany. All rights reserved. Contact: email@christian-fries.de.
 *
 * Created on 10.02.2004
 */
package com.bmatthias.finmath.test;

import com.bmatthias.finmath.test.tools.tools.CSVHelper;
import com.bmatthias.finmath.test.tools.tools.CalibrationData;
import net.finmath.exception.CalculationException;
import net.finmath.marketdata.model.AnalyticModelInterface;
import net.finmath.marketdata.model.curves.ForwardCurveInterface;
import net.finmath.montecarlo.BrownianMotion;
import net.finmath.montecarlo.BrownianMotionInterface;
import net.finmath.montecarlo.RandomVariable;
import net.finmath.montecarlo.interestrate.*;
import net.finmath.montecarlo.interestrate.LIBORMarketModel.Measure;
import net.finmath.montecarlo.interestrate.LIBORMarketModelInterface.CalibrationItem;
import net.finmath.montecarlo.interestrate.modelplugins.*;
import net.finmath.montecarlo.interestrate.products.AbstractLIBORMonteCarloProduct;
import net.finmath.montecarlo.process.ProcessEulerScheme;
import net.finmath.stochastic.RandomVariableInterface;
import net.finmath.time.TimeDiscretizationInterface;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.primes.Primes;

import java.io.File;
import java.util.*;

/**
 * This class tests the LIBOR market model and products.
 *
 * @author Christian Fries
 */
public class MultiCurveLIBORMarketModelCalibration {
    private ForwardCurveInterface oisShift;
    private ForwardCurveInterface spreadShift;
    private int numberOfFactors = 20;
    private int numberOfPaths = 10000;
    private Integer numberOfParams = 17;
    private String measure = Measure.SPOT.name();
    private String multiCurveModel = MultiCurveLIBORMarketModel.MultiCurveModel.ADDITIVE.name();
    private boolean useSeperateCorrelationModels = false;
    private int numberOfSeeds = 1;
    int numberOfRandomRuns = 0;

    private boolean calibrateSC = false;
    private boolean calibrateMC = false;
    private String stateSpace = LIBORMarketModel.StateSpace.LOGNORMAL.name();

    private boolean setParams = true;
    double[] oisParams = new double[] {
            0.2, 0.005, 0.3, 0.2, //vola
            0.1, 0.2, 0.1 //corr
    };
    double[] spreadParams = new double[] {
            0.4, 0.005, 0.5, 0.2, //vola
            0.2, 0.1, 0.2, //corr
            0.5, //corr
            0.0 //shift
    };

    public MultiCurveLIBORMarketModelCalibration() {}

    public MultiCurveLIBORMarketModelCalibration(
            Integer numberOfFactors, Integer[] numberOfPaths,
            MultiCurveLIBORMarketModel.MultiCurveModel multiCurveModel,
            Boolean useSeperateCorrelationModels, Integer numberOfParams,
            Double[] oisParams, Double[] spreadParams, Boolean calibrate, Boolean setParams,
            ForwardCurveInterface oisShift, ForwardCurveInterface spreadShift) {
        this.oisShift = oisShift;
        this.spreadShift = spreadShift;
        if(numberOfPaths[0] != null) this.numberOfSeeds = numberOfPaths[0];
        if(numberOfPaths[1] != null) this.numberOfPaths = numberOfPaths[1];
        if(numberOfParams != null) this.numberOfParams = numberOfParams;
        if(numberOfFactors != null) this.numberOfFactors = numberOfFactors;
        if(multiCurveModel != null) this.multiCurveModel = multiCurveModel.name();
        if(useSeperateCorrelationModels != null) this.useSeperateCorrelationModels = useSeperateCorrelationModels;
        if(oisParams != null) this.oisParams = Arrays.stream(oisParams).mapToDouble(Double::doubleValue).toArray();
        if(spreadParams != null) this.spreadParams = Arrays.stream(spreadParams).mapToDouble(Double::doubleValue).toArray();
        if(calibrate != null) this.calibrateMC = calibrate;
        if(calibrate != null) this.calibrateSC = calibrate;
        if(setParams != null) this.setParams = setParams;
    }

    public void start(CalibrationData calibrationData, String folderName) throws Exception {
        File outputFolder = new File(folderName + "_1");
        for (int i = 2; outputFolder.exists(); i++) {
            outputFolder = new File(folderName  + "_" + i);
        }
        outputFolder.mkdirs();

        runTest(calibrationData, outputFolder.getPath() + File.separator + "initial_calibration");

        double[] oisParams = Arrays.copyOf(this.oisParams, this.oisParams.length);
        double[] spreadParams = Arrays.copyOf(this.spreadParams, this.spreadParams.length);
        Random random = new Random(37);
        for(int run = 0; run < numberOfRandomRuns; run++) {
            try {
                varyParameters(random, oisParams, spreadParams);
                runTest(calibrationData, outputFolder.getPath() + File.separator + String.valueOf("random_run_" + (run+1)));
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                System.gc();
            }
        }
    }

    private void varyParameters(Random random, double[] oisParams, double[] spreadParams) {
        for(int p = 0; p < oisParams.length; p++) {
            this.oisParams[p] = Math.round(100.0 * oisParams[p] * (1.0 + .2 * random.nextGaussian())) / 100.0;
        }
        for(int p = 0; p < spreadParams.length; p++) {
            this.spreadParams[p] = Math.round(100.0 * spreadParams[p] * (1.0 + .2 * random.nextGaussian())) / 100.0;
        }
        this.calibrateMC = this.calibrateSC = false;
        this.setParams = true;
    }

    private void runTest(CalibrationData calibrationData, String baseName) throws Exception {
        TimeDiscretizationInterface timeDiscretization = calibrationData.getTimeDiscretization();
        TimeDiscretizationInterface liborPeriodDiscretization = calibrationData.getPeriodDiscretization();
        AnalyticModelInterface analyticModel = calibrationData.getAnalyticModel();
        String eoniaCurveName = calibrationData.getFirstCurveName();
        String forwardCurveName = calibrationData.getSecondCurveName();
        CalibrationItem[] eoniaCalibrationItems = calibrationData.getFirstCurveCalibrationItems();
        CalibrationItem[] euriborCalibrationItems = calibrationData.getSecondCurveCalibrationItems();
        Map<String, List<AbstractLIBORMonteCarloProduct>> pricingProducts = calibrationData.getPricingProducts();

        Map<String, Object> mcProperties = new HashMap<>();
        mcProperties.put("measure", measure);
        mcProperties.put("stateSpace", stateSpace);
        mcProperties.put("multiCurveModel", multiCurveModel);

		/*
		 * Create a LIBOR Market Model
		 */
        AbstractLIBORCovarianceModelParametric covarianceModelParametric = getSingleCurveCovarianceModel(timeDiscretization, liborPeriodDiscretization);

        if(setParams) covarianceModelParametric.setParameter(oisParams);

        LIBORMarketModelInterface scLiborMarketModelCalibrated = new MultiCurveLIBORMarketModel(
                liborPeriodDiscretization, analyticModel, eoniaCurveName, eoniaCurveName,
                oisShift, spreadShift,
                covarianceModelParametric,
                calibrateSC ? eoniaCalibrationItems : new CalibrationItem[0], mcProperties);

        this.oisParams = ((AbstractLIBORCovarianceModelParametric) scLiborMarketModelCalibrated.getCovarianceModel()).getParameter();
        //for (double p : this.oisParams) System.out.println(p);
        //System.out.println();

        covarianceModelParametric = getMultiCurveCovarianceModel(timeDiscretization, liborPeriodDiscretization, this.oisParams);

        if (setParams) covarianceModelParametric.setParameter(spreadParams);

        long start = System.currentTimeMillis();

        LIBORMarketModelInterface mcLiborMarketModelCalibrated = new MultiCurveLIBORMarketModel(
                liborPeriodDiscretization, analyticModel, forwardCurveName, eoniaCurveName,
                oisShift, spreadShift,
                covarianceModelParametric,
                calibrateMC ? euriborCalibrationItems : new CalibrationItem[0], mcProperties);

        this.spreadParams = ((AbstractLIBORCovarianceModelParametric) mcLiborMarketModelCalibrated.getCovarianceModel()).getParameter();


        //for (double p : this.spreadParams) System.out.println(p);
        //System.out.println();

        long duration = System.currentTimeMillis() - start;
        System.out.println("Calibration time:\t" + duration);
        start = System.currentTimeMillis();

		/*
		 * Test our calibration
		 */

        int primeNumber = 1009;
        List<Integer> primes = new ArrayList<>();
        while (primes.size() < numberOfSeeds) {
            primes.add(primeNumber);
            primeNumber = Primes.nextPrime(primeNumber + 1);
        }

        LinkedHashMap<String, RandomVariableInterface[]> results = new LinkedHashMap<>();

        for (Integer prime : primes) {
            System.gc();

            //System.out.println(prime);
            BrownianMotionInterface scBrownianMotion = new BrownianMotion(timeDiscretization, numberOfFactors, numberOfPaths, prime /* seed */);
            BrownianMotionInterface mcBrownianMotion = new BrownianMotion(timeDiscretization, 2 * numberOfFactors, numberOfPaths, prime /* seed */);

            ProcessEulerScheme scProcessSpot = new ProcessEulerScheme(scBrownianMotion);
            LIBORModelMonteCarloSimulation scSimSpot = new LIBORModelMonteCarloSimulation(scLiborMarketModelCalibrated, scProcessSpot);

            ProcessEulerScheme mcProcessSpot = new ProcessEulerScheme(mcBrownianMotion);
            LIBORModelMonteCarloSimulation mcSimSpot  = new LIBORModelMonteCarloSimulation(mcLiborMarketModelCalibrated, mcProcessSpot);

            //CSVHelper.printProcess(mcProcessSpot, mcLiborMarketModelCalibrated);
            //CSVCreator.printProcess(scProcessSpot, scModelSpot);

            RandomVariableInterface[] targetValuesOIS = new RandomVariableInterface[eoniaCalibrationItems.length];
            RandomVariableInterface[] targetValuesLIBOR = new RandomVariableInterface[euriborCalibrationItems.length];
            for(int i = 0; i < eoniaCalibrationItems.length; i++) {
                targetValuesOIS[i] = new RandomVariable(eoniaCalibrationItems[i].calibrationTargetValue);
            }
            for(int i = 0; i < euriborCalibrationItems.length; i++) {
                targetValuesLIBOR[i] = new RandomVariable(euriborCalibrationItems[i].calibrationTargetValue);
            }
            results.put("Target (OIS)", targetValuesOIS);
            results.put("Target (LIBOR)", targetValuesLIBOR);

            for(Map.Entry<String, List<AbstractLIBORMonteCarloProduct>> entry : pricingProducts.entrySet()) {
                List<AbstractLIBORMonteCarloProduct> products = entry.getValue();
                if (results.get(entry.getKey()) == null) {
                    results.put(entry.getKey(), new RandomVariableInterface[products.size()]);
                }

                for(int i = 0; i < products.size(); i++) {
                    results.get(entry.getKey())[i] = getOptionValue(results.get(entry.getKey())[i], products.get(i), entry.getKey().toLowerCase().contains("multi") ? mcSimSpot : scSimSpot);
                }
            }
        }

        CSVHelper.writeValues(baseName , results);
        CSVHelper.writeErrorStatistics(baseName, results);
        CSVHelper.writeParamsAndDuration(baseName, this.oisParams, this.spreadParams, duration, calibrationData);
        CSVHelper.writeProcess(baseName, (MultiCurveLIBORMarketModel)mcLiborMarketModelCalibrated);
        CSVHelper.writeFactorMatrix(baseName, (LIBORCovarianceModelFromVolatilityAndCorrelationExtraParameters) mcLiborMarketModelCalibrated.getCovarianceModel());
        CSVHelper.writeCorrelationMatrix(baseName, (LIBORCovarianceModelFromVolatilityAndCorrelationExtraParameters) mcLiborMarketModelCalibrated.getCovarianceModel());
        CSVHelper.writeCovariance(baseName, mcLiborMarketModelCalibrated.getCovarianceModel());
        CSVHelper.writeVolatilitySurface(baseName, mcLiborMarketModelCalibrated.getCovarianceModel());

        System.out.println("Calculation time:\t" + (System.currentTimeMillis() - start));
        System.out.println(baseName);
        System.out.println("__________________________________________________________________________________________\n");
    }

    private AbstractLIBORCovarianceModelParametric getMultiCurveCovarianceModel(TimeDiscretizationInterface timeDiscretization, TimeDiscretizationInterface liborPeriodDiscretization, double[] scParams) {
        double[] fixedVolParams = new double[4];
        System.arraycopy(scParams, 0, fixedVolParams, 0, 4);

        double[] fixedCorParams = new double[scParams.length - 4];
        System.arraycopy(scParams, 4, fixedCorParams, 0, scParams.length - 4);

        LIBORCorrelationModel correlationModel;
        if (MultiCurveLIBORMarketModel.MultiCurveModel.MMARTINGALE.name().equals(multiCurveModel.toUpperCase())) {
            correlationModel = new LIBORCorrelationModelIndependentCurveSevenParameterExponentialDecay(timeDiscretization, liborPeriodDiscretization, 2 * numberOfFactors, fixedCorParams, true);
        } else if (numberOfParams == 11) {
            correlationModel = useSeperateCorrelationModels ?
                    new LIBORCorrelationModelSeperateCurveThreeParameterExponentialDecay(timeDiscretization, liborPeriodDiscretization, 2 * numberOfFactors, fixedCorParams, true) :
                    new LIBORCorrelationModelTwoCurveThreeParameterExponentialDecay(timeDiscretization, liborPeriodDiscretization, 2 * numberOfFactors, fixedCorParams, true);
        } else if (numberOfParams == 13) {
            correlationModel = useSeperateCorrelationModels ?
                    new LIBORCorrelationModelSeperateCurveFiveParameterExponentialDecay(timeDiscretization, liborPeriodDiscretization, 2 * numberOfFactors, fixedCorParams, true) :
                    new LIBORCorrelationModelTwoCurveFiveParameterExponentialDecay(timeDiscretization, liborPeriodDiscretization, 2 * numberOfFactors, fixedCorParams, true);
        } else if (numberOfParams == 15) {
            correlationModel = useSeperateCorrelationModels ?
                    new LIBORCorrelationModelSeperateCurveSevenParameterExponentialDecay(timeDiscretization, liborPeriodDiscretization, 2 * numberOfFactors, fixedCorParams, true) :
                    new LIBORCorrelationModelTwoCurveSevenParameterExponentialDecay(timeDiscretization, liborPeriodDiscretization, 2 * numberOfFactors, fixedCorParams, true);
        } else if (numberOfParams == 16) {
            correlationModel = new LIBORCorrelationModelSeperateCurveEightParameterExponentialDecay(timeDiscretization, liborPeriodDiscretization, 2 * numberOfFactors, fixedCorParams, true);
        } else if (numberOfParams == 17) {
            correlationModel = useSeperateCorrelationModels ?
                    new LIBORCorrelationModelSeperateCurveNineParameterExponentialDecay(timeDiscretization, liborPeriodDiscretization, 2 * numberOfFactors, fixedCorParams, true) :
                    new LIBORCorrelationModelTwoCurveNineParameterExponentialDecay(timeDiscretization, liborPeriodDiscretization, 2 * numberOfFactors, fixedCorParams, true);
        } else if (numberOfParams == 18) {
            correlationModel = useSeperateCorrelationModels ?
                    new LIBORCorrelationModelSeperateCurveTenParameterExponentialDecay(timeDiscretization, liborPeriodDiscretization, 2 * numberOfFactors, fixedCorParams, true) :
                    new LIBORCorrelationModelTwoCurveTenParameterExponentialDecay(timeDiscretization, liborPeriodDiscretization, 2 * numberOfFactors, fixedCorParams, true);
        } else {
            throw new IllegalArgumentException("" + numberOfParams + " params not supported.");
        }

        LIBORVolatilityModel volatilityModel = new LIBORVolatilityModelTwoCurveEightParameterExponentialForm(timeDiscretization, liborPeriodDiscretization, fixedVolParams, true);
        return new LIBORCovarianceModelFromVolatilityAndCorrelationExtraParameters(timeDiscretization, liborPeriodDiscretization, volatilityModel, correlationModel, new double[]{ 0.5 });
    }

    private AbstractLIBORCovarianceModelParametric getSingleCurveCovarianceModel(TimeDiscretizationInterface timeDiscretization, TimeDiscretizationInterface liborPeriodDiscretization) {
        LIBORCorrelationModel correlationModel;
        if (numberOfParams <= 13) {
            correlationModel = new LIBORCorrelationModelExponentialDecay(timeDiscretization, liborPeriodDiscretization, numberOfFactors, 0.1, true);
        } else {
            correlationModel = new LIBORCorrelationModelThreeParameterExponentialDecay(timeDiscretization, liborPeriodDiscretization, numberOfFactors, 0.1, 0.1, 0.1, true);
        }
        LIBORVolatilityModel volatilityModel = new LIBORVolatilityModelFourParameterExponentialForm(timeDiscretization, liborPeriodDiscretization, 0.1, 0.1, 0.1, 0.1, true);
        return new LIBORCovarianceModelFromVolatilityAndCorrelationExtraParameters(timeDiscretization, liborPeriodDiscretization, volatilityModel, correlationModel, new double[]{ 0.5 });
    }

    private RandomVariableInterface getOptionValue(RandomVariableInterface currentResult, AbstractLIBORMonteCarloProduct product,
                                                   LIBORModelMonteCarloSimulationInterface simulation) throws CalculationException {
        if (simulation != null && currentResult != null) {
            RandomVariableInterface newResult = product.getValue(0.0, simulation);
            double[] realizations = ArrayUtils.addAll(currentResult.getRealizations(), newResult.getRealizations());
            return new RandomVariable(currentResult.getFiltrationTime(), realizations);
        } else if (simulation != null) {
            return product.getValue(0.0, simulation);
        } else {
            return currentResult != null ? currentResult : new RandomVariable(0.0);
        }
    }
}