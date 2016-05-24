package com.bmatthias.finmath.test.tools.tools.calibrationdata;

import com.bmatthias.finmath.test.tools.tools.CalibrationData;
import net.finmath.exception.CalculationException;
import net.finmath.marketdata.model.AnalyticModel;
import net.finmath.marketdata.model.AnalyticModelInterface;
import net.finmath.marketdata.model.curves.CurveInterface;
import net.finmath.marketdata.model.curves.DiscountCurveFromForwardCurve;
import net.finmath.marketdata.model.curves.DiscountCurveInterface;
import net.finmath.marketdata.model.curves.ForwardCurveInterface;
import net.finmath.modelling.ProductInterface;
import net.finmath.montecarlo.RandomVariable;
import net.finmath.montecarlo.interestrate.LIBORMarketModelInterface;
import net.finmath.montecarlo.interestrate.LIBORModelMonteCarloSimulationInterface;
import net.finmath.montecarlo.interestrate.MultiCurveLIBORMarketModel;
import net.finmath.montecarlo.interestrate.products.*;
import net.finmath.stochastic.RandomVariableInterface;
import net.finmath.time.TimeDiscretization;
import net.finmath.time.TimeDiscretizationInterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

public class SyntheticProductsWithCapletCalibration {
    public enum CalibrationMethod {
        INTEGRATED_EXPECTATION, JU, SWAPTION, NONE
    }

