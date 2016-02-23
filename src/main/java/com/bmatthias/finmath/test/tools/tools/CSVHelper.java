package com.bmatthias.finmath.test.tools.tools;

import au.com.bytecode.opencsv.CSVReader;
import net.finmath.exception.CalculationException;
import net.finmath.interpolation.RationalFunctionInterpolation;
import net.finmath.marketdata.model.curves.ForwardCurve;
import net.finmath.marketdata.model.curves.ForwardCurveInterface;
import net.finmath.montecarlo.interestrate.LIBORMarketModelInterface;
import net.finmath.montecarlo.interestrate.MultiCurveLIBORMarketModel;
import net.finmath.montecarlo.interestrate.modelplugins.AbstractLIBORCovarianceModel;
import net.finmath.montecarlo.interestrate.modelplugins.LIBORCovarianceModelFromVolatilityAndCorrelationExtraParameters;
import net.finmath.montecarlo.process.ProcessInterface;
import net.finmath.stochastic.RandomVariableInterface;

import java.io.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.stream.IntStream;

public class CSVHelper {
    private static DecimalFormat formatterValue		= new DecimalFormat("##0.00;-##0.00", new DecimalFormatSymbols(Locale.ENGLISH));
    private static DecimalFormat formatterDeviation	= new DecimalFormat("0.0000000000E00;-0.0000000000E00", new DecimalFormatSymbols(Locale.ENGLISH));

    public static void writeParamsAndDuration(String fileName, double[] scParams, double[] mcParams, long duration, CalibrationData calibrationData) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        for(int n = 0; n < scParams.length - 1; n++) {
            stringBuilder.append("\t" + "Parameter ").append(n + 1);
        }
        for(int n = 0; n < mcParams.length - 1; n++) {
            stringBuilder.append("\t" + "Parameter ").append(n + 1);
        }
        stringBuilder.replace(0,1,"").append("\n");
        for (double p : scParams) stringBuilder.append(p).append("\t");
        for (double p : mcParams) stringBuilder.append(p).append("\t");

