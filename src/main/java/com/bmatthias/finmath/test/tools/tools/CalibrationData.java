package com.bmatthias.finmath.test.tools.tools;

import net.finmath.marketdata.model.AnalyticModelInterface;
import net.finmath.montecarlo.interestrate.LIBORMarketModelInterface;
import net.finmath.montecarlo.interestrate.products.AbstractLIBORMonteCarloProduct;
import net.finmath.time.TimeDiscretizationInterface;

import java.util.LinkedHashMap;
import java.util.List;

public class CalibrationData {
    private TimeDiscretizationInterface timeDiscretization;
    private  TimeDiscretizationInterface periodDiscretization;
    private AnalyticModelInterface analyticModel;
    private  String firstCurveName;
    private  String secondCurveName;
    private  LIBORMarketModelInterface.CalibrationItem[] firstCurveCalibrationItems;
    private  LIBORMarketModelInterface.CalibrationItem[] secondCurveCalibrationItems;
    private LinkedHashMap<String, List<AbstractLIBORMonteCarloProduct>> pricingProducts;

    public CalibrationData(TimeDiscretizationInterface timeDiscretization,
                           TimeDiscretizationInterface periodDiscretization,
                           AnalyticModelInterface analyticModel,
                           String firstCurveName,
                           String secondCurveName,
                           LIBORMarketModelInterface.CalibrationItem[] firstCurveCalibrationItems,
                           LIBORMarketModelInterface.CalibrationItem[] secondCurveCalibrationItems,
                           LinkedHashMap<String, List<AbstractLIBORMonteCarloProduct>> pricingProducts) {
        this.timeDiscretization = timeDiscretization;
        this.periodDiscretization = periodDiscretization;
        this.analyticModel = analyticModel;
        this.firstCurveName = firstCurveName;
        this.secondCurveName = secondCurveName;
        this.firstCurveCalibrationItems = firstCurveCalibrationItems;
        this.secondCurveCalibrationItems = secondCurveCalibrationItems;
        this.pricingProducts = pricingProducts;
    }

    public TimeDiscretizationInterface getTimeDiscretization() {
        return timeDiscretization;
    }

    public TimeDiscretizationInterface getPeriodDiscretization() {
        return periodDiscretization;
    }

    public AnalyticModelInterface getAnalyticModel() {
        return analyticModel;
    }

    public String getSecondCurveName() {
        return secondCurveName;
    }

    public String getFirstCurveName() {
        return firstCurveName;
    }

    public LIBORMarketModelInterface.CalibrationItem[] getFirstCurveCalibrationItems() {
        return firstCurveCalibrationItems;
    }

    public LIBORMarketModelInterface.CalibrationItem[] getSecondCurveCalibrationItems() {
        return secondCurveCalibrationItems;
    }

    public LinkedHashMap<String, List<AbstractLIBORMonteCarloProduct>> getPricingProducts() {
        return pricingProducts;
    }
}