    public static CalibrationData newInstance(ForwardCurveInterface iborForwardCurve, ForwardCurveInterface oisForwardCurve, double euriborVol, double eoniaVol, CalibrationMethod calibrationMethod) {
        double liborPeriodLength	= 0.5;
        double liborRateTimeHorzion	= 20.0;
        TimeDiscretization liborPeriodDiscretization = new TimeDiscretization(0.0, (int) (liborRateTimeHorzion / liborPeriodLength), liborPeriodLength);

        DiscountCurveInterface discountCurve = new DiscountCurveFromForwardCurve("DiscountCurve", oisForwardCurve);
        AnalyticModelInterface analyticModel = new AnalyticModel(new CurveInterface[] { discountCurve, oisForwardCurve, iborForwardCurve });

        LinkedHashMap<String, List<AbstractLIBORMonteCarloProduct>> pricingProducts = new LinkedHashMap<>();

		/*
		 * Create a set of calibration products.
		 */
        ArrayList<LIBORMarketModelInterface.CalibrationItem> eoniaCalibrationItems = new ArrayList<>();
        ArrayList<LIBORMarketModelInterface.CalibrationItem> euriborCalibrationItems = new ArrayList<>();

        for (int exerciseIndex = 2, component = 1; exerciseIndex <= liborPeriodDiscretization.getNumberOfTimeSteps() - 5; exerciseIndex += 5) {
            double exerciseDate = liborPeriodDiscretization.getTime(exerciseIndex);
            for (int numberOfPeriods = 2; numberOfPeriods < liborPeriodDiscretization.getNumberOfTimeSteps() - exerciseIndex; numberOfPeriods += 4, component++) {
                double maturity = liborPeriodDiscretization.getTime(component);

                double[] swapTenor = new double[numberOfPeriods + 1];
                double swapPeriodLength = 0.5;

                for (int periodStartIndex = 0; periodStartIndex <= numberOfPeriods; periodStartIndex++) {
                    swapTenor[periodStartIndex] = exerciseDate + periodStartIndex * swapPeriodLength;
                }

                TimeDiscretizationInterface fixAndFloatTenor = new TimeDiscretization(swapTenor);

                // EONIA Swaptions swap rate
                double eoniaSwaprate = net.finmath.marketdata.products.Swap.getForwardSwapRate(
                        fixAndFloatTenor,
                        fixAndFloatTenor,
                        oisForwardCurve,
                        discountCurve
                );

                // EURIBOR Swaptions swap rate
                double euriborSwaprate = net.finmath.marketdata.products.Swap.getForwardSwapRate(
                        fixAndFloatTenor,
                        fixAndFloatTenor,
                        iborForwardCurve,
                        discountCurve
                );

                // Set swap rates for each period
                double[] eoniaSwaprates = new double[numberOfPeriods];
                Arrays.fill(eoniaSwaprates, eoniaSwaprate);

                double[] euriborSwaprates = new double[numberOfPeriods];
                Arrays.fill(euriborSwaprates, euriborSwaprate);

                double eoniaStrike = oisForwardCurve.getForward(null, maturity);
                double euriborStrike = iborForwardCurve.getForward(null, maturity);

                switch (calibrationMethod) {
                    case INTEGRATED_EXPECTATION:
                        AbstractLIBORMonteCarloProduct eoniaCapletAnalytic = new CapletAnalyticApproximation(eoniaStrike, maturity, CapletAnalyticApproximation.ValueUnit.VOLATILITY);
                        AbstractLIBORMonteCarloProduct euriborCapletCalibration = new CapletAnalyticApproximation(
                                euriborStrike, maturity, CapletAnalyticApproximation.ValueUnit.VOLATILITY,
                                CapletAnalyticApproximation.MultiCurveApproximation.INTEGRATED_EXPECTATION
                        );
                        // This is just some volatility used for testing, true market data should go here.
                        double eoniaTargetCapletVolatility = eoniaVol * ((0.5 + 0.01 * liborRateTimeHorzion * maturity) * Math.exp(-0.01 * liborRateTimeHorzion * maturity) + 0.5);
                        double euriborTargetCapletVolatility = euriborVol * ((0.5 + 0.01 * liborRateTimeHorzion * maturity) * Math.exp(-0.01 * liborRateTimeHorzion * maturity) + 0.5);
                        eoniaCalibrationItems.add(new LIBORMarketModelInterface.CalibrationItem(eoniaCapletAnalytic, eoniaTargetCapletVolatility, 1.0));
                        euriborCalibrationItems.add(new LIBORMarketModelInterface.CalibrationItem(euriborCapletCalibration, euriborTargetCapletVolatility, 1.0));
                        break;
                    case JU:
                        eoniaCapletAnalytic = new CapletAnalyticApproximation(eoniaStrike, maturity, CapletAnalyticApproximation.ValueUnit.VOLATILITY);
                        euriborCapletCalibration = new CapletAnalyticApproximation(
                                euriborStrike, maturity, CapletAnalyticApproximation.ValueUnit.VOLATILITY,
                                CapletAnalyticApproximation.MultiCurveApproximation.JU
                        );
                        // This is just some volatility used for testing, true market data should go here.
                        eoniaTargetCapletVolatility = eoniaVol * ((0.5 + 0.01 * liborRateTimeHorzion * maturity) * Math.exp(-0.01 * liborRateTimeHorzion * maturity) + 0.5);
                        euriborTargetCapletVolatility = euriborVol * ((0.5 + 0.01 * liborRateTimeHorzion * maturity) * Math.exp(-0.01 * liborRateTimeHorzion * maturity) + 0.5);
                        eoniaCalibrationItems.add(new LIBORMarketModelInterface.CalibrationItem(eoniaCapletAnalytic, eoniaTargetCapletVolatility, 1.0));
                        euriborCalibrationItems.add(new LIBORMarketModelInterface.CalibrationItem(euriborCapletCalibration, euriborTargetCapletVolatility, 1.0));
                        break;
                    default:
                    case SWAPTION:
                        SwaptionAnalyticApproximation eoniaSwaptionAnalyticVola = new SwaptionAnalyticApproximation(
                                eoniaSwaprate, swapTenor, SwaptionAnalyticApproximation.ValueUnit.VOLATILITY, SwaptionAnalyticApproximation.MultiCurveApproximation.SINGLE_CURVE
                        );
                        SwaptionAnalyticApproximation euriborSwaptionAnalyticSimpleVola = new SwaptionAnalyticApproximation(
                                euriborSwaprate, swapTenor, SwaptionAnalyticApproximation.ValueUnit.VOLATILITY, SwaptionAnalyticApproximation.MultiCurveApproximation.DRIFT_FREEZE
                        );

                        // This is just some swaption volatility used for testing, true market data should go here.
                        double eoniaSwaptionTargetVolatilty = eoniaVol + eoniaVol * Math.exp(-exerciseDate / 10.0) + eoniaVol * Math.exp(-(exerciseDate+numberOfPeriods) / 10.0);
                        double euriborSwaptionTargetVolatilty = euriborVol + euriborVol * Math.exp(-exerciseDate / 10.0) + euriborVol * Math.exp(-(exerciseDate+numberOfPeriods) / 10.0);

                        eoniaCalibrationItems.add(new LIBORMarketModelInterface.CalibrationItem(eoniaSwaptionAnalyticVola, eoniaSwaptionTargetVolatilty, 1.0));
                        euriborCalibrationItems.add(new LIBORMarketModelInterface.CalibrationItem(euriborSwaptionAnalyticSimpleVola, euriborSwaptionTargetVolatilty, 1.0));
                        break;
                }

                putCaplets(pricingProducts, maturity, liborPeriodLength, euriborStrike, eoniaStrike);
                putSwaptions(pricingProducts, swapTenor, euriborSwaprate, eoniaSwaprate);
                putRatesAndTime(pricingProducts, maturity, liborPeriodLength);
            }
        }

        return new CalibrationData(liborPeriodDiscretization,
                liborPeriodDiscretization,
                analyticModel,
                oisForwardCurve.getName(),
                iborForwardCurve.getName(),
                eoniaCalibrationItems.toArray(new LIBORMarketModelInterface.CalibrationItem[eoniaCalibrationItems.size()]),
                euriborCalibrationItems.toArray(new LIBORMarketModelInterface.CalibrationItem[euriborCalibrationItems.size()]),
                pricingProducts);
    }