        try(FileWriter writer = new FileWriter(new File(fileName + "_params.csv"))) {
            writer.write(stringBuilder.substring(0, stringBuilder.length() - 1));
        }
    }

    public static void writeErrorStatistics(String fileName, LinkedHashMap<String, RandomVariableInterface[]> results) throws IOException {
        try(FileWriter max = new FileWriter(new File(fileName + "_max_errs.csv"));
            FileWriter mean = new FileWriter(new File(fileName + "_mean_errs.csv"));
            FileWriter square = new FileWriter(new File(fileName + "_square_errs.csv"));
            FileWriter normMax = new FileWriter(new File(fileName + "_normmax_errs.csv"));
            FileWriter normMean = new FileWriter(new File(fileName + "_normmean_errs.csv"));
            FileWriter normSquare = new FileWriter(new File(fileName + "_normsquare_errs.csv"))) {

            int maxProducts = 0;
            for (String productName : results.keySet()) {
                max.write("\t" + productName);
                mean.write("\t" + productName);
                square.write("\t" + productName);
                normMax.write("\t" + productName);
                normMean.write("\t" + productName);
                normSquare.write("\t" + productName);
                maxProducts = Math.max(results.get(productName).length, maxProducts);
            }
            max.write("\n");
            mean.write("\n");
            square.write("\n");
            normMax.write("\n");
            normMean.write("\n");
            normSquare.write("\n");

            for (String productName1 : results.keySet()) {
                max.write(productName1 + "\t");
                mean.write(productName1 + "\t");
                square.write(productName1 + "\t");
                normMax.write(productName1 + "\t");
                normMean.write(productName1 + "\t");
                normSquare.write(productName1 + "\t");
                RandomVariableInterface[] productValues1 = results.get(productName1);
                for (String productName2 : results.keySet()) {
                    RandomVariableInterface[] productValues2 = results.get(productName2);

                    double maxDeviation = 0.0, meanAbsDeviation = 0.0, meanSquareDeviation = 0.0, meanMeasurement = 0.0;

                    for(int productIndex = 0; productIndex < maxProducts; productIndex++) {
                        double deviation = Math.abs(productValues1[productIndex].getAverage() - productValues2[productIndex].getAverage());

                        meanMeasurement += productValues1[productIndex].getAverage() + productValues2[productIndex].getAverage();

                        maxDeviation = Math.max(deviation, maxDeviation);
                        meanAbsDeviation += deviation;
                        meanSquareDeviation += deviation * deviation;
                    }

                    max.write(formatterDeviation.format(maxDeviation) + "\t");
                    normMax.write(formatterDeviation.format(maxDeviation / (meanMeasurement / 2.0 / maxProducts)) + "\t");
                    mean.write(formatterDeviation.format(meanAbsDeviation / maxProducts) + "\t");
                    normMean.write(formatterDeviation.format(meanAbsDeviation / maxProducts / (meanMeasurement / 2.0 / maxProducts)) + "\t");
                    square.write(formatterDeviation.format(Math.sqrt(meanSquareDeviation / maxProducts)) + "\t");
                    normSquare.write(formatterDeviation.format(Math.sqrt(meanSquareDeviation / maxProducts) / (meanMeasurement / 2.0 / maxProducts)) + "\t");
                }
                max.write("\n");
                mean.write("\n");
                square.write("\n");
                normMax.write("\n");
                normMean.write("\n");
                normSquare.write("\n");
            }
        }
    }

    public static void writeValues(String fileName, LinkedHashMap<String, RandomVariableInterface[]> results) throws Exception {
        try(FileWriter writer = new FileWriter(new File(fileName + "_results.csv"));
            FileWriter shortWriter = new FileWriter(new File(fileName + "_short.csv"));
            FileWriter standardDeviationWriter = new FileWriter(new File(fileName + "_standardDeviation.csv"))) {
            int maxProducts = 0;
            for (String productName : results.keySet()) {
                writer.write(productName + "\t" + productName + " Error" + "\t");
                shortWriter.write(productName + "\t");
                standardDeviationWriter.write(productName + "\t");
                maxProducts = Math.max(results.get(productName).length, maxProducts);
            }
            writer.write("\n");
            shortWriter.write("\n");
            standardDeviationWriter.write("\n");

            for(int productIndex = 0; productIndex < maxProducts; productIndex++) {
                for (String productName : results.keySet()) {
                    double value = 100.0 * results.get(productName)[productIndex].getAverage();
                    double standardDeviation = 2.0 * results.get(productName)[productIndex].getStandardDeviation();
                    writer.write(value + "\t" + standardDeviation + "\t");
                    standardDeviationWriter.write(standardDeviation + "\t");
                    shortWriter.write(formatterValue.format(Math.signum(value) * Math.min(Math.abs(value), 99999)) + "\t");
                }
                writer.write("\n");
                shortWriter.write("\n");
                standardDeviationWriter.write("\n");
            }
        }
    }

    public static void writeCorrelationMatrix(String fileName, LIBORCovarianceModelFromVolatilityAndCorrelationExtraParameters model) throws Exception {
        try (FileWriter writer = new FileWriter(new File(fileName + "_correlation.csv"))) {
            writer.write("Component1\tComponent2\tCorrelation\n");
            for(int component1 = 0; component1 < 2 * model.getLiborPeriodDiscretization().getNumberOfTimeSteps(); component1++) {
                for(int component2 = 0; component2 < 2 * model.getLiborPeriodDiscretization().getNumberOfTimeSteps(); component2++) {
                    writer.write(component1 + "\t" + component2 + "\t" + String.valueOf(model.getCorrelationModel().getCorrelation(0, component1, component2)) + "\n");
                }
            }
        }
    }

    public static void writeCovariance(String fileName, AbstractLIBORCovarianceModel model) throws Exception {
        try (FileWriter writer = new FileWriter(new File(fileName + "_covariance.csv"))) {
            writer.write("Component1\tComponent2\tCovariance\n");
            for(int component1 = 0; component1 < 2 * model.getLiborPeriodDiscretization().getNumberOfTimeSteps(); component1++) {
                for(int component2 = 0; component2 < 2 * model.getLiborPeriodDiscretization().getNumberOfTimeSteps(); component2++) {
                    writer.write(component1 + "\t" + component2 + "\t" + String.valueOf(model.getCovariance(0.0, component1, component2, null).getAverage()) + "\n");
                }
            }
        }
    }

    public static void writeVolatilitySurface(String fileName, AbstractLIBORCovarianceModel model) throws Exception {
        int times = model.getLiborPeriodDiscretization().getNumberOfTimes();
        try (FileWriter writer = new FileWriter(new File(fileName + "_volatility.csv"))) {
            writer.write("Time\tComponent\tVolatility OIS\tVolatility Spread\n");
            for(int timeIndex = 0; timeIndex < times; timeIndex++) {
                for(int component = 0; component < times - 1; component++) {
                    writer.write(timeIndex + "\t" + component + "\t"
                            + String.valueOf(Math.sqrt(model.getCovariance(timeIndex, component, component, null).getAverage())) + "\t"
                            + String.valueOf(Math.sqrt(model.getCovariance(timeIndex, component + times - 1, component + times - 1, null).getAverage())) + "\n");
                }
            }
        }
    }

    public static void printProcess(ProcessInterface process, LIBORMarketModelInterface model) throws CalculationException {
        int numberOfComponents = model.getNumberOfComponents();
        int numberOfTimes = process.getTimeDiscretization().getNumberOfTimes();
        if (model instanceof MultiCurveLIBORMarketModel) {
            for (int component = 0; component < numberOfComponents / 2; component++) {
                for (int timeIndex = 0; timeIndex < numberOfTimes &&
                        process.getTime(timeIndex) < model.getLiborPeriodDiscretization().getTime(component); timeIndex++) {
                    RandomVariableInterface forward = process.getProcessValue(timeIndex, component);
                    RandomVariableInterface spread = process.getProcessValue(timeIndex, component + numberOfComponents / 2);
                    try {
                        System.out.println(process.getTime(timeIndex) +
                                "\t" + forward.getAverage() +
                                "\t" + spread.getAverage() +
                                "\t" + ((MultiCurveLIBORMarketModel) model).getProcessValue(timeIndex, component).getAverage() +
                                "\t" + model.getNumeraire(process.getTime(timeIndex + 1)).getAverage() +
                                "\t" + ((MultiCurveLIBORMarketModel) model).getProcessValue(timeIndex, component)
                                .div(model.getNumeraire(process.getTime(timeIndex + 1)))
                                .div(model.getDiscountCurve().getDiscountFactor(process.getTime(timeIndex + 1))).getAverage());
                    } catch (CalculationException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println();
            }
        } else {
            for (int timeIndex = 0; timeIndex < numberOfComponents; timeIndex++) {
                RandomVariableInterface forward = process.getProcessValue(timeIndex, timeIndex);
                try {
                    System.out.println(process.getTime(timeIndex) +
                            "\t" + forward.getAverage() +
                            "\t" + model.getNumeraire(process.getTime(timeIndex + 1)).getAverage() +
                            "\t" + forward
                            .div(model.getNumeraire(process.getTime(timeIndex + 1)))
                            .div(model.getDiscountCurve().getDiscountFactor(process.getTime(timeIndex + 1))).getAverage());
                } catch (CalculationException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void writeProcess(String fileName, MultiCurveLIBORMarketModel model) throws Exception {
        try (FileWriter writer = new FileWriter(new File(fileName + "_process.csv"))) {
            writer.write("Time\tF\tS\tL\tN\tE(L/N)");
            int numberOfComponents = model.getNumberOfComponents();
            for (int component = 0; component < numberOfComponents / 2; component++) {
                writer.write("\n" + model.getProcess().getTime(component) +
                        "\t" + model.getForward(component, component).getAverage() +
                        "\t" + model.getProcess().getProcessValue(component, component + numberOfComponents / 2).getAverage() +
                        "\t" + model.getLIBOR(component, component).getAverage() +
                        "\t" + model.getNumeraire(model.getProcess().getTime(component + 1)).getAverage() +
                        "\t" + model.getLIBOR(component, component)
                        .div(model.getNumeraire(model.getProcess().getTime(component + 1)))
                        .div(model.getDiscountCurve().getDiscountFactor(model.getProcess().getTime(component + 1))).getAverage());
            }
        }
    }

    private static void serialFileToCsv(String fileName) throws IOException {
        String[] input = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(new File(fileName)))) {
            input = reader.readLine().split(" ");
        }
        try (FileWriter fileWriter = new FileWriter(new File(fileName))) {
            for (int i = 0; i < input.length - 1; i+=2) {
                fileWriter.write(input[i].trim() + "\t" + input[i+1].trim() + "\n");
            }
        }
    }

    private static void discountsToForwards(String inputFile, String outputFile) throws IOException {
        List<Double> dates = new ArrayList<>();
        List<Double> forwards = new ArrayList<>();
        try (FileReader reader = new FileReader(new File(inputFile))){
            CSVReader csvReader = new CSVReader(reader);

            String[] line;
            while ((line = csvReader.readNext()) != null) {
                dates.add(Double.parseDouble(line[0]));
                forwards.add(Double.parseDouble(line[1]));
            }
        }

        ForwardCurveInterface forwardCurve = ForwardCurve.createForwardCurveFromDiscountFactors("",
                dates.stream().mapToDouble(Double::doubleValue).toArray(),
                forwards.stream().mapToDouble(Double::doubleValue).toArray(),
                0.5, "DiscountCurve"
        );

        try (FileWriter fileWriter = new FileWriter(new File(outputFile))) {
            for (Double date : dates) {
                fileWriter.write(date + "," + 100.0 * forwardCurve.getForward(null, date) + "\n");
            }
        }
    }

    public static ForwardCurveInterface forwardCurveFromForwards(String fileName, double liborPeriodLength, double liborRateTimeHorzion) throws IOException {
        List<Double> dates = new ArrayList<>();
        List<Double> forwards = new ArrayList<>();
        try (Reader reader = new InputStreamReader(CSVHelper.class.getResourceAsStream("/curves/" + fileName + ".csv"))){
            CSVReader csvReader = new CSVReader(reader);

            String[] line;
            while ((line = csvReader.readNext()) != null) {
                dates.add(Double.parseDouble(line[0]));
                forwards.add(0.01 * Double.parseDouble(line[1]));
            }
        }
        RationalFunctionInterpolation interpolation = new RationalFunctionInterpolation(
                dates.stream().mapToDouble(Double::doubleValue).toArray(),
                forwards.stream().mapToDouble(Double::doubleValue).toArray(),
                RationalFunctionInterpolation.InterpolationMethod.AKIMA,
                RationalFunctionInterpolation.ExtrapolationMethod.DEFAULT
        );
        double[] fixingTimes = IntStream.range(0,(int)(liborRateTimeHorzion/liborPeriodLength)).mapToDouble((i) -> liborPeriodLength * i).toArray();
        double[] interpolatedRates = IntStream.range(0,(int)(liborRateTimeHorzion/liborPeriodLength)).mapToDouble((i) -> interpolation.getValue(liborPeriodLength * i)).toArray();

        return ForwardCurve.createForwardCurveFromForwards(
                fileName.split("_")[0] + "_" + fileName.split("_")[1],
                "DiscountCurve",
                fixingTimes,
                interpolatedRates,
                liborPeriodLength
        );
    }
}