    private static void putCaplets(LinkedHashMap<String, List<AbstractLIBORMonteCarloProduct>> pricingProducts, double maturity, double periodLength, double euriborStrike, double eoniaStrike) {
        AbstractLIBORMonteCarloProduct eoniaCaplet = new Caplet(maturity, periodLength, eoniaStrike, periodLength, false, ProductInterface.ValueUnit.VOLATILITY);
        AbstractLIBORMonteCarloProduct eoniaCapletAnalytic = new CapletAnalyticApproximation(eoniaStrike, maturity, CapletAnalyticApproximation.ValueUnit.VOLATILITY);

        AbstractLIBORMonteCarloProduct eoniaCapletValue = new Caplet(maturity, periodLength, eoniaStrike, periodLength, false, ProductInterface.ValueUnit.VALUE);
        AbstractLIBORMonteCarloProduct eoniaCapletAnalyticValue = new CapletAnalyticApproximation(eoniaStrike, maturity, CapletAnalyticApproximation.ValueUnit.VALUE);
        AbstractLIBORMonteCarloProduct eoniaCapletZeroStrikeValue = new Caplet(maturity, periodLength, 0.0, periodLength, false, ProductInterface.ValueUnit.VALUE);
        AbstractLIBORMonteCarloProduct eoniaCapletAnalyticZeroStrikeValue = new CapletAnalyticApproximation(0.0, maturity, CapletAnalyticApproximation.ValueUnit.VALUE);

        AbstractLIBORMonteCarloProduct euriborCaplet = new Caplet(maturity, periodLength, euriborStrike, periodLength,
                false, ProductInterface.ValueUnit.VOLATILITY);
        AbstractLIBORMonteCarloProduct euriborCapletAnalyticDriftFreeze = new CapletAnalyticApproximation(
                euriborStrike, maturity, CapletAnalyticApproximation.ValueUnit.VOLATILITY,
                CapletAnalyticApproximation.MultiCurveApproximation.DRIFT_FREEZE);
        AbstractLIBORMonteCarloProduct euriborCapletAnalyticIntegratedVariance = new CapletAnalyticApproximation(
                euriborStrike, maturity, CapletAnalyticApproximation.ValueUnit.VOLATILITY,
                CapletAnalyticApproximation.MultiCurveApproximation.INTEGRATED_EXPECTATION);
        AbstractLIBORMonteCarloProduct euriborCapletAnalyticFentonWilkinson = new CapletAnalyticApproximation(
                euriborStrike, maturity, CapletAnalyticApproximation.ValueUnit.VOLATILITY,
                CapletAnalyticApproximation.MultiCurveApproximation.LEVY);
        AbstractLIBORMonteCarloProduct euriborCapletAnalyticJu = new CapletAnalyticApproximation(
                euriborStrike, maturity, CapletAnalyticApproximation.ValueUnit.VOLATILITY,
                CapletAnalyticApproximation.MultiCurveApproximation.JU);
        AbstractLIBORMonteCarloProduct euriborCapletAnalyticHo = new CapletAnalyticApproximation(
                euriborStrike, maturity, CapletAnalyticApproximation.ValueUnit.VOLATILITY,
                CapletAnalyticApproximation.MultiCurveApproximation.HO);

        AbstractLIBORMonteCarloProduct euriborCapletValue = new Caplet(maturity, periodLength, euriborStrike, periodLength,
                false, ProductInterface.ValueUnit.VALUE);
        AbstractLIBORMonteCarloProduct euriborCapletAnalyticDriftFreezeValue = new CapletAnalyticApproximation(
                euriborStrike, maturity, CapletAnalyticApproximation.ValueUnit.VALUE,
                CapletAnalyticApproximation.MultiCurveApproximation.DRIFT_FREEZE);
        AbstractLIBORMonteCarloProduct euriborCapletAnalyticIntegratedVarianceValue = new CapletAnalyticApproximation(
                euriborStrike, maturity, CapletAnalyticApproximation.ValueUnit.VALUE,
                CapletAnalyticApproximation.MultiCurveApproximation.INTEGRATED_EXPECTATION);
        AbstractLIBORMonteCarloProduct euriborCapletAnalyticFentonWilkinsonValue = new CapletAnalyticApproximation(
                euriborStrike, maturity, CapletAnalyticApproximation.ValueUnit.VALUE,
                CapletAnalyticApproximation.MultiCurveApproximation.LEVY);
        AbstractLIBORMonteCarloProduct euriborCapletAnalyticJuValue = new CapletAnalyticApproximation(
                euriborStrike, maturity, CapletAnalyticApproximation.ValueUnit.VALUE,
                CapletAnalyticApproximation.MultiCurveApproximation.JU);
        AbstractLIBORMonteCarloProduct euriborCapletAnalyticHoValue = new CapletAnalyticApproximation(
                euriborStrike, maturity, CapletAnalyticApproximation.ValueUnit.VALUE,
                CapletAnalyticApproximation.MultiCurveApproximation.HO);
        AbstractLIBORMonteCarloProduct euriborCapletZeroStrikeValue = new Caplet(maturity, periodLength,
                0.0, periodLength, false, ProductInterface.ValueUnit.VALUE);
        AbstractLIBORMonteCarloProduct euriborCapletAnalyticZeroStrikeDriftFreezeValue = new CapletAnalyticApproximation(
                0.0, maturity, CapletAnalyticApproximation.ValueUnit.VALUE,
                CapletAnalyticApproximation.MultiCurveApproximation.DRIFT_FREEZE);
        AbstractLIBORMonteCarloProduct euriborCapletAnalyticZeroStrikeIntegratedVarianceValue = new CapletAnalyticApproximation(
                0.0, maturity, CapletAnalyticApproximation.ValueUnit.VALUE,
                CapletAnalyticApproximation.MultiCurveApproximation.INTEGRATED_EXPECTATION);
        AbstractLIBORMonteCarloProduct euriborCapletAnalyticZeroStrikeFentonWilkinsonValue = new CapletAnalyticApproximation(
                0.0, maturity, CapletAnalyticApproximation.ValueUnit.VALUE,
                CapletAnalyticApproximation.MultiCurveApproximation.JU);

        put(pricingProducts, "Caplet Vola Analytic Single-Curve", eoniaCapletAnalytic);
        put(pricingProducts, "Caplet Vola Monte-Carlo Single-Curve", eoniaCaplet);
        put(pricingProducts, "Caplet Vola Analytic Multi-Curve Fenton-Wilkinson", euriborCapletAnalyticFentonWilkinson);
        put(pricingProducts, "Caplet Vola Analytic Multi-Curve Ju", euriborCapletAnalyticJu);
        put(pricingProducts, "Caplet Vola Analytic Multi-Curve Ho", euriborCapletAnalyticHo);
        put(pricingProducts, "Caplet Vola Analytic Multi-Curve Drift-Freeze", euriborCapletAnalyticDriftFreeze);
        put(pricingProducts, "Caplet Vola Analytic Multi-Curve Integrated-Expectation", euriborCapletAnalyticIntegratedVariance);
        put(pricingProducts, "Caplet Vola Monte-Carlo Multi-Curve", euriborCaplet);

        put(pricingProducts, "Caplet Value Analytic Single-Curve", eoniaCapletAnalyticValue);
        put(pricingProducts, "Caplet Value Monte-Carlo Single-Curve", eoniaCapletValue);
        //put(pricingProducts, "Caplet Value Analytic Zero-Strike Single-Curve", eoniaCapletAnalyticZeroStrikeValue, maturity, periodLength);
        //put(pricingProducts, "Caplet Value Monte-Carlo Zero-Strike Single-Curve", eoniaCapletZeroStrikeValue, maturity, periodLength);
        put(pricingProducts, "Caplet Value Analytic Multi-Curve Fenton-Wilkinson", euriborCapletAnalyticFentonWilkinsonValue);
        put(pricingProducts, "Caplet Value Analytic Multi-Curve Ju", euriborCapletAnalyticJuValue);
        put(pricingProducts, "Caplet Value Analytic Multi-Curve Ho", euriborCapletAnalyticHoValue);
        put(pricingProducts, "Caplet Value Analytic Multi-Curve Drift-Freeze", euriborCapletAnalyticDriftFreezeValue);
        put(pricingProducts, "Caplet Value Analytic Multi-Curve Integrated-Expectation", euriborCapletAnalyticIntegratedVarianceValue);
        put(pricingProducts, "Caplet Value Monte-Carlo Multi-Curve", euriborCapletValue);
        //put(pricingProducts, "Caplet Value Analytic Zero-Strike Multi-Curve Fenton-Wilkinson", euriborCapletAnalyticZeroStrikeFentonWilkinsonValue, maturity, periodLength);
        //put(pricingProducts, "Caplet Value Analytic Zero-Strike Multi-Curve Drift-Freeze", euriborCapletAnalyticZeroStrikeDriftFreezeValue, maturity, periodLength);
        //put(pricingProducts, "Caplet Value Analytic Zero-Strike Multi-Curve Integrated-Expectation", euriborCapletAnalyticZeroStrikeIntegratedVarianceValue, maturity, periodLength);
        //put(pricingProducts, "Caplet Value Monte-Carlo Zero-Strike Multi-Curve", euriborCapletZeroStrikeValue, maturity, periodLength);
    }

    private static void putSwaptions(LinkedHashMap<String, List<AbstractLIBORMonteCarloProduct>> pricingProducts, double[] swapTenor, double euriborSwaprate, double eoniaSwaprate) {
        SwaptionAnalyticApproximation eoniaSwaptionAnalyticVola = new SwaptionAnalyticApproximation(
                eoniaSwaprate, swapTenor, SwaptionAnalyticApproximation.ValueUnit.VOLATILITY, SwaptionAnalyticApproximation.MultiCurveApproximation.DRIFT_FREEZE
        );
        SwaptionAnalyticApproximation eoniaSwaptionAnalyticValue = new SwaptionAnalyticApproximation(
                eoniaSwaprate, swapTenor, SwaptionAnalyticApproximation.ValueUnit.VALUE
        );

        SwaptionAnalyticApproximation euriborSwaptionAnalyticSimpleVola = new SwaptionAnalyticApproximation(
                euriborSwaprate, swapTenor, SwaptionAnalyticApproximation.ValueUnit.VOLATILITY, SwaptionAnalyticApproximation.MultiCurveApproximation.DRIFT_FREEZE
        );
        SwaptionAnalyticApproximation euriborSwaptionAnalyticIntegratedExpectation = new SwaptionAnalyticApproximation(
                euriborSwaprate, swapTenor, SwaptionAnalyticApproximation.ValueUnit.VOLATILITY, SwaptionAnalyticApproximation.MultiCurveApproximation.INTEGRATED_EXPECTATION
        );

        SwaptionAnalyticApproximation euriborSwaptionAnalyticSimpleValue = new SwaptionAnalyticApproximation(
                euriborSwaprate, swapTenor, SwaptionAnalyticApproximation.ValueUnit.VALUE, SwaptionAnalyticApproximation.MultiCurveApproximation.DRIFT_FREEZE
        );
        SwaptionAnalyticApproximation euriborSwaptionAnalyticIntegratedExpectationValue = new SwaptionAnalyticApproximation(
                euriborSwaprate, swapTenor, SwaptionAnalyticApproximation.ValueUnit.VALUE, SwaptionAnalyticApproximation.MultiCurveApproximation.INTEGRATED_EXPECTATION
        );

        TimeDiscretizationInterface fixAndFloatTenor = new TimeDiscretization(swapTenor);

        AbstractLIBORMonteCarloProduct eoniaSwaptionMonteCarloValue = SwaptionFactory.createSwaption("SwaptionSimple", eoniaSwaprate, fixAndFloatTenor, "VALUE");
        AbstractLIBORMonteCarloProduct eoniaSwaptionMonteCarloVola = SwaptionFactory.createSwaption("SwaptionSimple", eoniaSwaprate, fixAndFloatTenor, "VOLATILITY");

        AbstractLIBORMonteCarloProduct euriborSwaptionMonteCarloValue = SwaptionFactory.createSwaption("SwaptionSimple", euriborSwaprate, fixAndFloatTenor, "VALUE");
        AbstractLIBORMonteCarloProduct euriborSwaptionMonteCarloVola = SwaptionFactory.createSwaption("SwaptionSimple", euriborSwaprate, fixAndFloatTenor, "VOLATILITY");

        put(pricingProducts, "Swaption Vola Analytic Single-Curve", eoniaSwaptionAnalyticVola);
        put(pricingProducts, "Swaption Vola Monte-Carlo Single-Curve", eoniaSwaptionMonteCarloVola);
        put(pricingProducts, "Swaption Vola Analytic Multi-Curve Drift-Freeze", euriborSwaptionAnalyticSimpleVola);
        put(pricingProducts, "Swaption Vola Analytic Multi-Curve Integrated-Expectation", euriborSwaptionAnalyticIntegratedExpectation);
        put(pricingProducts, "Swaption Vola Monte-Carlo Multi-Curve", euriborSwaptionMonteCarloVola);

        put(pricingProducts, "Swaption Value Analytic Single-Curve", eoniaSwaptionAnalyticValue);
        put(pricingProducts, "Swaption Value Monte-Carlo Single-Curve", eoniaSwaptionMonteCarloValue);
        put(pricingProducts, "Swaption Value Analytic Multi-Curve Drift-Freeze", euriborSwaptionAnalyticSimpleValue);
        put(pricingProducts, "Swaption Value Analytic Multi-Curve Integrated-Expectation", euriborSwaptionAnalyticIntegratedExpectationValue);
        put(pricingProducts, "Swaption Value Monte-Carlo Multi-Curve", euriborSwaptionMonteCarloValue);

        put(pricingProducts, "Swaption Exercise Date", new AbstractLIBORMonteCarloProduct() {
            @Override
            public RandomVariableInterface getValue(double evaluationTime, LIBORModelMonteCarloSimulationInterface model) throws CalculationException {
                return new RandomVariable(0.01 * swapTenor[0]);
            }
        });
        final int periods = swapTenor.length - 1;
        put(pricingProducts, "Swaption Number Of Periods", new AbstractLIBORMonteCarloProduct() {
            @Override
            public RandomVariableInterface getValue(double evaluationTime, LIBORModelMonteCarloSimulationInterface model) throws CalculationException {
                return new RandomVariable(0.01 * periods);
            }
        });
    }

    private static void put(LinkedHashMap<String, List<AbstractLIBORMonteCarloProduct>> map, String key, AbstractLIBORMonteCarloProduct value) {
        if (map.get(key) == null) map.put(key, new ArrayList<>());
        map.get(key).add(value);
    }

    private static void put(LinkedHashMap<String, List<AbstractLIBORMonteCarloProduct>> map, String key, AbstractLIBORMonteCarloProduct value, double maturity) {
        if (map.get(key) == null) map.put(key, new ArrayList<>());
        map.get(key).add(new AbstractLIBORMonteCarloProduct() {
            @Override
            public RandomVariableInterface getValue(double evaluationTime, LIBORModelMonteCarloSimulationInterface model) throws CalculationException {
                LIBORMarketModelInterface liborMarketModel = model.getModel();
                return value.getValue(evaluationTime, model)
                        .div(liborMarketModel.getDiscountCurve().getDiscountFactor(liborMarketModel.getAnalyticModel(), model.getTime(model.getTimeIndex(maturity) + 1)));
            }
        });
    }

    private static void put(LinkedHashMap<String, List<AbstractLIBORMonteCarloProduct>> map, String key, AbstractLIBORMonteCarloProduct value, double maturity, double timeStep) {
        if (map.get(key) == null) map.put(key, new ArrayList<>());
        map.get(key).add(new AbstractLIBORMonteCarloProduct() {
            @Override
            public RandomVariableInterface getValue(double evaluationTime, LIBORModelMonteCarloSimulationInterface model) throws CalculationException {
                LIBORMarketModelInterface liborMarketModel = model.getModel();
                return value.getValue(evaluationTime, model)
                        .div(liborMarketModel.getDiscountCurve().getDiscountFactor(liborMarketModel.getAnalyticModel(), model.getTime(model.getTimeIndex(maturity) + 1)))
                        .div(timeStep);
            }
        });
    }

    private static void putRatesAndTime(LinkedHashMap<String, List<AbstractLIBORMonteCarloProduct>> pricingProducts, double maturity, double periodLength) {
        AbstractLIBORMonteCarloProduct numeraireSingleCurve = new Numeraire(maturity);
        AbstractLIBORMonteCarloProduct discountSingleCurve = new DiscountFactor(maturity);
        AbstractLIBORMonteCarloProduct oisSpotRateSingleCurve = new LIBOR(maturity, periodLength);
        AbstractLIBORMonteCarloProduct numeraireMultiCurve = new Numeraire(maturity);
        AbstractLIBORMonteCarloProduct oisSpotRateMultiCurve = new Forward(maturity);
        AbstractLIBORMonteCarloProduct euriborSpotRateMultiCurve = new LIBOR(maturity, periodLength);

        put(pricingProducts, "Numeraire Single-Curve", numeraireSingleCurve);
        put(pricingProducts, "Discount Single-Curve", discountSingleCurve);
        put(pricingProducts, "OIS Spot Rate Single-Curve", oisSpotRateSingleCurve, maturity);
        put(pricingProducts, "Numeraire Multi-Curve", numeraireMultiCurve);
        put(pricingProducts, "OIS Spot Rate Multi-Curve", oisSpotRateMultiCurve, maturity);
        put(pricingProducts, "LIBOR Spot Rate Multi-Curve", euriborSpotRateMultiCurve, maturity);

        put(pricingProducts, "OIS Forward Rate Multi-Curve", new AbstractLIBORMonteCarloProduct() {
            @Override
            public RandomVariableInterface getValue(double evaluationTime, LIBORModelMonteCarloSimulationInterface model) throws CalculationException {
                MultiCurveLIBORMarketModel liborMarketModel = (MultiCurveLIBORMarketModel) model.getModel();
                return new RandomVariable(evaluationTime, liborMarketModel.getRiskFreeCurve().getForward(liborMarketModel.getAnalyticModel(), maturity));
            }
        });
        put(pricingProducts, "LIBOR Forward Rate Multi-Curve", new AbstractLIBORMonteCarloProduct() {
            @Override
            public RandomVariableInterface getValue(double evaluationTime, LIBORModelMonteCarloSimulationInterface model) throws CalculationException {
                MultiCurveLIBORMarketModel liborMarketModel = (MultiCurveLIBORMarketModel) model.getModel();
                return new RandomVariable(evaluationTime, liborMarketModel.getForwardRateCurve().getForward(liborMarketModel.getAnalyticModel(), maturity));
            }
        });
        put(pricingProducts, "Time", new AbstractLIBORMonteCarloProduct() {
            @Override
            public RandomVariableInterface getValue(double evaluationTime, LIBORModelMonteCarloSimulationInterface model) throws CalculationException {
                return new RandomVariable(0.01 * maturity);
            }
        });
    }
}