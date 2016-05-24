package com.bmatthias.finmath.test.tools.tools;

import au.com.bytecode.opencsv.CSVReader;

import java.io.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;

public class TexCreator {
    private static DecimalFormat formatterParam = new DecimalFormat("##0.0;-##0.0", new DecimalFormatSymbols(Locale.ENGLISH));

    public static void createPlot(double yMin, double yMax, String xLabel, String yLabel, String caption, List<String[]> filesToProducts, String fileName) throws IOException {
        StringBuilder plots = new StringBuilder();
        StringBuilder productString = new StringBuilder();
        for (int i = 0; i < filesToProducts.size(); i++) {
            String[] entry = filesToProducts.get(i);
            String product = entry[1];
            String legendEntry = entry[2];
            String fileToPlot = entry[0];

            String xColumn = fileToPlot.contains("swaption") ? "=Time" : "=Time";
            productString.append(legendEntry).append(",");
            plots.append("\\\\addplot+[only marks,error bars/.cd,y dir=both,y explicit,error bar style={line width=0.5pt}]" + "[smooth] \n")
                    .append("        table[x").append(xColumn).append(",y=productName,y error=productName Error,col sep=tab] {fileName_results.csv};\n"
                    .replaceAll("fileName", fileToPlot)
                    .replaceAll("productName", product)).append("\n");
        }

        writePlot(yMin, yMax, xLabel, yLabel, caption, fileName, plots.toString(), productString.substring(0, productString.length() - 1));
    }

    public static void createPlotWithErrors(double yMin, double yMax, String xLabel, String yLabel, String caption, List<String[]> filesToProducts, String fileName) throws IOException {
        StringBuilder valuePlots = new StringBuilder();
        StringBuilder errorPlots = new StringBuilder();
        StringBuilder productString = new StringBuilder();
        for (int i = 0; i < filesToProducts.size(); i++) {
            String[] entry = filesToProducts.get(i);
            String product = entry[1];
            String legendEntry = entry[2];
            String fileToPlot = entry[0];

            String xColumn = fileToPlot.contains("swaption") ? "=Time" : "=Time";
            productString.append(legendEntry).append(",");
            valuePlots.append("\\\\addplot+[only marks,error bars/.cd,y dir=both,y explicit,error bar style={line width=0.5pt}]" + "[smooth] \n")
                    .append("        table[x").append(xColumn).append(",y=productName,y error=productName Error,col sep=tab] {fileName_results.csv};\n"
                    .replaceAll("fileName", fileToPlot)
                    .replaceAll("productName", product)).append("\n");

            if(i > 0) errorPlots.append("\\\\addplot+[only marks,error bars/.cd,y dir=both,y explicit,error bar style={line width=0.5pt}]" + "[smooth] \n")
                    .append("        table[x").append(xColumn).append(",y expr=abs(\\\\thisrow{productName} - \\\\thisrow{referenceProduct})/abs(\\\\thisrow{referenceProduct}),y error=productName Error,col sep=tab] {fileName_results.csv};\n"
                    .replaceAll("fileName", fileToPlot)
                    .replaceAll("referenceProduct", filesToProducts.get(0)[1])
                    .replaceAll("productName", product)).append("\n");
        }

        writePlotWithErrors(yMin, yMax, xLabel, yLabel, caption, fileName, valuePlots.toString(), errorPlots.toString(), productString.substring(0, productString.length() - 1));
    }

    public static void writePlot(double yMin, double yMax, String xLabel, String yLabel, String caption, String fileName, String plots, String productString) throws IOException {
        try (Reader fileReader = new InputStreamReader(TexCreator.class.getResourceAsStream("/templates/plot_template.tex"));
             BufferedReader reader = new BufferedReader(fileReader);
             Writer writer = new FileWriter(fileName + "_plot.tex")
        ) {
            reader.lines().forEach(line -> {
                        try {
                            writer.write(line
                                    .replaceAll("plotsArea", plots)
                                    .replaceAll("productString", productString)
                                    .replaceAll("yMin", String.valueOf(yMin))
                                    .replaceAll("yMax", String.valueOf(yMax))
                                    .replaceAll("xLabel", xLabel)
                                    .replaceAll("yLabel", yLabel)
                                    .replaceAll("Caption", caption)+ "\n"
                            );
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
            );
        }
    }

    public static void writePlotWithErrors(double yMin, double yMax, String xLabel, String yLabel, String caption, String fileName, String valuePlots, String errorPlots, String productString) throws IOException {
        try (Reader fileReader = new InputStreamReader(TexCreator.class.getResourceAsStream("/templates/plot_with_errors_template.tex"));
             BufferedReader reader = new BufferedReader(fileReader);
             Writer writer = new FileWriter(fileName + "_plot.tex")
        ) {
            reader.lines().forEach(line -> {
                        try {
                            writer.write(line
                                    .replaceAll("plotsArea", valuePlots)
                                    .replaceAll("errorPlotsArea", errorPlots)
                                    .replaceAll("productString", productString)
                                    .replaceAll("yMin", String.valueOf(yMin))
                                    .replaceAll("yMax", String.valueOf(yMax))
                                    .replaceAll("xLabel", xLabel)
                                    .replaceAll("yLabel", yLabel)
                                    .replaceAll("Caption", caption)+ "\n"
                            );
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
            );
        }
    }

    public static void createErrorPlot(String caption, Map<String, double[][]> errorsMap, String fileName) throws IOException {
        StringBuilder maxErrorCoordinates = new StringBuilder();
        StringBuilder meanErrorCoordinates = new StringBuilder();
        StringBuilder squaredErrorCoordinates = new StringBuilder();
        StringBuilder maxErrorCoordinatesMax = new StringBuilder();
        StringBuilder meanErrorCoordinatesMax = new StringBuilder();
        StringBuilder squaredErrorCoordinatesMax = new StringBuilder();
        StringBuilder maxErrorCoordinatesMin = new StringBuilder();
        StringBuilder meanErrorCoordinatesMin = new StringBuilder();
        StringBuilder squaredErrorCoordinatesMin = new StringBuilder();
        StringBuilder testTypes = new StringBuilder();
        for (Map.Entry<String, double[][]> entry : errorsMap.entrySet()) {
            maxErrorCoordinates.append("(").append(entry.getKey()).append(",").append(entry.getValue()[0][0]).append(") ");
            meanErrorCoordinates.append("(").append(entry.getKey()).append(",").append(entry.getValue()[1][0]).append(") ");
            squaredErrorCoordinates.append("(").append(entry.getKey()).append(",").append(entry.getValue()[2][0]).append(") ");

            maxErrorCoordinatesMax.append("(").append(entry.getKey()).append(",").append(entry.getValue()[0][1]).append(") ");
            meanErrorCoordinatesMax.append("(").append(entry.getKey()).append(",").append(entry.getValue()[1][1]).append(") ");
            squaredErrorCoordinatesMax.append("(").append(entry.getKey()).append(",").append(entry.getValue()[2][1]).append(") ");

            maxErrorCoordinatesMin.append("(").append(entry.getKey()).append(",").append(entry.getValue()[0][2]).append(") ");
            meanErrorCoordinatesMin.append("(").append(entry.getKey()).append(",").append(entry.getValue()[1][2]).append(") ");
            squaredErrorCoordinatesMin.append("(").append(entry.getKey()).append(",").append(entry.getValue()[2][2]).append(") ");

            testTypes.append(entry.getKey()).append(",");
        }

        try (Reader fileReader = new InputStreamReader(TexCreator.class.getResourceAsStream("/templates/errs_template.tex"));
             BufferedReader reader = new BufferedReader(fileReader);
             Writer writer = new FileWriter(fileName + "_errs.tex")) {
            int barWidth = 96 / errorsMap.size();
            reader.lines().forEach(
                    line -> {
                        try {
                            writer.write(line
                                    .replaceAll("testTypes", testTypes.substring(0, testTypes.length() - 1))
                                    .replaceAll("Caption", caption)
                                    .replaceAll("maxErrorCoordinates", maxErrorCoordinates.toString())
                                    .replaceAll("meanErrorCoordinates", meanErrorCoordinates.toString())
                                    .replaceAll("squaredErrorCoordinates", squaredErrorCoordinates.toString())
                                    .replaceAll("maxErrorMaxCoordinates", maxErrorCoordinatesMax.toString())
                                    .replaceAll("meanErrorMaxCoordinates", meanErrorCoordinatesMax.toString())
                                    .replaceAll("squaredErrorMaxCoordinates", squaredErrorCoordinatesMax.toString())
                                    .replaceAll("maxErrorMinCoordinates", maxErrorCoordinatesMin.toString())
                                    .replaceAll("meanErrorMinCoordinates", meanErrorCoordinatesMin.toString())
                                    .replaceAll("squaredErrorMinCoordinates", squaredErrorCoordinatesMin.toString())
                                    .replaceAll("numberOfTests", String.valueOf(errorsMap.size() -1))
                                    .replaceAll("barWidth", String.valueOf(barWidth))+ "\n"
                            );
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
            );
        }
    }

    public static void createParamsTable(String fileOrDirectoryName, String testName) throws IOException {
        File fileOrDirectory = new File(fileOrDirectoryName);
        try(FileWriter writer = new FileWriter(testName + "_params.tex")) {
            if (fileOrDirectory.isDirectory()) {
                FilenameFilter filter = (dir, name) -> name.endsWith("_params.csv");
                File[] files = fileOrDirectory.listFiles(filter);
                if (files.length == 0) {
                    List<File> filesRecursive = new ArrayList<>();
                    for (File file : fileOrDirectory.listFiles()) {
                        filesRecursive.addAll(Arrays.asList(file.listFiles(filter)));
                    }
                    files = filesRecursive.toArray(new File[0]);
                }

                StringBuilder definitionVola = new StringBuilder();
                StringBuilder definitionCorr = new StringBuilder();
                StringBuilder headerVola = new StringBuilder();
                StringBuilder headerCorr = new StringBuilder();
                StringBuilder valuesVola = new StringBuilder();
                StringBuilder valuesCorr = new StringBuilder();
                String[] columns;

                try (FileReader reader = new FileReader(files[0])){
                    CSVReader csvReader = new CSVReader(reader, '\t');
                    columns = csvReader.readNext();

                    for (int i = 0; i < columns.length; i++) {
                        String columnName = columns[i];
                        if (columnName.contains("Parameter")) {
                            int n = Integer.parseInt(columnName.replace("Parameter", "").trim());
                            if (n <= 4) {
                                String curve = i <= 4 ? "^F" : "^S";
                                definitionVola.append(" l |");
                                headerVola.append("$v").append(curve).append("_{").append(n).append("}$ & ");
                            } else {
                                String curve = i <= 7 ? "^F" : "^S";
                                definitionCorr.append(" l |");
                                headerCorr.append("$r").append(curve).append("_{").append(n - 4).append("}$ & ");
                            }
                        } else {
                            headerCorr.append(columnName).append(" & ");
                        }
                    }

                    definitionVola.append("}\\hline\n");
                    definitionCorr.append("}\\hline\n");
                }

                for (File file : files) {
                    try (FileReader reader = new FileReader(file)) {
                        CSVReader csvReader = new CSVReader(reader, '\t');
                        csvReader.readNext();
                        String[] readNext = csvReader.readNext();
                        StringBuilder valuesVolaFile = new StringBuilder();
                        StringBuilder valuesCorrFile = new StringBuilder();
                        for (int i = 0; i < columns.length; i++) {
                            String value = readNext[i];
                            double doubleValue = 0.0;
                            try {
                                doubleValue = Double.parseDouble(value);
                            } catch (Exception e) {}
                            int n = Integer.parseInt(columns[i].replace("Parameter", "").trim());
                            if (n <= 4) {
                                valuesVolaFile.append(formatterParam.format(doubleValue)).append(" & ");
                            } else {
                                valuesCorrFile.append(formatterParam.format(doubleValue)).append(" & ");
                            }
                        }
                        valuesVola.append(valuesVolaFile.substring(0, valuesVolaFile.length() - 2)).append("\\\\\\hline\n");
                        valuesCorr.append(valuesCorrFile.substring(0, valuesCorrFile.length() - 2)).append("\\\\\\hline\n");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                writer.write("\\begin{center}\n\t\\begin{tabular}{|");
                writer.write(definitionVola.toString());
                writer.write(headerVola.substring(0, headerVola.length() - 2) + "\\\\\\hline\n");
                writer.write(valuesVola.toString());
                writer.write("\t\\end{tabular}\n\\end{center}");
                writer.write("\\captionof{table}{Table of volatility parameters.}");
                writer.write("\n");
                writer.write("\\begin{center}\n\t\\begin{tabular}{|");
                writer.write(definitionCorr.toString());
                writer.write(headerCorr.substring(0, headerCorr.length() - 2) + "\\\\\\hline\n");
                writer.write(valuesCorr.toString());
                writer.write("\t\\end{tabular}\n\\end{center}");
                writer.write("\\captionof{table}{Table of correlation parameters.}");
            } else {
                try (FileReader reader = new FileReader(fileOrDirectory)){
                    CSVReader csvReader = new CSVReader(reader, '\t');
                    String[] columns = csvReader.readNext();
                    for (String columnName : columns) {
                        writer.write(" l |");
                    }
                    writer.write("}\n\t\\hline");
                    StringBuilder line = new StringBuilder();
                    for (String columnName : columns) {
                        if (columnName.contains("Parameter")) {
                            int n = Integer.parseInt(columnName.replace("Parameter","").trim());
                            if (n <= 4) {
                                line.append("$v_{").append(n).append("}$ & ");
                            } else {
                                line.append("$r_{").append(n-4).append("}$ & ");
                            }
                        } else {
                            line.append(columnName).append(" & ");
                        }
                    }
                    writer.write(line.substring(0, line.length() - 2) + "\\\\\\hline\n");
                    line = new StringBuilder();
                    for (String value : csvReader.readNext()) {
                        double doubleValue = 0.0;
                        try {
                            doubleValue = Double.parseDouble(value);
                        } catch (Exception e) {}
                        line.append(formatterParam.format(doubleValue)).append(" & ");
                    }
                    writer.write(line.substring(0, line.length() - 2) + "\\\\\\hline\n");
                }
            }

        }
    }

    public static void generateStats() throws IOException {
        for (File params : new File("csv").listFiles()) {
            if (params.getName().contains("_params")) {
                try (FileReader paramsFileReader = new FileReader(params);
                     Reader fileReader = new InputStreamReader(TexCreator.class.getResourceAsStream("/templates/stats_template.tex"));
                     BufferedReader reader = new BufferedReader(fileReader);
                     Writer writer = new FileWriter(params.getAbsolutePath().replace("_params", "").replace("csv", "tex"))
                ) {
                    CSVReader paramsReader = new CSVReader(paramsFileReader);
                    paramsReader.readNext();
                    String calibrationTime = paramsReader.readNext()[0].split("\t")[0];

                    reader.lines().forEach(
                            line -> {
                                try {
                                    writer.write(line
                                                    .replaceAll("calibrationTime", calibrationTime)
                                                    .replaceAll("fileName", params.getName().replace("_params", "_errs")) + "\n"
                                    );
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                    );
                }
            }
        }
    }

    public static void generatePlots() throws IOException {
        for (File params : new File("csv").listFiles()) {
            if (params.getName().contains("_params")) {
                try (FileReader paramsFileReader = new FileReader(params);
                     Reader fileReader = new InputStreamReader(TexCreator.class.getResourceAsStream("/templates/plots_template.tex"));
                     BufferedReader reader = new BufferedReader(fileReader);
                     Writer writer = new FileWriter(params.getAbsolutePath().replace("_params", "").replace("csv", "tex"))
                ) {
                    CSVReader paramsReader = new CSVReader(paramsFileReader);
                    paramsReader.readNext();
                    String calibrationTime = paramsReader.readNext()[0].split("\t")[0];

                    reader.lines().forEach(
                            line -> {
                                try {
                                    writer.write(line
                                                    .replaceAll("calibrationTime", calibrationTime)
                                                    .replaceAll("fileName", params.getName().replace("_params", "_errs")) + "\n"
                                    );
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                    );
                }
            }
        }
    }

    public static void generateErrorPlot(String caption, Map<String, Object[]> testToFileMap, String fileName) throws IOException {
        Map<String, double[][]> errorsMap = new LinkedHashMap<>();

        for (Map.Entry<String, Object[]> entry : testToFileMap.entrySet()) {
            double[][] errors = new double[3][3];
            errorsMap.put(entry.getKey(), errors);

            File fileOrDirectory = new File(entry.getValue()[0].toString());

            if (fileOrDirectory.isDirectory()) {
                FilenameFilter filter = (dir, name) -> name.endsWith("_results.csv");
                File[] individualTests = fileOrDirectory.listFiles(filter);
                for (File file : individualTests) {
                    aggregateErrors(file.getPath().replace("_results.csv",""), entry, errors);
                }
                for(int n = 0; n < 3; n++) {
                    errors[n][0] /= individualTests.length;
                }
            } else {
                aggregateErrors(entry.getValue()[0].toString(), entry, errors);
            }
        }

        createErrorPlot(caption, errorsMap, fileName);
    }

    private static void aggregateErrors(String fileName, Map.Entry<String, Object[]> entry, double[][] errors) throws IOException {
        String[] errorTypeFileSuffixes = new String[] { "_max_errs.csv", "_mean_errs.csv", "_square_errs.csv" };

        for (int n = 0; n < 3; n++) {
            String fileToRead = fileName + errorTypeFileSuffixes[n];
            try (BufferedReader fileReader = new BufferedReader(new FileReader(fileToRead))) {
                CSVReader csvReader = new CSVReader(fileReader, '\t');

                String product1 = ((String[])entry.getValue()[1])[0];
                String product2 = ((String[])entry.getValue()[1])[1];
                try {
                    String[] line = csvReader.readNext();
                    for(int productIndex = 0; productIndex < line.length; productIndex++) {
                        if (product1.equals(line[productIndex])) {
                            while ((line = csvReader.readNext()) != null) {
                                if(product2.equals(line[0])) break;
                            }

                            double error = Double.MAX_VALUE;
                            try {
                                error = Double.parseDouble(line[productIndex]);
                            } catch (Exception e) {}
                            errors[n][0] += error;
                            errors[n][1] = Math.max(errors[n][1], error);
                            errors[n][2] = errors[n][2] == 0.0 ? error : Math.min(errors[n][2], error);
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        String fileName = null;
        Map<String, Object[]> testToFileMap = new LinkedHashMap<>();
        String[] comparison1 = new String[0];
        String[] comparison2 = new String[0];
        List<String[]> filesToProducts = new ArrayList<>();
        try {
            fileName = "caplet_calibration_targets";

            filesToProducts = new ArrayList<>();

            filesToProducts.add(new String[] { "csv/convergence_low_vol_high_interest/INTEGRATED_EXPECTATIONM1x100015P10trueC0303ADDITIVE2010_1/initial_calibration", "Target (OIS)", "Low Volatility" });
            filesToProducts.add(new String[] { "csv/convergence_mid_vol_low_interest/INTEGRATED_EXPECTATIONM1x100015P10trueC0606ADDITIVE2013_1/initial_calibration", "Target (OIS)", "Medium Volatility" });
            filesToProducts.add(new String[] { "csv/convergence_high_vol_negative_interest/INTEGRATED_EXPECTATIONM1x100015P10trueC0909ADDITIVE2015_1/initial_calibration", "Target (OIS)", "High Volatility" });
            //filesToProducts.add(new String[] { "csv/monte_carlo_paths_comparison/capletsM1x100015P10trueC0909F1010L2010ADDITIVE_1/initial_calibration", "Target (OIS)", "" });

            createPlot(10, 110,
                    "Caplet Maturity [years]",
                    "Caplet Volatility [\\\\%]",
                    "Target caplet volatilities in different test scenarios.",
                    filesToProducts, fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fileName = "ois_curves";

            filesToProducts = new ArrayList<>();

            filesToProducts.add(new String[] { "csv/convergence_low_vol_high_interest/INTEGRATED_EXPECTATIONM1x100015P10trueC0303ADDITIVE2010_1/initial_calibration", "OIS Forward Rate Multi-Curve", "OIS High" });
            filesToProducts.add(new String[] { "csv/convergence_mid_vol_low_interest/INTEGRATED_EXPECTATIONM1x100015P10trueC0606ADDITIVE2013_1/initial_calibration", "OIS Forward Rate Multi-Curve", "OIS Low" });
            filesToProducts.add(new String[] { "csv/convergence_high_vol_negative_interest/INTEGRATED_EXPECTATIONM1x100015P10trueC0909ADDITIVE2015_1/initial_calibration", "OIS Forward Rate Multi-Curve", "OIS Negative" });
            filesToProducts.add(new String[] { "csv/convergence_low_vol_high_interest/INTEGRATED_EXPECTATIONM1x100015P10trueC0303ADDITIVE2010_1/initial_calibration", "LIBOR Forward Rate Multi-Curve", "IBOR High" });
            filesToProducts.add(new String[] { "csv/convergence_mid_vol_low_interest/INTEGRATED_EXPECTATIONM1x100015P10trueC0606ADDITIVE2013_1/initial_calibration", "LIBOR Forward Rate Multi-Curve", "IBOR Low" });
            filesToProducts.add(new String[] { "csv/convergence_high_vol_negative_interest/INTEGRATED_EXPECTATIONM1x100015P10trueC0909ADDITIVE2015_1/initial_calibration", "LIBOR Forward Rate Multi-Curve", "IBOR Negative" });

            createPlot(-0.5, 5.0,
                    "Time [years]",
                    "Forward Rate [\\\\%]",
                    "Different forward rates test scenarios.",
                    filesToProducts, fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fileName = "ois_forward_vs_spot_single_curve_low_vol_high_interest";

            createParamsTable("csv/convergence_low_vol_high_interest/INTEGRATED_EXPECTATIONM1x100015P40trueC0303ADDITIVE2010_1", fileName);

            testToFileMap = new LinkedHashMap<>();

            comparison1 = new String[]{ "OIS Forward Rate Multi-Curve", "OIS Spot Rate Single-Curve" };

            testToFileMap.put("1x1000", new Object[] { "csv/convergence_low_vol_high_interest/INTEGRATED_EXPECTATIONM1x100015P40trueC0303ADDITIVE2010_1", comparison1 });
            testToFileMap.put("1x10000", new Object[] { "csv/convergence_low_vol_high_interest/INTEGRATED_EXPECTATIONM1x1000015P40trueC0303ADDITIVE2010_1", comparison1 });
            testToFileMap.put("1x100000", new Object[] { "csv/convergence_low_vol_high_interest/INTEGRATED_EXPECTATIONM1x10000015P40trueC0303ADDITIVE2010_1", comparison1 });
            testToFileMap.put("10x100000", new Object[] { "csv/convergence_low_vol_high_interest/INTEGRATED_EXPECTATIONM10x10000015P40trueC0303ADDITIVE2010_1", comparison1 });

            generateErrorPlot("Errors of Monte-Carlo valuation of single-curve forward rates vs. their expected values.", testToFileMap, fileName);

            filesToProducts = new ArrayList<>();

            filesToProducts.add(new String[] { "csv/convergence_low_vol_high_interest/INTEGRATED_EXPECTATIONM1x100015P40trueC0303ADDITIVE2010_1/initial_calibration", "OIS Forward Rate Multi-Curve", "Reference" });
            filesToProducts.add(new String[] { "csv/convergence_low_vol_high_interest/INTEGRATED_EXPECTATIONM1x100015P40trueC0303ADDITIVE2010_1/initial_calibration", "OIS Spot Rate Single-Curve", "1x1000" });
            filesToProducts.add(new String[] { "csv/convergence_low_vol_high_interest/INTEGRATED_EXPECTATIONM1x1000015P40trueC0303ADDITIVE2010_1/initial_calibration", "OIS Spot Rate Single-Curve", "1x10000" });
            filesToProducts.add(new String[] { "csv/convergence_low_vol_high_interest/INTEGRATED_EXPECTATIONM1x10000015P40trueC0303ADDITIVE2010_1/initial_calibration", "OIS Spot Rate Single-Curve", "1x100000" });
            filesToProducts.add(new String[] { "csv/convergence_low_vol_high_interest/INTEGRATED_EXPECTATIONM10x10000015P40trueC0303ADDITIVE2010_1/initial_calibration", "OIS Spot Rate Single-Curve", "10x100000" });

            createPlot(0.0, 4.0,
                    "Time [years]",
                    "Rate [\\\\%]",
                    "Valuation of single-curve forward rates with different amounts of Monte-Carlo paths. Here, MxN means the average was taken over M runs with N paths, respectively.",
                    filesToProducts, fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fileName = "ois_forward_vs_spot_single_curve_high_vol_negative_interest";

            createParamsTable("csv/convergence_high_vol_negative_interest/INTEGRATED_EXPECTATIONM1x100015P40trueC0909ADDITIVE2015_1", fileName);

            testToFileMap = new LinkedHashMap<>();

            comparison1 = new String[]{ "OIS Forward Rate Multi-Curve", "OIS Spot Rate Single-Curve" };

            testToFileMap.put("1x1000", new Object[] { "csv/convergence_high_vol_negative_interest/INTEGRATED_EXPECTATIONM1x100015P40trueC0909ADDITIVE2015_1", comparison1 });
            testToFileMap.put("1x10000", new Object[] { "csv/convergence_high_vol_negative_interest/INTEGRATED_EXPECTATIONM1x1000015P40trueC0909ADDITIVE2015_1", comparison1 });
            testToFileMap.put("1x100000", new Object[] { "csv/convergence_high_vol_negative_interest/INTEGRATED_EXPECTATIONM1x10000015P40trueC0909ADDITIVE2015_1", comparison1 });
            testToFileMap.put("10x100000", new Object[] { "csv/convergence_high_vol_negative_interest/INTEGRATED_EXPECTATIONM10x10000015P40trueC0909ADDITIVE2015_1", comparison1 });

            generateErrorPlot("Errors of Monte-Carlo valuation of single-curve forward rates vs. their expected values.", testToFileMap, fileName);

            filesToProducts = new ArrayList<>();

            filesToProducts.add(new String[] { "csv/convergence_high_vol_negative_interest/INTEGRATED_EXPECTATIONM1x100015P40trueC0909ADDITIVE2015_1/initial_calibration", "OIS Forward Rate Multi-Curve", "Reference" });
            filesToProducts.add(new String[] { "csv/convergence_high_vol_negative_interest/INTEGRATED_EXPECTATIONM1x100015P40trueC0909ADDITIVE2015_1/initial_calibration", "OIS Spot Rate Single-Curve", "1x1000" });
            filesToProducts.add(new String[] { "csv/convergence_high_vol_negative_interest/INTEGRATED_EXPECTATIONM1x1000015P40trueC0909ADDITIVE2015_1/initial_calibration", "OIS Spot Rate Single-Curve", "1x10000" });
            filesToProducts.add(new String[] { "csv/convergence_high_vol_negative_interest/INTEGRATED_EXPECTATIONM1x10000015P40trueC0909ADDITIVE2015_1/initial_calibration", "OIS Spot Rate Single-Curve", "1x100000" });
            filesToProducts.add(new String[] { "csv/convergence_high_vol_negative_interest/INTEGRATED_EXPECTATIONM10x10000015P40trueC0909ADDITIVE2015_1/initial_calibration", "OIS Spot Rate Single-Curve", "10x100000" });

            createPlot(-0.15, 0.02,
                    "Time [years]",
                    "Rate [\\\\%]",
                    "Valuation of single-curve forward rates with different amounts of Monte-Carlo paths. Here, MxN means the average was taken over M runs with N paths, respectively.",
                    filesToProducts, fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            fileName = "caplet_vola_single_curve_mid_vol_negative_interest";

            createParamsTable("csv/convergence_mid_vol_negative_interest/INTEGRATED_EXPECTATIONM1x100015P40trueC0606ADDITIVE2015_1", fileName);

            testToFileMap = new LinkedHashMap<>();

            comparison1 =  new String[] { "Caplet Vola Monte-Carlo Single-Curve", "Caplet Vola Analytic Single-Curve" };

            testToFileMap.put("1x1000", new Object[] { "csv/convergence_mid_vol_negative_interest/INTEGRATED_EXPECTATIONM1x100015P40trueC0606ADDITIVE2015_1", comparison1 });
            testToFileMap.put("1x10000", new Object[] { "csv/convergence_mid_vol_negative_interest/INTEGRATED_EXPECTATIONM1x1000015P40trueC0606ADDITIVE2015_1", comparison1 });
            testToFileMap.put("1x100000", new Object[] { "csv/convergence_mid_vol_negative_interest/INTEGRATED_EXPECTATIONM1x10000015P40trueC0606ADDITIVE2015_1", comparison1 });
            testToFileMap.put("10x100000", new Object[] { "csv/convergence_mid_vol_negative_interest/INTEGRATED_EXPECTATIONM10x10000015P40trueC0606ADDITIVE2015_1", comparison1 });

            generateErrorPlot("Errors of Monte-Carlo valuation of caplets vs. their Black-Scholes valuation in a single-curve model.", testToFileMap, fileName);

            filesToProducts = new ArrayList<>();

            filesToProducts.add(new String[] { "csv/convergence_mid_vol_negative_interest/INTEGRATED_EXPECTATIONM1x100015P40trueC0606ADDITIVE2015_1/initial_calibration", "Caplet Vola Analytic Single-Curve", "Analytic" });
            filesToProducts.add(new String[] { "csv/convergence_mid_vol_negative_interest/INTEGRATED_EXPECTATIONM1x100015P40trueC0606ADDITIVE2015_1/initial_calibration", "Caplet Vola Monte-Carlo Single-Curve", "1x1000" });
            filesToProducts.add(new String[] { "csv/convergence_mid_vol_negative_interest/INTEGRATED_EXPECTATIONM1x1000015P40trueC0606ADDITIVE2015_1/initial_calibration", "Caplet Vola Monte-Carlo Single-Curve", "1x10000" });
            filesToProducts.add(new String[] { "csv/convergence_mid_vol_negative_interest/INTEGRATED_EXPECTATIONM1x10000015P40trueC0606ADDITIVE2015_1/initial_calibration", "Caplet Vola Monte-Carlo Single-Curve", "1x100000" });
            filesToProducts.add(new String[] { "csv/convergence_mid_vol_negative_interest/INTEGRATED_EXPECTATIONM10x10000015P40trueC0606ADDITIVE2015_1/initial_calibration", "Caplet Vola Monte-Carlo Single-Curve", "10x100000" });

            createPlot(30, 70,
                    "Caplet Maturity [years]",
                    "Caplet Vola [\\\\%]",
                    "Valuation of ATM caplets with underlying (single-curve) forward rates. Monte-Carlo valuation with different amounts of paths is compared to the analytical valuation through the Black-Scholes formula.",
                    filesToProducts, fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fileName = "caplet_vola_single_curve_high_vol_negative_interest";

            createParamsTable("csv/convergence_high_vol_negative_interest/INTEGRATED_EXPECTATIONM1x100015P40trueC0909ADDITIVE2015_1", fileName);

            testToFileMap = new LinkedHashMap<>();

            comparison1 =  new String[] { "Caplet Vola Monte-Carlo Single-Curve", "Caplet Vola Analytic Single-Curve" };

            testToFileMap.put("1x1000", new Object[] { "csv/convergence_high_vol_negative_interest/INTEGRATED_EXPECTATIONM1x100015P40trueC0909ADDITIVE2015_1", comparison1 });
            testToFileMap.put("1x10000", new Object[] { "csv/convergence_high_vol_negative_interest/INTEGRATED_EXPECTATIONM1x1000015P40trueC0909ADDITIVE2015_1", comparison1 });
            testToFileMap.put("1x100000", new Object[] { "csv/convergence_high_vol_negative_interest/INTEGRATED_EXPECTATIONM1x10000015P40trueC0909ADDITIVE2015_1", comparison1 });
            testToFileMap.put("10x100000", new Object[] { "csv/convergence_high_vol_negative_interest/INTEGRATED_EXPECTATIONM10x10000015P40trueC0909ADDITIVE2015_1", comparison1 });

            generateErrorPlot("Errors of Monte-Carlo valuation of caplets vs. their Black-Scholes valuation in a single-curve model.", testToFileMap, fileName);

            filesToProducts = new ArrayList<>();

            filesToProducts.add(new String[] { "csv/convergence_high_vol_negative_interest/INTEGRATED_EXPECTATIONM1x100015P40trueC0909ADDITIVE2015_1/initial_calibration", "Caplet Vola Analytic Single-Curve", "Analytic" });
            filesToProducts.add(new String[] { "csv/convergence_high_vol_negative_interest/INTEGRATED_EXPECTATIONM1x100015P40trueC0909ADDITIVE2015_1/initial_calibration", "Caplet Vola Monte-Carlo Single-Curve", "1x1000" });
            filesToProducts.add(new String[] { "csv/convergence_high_vol_negative_interest/INTEGRATED_EXPECTATIONM1x1000015P40trueC0909ADDITIVE2015_1/initial_calibration", "Caplet Vola Monte-Carlo Single-Curve", "1x10000" });
            filesToProducts.add(new String[] { "csv/convergence_high_vol_negative_interest/INTEGRATED_EXPECTATIONM1x10000015P40trueC0909ADDITIVE2015_1/initial_calibration", "Caplet Vola Monte-Carlo Single-Curve", "1x100000" });
            filesToProducts.add(new String[] { "csv/convergence_high_vol_negative_interest/INTEGRATED_EXPECTATIONM10x10000015P40trueC0909ADDITIVE2015_1/initial_calibration", "Caplet Vola Monte-Carlo Single-Curve", "10x100000" });

            createPlot(50, 120,
                    "Caplet Maturity [years]",
                    "Caplet Vola [\\\\%]",
                    "Valuation of ATM caplets with underlying (single-curve) forward rates. Monte-Carlo valuation with different amounts of paths is compared to the analytical valuation through the Black-Scholes formula.",
                    filesToProducts, fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            fileName = "libor_ois_forward_vs_spot_multi_curve_mid_vol_negative_interest_additive";

            createParamsTable("csv/convergence_mid_vol_negative_interest/INTEGRATED_EXPECTATIONM1x100015P40trueC0606ADDITIVE2015_1", fileName);

            testToFileMap = new LinkedHashMap<>();

            comparison1 = new String[]{ "OIS Forward Rate Multi-Curve", "OIS Spot Rate Multi-Curve" };

            testToFileMap.put("1x1000", new Object[] { "csv/convergence_mid_vol_negative_interest/INTEGRATED_EXPECTATIONM1x100015P40trueC0606ADDITIVE2015_1", comparison1 });
            testToFileMap.put("1x10000", new Object[] { "csv/convergence_mid_vol_negative_interest/INTEGRATED_EXPECTATIONM1x1000015P40trueC0606ADDITIVE2015_1", comparison1 });
            testToFileMap.put("1x100000", new Object[] { "csv/convergence_mid_vol_negative_interest/INTEGRATED_EXPECTATIONM1x10000015P40trueC0606ADDITIVE2015_1", comparison1 });
            testToFileMap.put("10x100000", new Object[] { "csv/convergence_mid_vol_negative_interest/INTEGRATED_EXPECTATIONM10x10000015P40trueC0606ADDITIVE2015_1", comparison1 });

            generateErrorPlot("Errors of Monte-Carlo valuation of (OIS) forward rates vs. their expected values.", testToFileMap, "ois_forward_vs_spot_multi_curve_mid_vol_negative_interest_additive");

            comparison1 = new String[]{ "LIBOR Forward Rate Multi-Curve", "LIBOR Spot Rate Multi-Curve" };

            testToFileMap.put("1x1000", new Object[] { "csv/convergence_mid_vol_negative_interest/INTEGRATED_EXPECTATIONM1x100015P40trueC0606ADDITIVE2015_1", comparison1 });
            testToFileMap.put("1x10000", new Object[] { "csv/convergence_mid_vol_negative_interest/INTEGRATED_EXPECTATIONM1x1000015P40trueC0606ADDITIVE2015_1", comparison1 });
            testToFileMap.put("1x100000", new Object[] { "csv/convergence_mid_vol_negative_interest/INTEGRATED_EXPECTATIONM1x10000015P40trueC0606ADDITIVE2015_1", comparison1 });
            testToFileMap.put("10x100000", new Object[] { "csv/convergence_mid_vol_negative_interest/INTEGRATED_EXPECTATIONM10x10000015P40trueC0606ADDITIVE2015_1", comparison1 });

            generateErrorPlot("Errors of Monte-Carlo valuation of (IBOR) forward rates vs. their expected values in the additive model.", testToFileMap, "libor_forward_vs_spot_multi_curve_mid_vol_negative_interest_additive");

            filesToProducts = new ArrayList<>();

            filesToProducts.add(new String[] { "csv/convergence_mid_vol_negative_interest/INTEGRATED_EXPECTATIONM1x100015P40trueC0606ADDITIVE2015_1/initial_calibration", "OIS Forward Rate Multi-Curve", "Reference" });
            filesToProducts.add(new String[] { "csv/convergence_mid_vol_negative_interest/INTEGRATED_EXPECTATIONM1x100015P40trueC0606ADDITIVE2015_1/initial_calibration", "OIS Spot Rate Multi-Curve", "1x1000" });
            filesToProducts.add(new String[] { "csv/convergence_mid_vol_negative_interest/INTEGRATED_EXPECTATIONM1x1000015P40trueC0606ADDITIVE2015_1/initial_calibration", "OIS Spot Rate Multi-Curve", "1x10000" });
            filesToProducts.add(new String[] { "csv/convergence_mid_vol_negative_interest/INTEGRATED_EXPECTATIONM1x10000015P40trueC0606ADDITIVE2015_1/initial_calibration", "OIS Spot Rate Multi-Curve", "1x100000" });
            filesToProducts.add(new String[] { "csv/convergence_mid_vol_negative_interest/INTEGRATED_EXPECTATIONM10x10000015P40trueC0606ADDITIVE2015_1/initial_calibration", "OIS Spot Rate Multi-Curve", "10x100000" });

            filesToProducts.add(new String[] { "csv/convergence_mid_vol_negative_interest/INTEGRATED_EXPECTATIONM1x100015P40trueC0606ADDITIVE2015_1/initial_calibration", "LIBOR Forward Rate Multi-Curve", "Reference" });
            filesToProducts.add(new String[] { "csv/convergence_mid_vol_negative_interest/INTEGRATED_EXPECTATIONM1x100015P40trueC0606ADDITIVE2015_1/initial_calibration", "LIBOR Spot Rate Multi-Curve", "1x1000" });
            filesToProducts.add(new String[] { "csv/convergence_mid_vol_negative_interest/INTEGRATED_EXPECTATIONM1x1000015P40trueC0606ADDITIVE2015_1/initial_calibration", "LIBOR Spot Rate Multi-Curve", "1x10000" });
            filesToProducts.add(new String[] { "csv/convergence_mid_vol_negative_interest/INTEGRATED_EXPECTATIONM1x10000015P40trueC0606ADDITIVE2015_1/initial_calibration", "LIBOR Spot Rate Multi-Curve", "1x100000" });
            filesToProducts.add(new String[] { "csv/convergence_mid_vol_negative_interest/INTEGRATED_EXPECTATIONM10x10000015P40trueC0606ADDITIVE2015_1/initial_calibration", "LIBOR Spot Rate Multi-Curve", "10x100000" });

            createPlot(-0.05,0.05,
                    "Time [years]",
                    "Rate [\\\\%]",
                    "Valuation of IBOR and OIS forward rates in the additive model with different amounts of Monte-Carlo paths.",
                    filesToProducts, fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            fileName = "libor_ois_forward_vs_spot_multi_curve_mid_vol_negative_interest_multiplicative";

            createParamsTable("csv/convergence_mid_vol_negative_interest/INTEGRATED_EXPECTATIONM1x100015P40trueC0606MULTIPLICATIVE2015_1", fileName);

            testToFileMap = new LinkedHashMap<>();

            comparison1 = new String[]{ "OIS Forward Rate Multi-Curve", "OIS Spot Rate Multi-Curve" };

            testToFileMap.put("1x1000", new Object[] { "csv/convergence_mid_vol_negative_interest/INTEGRATED_EXPECTATIONM1x100015P40trueC0606MULTIPLICATIVE2015_1", comparison1 });
            testToFileMap.put("1x10000", new Object[] { "csv/convergence_mid_vol_negative_interest/INTEGRATED_EXPECTATIONM1x1000015P40trueC0606MULTIPLICATIVE2015_1", comparison1 });
            testToFileMap.put("1x100000", new Object[] { "csv/convergence_mid_vol_negative_interest/INTEGRATED_EXPECTATIONM1x10000015P40trueC0606MULTIPLICATIVE2015_1", comparison1 });
            testToFileMap.put("10x100000", new Object[] { "csv/convergence_mid_vol_negative_interest/INTEGRATED_EXPECTATIONM10x10000015P40trueC0606MULTIPLICATIVE2015_1", comparison1 });

            generateErrorPlot("Errors of Monte-Carlo valuation of (OIS) forward rates vs. their expected values.", testToFileMap, "ois_forward_vs_spot_multi_curve_mid_vol_negative_interest_multiplicative");

            comparison1 = new String[]{ "LIBOR Forward Rate Multi-Curve", "LIBOR Spot Rate Multi-Curve" };

            testToFileMap.put("1x1000", new Object[] { "csv/convergence_mid_vol_negative_interest/INTEGRATED_EXPECTATIONM1x100015P40trueC0606MULTIPLICATIVE2015_1", comparison1 });
            testToFileMap.put("1x10000", new Object[] { "csv/convergence_mid_vol_negative_interest/INTEGRATED_EXPECTATIONM1x1000015P40trueC0606MULTIPLICATIVE2015_1", comparison1 });
            testToFileMap.put("1x100000", new Object[] { "csv/convergence_mid_vol_negative_interest/INTEGRATED_EXPECTATIONM1x10000015P40trueC0606MULTIPLICATIVE2015_1", comparison1 });
            testToFileMap.put("10x100000", new Object[] { "csv/convergence_mid_vol_negative_interest/INTEGRATED_EXPECTATIONM10x10000015P40trueC0606MULTIPLICATIVE2015_1", comparison1 });

            generateErrorPlot("Errors of Monte-Carlo valuation of (IBOR) forward rates vs. their expected values in the multiplicative model.", testToFileMap, "libor_forward_vs_spot_multi_curve_mid_vol_negative_interest_multiplicative");

            filesToProducts = new ArrayList<>();

            filesToProducts.add(new String[] { "csv/convergence_mid_vol_negative_interest/INTEGRATED_EXPECTATIONM1x100015P40trueC0606MULTIPLICATIVE2015_1/initial_calibration", "OIS Forward Rate Multi-Curve", "Reference" });
            filesToProducts.add(new String[] { "csv/convergence_mid_vol_negative_interest/INTEGRATED_EXPECTATIONM1x100015P40trueC0606MULTIPLICATIVE2015_1/initial_calibration", "OIS Spot Rate Multi-Curve", "1x1000" });
            filesToProducts.add(new String[] { "csv/convergence_mid_vol_negative_interest/INTEGRATED_EXPECTATIONM1x1000015P40trueC0606MULTIPLICATIVE2015_1/initial_calibration", "OIS Spot Rate Multi-Curve", "1x10000" });
            filesToProducts.add(new String[] { "csv/convergence_mid_vol_negative_interest/INTEGRATED_EXPECTATIONM1x10000015P40trueC0606MULTIPLICATIVE2015_1/initial_calibration", "OIS Spot Rate Multi-Curve", "1x100000" });
            filesToProducts.add(new String[] { "csv/convergence_mid_vol_negative_interest/INTEGRATED_EXPECTATIONM10x10000015P40trueC0606MULTIPLICATIVE2015_1/initial_calibration", "OIS Spot Rate Multi-Curve", "10x100000" });

            filesToProducts.add(new String[] { "csv/convergence_mid_vol_negative_interest/INTEGRATED_EXPECTATIONM1x100015P40trueC0606MULTIPLICATIVE2015_1/initial_calibration", "LIBOR Forward Rate Multi-Curve", "Reference" });
            filesToProducts.add(new String[] { "csv/convergence_mid_vol_negative_interest/INTEGRATED_EXPECTATIONM1x100015P40trueC0606MULTIPLICATIVE2015_1/initial_calibration", "LIBOR Spot Rate Multi-Curve", "1x1000" });
            filesToProducts.add(new String[] { "csv/convergence_mid_vol_negative_interest/INTEGRATED_EXPECTATIONM1x1000015P40trueC0606MULTIPLICATIVE2015_1/initial_calibration", "LIBOR Spot Rate Multi-Curve", "1x10000" });
            filesToProducts.add(new String[] { "csv/convergence_mid_vol_negative_interest/INTEGRATED_EXPECTATIONM1x10000015P40trueC0606MULTIPLICATIVE2015_1/initial_calibration", "LIBOR Spot Rate Multi-Curve", "1x100000" });
            filesToProducts.add(new String[] { "csv/convergence_mid_vol_negative_interest/INTEGRATED_EXPECTATIONM10x10000015P40trueC0606MULTIPLICATIVE2015_1/initial_calibration", "LIBOR Spot Rate Multi-Curve", "10x100000" });

            createPlot(-0.05,0.05,
                    "Time [years]",
                    "Rate [\\\\%]",
                    "Valuation of IBOR and OIS forward rates in the multiplicative model with different amounts of Monte-Carlo paths.",
                    filesToProducts, fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            fileName = "libor_ois_forward_vs_spot_multi_curve_high_vol_negative_interest_additive";

            createParamsTable("csv/convergence_high_vol_negative_interest/INTEGRATED_EXPECTATIONM1x100015P40trueC0909ADDITIVE2015_1", fileName);

            testToFileMap = new LinkedHashMap<>();

            comparison1 = new String[]{ "OIS Forward Rate Multi-Curve", "OIS Spot Rate Multi-Curve" };

            testToFileMap.put("1x1000", new Object[] { "csv/convergence_high_vol_negative_interest/INTEGRATED_EXPECTATIONM1x100015P40trueC0909ADDITIVE2015_1", comparison1 });
            testToFileMap.put("1x10000", new Object[] { "csv/convergence_high_vol_negative_interest/INTEGRATED_EXPECTATIONM1x1000015P40trueC0909ADDITIVE2015_1", comparison1 });
            testToFileMap.put("1x100000", new Object[] { "csv/convergence_high_vol_negative_interest/INTEGRATED_EXPECTATIONM1x10000015P40trueC0909ADDITIVE2015_1", comparison1 });
            testToFileMap.put("10x100000", new Object[] { "csv/convergence_high_vol_negative_interest/INTEGRATED_EXPECTATIONM10x10000015P40trueC0909ADDITIVE2015_1", comparison1 });

            generateErrorPlot("Errors of Monte-Carlo valuation of (OIS) forward rates vs. their expected values.", testToFileMap, "ois_forward_vs_spot_multi_curve_high_vol_negative_interest_additive");

            comparison1 = new String[]{ "LIBOR Forward Rate Multi-Curve", "LIBOR Spot Rate Multi-Curve" };

            testToFileMap.put("1x1000", new Object[] { "csv/convergence_high_vol_negative_interest/INTEGRATED_EXPECTATIONM1x100015P40trueC0909ADDITIVE2015_1", comparison1 });
            testToFileMap.put("1x10000", new Object[] { "csv/convergence_high_vol_negative_interest/INTEGRATED_EXPECTATIONM1x1000015P40trueC0909ADDITIVE2015_1", comparison1 });
            testToFileMap.put("1x100000", new Object[] { "csv/convergence_high_vol_negative_interest/INTEGRATED_EXPECTATIONM1x10000015P40trueC0909ADDITIVE2015_1", comparison1 });
            testToFileMap.put("10x100000", new Object[] { "csv/convergence_high_vol_negative_interest/INTEGRATED_EXPECTATIONM10x10000015P40trueC0909ADDITIVE2015_1", comparison1 });

            generateErrorPlot("Errors of Monte-Carlo valuation of (IBOR) forward rates vs. their expected values in the additive model.", testToFileMap, "libor_forward_vs_spot_multi_curve_high_vol_negative_interest_additive");

            filesToProducts = new ArrayList<>();

            filesToProducts.add(new String[] { "csv/convergence_high_vol_negative_interest/INTEGRATED_EXPECTATIONM1x100015P40trueC0909ADDITIVE2015_1/initial_calibration", "OIS Forward Rate Multi-Curve", "Reference" });
            filesToProducts.add(new String[] { "csv/convergence_high_vol_negative_interest/INTEGRATED_EXPECTATIONM1x100015P40trueC0909ADDITIVE2015_1/initial_calibration", "OIS Spot Rate Multi-Curve", "1x1000" });
            filesToProducts.add(new String[] { "csv/convergence_high_vol_negative_interest/INTEGRATED_EXPECTATIONM1x1000015P40trueC0909ADDITIVE2015_1/initial_calibration", "OIS Spot Rate Multi-Curve", "1x10000" });
            filesToProducts.add(new String[] { "csv/convergence_high_vol_negative_interest/INTEGRATED_EXPECTATIONM1x10000015P40trueC0909ADDITIVE2015_1/initial_calibration", "OIS Spot Rate Multi-Curve", "1x100000" });
            filesToProducts.add(new String[] { "csv/convergence_high_vol_negative_interest/INTEGRATED_EXPECTATIONM10x10000015P40trueC0909ADDITIVE2015_1/initial_calibration", "OIS Spot Rate Multi-Curve", "10x100000" });

            filesToProducts.add(new String[] { "csv/convergence_high_vol_negative_interest/INTEGRATED_EXPECTATIONM1x100015P40trueC0909ADDITIVE2015_1/initial_calibration", "LIBOR Forward Rate Multi-Curve", "Reference" });
            filesToProducts.add(new String[] { "csv/convergence_high_vol_negative_interest/INTEGRATED_EXPECTATIONM1x100015P40trueC0909ADDITIVE2015_1/initial_calibration", "LIBOR Spot Rate Multi-Curve", "1x1000" });
            filesToProducts.add(new String[] { "csv/convergence_high_vol_negative_interest/INTEGRATED_EXPECTATIONM1x1000015P40trueC0909ADDITIVE2015_1/initial_calibration", "LIBOR Spot Rate Multi-Curve", "1x10000" });
            filesToProducts.add(new String[] { "csv/convergence_high_vol_negative_interest/INTEGRATED_EXPECTATIONM1x10000015P40trueC0909ADDITIVE2015_1/initial_calibration", "LIBOR Spot Rate Multi-Curve", "1x100000" });
            filesToProducts.add(new String[] { "csv/convergence_high_vol_negative_interest/INTEGRATED_EXPECTATIONM10x10000015P40trueC0909ADDITIVE2015_1/initial_calibration", "LIBOR Spot Rate Multi-Curve", "10x100000" });

            createPlot(-0.05,0.05,
                    "Time [years]",
                    "Rate [\\\\%]",
                    "Valuation of IBOR and OIS forward rates in the additive model with different amounts of Monte-Carlo paths.",
                    filesToProducts, fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            fileName = "libor_ois_forward_vs_spot_multi_curve_high_vol_negative_interest_multiplicative";

            createParamsTable("csv/convergence_high_vol_negative_interest/INTEGRATED_EXPECTATIONM1x100015P40trueC0909MULTIPLICATIVE2015_1", fileName);

            testToFileMap = new LinkedHashMap<>();

            comparison1 = new String[]{ "OIS Forward Rate Multi-Curve", "OIS Spot Rate Multi-Curve" };

            testToFileMap.put("1x1000", new Object[] { "csv/convergence_high_vol_negative_interest/INTEGRATED_EXPECTATIONM1x100015P40trueC0909MULTIPLICATIVE2015_1", comparison1 });
            testToFileMap.put("1x10000", new Object[] { "csv/convergence_high_vol_negative_interest/INTEGRATED_EXPECTATIONM1x1000015P40trueC0909MULTIPLICATIVE2015_1", comparison1 });
            testToFileMap.put("1x100000", new Object[] { "csv/convergence_high_vol_negative_interest/INTEGRATED_EXPECTATIONM1x10000015P40trueC0909MULTIPLICATIVE2015_1", comparison1 });
            testToFileMap.put("10x100000", new Object[] { "csv/convergence_high_vol_negative_interest/INTEGRATED_EXPECTATIONM10x10000015P40trueC0909MULTIPLICATIVE2015_1", comparison1 });

            generateErrorPlot("Errors of Monte-Carlo valuation of (OIS) forward rates vs. their expected values.", testToFileMap, "ois_forward_vs_spot_multi_curve_high_vol_negative_interest_multiplicative");

            comparison1 = new String[]{ "LIBOR Forward Rate Multi-Curve", "LIBOR Spot Rate Multi-Curve" };

            testToFileMap.put("1x1000", new Object[] { "csv/convergence_high_vol_negative_interest/INTEGRATED_EXPECTATIONM1x100015P40trueC0909MULTIPLICATIVE2015_1", comparison1 });
            testToFileMap.put("1x10000", new Object[] { "csv/convergence_high_vol_negative_interest/INTEGRATED_EXPECTATIONM1x1000015P40trueC0909MULTIPLICATIVE2015_1", comparison1 });
            testToFileMap.put("1x100000", new Object[] { "csv/convergence_high_vol_negative_interest/INTEGRATED_EXPECTATIONM1x10000015P40trueC0909MULTIPLICATIVE2015_1", comparison1 });
            testToFileMap.put("10x100000", new Object[] { "csv/convergence_high_vol_negative_interest/INTEGRATED_EXPECTATIONM10x10000015P40trueC0909MULTIPLICATIVE2015_1", comparison1 });

            generateErrorPlot("Errors of Monte-Carlo valuation of (IBOR) forward rates vs. their expected values in the multiplicative model.", testToFileMap, "libor_forward_vs_spot_multi_curve_high_vol_negative_interest_multiplicative");

            filesToProducts = new ArrayList<>();

            filesToProducts.add(new String[] { "csv/convergence_high_vol_negative_interest/INTEGRATED_EXPECTATIONM1x100015P40trueC0909MULTIPLICATIVE2015_1/initial_calibration", "OIS Forward Rate Multi-Curve", "Reference" });
            filesToProducts.add(new String[] { "csv/convergence_high_vol_negative_interest/INTEGRATED_EXPECTATIONM1x100015P40trueC0909MULTIPLICATIVE2015_1/initial_calibration", "OIS Spot Rate Multi-Curve", "1x1000" });
            filesToProducts.add(new String[] { "csv/convergence_high_vol_negative_interest/INTEGRATED_EXPECTATIONM1x1000015P40trueC0909MULTIPLICATIVE2015_1/initial_calibration", "OIS Spot Rate Multi-Curve", "1x10000" });
            filesToProducts.add(new String[] { "csv/convergence_high_vol_negative_interest/INTEGRATED_EXPECTATIONM1x10000015P40trueC0909MULTIPLICATIVE2015_1/initial_calibration", "OIS Spot Rate Multi-Curve", "1x100000" });
            filesToProducts.add(new String[] { "csv/convergence_high_vol_negative_interest/INTEGRATED_EXPECTATIONM10x10000015P40trueC0909MULTIPLICATIVE2015_1/initial_calibration", "OIS Spot Rate Multi-Curve", "10x100000" });

            filesToProducts.add(new String[] { "csv/convergence_high_vol_negative_interest/INTEGRATED_EXPECTATIONM1x100015P40trueC0909MULTIPLICATIVE2015_1/initial_calibration", "LIBOR Forward Rate Multi-Curve", "Reference" });
            filesToProducts.add(new String[] { "csv/convergence_high_vol_negative_interest/INTEGRATED_EXPECTATIONM1x100015P40trueC0909MULTIPLICATIVE2015_1/initial_calibration", "LIBOR Spot Rate Multi-Curve", "1x1000" });
            filesToProducts.add(new String[] { "csv/convergence_high_vol_negative_interest/INTEGRATED_EXPECTATIONM1x1000015P40trueC0909MULTIPLICATIVE2015_1/initial_calibration", "LIBOR Spot Rate Multi-Curve", "1x10000" });
            filesToProducts.add(new String[] { "csv/convergence_high_vol_negative_interest/INTEGRATED_EXPECTATIONM1x10000015P40trueC0909MULTIPLICATIVE2015_1/initial_calibration", "LIBOR Spot Rate Multi-Curve", "1x100000" });
            filesToProducts.add(new String[] { "csv/convergence_high_vol_negative_interest/INTEGRATED_EXPECTATIONM10x10000015P40trueC0909MULTIPLICATIVE2015_1/initial_calibration", "LIBOR Spot Rate Multi-Curve", "10x100000" });

            createPlot(-0.05,0.05,
                    "Time [years]",
                    "Rate [\\\\%]",
                    "Valuation of IBOR and OIS forward rates in the multiplicative model with different amounts of Monte-Carlo paths.",
                    filesToProducts, fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            fileName = "libor_ois_forward_vs_spot_multi_curve_mid_vol_low_interest_additive";

            createParamsTable("csv/convergence_mid_vol_low_interest/INTEGRATED_EXPECTATIONM1x100015P40trueC0606ADDITIVE2013_1", fileName);

            testToFileMap = new LinkedHashMap<>();

            comparison1 = new String[]{ "OIS Forward Rate Multi-Curve", "OIS Spot Rate Multi-Curve" };

            testToFileMap.put("1x1000", new Object[] { "csv/convergence_mid_vol_low_interest/INTEGRATED_EXPECTATIONM1x100015P40trueC0606ADDITIVE2013_1", comparison1 });
            testToFileMap.put("1x10000", new Object[] { "csv/convergence_mid_vol_low_interest/INTEGRATED_EXPECTATIONM1x1000015P40trueC0606ADDITIVE2013_1", comparison1 });
            testToFileMap.put("1x100000", new Object[] { "csv/convergence_mid_vol_low_interest/INTEGRATED_EXPECTATIONM1x10000015P40trueC0606ADDITIVE2013_1", comparison1 });
            testToFileMap.put("10x100000", new Object[] { "csv/convergence_mid_vol_low_interest/INTEGRATED_EXPECTATIONM10x10000015P40trueC0606ADDITIVE2013_1", comparison1 });

            generateErrorPlot("Errors of Monte-Carlo valuation of (OIS) forward rates vs. their expected values.", testToFileMap, "ois_forward_vs_spot_multi_curve_mid_vol_low_interest_additive");

            comparison1 = new String[]{ "LIBOR Forward Rate Multi-Curve", "LIBOR Spot Rate Multi-Curve" };

            testToFileMap.put("1x1000", new Object[] { "csv/convergence_mid_vol_low_interest/INTEGRATED_EXPECTATIONM1x100015P40trueC0606ADDITIVE2013_1", comparison1 });
            testToFileMap.put("1x10000", new Object[] { "csv/convergence_mid_vol_low_interest/INTEGRATED_EXPECTATIONM1x1000015P40trueC0606ADDITIVE2013_1", comparison1 });
            testToFileMap.put("1x100000", new Object[] { "csv/convergence_mid_vol_low_interest/INTEGRATED_EXPECTATIONM1x10000015P40trueC0606ADDITIVE2013_1", comparison1 });
            testToFileMap.put("10x100000", new Object[] { "csv/convergence_mid_vol_low_interest/INTEGRATED_EXPECTATIONM10x10000015P40trueC0606ADDITIVE2013_1", comparison1 });

            generateErrorPlot("Errors of Monte-Carlo valuation of (IBOR) forward rates vs. their expected values in the additive model.", testToFileMap, "libor_forward_vs_spot_multi_curve_mid_vol_low_interest_additive");

            filesToProducts = new ArrayList<>();

            filesToProducts.add(new String[] { "csv/convergence_mid_vol_low_interest/INTEGRATED_EXPECTATIONM1x100015P40trueC0606ADDITIVE2013_1/initial_calibration", "OIS Forward Rate Multi-Curve", "Reference" });
            filesToProducts.add(new String[] { "csv/convergence_mid_vol_low_interest/INTEGRATED_EXPECTATIONM1x100015P40trueC0606ADDITIVE2013_1/initial_calibration", "OIS Spot Rate Multi-Curve", "1x1000" });
            filesToProducts.add(new String[] { "csv/convergence_mid_vol_low_interest/INTEGRATED_EXPECTATIONM1x1000015P40trueC0606ADDITIVE2013_1/initial_calibration", "OIS Spot Rate Multi-Curve", "1x10000" });
            filesToProducts.add(new String[] { "csv/convergence_mid_vol_low_interest/INTEGRATED_EXPECTATIONM1x10000015P40trueC0606ADDITIVE2013_1/initial_calibration", "OIS Spot Rate Multi-Curve", "1x100000" });
            filesToProducts.add(new String[] { "csv/convergence_mid_vol_low_interest/INTEGRATED_EXPECTATIONM10x10000015P40trueC0606ADDITIVE2013_1/initial_calibration", "OIS Spot Rate Multi-Curve", "10x100000" });

            filesToProducts.add(new String[] { "csv/convergence_mid_vol_low_interest/INTEGRATED_EXPECTATIONM1x100015P40trueC0606ADDITIVE2013_1/initial_calibration", "LIBOR Forward Rate Multi-Curve", "Reference" });
            filesToProducts.add(new String[] { "csv/convergence_mid_vol_low_interest/INTEGRATED_EXPECTATIONM1x100015P40trueC0606ADDITIVE2013_1/initial_calibration", "LIBOR Spot Rate Multi-Curve", "1x1000" });
            filesToProducts.add(new String[] { "csv/convergence_mid_vol_low_interest/INTEGRATED_EXPECTATIONM1x1000015P40trueC0606ADDITIVE2013_1/initial_calibration", "LIBOR Spot Rate Multi-Curve", "1x10000" });
            filesToProducts.add(new String[] { "csv/convergence_mid_vol_low_interest/INTEGRATED_EXPECTATIONM1x10000015P40trueC0606ADDITIVE2013_1/initial_calibration", "LIBOR Spot Rate Multi-Curve", "1x100000" });
            filesToProducts.add(new String[] { "csv/convergence_mid_vol_low_interest/INTEGRATED_EXPECTATIONM10x10000015P40trueC0606ADDITIVE2013_1/initial_calibration", "LIBOR Spot Rate Multi-Curve", "10x100000" });

            createPlot(0,4,
                    "Time [years]",
                    "Rate [\\\\%]",
                    "Valuation of IBOR and OIS forward rates in the additive model with different amounts of Monte-Carlo paths.",
                    filesToProducts, fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            fileName = "libor_ois_forward_vs_spot_multi_curve_mid_vol_low_interest_multiplicative";

            createParamsTable("csv/convergence_mid_vol_low_interest/INTEGRATED_EXPECTATIONM1x100015P40trueC0606MULTIPLICATIVE2013_1", fileName);

            testToFileMap = new LinkedHashMap<>();

            comparison1 = new String[]{ "OIS Forward Rate Multi-Curve", "OIS Spot Rate Multi-Curve" };

            testToFileMap.put("1x1000", new Object[] { "csv/convergence_mid_vol_low_interest/INTEGRATED_EXPECTATIONM1x100015P40trueC0606MULTIPLICATIVE2013_1", comparison1 });
            testToFileMap.put("1x10000", new Object[] { "csv/convergence_mid_vol_low_interest/INTEGRATED_EXPECTATIONM1x1000015P40trueC0606MULTIPLICATIVE2013_1", comparison1 });
            testToFileMap.put("1x100000", new Object[] { "csv/convergence_mid_vol_low_interest/INTEGRATED_EXPECTATIONM1x10000015P40trueC0606MULTIPLICATIVE2013_1", comparison1 });
            testToFileMap.put("10x100000", new Object[] { "csv/convergence_mid_vol_low_interest/INTEGRATED_EXPECTATIONM10x10000015P40trueC0606MULTIPLICATIVE2013_1", comparison1 });

            generateErrorPlot("Errors of Monte-Carlo valuation of (OIS) forward rates vs. their expected values.", testToFileMap, "ois_forward_vs_spot_multi_curve_mid_vol_low_interest_multiplicative");

            comparison1 = new String[]{ "LIBOR Forward Rate Multi-Curve", "LIBOR Spot Rate Multi-Curve" };

            testToFileMap.put("1x1000", new Object[] { "csv/convergence_mid_vol_low_interest/INTEGRATED_EXPECTATIONM1x100015P40trueC0606MULTIPLICATIVE2013_1", comparison1 });
            testToFileMap.put("1x10000", new Object[] { "csv/convergence_mid_vol_low_interest/INTEGRATED_EXPECTATIONM1x1000015P40trueC0606MULTIPLICATIVE2013_1", comparison1 });
            testToFileMap.put("1x100000", new Object[] { "csv/convergence_mid_vol_low_interest/INTEGRATED_EXPECTATIONM1x10000015P40trueC0606MULTIPLICATIVE2013_1", comparison1 });
            testToFileMap.put("10x100000", new Object[] { "csv/convergence_mid_vol_low_interest/INTEGRATED_EXPECTATIONM10x10000015P40trueC0606MULTIPLICATIVE2013_1", comparison1 });

            generateErrorPlot("Errors of Monte-Carlo valuation of (IBOR) forward rates vs. their expected values in the multiplicative model.", testToFileMap, "libor_forward_vs_spot_multi_curve_mid_vol_low_interest_multiplicative");

            filesToProducts = new ArrayList<>();

            filesToProducts.add(new String[] { "csv/convergence_mid_vol_low_interest/INTEGRATED_EXPECTATIONM1x100015P40trueC0606MULTIPLICATIVE2013_1/initial_calibration", "OIS Forward Rate Multi-Curve", "Reference" });
            filesToProducts.add(new String[] { "csv/convergence_mid_vol_low_interest/INTEGRATED_EXPECTATIONM1x100015P40trueC0606MULTIPLICATIVE2013_1/initial_calibration", "OIS Spot Rate Multi-Curve", "1x1000" });
            filesToProducts.add(new String[] { "csv/convergence_mid_vol_low_interest/INTEGRATED_EXPECTATIONM1x1000015P40trueC0606MULTIPLICATIVE2013_1/initial_calibration", "OIS Spot Rate Multi-Curve", "1x10000" });
            filesToProducts.add(new String[] { "csv/convergence_mid_vol_low_interest/INTEGRATED_EXPECTATIONM1x10000015P40trueC0606MULTIPLICATIVE2013_1/initial_calibration", "OIS Spot Rate Multi-Curve", "1x100000" });
            filesToProducts.add(new String[] { "csv/convergence_mid_vol_low_interest/INTEGRATED_EXPECTATIONM10x10000015P40trueC0606MULTIPLICATIVE2013_1/initial_calibration", "OIS Spot Rate Multi-Curve", "10x100000" });

            filesToProducts.add(new String[] { "csv/convergence_mid_vol_low_interest/INTEGRATED_EXPECTATIONM1x100015P40trueC0606MULTIPLICATIVE2013_1/initial_calibration", "LIBOR Forward Rate Multi-Curve", "Reference" });
            filesToProducts.add(new String[] { "csv/convergence_mid_vol_low_interest/INTEGRATED_EXPECTATIONM1x100015P40trueC0606MULTIPLICATIVE2013_1/initial_calibration", "LIBOR Spot Rate Multi-Curve", "1x1000" });
            filesToProducts.add(new String[] { "csv/convergence_mid_vol_low_interest/INTEGRATED_EXPECTATIONM1x1000015P40trueC0606MULTIPLICATIVE2013_1/initial_calibration", "LIBOR Spot Rate Multi-Curve", "1x10000" });
            filesToProducts.add(new String[] { "csv/convergence_mid_vol_low_interest/INTEGRATED_EXPECTATIONM1x10000015P40trueC0606MULTIPLICATIVE2013_1/initial_calibration", "LIBOR Spot Rate Multi-Curve", "1x100000" });
            filesToProducts.add(new String[] { "csv/convergence_mid_vol_low_interest/INTEGRATED_EXPECTATIONM10x10000015P40trueC0606MULTIPLICATIVE2013_1/initial_calibration", "LIBOR Spot Rate Multi-Curve", "10x100000" });

            createPlot(0,4,
                    "Time [years]",
                    "Rate [\\\\%]",
                    "Valuation of IBOR and OIS forward rates in the multiplicative model with different amounts of Monte-Carlo paths.",
                    filesToProducts, fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            fileName = "libor_ois_forward_vs_spot_multi_curve_low_vol_high_interest_additive";

            createParamsTable("csv/convergence_low_vol_high_interest/INTEGRATED_EXPECTATIONM1x100015P40trueC0303ADDITIVE2010_1", fileName);

            testToFileMap = new LinkedHashMap<>();

            comparison1 = new String[]{ "OIS Forward Rate Multi-Curve", "OIS Spot Rate Multi-Curve" };

            testToFileMap.put("1x1000", new Object[] { "csv/convergence_low_vol_high_interest/INTEGRATED_EXPECTATIONM1x100015P40trueC0303ADDITIVE2010_1", comparison1 });
            testToFileMap.put("1x10000", new Object[] { "csv/convergence_low_vol_high_interest/INTEGRATED_EXPECTATIONM1x1000015P40trueC0303ADDITIVE2010_1", comparison1 });
            testToFileMap.put("1x100000", new Object[] { "csv/convergence_low_vol_high_interest/INTEGRATED_EXPECTATIONM1x10000015P40trueC0303ADDITIVE2010_1", comparison1 });
            testToFileMap.put("10x100000", new Object[] { "csv/convergence_low_vol_high_interest/INTEGRATED_EXPECTATIONM10x10000015P40trueC0303ADDITIVE2010_1", comparison1 });

            generateErrorPlot("Errors of Monte-Carlo valuation of (OIS) forward rates vs. their expected values.", testToFileMap, "ois_forward_vs_spot_multi_curve_low_vol_high_interest_additive");

            comparison1 = new String[]{ "LIBOR Forward Rate Multi-Curve", "LIBOR Spot Rate Multi-Curve" };

            testToFileMap.put("1x1000", new Object[] { "csv/convergence_low_vol_high_interest/INTEGRATED_EXPECTATIONM1x100015P40trueC0303ADDITIVE2010_1", comparison1 });
            testToFileMap.put("1x10000", new Object[] { "csv/convergence_low_vol_high_interest/INTEGRATED_EXPECTATIONM1x1000015P40trueC0303ADDITIVE2010_1", comparison1 });
            testToFileMap.put("1x100000", new Object[] { "csv/convergence_low_vol_high_interest/INTEGRATED_EXPECTATIONM1x10000015P40trueC0303ADDITIVE2010_1", comparison1 });
            testToFileMap.put("10x100000", new Object[] { "csv/convergence_low_vol_high_interest/INTEGRATED_EXPECTATIONM10x10000015P40trueC0303ADDITIVE2010_1", comparison1 });

            generateErrorPlot("Errors of Monte-Carlo valuation of (IBOR) forward rates vs. their expected values in the additive model.", testToFileMap, "libor_forward_vs_spot_multi_curve_low_vol_high_interest_additive");

            filesToProducts = new ArrayList<>();

            filesToProducts.add(new String[] { "csv/convergence_low_vol_high_interest/INTEGRATED_EXPECTATIONM1x100015P40trueC0303ADDITIVE2010_1/initial_calibration", "OIS Forward Rate Multi-Curve", "Reference" });
            filesToProducts.add(new String[] { "csv/convergence_low_vol_high_interest/INTEGRATED_EXPECTATIONM1x100015P40trueC0303ADDITIVE2010_1/initial_calibration", "OIS Spot Rate Multi-Curve", "1x1000" });
            filesToProducts.add(new String[] { "csv/convergence_low_vol_high_interest/INTEGRATED_EXPECTATIONM1x1000015P40trueC0303ADDITIVE2010_1/initial_calibration", "OIS Spot Rate Multi-Curve", "1x10000" });
            filesToProducts.add(new String[] { "csv/convergence_low_vol_high_interest/INTEGRATED_EXPECTATIONM1x10000015P40trueC0303ADDITIVE2010_1/initial_calibration", "OIS Spot Rate Multi-Curve", "1x100000" });
            filesToProducts.add(new String[] { "csv/convergence_low_vol_high_interest/INTEGRATED_EXPECTATIONM10x10000015P40trueC0303ADDITIVE2010_1/initial_calibration", "OIS Spot Rate Multi-Curve", "10x100000" });

            filesToProducts.add(new String[] { "csv/convergence_low_vol_high_interest/INTEGRATED_EXPECTATIONM1x100015P40trueC0303ADDITIVE2010_1/initial_calibration", "LIBOR Forward Rate Multi-Curve", "Reference" });
            filesToProducts.add(new String[] { "csv/convergence_low_vol_high_interest/INTEGRATED_EXPECTATIONM1x100015P40trueC0303ADDITIVE2010_1/initial_calibration", "LIBOR Spot Rate Multi-Curve", "1x1000" });
            filesToProducts.add(new String[] { "csv/convergence_low_vol_high_interest/INTEGRATED_EXPECTATIONM1x1000015P40trueC0303ADDITIVE2010_1/initial_calibration", "LIBOR Spot Rate Multi-Curve", "1x10000" });
            filesToProducts.add(new String[] { "csv/convergence_low_vol_high_interest/INTEGRATED_EXPECTATIONM1x10000015P40trueC0303ADDITIVE2010_1/initial_calibration", "LIBOR Spot Rate Multi-Curve", "1x100000" });
            filesToProducts.add(new String[] { "csv/convergence_low_vol_high_interest/INTEGRATED_EXPECTATIONM10x10000015P40trueC0303ADDITIVE2010_1/initial_calibration", "LIBOR Spot Rate Multi-Curve", "10x100000" });

            createPlot(0,4,
                    "Time [years]",
                    "Rate [\\\\%]",
                    "Valuation of IBOR and OIS forward rates in the additive model with different amounts of Monte-Carlo paths.",
                    filesToProducts, fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            fileName = "libor_ois_forward_vs_spot_multi_curve_low_vol_high_interest_multiplicative";

            createParamsTable("csv/convergence_low_vol_high_interest/INTEGRATED_EXPECTATIONM1x100015P40trueC0303MULTIPLICATIVE2010_1", fileName);

            testToFileMap = new LinkedHashMap<>();

            comparison1 = new String[]{ "OIS Forward Rate Multi-Curve", "OIS Spot Rate Multi-Curve" };

            testToFileMap.put("1x1000", new Object[] { "csv/convergence_low_vol_high_interest/INTEGRATED_EXPECTATIONM1x100015P40trueC0303MULTIPLICATIVE2010_1", comparison1 });
            testToFileMap.put("1x10000", new Object[] { "csv/convergence_low_vol_high_interest/INTEGRATED_EXPECTATIONM1x1000015P40trueC0303MULTIPLICATIVE2010_1", comparison1 });
            testToFileMap.put("1x100000", new Object[] { "csv/convergence_low_vol_high_interest/INTEGRATED_EXPECTATIONM1x10000015P40trueC0303MULTIPLICATIVE2010_1", comparison1 });
            testToFileMap.put("10x100000", new Object[] { "csv/convergence_low_vol_high_interest/INTEGRATED_EXPECTATIONM10x10000015P40trueC0303MULTIPLICATIVE2010_1", comparison1 });

            generateErrorPlot("Errors of Monte-Carlo valuation of (OIS) forward rates vs. their expected values.", testToFileMap, "ois_forward_vs_spot_multi_curve_low_vol_high_interest_multiplicative");

            comparison1 = new String[]{ "LIBOR Forward Rate Multi-Curve", "LIBOR Spot Rate Multi-Curve" };

            testToFileMap.put("1x1000", new Object[] { "csv/convergence_low_vol_high_interest/INTEGRATED_EXPECTATIONM1x100015P40trueC0303MULTIPLICATIVE2010_1", comparison1 });
            testToFileMap.put("1x10000", new Object[] { "csv/convergence_low_vol_high_interest/INTEGRATED_EXPECTATIONM1x1000015P40trueC0303MULTIPLICATIVE2010_1", comparison1 });
            testToFileMap.put("1x100000", new Object[] { "csv/convergence_low_vol_high_interest/INTEGRATED_EXPECTATIONM1x10000015P40trueC0303MULTIPLICATIVE2010_1", comparison1 });
            testToFileMap.put("10x100000", new Object[] { "csv/convergence_low_vol_high_interest/INTEGRATED_EXPECTATIONM10x10000015P40trueC0303MULTIPLICATIVE2010_1", comparison1 });

            generateErrorPlot("Errors of Monte-Carlo valuation of (IBOR) forward rates vs. their expected values in the multiplicative model.", testToFileMap, "libor_forward_vs_spot_multi_curve_low_vol_high_interest_multiplicative");

            filesToProducts = new ArrayList<>();

            filesToProducts.add(new String[] { "csv/convergence_low_vol_high_interest/INTEGRATED_EXPECTATIONM1x100015P40trueC0303MULTIPLICATIVE2010_1/initial_calibration", "OIS Forward Rate Multi-Curve", "Reference" });
            filesToProducts.add(new String[] { "csv/convergence_low_vol_high_interest/INTEGRATED_EXPECTATIONM1x100015P40trueC0303MULTIPLICATIVE2010_1/initial_calibration", "OIS Spot Rate Multi-Curve", "1x1000" });
            filesToProducts.add(new String[] { "csv/convergence_low_vol_high_interest/INTEGRATED_EXPECTATIONM1x1000015P40trueC0303MULTIPLICATIVE2010_1/initial_calibration", "OIS Spot Rate Multi-Curve", "1x10000" });
            filesToProducts.add(new String[] { "csv/convergence_low_vol_high_interest/INTEGRATED_EXPECTATIONM1x10000015P40trueC0303MULTIPLICATIVE2010_1/initial_calibration", "OIS Spot Rate Multi-Curve", "1x100000" });
            filesToProducts.add(new String[] { "csv/convergence_low_vol_high_interest/INTEGRATED_EXPECTATIONM10x10000015P40trueC0303MULTIPLICATIVE2010_1/initial_calibration", "OIS Spot Rate Multi-Curve", "10x100000" });

            filesToProducts.add(new String[] { "csv/convergence_low_vol_high_interest/INTEGRATED_EXPECTATIONM1x100015P40trueC0303MULTIPLICATIVE2010_1/initial_calibration", "LIBOR Forward Rate Multi-Curve", "Reference" });
            filesToProducts.add(new String[] { "csv/convergence_low_vol_high_interest/INTEGRATED_EXPECTATIONM1x100015P40trueC0303MULTIPLICATIVE2010_1/initial_calibration", "LIBOR Spot Rate Multi-Curve", "1x1000" });
            filesToProducts.add(new String[] { "csv/convergence_low_vol_high_interest/INTEGRATED_EXPECTATIONM1x1000015P40trueC0303MULTIPLICATIVE2010_1/initial_calibration", "LIBOR Spot Rate Multi-Curve", "1x10000" });
            filesToProducts.add(new String[] { "csv/convergence_low_vol_high_interest/INTEGRATED_EXPECTATIONM1x10000015P40trueC0303MULTIPLICATIVE2010_1/initial_calibration", "LIBOR Spot Rate Multi-Curve", "1x100000" });
            filesToProducts.add(new String[] { "csv/convergence_low_vol_high_interest/INTEGRATED_EXPECTATIONM10x10000015P40trueC0303MULTIPLICATIVE2010_1/initial_calibration", "LIBOR Spot Rate Multi-Curve", "10x100000" });

            createPlot(0,4,
                    "Time [years]",
                    "Rate [\\\\%]",
                    "Valuation of IBOR and OIS forward rates in the multiplicative model with different amounts of Monte-Carlo paths.",
                    filesToProducts, fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            fileName = "forward_rates";

            filesToProducts = new ArrayList<>();

            filesToProducts.add(new String[] { "csv/convergence_low_vol_high_interest/INTEGRATED_EXPECTATIONM1x100015P10trueC0303ADDITIVE2010_1/initial_calibration", "OIS Forward Rate Multi-Curve", "OIS High" });
            filesToProducts.add(new String[] { "csv/convergence_mid_vol_low_interest/INTEGRATED_EXPECTATIONM1x100015P10trueC0606ADDITIVE2013_1/initial_calibration", "OIS Forward Rate Multi-Curve", "OIS Low" });
            filesToProducts.add(new String[] { "csv/convergence_high_vol_negative_interest/INTEGRATED_EXPECTATIONM1x100015P10trueC0909ADDITIVE2015_1/initial_calibration", "OIS Forward Rate Multi-Curve", "OIS Negative" });
            filesToProducts.add(new String[] { "csv/convergence_low_vol_high_interest/INTEGRATED_EXPECTATIONM1x100015P10trueC0303ADDITIVE2010_1/initial_calibration", "LIBOR Forward Rate Multi-Curve", "IBOR High" });
            filesToProducts.add(new String[] { "csv/convergence_mid_vol_low_interest/INTEGRATED_EXPECTATIONM1x100015P10trueC0606ADDITIVE2013_1/initial_calibration", "LIBOR Forward Rate Multi-Curve", "IBOR Low" });
            filesToProducts.add(new String[] { "csv/convergence_high_vol_negative_interest/INTEGRATED_EXPECTATIONM1x100015P10trueC0909ADDITIVE2015_1/initial_calibration", "LIBOR Forward Rate Multi-Curve", "IBOR Negative" });

            createPlot(-0.5, 5.0,
                    "Time [years]",
                    "Forward Rate [\\\\%]",
                    "Different forward rates test scenarios.",
                    filesToProducts, fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            fileName = "caplet_vola_multi_curve_high_vol_negative_interest_additive";

            createParamsTable("csv/pricing_high_vol_negative_interest/MONTE_CARLOM10x10000015P40trueC0909ADDITIVE2015_4", fileName);

            testToFileMap = new LinkedHashMap<>();

            comparison1 = new String[] { "Caplet Vola Monte-Carlo Multi-Curve", "Caplet Vola Analytic Multi-Curve Integrated-Expectation" };
            comparison2 = new String[] { "Caplet Vola Monte-Carlo Multi-Curve", "Caplet Vola Analytic Multi-Curve Fenton-Wilkinson" };

            testToFileMap.put("Integrated-Variance", new Object[] { "csv/pricing_high_vol_negative_interest/MONTE_CARLOM10x10000015P40trueC0909ADDITIVE2015_4", comparison1 });
            testToFileMap.put("Fenton-Wilkinson", new Object[] { "csv/pricing_high_vol_negative_interest/MONTE_CARLOM10x10000015P40trueC0909ADDITIVE2015_4", comparison2 });

            generateErrorPlot("Errors of caplet valuation with different approximation techniques vs. their Monte-Carlo valuation (100,000 paths).", testToFileMap, fileName);

            filesToProducts = new ArrayList<>();

            filesToProducts.add(new String[] { "csv/pricing_high_vol_negative_interest/MONTE_CARLOM10x10000015P40trueC0909ADDITIVE2015_4/initial_calibration", "Caplet Vola Monte-Carlo Multi-Curve", "Monte-Carlo" });
            filesToProducts.add(new String[] { "csv/pricing_high_vol_negative_interest/MONTE_CARLOM10x10000015P40trueC0909ADDITIVE2015_4/initial_calibration", "Caplet Vola Analytic Multi-Curve Integrated-Expectation", "Integrated-Variance" });
            filesToProducts.add(new String[] { "csv/pricing_high_vol_negative_interest/MONTE_CARLOM10x10000015P40trueC0909ADDITIVE2015_4/initial_calibration", "Caplet Vola Analytic Multi-Curve Fenton-Wilkinson", "Fenton-Wilkinson" });

            createPlot(40, 120,
                    "Caplet Maturity [years]",
                    "Caplet Vola [\\\\%]",
                    "Valuation of ATM caplets with underlying (IBOR) forward rates. Analytical valuation through different approximation formulas and Monte-Carlo valuation (100,000 paths).",
                    filesToProducts, fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fileName = "caplet_vola_multi_curve_high_vol_negative_interest_multiplicative";

            createParamsTable("csv/pricing_high_vol_negative_interest/MONTE_CARLOM10x10000015P40trueC0909MULTIPLICATIVE2015_4", fileName);

            testToFileMap = new LinkedHashMap<>();

            comparison1 = new String[] { "Caplet Vola Monte-Carlo Multi-Curve", "Caplet Vola Analytic Multi-Curve Integrated-Expectation" };
            comparison2 = new String[] { "Caplet Vola Monte-Carlo Multi-Curve", "Caplet Vola Analytic Multi-Curve Fenton-Wilkinson" };

            testToFileMap.put("Integrated-Variance", new Object[] { "csv/pricing_high_vol_negative_interest/MONTE_CARLOM10x10000015P40trueC0909MULTIPLICATIVE2015_4", comparison1 });
            testToFileMap.put("Fenton-Wilkinson", new Object[] { "csv/pricing_high_vol_negative_interest/MONTE_CARLOM10x10000015P40trueC0909MULTIPLICATIVE2015_4", comparison2 });

            generateErrorPlot("Errors of caplet valuation with different approximation techniques vs. their Monte-Carlo valuation (100,000 paths).", testToFileMap, fileName);

            filesToProducts = new ArrayList<>();

            filesToProducts.add(new String[] { "csv/pricing_high_vol_negative_interest/MONTE_CARLOM10x10000015P40trueC0909MULTIPLICATIVE2015_4/initial_calibration", "Caplet Vola Monte-Carlo Multi-Curve", "Monte-Carlo" });
            filesToProducts.add(new String[] { "csv/pricing_high_vol_negative_interest/MONTE_CARLOM10x10000015P40trueC0909MULTIPLICATIVE2015_4/initial_calibration", "Caplet Vola Analytic Multi-Curve Integrated-Expectation", "Integrated-Variance" });
            filesToProducts.add(new String[] { "csv/pricing_high_vol_negative_interest/MONTE_CARLOM10x10000015P40trueC0909MULTIPLICATIVE2015_4/initial_calibration", "Caplet Vola Analytic Multi-Curve Fenton-Wilkinson", "Fenton-Wilkinson" });

            createPlot(40, 120,
                    "Caplet Maturity [years]",
                    "Caplet Vola [\\\\%]",
                    "Valuation of ATM caplets with underlying (IBOR) forward rates. Analytical valuation through different approximation formulas and Monte-Carlo valuation (100,000 paths).",
                    filesToProducts, fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            fileName = "caplet_vola_multi_curve_mid_vol_low_interest_additive";

            createParamsTable("csv/pricing_mid_vol_low_interest/MONTE_CARLOM10x10000015P40trueC0606ADDITIVE2013_4", fileName);

            testToFileMap = new LinkedHashMap<>();

            comparison1 = new String[] { "Caplet Vola Monte-Carlo Multi-Curve", "Caplet Vola Analytic Multi-Curve Integrated-Expectation" };
            comparison2 = new String[] { "Caplet Vola Monte-Carlo Multi-Curve", "Caplet Vola Analytic Multi-Curve Fenton-Wilkinson" };

            testToFileMap.put("Integrated-Variance", new Object[] { "csv/pricing_mid_vol_low_interest/MONTE_CARLOM10x10000015P40trueC0606ADDITIVE2013_4", comparison1 });
            testToFileMap.put("Fenton-Wilkinson", new Object[] { "csv/pricing_mid_vol_low_interest/MONTE_CARLOM10x10000015P40trueC0606ADDITIVE2013_4", comparison2 });

            generateErrorPlot("Errors of caplet valuation with different approximation techniques vs. their Monte-Carlo valuation (100,000 paths).", testToFileMap, fileName);

            filesToProducts = new ArrayList<>();

            filesToProducts.add(new String[] { "csv/pricing_mid_vol_low_interest/MONTE_CARLOM10x10000015P40trueC0606ADDITIVE2013_4/initial_calibration", "Caplet Vola Monte-Carlo Multi-Curve", "Monte-Carlo" });
            filesToProducts.add(new String[] { "csv/pricing_mid_vol_low_interest/MONTE_CARLOM10x10000015P40trueC0606ADDITIVE2013_4/initial_calibration", "Caplet Vola Analytic Multi-Curve Integrated-Expectation", "Integrated-Variance" });
            filesToProducts.add(new String[] { "csv/pricing_mid_vol_low_interest/MONTE_CARLOM10x10000015P40trueC0606ADDITIVE2013_4/initial_calibration", "Caplet Vola Analytic Multi-Curve Fenton-Wilkinson", "Fenton-Wilkinson" });

            createPlot(30, 70,
                    "Caplet Maturity [years]",
                    "Caplet Vola [\\\\%]",
                    "Valuation of ATM caplets with underlying (IBOR) forward rates. Analytical valuation through different approximation formulas and Monte-Carlo valuation (100,000 paths).",
                    filesToProducts, fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fileName = "caplet_vola_multi_curve_mid_vol_low_interest_multiplicative";

            createParamsTable("csv/pricing_mid_vol_low_interest/MONTE_CARLOM10x10000015P40trueC0606MULTIPLICATIVE2013_4", fileName);

            testToFileMap = new LinkedHashMap<>();

            comparison1 = new String[] { "Caplet Vola Monte-Carlo Multi-Curve", "Caplet Vola Analytic Multi-Curve Integrated-Expectation" };
            comparison2 = new String[] { "Caplet Vola Monte-Carlo Multi-Curve", "Caplet Vola Analytic Multi-Curve Fenton-Wilkinson" };

            testToFileMap.put("Integrated-Variance", new Object[] { "csv/pricing_mid_vol_low_interest/MONTE_CARLOM10x10000015P40trueC0606MULTIPLICATIVE2013_4", comparison1 });
            testToFileMap.put("Fenton-Wilkinson", new Object[] { "csv/pricing_mid_vol_low_interest/MONTE_CARLOM10x10000015P40trueC0606MULTIPLICATIVE2013_4", comparison2 });

            generateErrorPlot("Errors of caplet valuation with different approximation techniques vs. their Monte-Carlo valuation (100,000 paths).", testToFileMap, fileName);

            filesToProducts = new ArrayList<>();

            filesToProducts.add(new String[] { "csv/pricing_mid_vol_low_interest/MONTE_CARLOM10x10000015P40trueC0606MULTIPLICATIVE2013_4/initial_calibration", "Caplet Vola Monte-Carlo Multi-Curve", "Monte-Carlo" });
            filesToProducts.add(new String[] { "csv/pricing_mid_vol_low_interest/MONTE_CARLOM10x10000015P40trueC0606MULTIPLICATIVE2013_4/initial_calibration", "Caplet Vola Analytic Multi-Curve Integrated-Expectation", "Integrated-Variance" });
            filesToProducts.add(new String[] { "csv/pricing_mid_vol_low_interest/MONTE_CARLOM10x10000015P40trueC0606MULTIPLICATIVE2013_4/initial_calibration", "Caplet Vola Analytic Multi-Curve Fenton-Wilkinson", "Fenton-Wilkinson" });

            createPlot(30, 70,
                    "Caplet Maturity [years]",
                    "Caplet Vola [\\\\%]",
                    "Valuation of ATM caplets with underlying (IBOR) forward rates. Analytical valuation through different approximation formulas and Monte-Carlo valuation (100,000 paths).",
                    filesToProducts, fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            fileName = "caplet_vola_multi_curve_low_vol_high_interest_additive";

            createParamsTable("csv/pricing_low_vol_high_interest/MONTE_CARLOM10x10000015P40trueC0303ADDITIVE2010_4", fileName);

            testToFileMap = new LinkedHashMap<>();

            comparison1 = new String[] { "Caplet Vola Monte-Carlo Multi-Curve", "Caplet Vola Analytic Multi-Curve Integrated-Expectation" };
            comparison2 = new String[] { "Caplet Vola Monte-Carlo Multi-Curve", "Caplet Vola Analytic Multi-Curve Fenton-Wilkinson" };

            testToFileMap.put("Integrated-Variance", new Object[] { "csv/pricing_low_vol_high_interest/MONTE_CARLOM10x10000015P40trueC0303ADDITIVE2010_4", comparison1 });
            testToFileMap.put("Fenton-Wilkinson", new Object[] { "csv/pricing_low_vol_high_interest/MONTE_CARLOM10x10000015P40trueC0303ADDITIVE2010_4", comparison2 });

            generateErrorPlot("Errors of caplet valuation with different approximation techniques vs. their Monte-Carlo valuation (100,000 paths).", testToFileMap, fileName);

            filesToProducts = new ArrayList<>();

            filesToProducts.add(new String[] { "csv/pricing_low_vol_high_interest/MONTE_CARLOM10x10000015P40trueC0303ADDITIVE2010_4/initial_calibration", "Caplet Vola Monte-Carlo Multi-Curve", "Monte-Carlo" });
            filesToProducts.add(new String[] { "csv/pricing_low_vol_high_interest/MONTE_CARLOM10x10000015P40trueC0303ADDITIVE2010_4/initial_calibration", "Caplet Vola Analytic Multi-Curve Integrated-Expectation", "Integrated-Variance" });
            filesToProducts.add(new String[] { "csv/pricing_low_vol_high_interest/MONTE_CARLOM10x10000015P40trueC0303ADDITIVE2010_4/initial_calibration", "Caplet Vola Analytic Multi-Curve Fenton-Wilkinson", "Fenton-Wilkinson" });

            createPlot(10, 40,
                    "Caplet Maturity [years]",
                    "Caplet Vola [\\\\%]",
                    "Valuation of ATM caplets with underlying (IBOR) forward rates. Analytical valuation through different approximation formulas and Monte-Carlo valuation (100,000 paths).",
                    filesToProducts, fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fileName = "caplet_vola_multi_curve_low_vol_high_interest_multiplicative";

            createParamsTable("csv/pricing_low_vol_high_interest/MONTE_CARLOM10x10000015P40trueC0303MULTIPLICATIVE2010_4", fileName);

            testToFileMap = new LinkedHashMap<>();

            comparison1 = new String[] { "Caplet Vola Monte-Carlo Multi-Curve", "Caplet Vola Analytic Multi-Curve Integrated-Expectation" };
            comparison2 = new String[] { "Caplet Vola Monte-Carlo Multi-Curve", "Caplet Vola Analytic Multi-Curve Fenton-Wilkinson" };

            testToFileMap.put("Integrated-Variance", new Object[] { "csv/pricing_low_vol_high_interest/MONTE_CARLOM10x10000015P40trueC0303MULTIPLICATIVE2010_4", comparison1 });
            testToFileMap.put("Fenton-Wilkinson", new Object[] { "csv/pricing_low_vol_high_interest/MONTE_CARLOM10x10000015P40trueC0303MULTIPLICATIVE2010_4", comparison2 });

            generateErrorPlot("Errors of caplet valuation with different approximation techniques vs. their Monte-Carlo valuation (100,000 paths).", testToFileMap, fileName);

            filesToProducts = new ArrayList<>();

            filesToProducts.add(new String[] { "csv/pricing_low_vol_high_interest/MONTE_CARLOM10x10000015P40trueC0303MULTIPLICATIVE2010_4/initial_calibration", "Caplet Vola Monte-Carlo Multi-Curve", "Monte-Carlo" });
            filesToProducts.add(new String[] { "csv/pricing_low_vol_high_interest/MONTE_CARLOM10x10000015P40trueC0303MULTIPLICATIVE2010_4/initial_calibration", "Caplet Vola Analytic Multi-Curve Integrated-Expectation", "Integrated-Variance" });
            filesToProducts.add(new String[] { "csv/pricing_low_vol_high_interest/MONTE_CARLOM10x10000015P40trueC0303MULTIPLICATIVE2010_4/initial_calibration", "Caplet Vola Analytic Multi-Curve Fenton-Wilkinson", "Fenton-Wilkinson" });

            createPlot(10, 40,
                    "Caplet Maturity [years]",
                    "Caplet Vola [\\\\%]",
                    "Valuation of ATM caplets with underlying (IBOR) forward rates. Analytical valuation through different approximation formulas and Monte-Carlo valuation (100,000 paths).",
                    filesToProducts, fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            fileName = "swaption_vola_multi_curve_high_vol_negative_interest_additive";

            createParamsTable("csv/pricing_high_vol_negative_interest/MONTE_CARLOM10x10000015P40trueC0909ADDITIVE2015_4", fileName);

            testToFileMap = new LinkedHashMap<>();

            comparison1 = new String[] { "Swaption Vola Monte-Carlo Multi-Curve", "Swaption Vola Analytic Multi-Curve Drift-Freeze" };
            comparison2 = new String[] { "Swaption Vola Monte-Carlo Multi-Curve", "Swaption Vola Analytic Multi-Curve Integrated-Expectation" };

            testToFileMap.put("Drift-Freeze", new Object[] { "csv/pricing_high_vol_negative_interest/MONTE_CARLOM10x10000015P40trueC0909ADDITIVE2015_4", comparison1 });
            testToFileMap.put("Integrated-Expectation", new Object[] { "csv/pricing_high_vol_negative_interest/MONTE_CARLOM10x10000015P40trueC0909ADDITIVE2015_4", comparison2 });

            generateErrorPlot("Errors of swaption valuation with different approximation techniques vs. their Monte-Carlo valuation (100,000 paths).", testToFileMap, fileName);

            filesToProducts = new ArrayList<>();

            filesToProducts.add(new String[] { "csv/pricing_high_vol_negative_interest/MONTE_CARLOM10x10000015P40trueC0909ADDITIVE2015_4/initial_calibration", "Swaption Vola Monte-Carlo Multi-Curve", "Monte-Carlo" });
            filesToProducts.add(new String[] { "csv/pricing_high_vol_negative_interest/MONTE_CARLOM10x10000015P40trueC0909ADDITIVE2015_4/initial_calibration", "Swaption Vola Analytic Multi-Curve Drift-Freeze", "Drift-Freeze" });
            filesToProducts.add(new String[] { "csv/pricing_high_vol_negative_interest/MONTE_CARLOM10x10000015P40trueC0909ADDITIVE2015_4/initial_calibration", "Swaption Vola Analytic Multi-Curve Integrated-Expectation", "Integrated-Expectation" });

            createPlot(0,120,
                    "Swap Maturity x Number of Periods [years]",
                    "Swaption Vola [\\\\%]",
                    "Valuation of ATM swaptions with underlying (IBOR) swap rates. Analytical valuation through different approximation formulas and Monte-Carlo valuation (100,000 paths).",
                    filesToProducts, fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fileName = "swaption_vola_multi_curve_high_vol_negative_interest_multiplicative";

            createParamsTable("csv/pricing_high_vol_negative_interest/MONTE_CARLOM10x10000015P40trueC0909MULTIPLICATIVE2015_4", fileName);

            testToFileMap = new LinkedHashMap<>();

            comparison1 = new String[] { "Swaption Vola Monte-Carlo Multi-Curve", "Swaption Vola Analytic Multi-Curve Drift-Freeze" };
            comparison2 = new String[] { "Swaption Vola Monte-Carlo Multi-Curve", "Swaption Vola Analytic Multi-Curve Integrated-Expectation" };

            testToFileMap.put("Drift-Freeze", new Object[] { "csv/pricing_high_vol_negative_interest/MONTE_CARLOM10x10000015P40trueC0909MULTIPLICATIVE2015_4", comparison1 });
            testToFileMap.put("Integrated-Expectation", new Object[] { "csv/pricing_high_vol_negative_interest/MONTE_CARLOM10x10000015P40trueC0909MULTIPLICATIVE2015_4", comparison2 });

            generateErrorPlot("Errors of swaption valuation with different approximation techniques vs. their Monte-Carlo valuation (100,000 paths).", testToFileMap, fileName);

            filesToProducts = new ArrayList<>();

            filesToProducts.add(new String[] { "csv/pricing_high_vol_negative_interest/MONTE_CARLOM10x10000015P40trueC0909MULTIPLICATIVE2015_4/initial_calibration", "Swaption Vola Monte-Carlo Multi-Curve", "Monte-Carlo" });
            filesToProducts.add(new String[] { "csv/pricing_high_vol_negative_interest/MONTE_CARLOM10x10000015P40trueC0909MULTIPLICATIVE2015_4/initial_calibration", "Swaption Vola Analytic Multi-Curve Drift-Freeze", "Drift-Freeze" });
            filesToProducts.add(new String[] { "csv/pricing_high_vol_negative_interest/MONTE_CARLOM10x10000015P40trueC0909MULTIPLICATIVE2015_4/initial_calibration", "Swaption Vola Analytic Multi-Curve Integrated-Expectation", "Integrated-Expectation" });

            createPlot(0,120,
                    "Swap Maturity x Number of Periods [years]",
                    "Swaption Vola [\\\\%]",
                    "Valuation of ATM swaptions with underlying (IBOR) swap rates. Analytical valuation through different approximation formulas and Monte-Carlo valuation (100,000 paths).",
                    filesToProducts, fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            fileName = "swaption_vola_multi_curve_mid_vol_low_interest_additive";

            createParamsTable("csv/pricing_mid_vol_low_interest/MONTE_CARLOM10x10000015P40trueC0606ADDITIVE2013_4", fileName);

            testToFileMap = new LinkedHashMap<>();

            comparison1 = new String[] { "Swaption Vola Monte-Carlo Multi-Curve", "Swaption Vola Analytic Multi-Curve Drift-Freeze" };
            comparison2 = new String[] { "Swaption Vola Monte-Carlo Multi-Curve", "Swaption Vola Analytic Multi-Curve Integrated-Expectation" };

            testToFileMap.put("Drift-Freeze", new Object[] { "csv/pricing_mid_vol_low_interest/MONTE_CARLOM10x10000015P40trueC0606ADDITIVE2013_4", comparison1 });
            testToFileMap.put("Integrated-Expectation", new Object[] { "csv/pricing_mid_vol_low_interest/MONTE_CARLOM10x10000015P40trueC0606ADDITIVE2013_4", comparison2 });

            generateErrorPlot("Errors of swaption valuation with different approximation techniques vs. their Monte-Carlo valuation (100,000 paths).", testToFileMap, fileName);

            filesToProducts = new ArrayList<>();

            filesToProducts.add(new String[] { "csv/pricing_mid_vol_low_interest/MONTE_CARLOM10x10000015P40trueC0606ADDITIVE2013_4/initial_calibration", "Swaption Vola Monte-Carlo Multi-Curve", "Monte-Carlo" });
            filesToProducts.add(new String[] { "csv/pricing_mid_vol_low_interest/MONTE_CARLOM10x10000015P40trueC0606ADDITIVE2013_4/initial_calibration", "Swaption Vola Analytic Multi-Curve Drift-Freeze", "Drift-Freeze" });
            filesToProducts.add(new String[] { "csv/pricing_mid_vol_low_interest/MONTE_CARLOM10x10000015P40trueC0606ADDITIVE2013_4/initial_calibration", "Swaption Vola Analytic Multi-Curve Integrated-Expectation", "Integrated-Expectation" });

            createPlot(0,80,
                    "Swap Maturity x Number of Periods [years]",
                    "Swaption Vola [\\\\%]",
                    "Valuation of ATM swaptions with underlying (IBOR) swap rates. Analytical valuation through different approximation formulas and Monte-Carlo valuation (100,000 paths).",
                    filesToProducts, fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fileName = "swaption_vola_multi_curve_mid_vol_low_interest_multiplicative";

            createParamsTable("csv/pricing_mid_vol_low_interest/MONTE_CARLOM10x10000015P40trueC0606MULTIPLICATIVE2013_4", fileName);

            testToFileMap = new LinkedHashMap<>();

            comparison1 = new String[] { "Swaption Vola Monte-Carlo Multi-Curve", "Swaption Vola Analytic Multi-Curve Drift-Freeze" };
            comparison2 = new String[] { "Swaption Vola Monte-Carlo Multi-Curve", "Swaption Vola Analytic Multi-Curve Integrated-Expectation" };

            testToFileMap.put("Drift-Freeze", new Object[] { "csv/pricing_mid_vol_low_interest/MONTE_CARLOM10x10000015P40trueC0606MULTIPLICATIVE2013_4", comparison1 });
            testToFileMap.put("Integrated-Expectation", new Object[] { "csv/pricing_mid_vol_low_interest/MONTE_CARLOM10x10000015P40trueC0606MULTIPLICATIVE2013_4", comparison2 });

            generateErrorPlot("Errors of swaption valuation with different approximation techniques vs. their Monte-Carlo valuation (100,000 paths).", testToFileMap, fileName);

            filesToProducts = new ArrayList<>();

            filesToProducts.add(new String[] { "csv/pricing_mid_vol_low_interest/MONTE_CARLOM10x10000015P40trueC0606MULTIPLICATIVE2013_4/initial_calibration", "Swaption Vola Monte-Carlo Multi-Curve", "Monte-Carlo" });
            filesToProducts.add(new String[] { "csv/pricing_mid_vol_low_interest/MONTE_CARLOM10x10000015P40trueC0606MULTIPLICATIVE2013_4/initial_calibration", "Swaption Vola Analytic Multi-Curve Drift-Freeze", "Drift-Freeze" });
            filesToProducts.add(new String[] { "csv/pricing_mid_vol_low_interest/MONTE_CARLOM10x10000015P40trueC0606MULTIPLICATIVE2013_4/initial_calibration", "Swaption Vola Analytic Multi-Curve Integrated-Expectation", "Integrated-Expectation" });

            createPlot(0,80,
                    "Swap Maturity x Number of Periods [years]",
                    "Swaption Vola [\\\\%]",
                    "Valuation of ATM swaptions with underlying (IBOR) swap rates. Analytical valuation through different approximation formulas and Monte-Carlo valuation (100,000 paths).",
                    filesToProducts, fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            fileName = "swaption_vola_multi_curve_low_vol_high_interest_additive";

            createParamsTable("csv/pricing_low_vol_high_interest/MONTE_CARLOM10x10000015P40trueC0303ADDITIVE2010_4", fileName);

            testToFileMap = new LinkedHashMap<>();

            comparison1 = new String[] { "Swaption Vola Monte-Carlo Multi-Curve", "Swaption Vola Analytic Multi-Curve Drift-Freeze" };
            comparison2 = new String[] { "Swaption Vola Monte-Carlo Multi-Curve", "Swaption Vola Analytic Multi-Curve Integrated-Expectation" };

            testToFileMap.put("Drift-Freeze", new Object[] { "csv/pricing_low_vol_high_interest/MONTE_CARLOM10x10000015P40trueC0303ADDITIVE2010_4", comparison1 });
            testToFileMap.put("Integrated-Expectation", new Object[] { "csv/pricing_low_vol_high_interest/MONTE_CARLOM10x10000015P40trueC0303ADDITIVE2010_4", comparison2 });

            generateErrorPlot("Errors of swaption valuation with different approximation techniques vs. their Monte-Carlo valuation (100,000 paths).", testToFileMap, fileName);

            filesToProducts = new ArrayList<>();

            filesToProducts.add(new String[] { "csv/pricing_low_vol_high_interest/MONTE_CARLOM10x10000015P40trueC0303ADDITIVE2010_4/initial_calibration", "Swaption Vola Monte-Carlo Multi-Curve", "Monte-Carlo" });
            filesToProducts.add(new String[] { "csv/pricing_low_vol_high_interest/MONTE_CARLOM10x10000015P40trueC0303ADDITIVE2010_4/initial_calibration", "Swaption Vola Analytic Multi-Curve Drift-Freeze", "Drift-Freeze" });
            filesToProducts.add(new String[] { "csv/pricing_low_vol_high_interest/MONTE_CARLOM10x10000015P40trueC0303ADDITIVE2010_4/initial_calibration", "Swaption Vola Analytic Multi-Curve Integrated-Expectation", "Integrated-Expectation" });

            createPlot(0,40,
                    "Swap Maturity x Number of Periods [years]",
                    "Swaption Vola [\\\\%]",
                    "Valuation of ATM swaptions with underlying (IBOR) swap rates. Analytical valuation through different approximation formulas and Monte-Carlo valuation (100,000 paths).",
                    filesToProducts, fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fileName = "swaption_vola_multi_curve_low_vol_high_interest_multiplicative";

            createParamsTable("csv/pricing_low_vol_high_interest/MONTE_CARLOM10x10000015P40trueC0303MULTIPLICATIVE2010_4", fileName);

            testToFileMap = new LinkedHashMap<>();

            comparison1 = new String[] { "Swaption Vola Monte-Carlo Multi-Curve", "Swaption Vola Analytic Multi-Curve Drift-Freeze" };
            comparison2 = new String[] { "Swaption Vola Monte-Carlo Multi-Curve", "Swaption Vola Analytic Multi-Curve Integrated-Expectation" };

            testToFileMap.put("Drift-Freeze", new Object[] { "csv/pricing_low_vol_high_interest/MONTE_CARLOM10x10000015P40trueC0303MULTIPLICATIVE2010_4", comparison1 });
            testToFileMap.put("Integrated-Expectation", new Object[] { "csv/pricing_low_vol_high_interest/MONTE_CARLOM10x10000015P40trueC0303MULTIPLICATIVE2010_4", comparison2 });

            generateErrorPlot("Errors of swaption valuation with different approximation techniques vs. their Monte-Carlo valuation (100,000 paths).", testToFileMap, fileName);

            filesToProducts = new ArrayList<>();

            filesToProducts.add(new String[] { "csv/pricing_low_vol_high_interest/MONTE_CARLOM10x10000015P40trueC0303MULTIPLICATIVE2010_4/initial_calibration", "Swaption Vola Monte-Carlo Multi-Curve", "Monte-Carlo" });
            filesToProducts.add(new String[] { "csv/pricing_low_vol_high_interest/MONTE_CARLOM10x10000015P40trueC0303MULTIPLICATIVE2010_4/initial_calibration", "Swaption Vola Analytic Multi-Curve Drift-Freeze", "Drift-Freeze" });
            filesToProducts.add(new String[] { "csv/pricing_low_vol_high_interest/MONTE_CARLOM10x10000015P40trueC0303MULTIPLICATIVE2010_4/initial_calibration", "Swaption Vola Analytic Multi-Curve Integrated-Expectation", "Integrated-Expectation" });

            createPlot(0,40,
                    "Swap Maturity x Number of Periods [years]",
                    "Swaption Vola [\\\\%]",
                    "Valuation of ATM swaptions with underlying (IBOR) swap rates. Analytical valuation through different approximation formulas and Monte-Carlo valuation (100,000 paths).",
                    filesToProducts, fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            fileName = "caplets_perfectly_correlated_spread_multiplicative";

            createParamsTable("csv/perfectly_correlated_uncorrelated_spread/MONTE_CARLOM10x10000015P8trueC0505MULTIPLICATIVEsynth_1", fileName);

            filesToProducts = new ArrayList<>();

            filesToProducts.add(new String[] { "csv/perfectly_correlated_uncorrelated_spread/MONTE_CARLOM10x10000015P8trueC0505MULTIPLICATIVEsynth_1/initial_calibration", "Caplet Vola Monte-Carlo Single-Curve", "Monte-Carlo Signle-Curve" });
            filesToProducts.add(new String[] { "csv/perfectly_correlated_uncorrelated_spread/MONTE_CARLOM10x10000015P8trueC0505MULTIPLICATIVEsynth_1/initial_calibration", "Caplet Vola Monte-Carlo Multi-Curve", "Monte-Carlo Multi-Curve" });
            filesToProducts.add(new String[] { "csv/perfectly_correlated_uncorrelated_spread/MONTE_CARLOM10x10000015P8trueC0505MULTIPLICATIVEsynth_1/initial_calibration", "Caplet Vola Analytic Single-Curve", "Analytic Single-Curve" });
            filesToProducts.add(new String[] { "csv/perfectly_correlated_uncorrelated_spread/MONTE_CARLOM10x10000015P8trueC0505MULTIPLICATIVEsynth_1/initial_calibration", "Caplet Vola Analytic Multi-Curve Integrated-Expectation", "Analytic Multi-Curve" });
            filesToProducts.add(new String[] { "csv/perfectly_correlated_uncorrelated_spread/MONTE_CARLOM10x10000015P8trueC0505MULTIPLICATIVEsynth_1/initial_calibration", "Caplet Vola Analytic Multi-Curve Fenton-Wilkinson", "Analytic Multi-Curve" });

            createPlot(40,65,
                    "Caplet Maturity [years]",
                    "Caplet Vola [\\\\%]",
                    "Valuation of ATM caplets in the single-curve model compared to the valuation in a multi-curve model where with perfectly correlated spread.",
                    filesToProducts, fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            fileName = "caplets_uncorrelated_spread_multiplicative";

            createParamsTable("csv/perfectly_correlated_uncorrelated_spread/MONTE_CARLOM10x10000015P8trueC0505MULTIPLICATIVEsynth_3", fileName);

            filesToProducts = new ArrayList<>();

            filesToProducts.add(new String[] { "csv/perfectly_correlated_uncorrelated_spread/MONTE_CARLOM10x10000015P8trueC0505MULTIPLICATIVEsynth_3/initial_calibration", "Caplet Vola Monte-Carlo Single-Curve", "Monte-Carlo Signle-Curve" });
            filesToProducts.add(new String[] { "csv/perfectly_correlated_uncorrelated_spread/MONTE_CARLOM10x10000015P8trueC0505MULTIPLICATIVEsynth_3/initial_calibration", "Caplet Vola Monte-Carlo Multi-Curve", "Monte-Carlo Multi-Curve" });
            filesToProducts.add(new String[] { "csv/perfectly_correlated_uncorrelated_spread/MONTE_CARLOM10x10000015P8trueC0505MULTIPLICATIVEsynth_3/initial_calibration", "Caplet Vola Analytic Single-Curve", "Analytic Single-Curve" });
            filesToProducts.add(new String[] { "csv/perfectly_correlated_uncorrelated_spread/MONTE_CARLOM10x10000015P8trueC0505MULTIPLICATIVEsynth_3/initial_calibration", "Caplet Vola Analytic Multi-Curve Integrated-Expectation", "Analytic Multi-Curve" });
            filesToProducts.add(new String[] { "csv/perfectly_correlated_uncorrelated_spread/MONTE_CARLOM10x10000015P8trueC0505MULTIPLICATIVEsynth_3/initial_calibration", "Caplet Vola Analytic Multi-Curve Fenton-Wilkinson", "Analytic Multi-Curve" });

            createPlot(30,65,
                    "Caplet Maturity [years]",
                    "Caplet Vola [\\\\%]",
                    "Valuation of ATM caplets in the single-curve model compared to the valuation in a multi-curve model where with perfectly correlated spread.",
                    filesToProducts, fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            fileName = "swaptions_perfectly_correlated_spread_multiplicative";

            createParamsTable("csv/perfectly_correlated_uncorrelated_spread/MONTE_CARLOM10x10000015P8trueC0505MULTIPLICATIVEsynth_1", fileName);

            filesToProducts = new ArrayList<>();

            filesToProducts.add(new String[] { "csv/perfectly_correlated_uncorrelated_spread/MONTE_CARLOM10x10000015P8trueC0505MULTIPLICATIVEsynth_1/initial_calibration", "Swaption Vola Monte-Carlo Single-Curve", "Monte-Carlo Signle-Curve" });
            filesToProducts.add(new String[] { "csv/perfectly_correlated_uncorrelated_spread/MONTE_CARLOM10x10000015P8trueC0505MULTIPLICATIVEsynth_1/initial_calibration", "Swaption Vola Monte-Carlo Multi-Curve", "Monte-Carlo Multi-Curve" });
            filesToProducts.add(new String[] { "csv/perfectly_correlated_uncorrelated_spread/MONTE_CARLOM10x10000015P8trueC0505MULTIPLICATIVEsynth_1/initial_calibration", "Swaption Vola Analytic Single-Curve", "Analytic Single-Curve" });
            filesToProducts.add(new String[] { "csv/perfectly_correlated_uncorrelated_spread/MONTE_CARLOM10x10000015P8trueC0505MULTIPLICATIVEsynth_1/initial_calibration", "Swaption Vola Analytic Multi-Curve Drift-Freeze", "Analytic Multi-Curve" });

            createPlot(40,65,
                    "Swap Maturity x Number of Periods [years]",
                    "Swaption Vola [\\\\%]",
                    "Valuation of ATM swaptions in the single-curve model compared to the valuation in a multi-curve model where with perfectly correlated spread.",
                    filesToProducts, fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            fileName = "swaptions_uncorrelated_spread_multiplicative";

            createParamsTable("csv/perfectly_correlated_uncorrelated_spread/MONTE_CARLOM10x10000015P8trueC0505MULTIPLICATIVEsynth_3", fileName);

            filesToProducts = new ArrayList<>();

            filesToProducts.add(new String[] { "csv/perfectly_correlated_uncorrelated_spread/MONTE_CARLOM10x10000015P8trueC0505MULTIPLICATIVEsynth_3/initial_calibration", "Swaption Vola Monte-Carlo Single-Curve", "Monte-Carlo Signle-Curve" });
            filesToProducts.add(new String[] { "csv/perfectly_correlated_uncorrelated_spread/MONTE_CARLOM10x10000015P8trueC0505MULTIPLICATIVEsynth_3/initial_calibration", "Swaption Vola Monte-Carlo Multi-Curve", "Monte-Carlo Multi-Curve" });
            filesToProducts.add(new String[] { "csv/perfectly_correlated_uncorrelated_spread/MONTE_CARLOM10x10000015P8trueC0505MULTIPLICATIVEsynth_3/initial_calibration", "Swaption Vola Analytic Single-Curve", "Analytic Single-Curve" });
            filesToProducts.add(new String[] { "csv/perfectly_correlated_uncorrelated_spread/MONTE_CARLOM10x10000015P8trueC0505MULTIPLICATIVEsynth_3/initial_calibration", "Swaption Vola Analytic Multi-Curve Drift-Freeze", "Analytic Multi-Curve" });

            createPlot(30,65,
                    "Swap Maturity x Number of Periods [years]",
                    "Swaption Vola [\\\\%]",
                    "Valuation of ATM swaptions in the single-curve model compared to the valuation in a multi-curve model where with perfectly correlated spread.",
                    filesToProducts, fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            fileName = "caplets_perfectly_correlated_spread_additive";

            createParamsTable("csv/perfectly_correlated_uncorrelated_spread/MONTE_CARLOM10x10000015P8trueC0505ADDITIVEsynth_1", fileName);

            filesToProducts = new ArrayList<>();

            filesToProducts.add(new String[] { "csv/perfectly_correlated_uncorrelated_spread/MONTE_CARLOM10x10000015P8trueC0505ADDITIVEsynth_1/initial_calibration", "Caplet Vola Monte-Carlo Single-Curve", "Monte-Carlo Signle-Curve" });
            filesToProducts.add(new String[] { "csv/perfectly_correlated_uncorrelated_spread/MONTE_CARLOM10x10000015P8trueC0505ADDITIVEsynth_1/initial_calibration", "Caplet Vola Monte-Carlo Multi-Curve", "Monte-Carlo Multi-Curve" });
            filesToProducts.add(new String[] { "csv/perfectly_correlated_uncorrelated_spread/MONTE_CARLOM10x10000015P8trueC0505ADDITIVEsynth_1/initial_calibration", "Caplet Vola Analytic Single-Curve", "Analytic Single-Curve" });
            filesToProducts.add(new String[] { "csv/perfectly_correlated_uncorrelated_spread/MONTE_CARLOM10x10000015P8trueC0505ADDITIVEsynth_1/initial_calibration", "Caplet Vola Analytic Multi-Curve Integrated-Expectation", "Analytic Multi-Curve" });
            filesToProducts.add(new String[] { "csv/perfectly_correlated_uncorrelated_spread/MONTE_CARLOM10x10000015P8trueC0505ADDITIVEsynth_1/initial_calibration", "Caplet Vola Analytic Multi-Curve Fenton-Wilkinson", "Analytic Multi-Curve" });

            createPlot(40,65,
                    "Caplet Maturity [years]",
                    "Caplet Vola [\\\\%]",
                    "Valuation of ATM caplets in the single-curve model compared to the valuation in a multi-curve model where with perfectly correlated spread.",
                    filesToProducts, fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            fileName = "caplets_uncorrelated_spread_additive";

            createParamsTable("csv/perfectly_correlated_uncorrelated_spread/MONTE_CARLOM10x10000015P8trueC0505ADDITIVEsynth_3", fileName);

            filesToProducts = new ArrayList<>();

            filesToProducts.add(new String[] { "csv/perfectly_correlated_uncorrelated_spread/MONTE_CARLOM10x10000015P8trueC0505ADDITIVEsynth_3/initial_calibration", "Caplet Vola Monte-Carlo Single-Curve", "Monte-Carlo Signle-Curve" });
            filesToProducts.add(new String[] { "csv/perfectly_correlated_uncorrelated_spread/MONTE_CARLOM10x10000015P8trueC0505ADDITIVEsynth_3/initial_calibration", "Caplet Vola Monte-Carlo Multi-Curve", "Monte-Carlo Multi-Curve" });
            filesToProducts.add(new String[] { "csv/perfectly_correlated_uncorrelated_spread/MONTE_CARLOM10x10000015P8trueC0505ADDITIVEsynth_3/initial_calibration", "Caplet Vola Analytic Single-Curve", "Analytic Single-Curve" });
            filesToProducts.add(new String[] { "csv/perfectly_correlated_uncorrelated_spread/MONTE_CARLOM10x10000015P8trueC0505ADDITIVEsynth_3/initial_calibration", "Caplet Vola Analytic Multi-Curve Integrated-Expectation", "Analytic Multi-Curve" });
            filesToProducts.add(new String[] { "csv/perfectly_correlated_uncorrelated_spread/MONTE_CARLOM10x10000015P8trueC0505ADDITIVEsynth_3/initial_calibration", "Caplet Vola Analytic Multi-Curve Fenton-Wilkinson", "Analytic Multi-Curve" });

            createPlot(30,65,
                    "Caplet Maturity [years]",
                    "Caplet Vola [\\\\%]",
                    "Valuation of ATM caplets in the single-curve model compared to the valuation in a multi-curve model where with perfectly correlated spread.",
                    filesToProducts, fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            fileName = "swaptions_perfectly_correlated_spread_additive";

            createParamsTable("csv/perfectly_correlated_uncorrelated_spread/MONTE_CARLOM10x10000015P8trueC0505ADDITIVEsynth_1", fileName);

            filesToProducts = new ArrayList<>();

            filesToProducts.add(new String[] { "csv/perfectly_correlated_uncorrelated_spread/MONTE_CARLOM10x10000015P8trueC0505ADDITIVEsynth_1/initial_calibration", "Swaption Vola Monte-Carlo Single-Curve", "Monte-Carlo Signle-Curve" });
            filesToProducts.add(new String[] { "csv/perfectly_correlated_uncorrelated_spread/MONTE_CARLOM10x10000015P8trueC0505ADDITIVEsynth_1/initial_calibration", "Swaption Vola Monte-Carlo Multi-Curve", "Monte-Carlo Multi-Curve" });
            filesToProducts.add(new String[] { "csv/perfectly_correlated_uncorrelated_spread/MONTE_CARLOM10x10000015P8trueC0505ADDITIVEsynth_1/initial_calibration", "Swaption Vola Analytic Single-Curve", "Analytic Single-Curve" });
            filesToProducts.add(new String[] { "csv/perfectly_correlated_uncorrelated_spread/MONTE_CARLOM10x10000015P8trueC0505ADDITIVEsynth_1/initial_calibration", "Swaption Vola Analytic Multi-Curve Drift-Freeze", "Analytic Multi-Curve" });

            createPlot(40,65,
                    "Swap Maturity x Number of Periods [years]",
                    "Swaption Vola [\\\\%]",
                    "Valuation of ATM swaptions in the single-curve model compared to the valuation in a multi-curve model where with perfectly correlated spread.",
                    filesToProducts, fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            fileName = "swaptions_uncorrelated_spread_additive";

            createParamsTable("csv/perfectly_correlated_uncorrelated_spread/MONTE_CARLOM10x10000015P8trueC0505ADDITIVEsynth_3", fileName);

            filesToProducts = new ArrayList<>();

            filesToProducts.add(new String[] { "csv/perfectly_correlated_uncorrelated_spread/MONTE_CARLOM10x10000015P8trueC0505ADDITIVEsynth_3/initial_calibration", "Swaption Vola Monte-Carlo Single-Curve", "Monte-Carlo Signle-Curve" });
            filesToProducts.add(new String[] { "csv/perfectly_correlated_uncorrelated_spread/MONTE_CARLOM10x10000015P8trueC0505ADDITIVEsynth_3/initial_calibration", "Swaption Vola Monte-Carlo Multi-Curve", "Monte-Carlo Multi-Curve" });
            filesToProducts.add(new String[] { "csv/perfectly_correlated_uncorrelated_spread/MONTE_CARLOM10x10000015P8trueC0505ADDITIVEsynth_3/initial_calibration", "Swaption Vola Analytic Single-Curve", "Analytic Single-Curve" });
            filesToProducts.add(new String[] { "csv/perfectly_correlated_uncorrelated_spread/MONTE_CARLOM10x10000015P8trueC0505ADDITIVEsynth_3/initial_calibration", "Swaption Vola Analytic Multi-Curve Drift-Freeze", "Analytic Multi-Curve" });

            createPlot(30,65,
                    "Swap Maturity x Number of Periods [years]",
                    "Swaption Vola [\\\\%]",
                    "Valuation of ATM swaptions in the single-curve model compared to the valuation in a multi-curve model where with perfectly correlated spread.",
                    filesToProducts, fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            fileName = "low_spread_value_convergence_forwards";

            createParamsTable("csv/low_spread_value_convergence/MONTE_CARLOM1x10000015P10trueC0505ADDITIVEsynth_1", fileName);

            filesToProducts = new ArrayList<>();

            filesToProducts.add(new String[] { "csv/low_spread_value_convergence/MONTE_CARLOM1x10000015P10trueC0505ADDITIVEsynth_1/initial_calibration", "OIS Forward Rate Multi-Curve", "OIS" });
            filesToProducts.add(new String[] { "csv/low_spread_value_convergence/MONTE_CARLOM1x10000015P10trueC0505ADDITIVEsynth_2/initial_calibration", "LIBOR Spot Rate Multi-Curve", "LIBOR" });
            filesToProducts.add(new String[] { "csv/low_spread_value_convergence/MONTE_CARLOM1x10000015P10trueC0505ADDITIVEsynth_3/initial_calibration", "LIBOR Spot Rate Multi-Curve", "LIBOR" });
            filesToProducts.add(new String[] { "csv/low_spread_value_convergence/MONTE_CARLOM1x10000015P10trueC0505ADDITIVEsynth_4/initial_calibration", "LIBOR Spot Rate Multi-Curve", "LIBOR" });
            filesToProducts.add(new String[] { "csv/low_spread_value_convergence/MONTE_CARLOM1x10000015P10trueC0505ADDITIVEsynth_5/initial_calibration", "LIBOR Spot Rate Multi-Curve", "LIBOR" });

            createPlot(1.0, 3.0,
                    "Time [years]",
                    "Rate [\\\\%]",
                    "Convergence of the LIBOR curve to the OIS curve for spreads converging to 0.",
                    filesToProducts, fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            fileName = "low_spread_value_convergence_caplets";

            createParamsTable("csv/low_spread_value_convergence/MONTE_CARLOM1x10000015P10trueC0505ADDITIVEsynth_1", fileName);

            filesToProducts = new ArrayList<>();

            filesToProducts.add(new String[] { "csv/low_spread_value_convergence/MONTE_CARLOM1x10000015P10trueC0505ADDITIVEsynth_1/initial_calibration", "Caplet Value Monte-Carlo Single-Curve", "OIS" });
            filesToProducts.add(new String[] { "csv/low_spread_value_convergence/MONTE_CARLOM1x10000015P10trueC0505ADDITIVEsynth_2/initial_calibration", "Caplet Value Monte-Carlo Multi-Curve", "LIBOR" });
            filesToProducts.add(new String[] { "csv/low_spread_value_convergence/MONTE_CARLOM1x10000015P10trueC0505ADDITIVEsynth_3/initial_calibration", "Caplet Value Monte-Carlo Multi-Curve", "LIBOR" });
            filesToProducts.add(new String[] { "csv/low_spread_value_convergence/MONTE_CARLOM1x10000015P10trueC0505ADDITIVEsynth_4/initial_calibration", "Caplet Value Monte-Carlo Multi-Curve", "LIBOR" });
            filesToProducts.add(new String[] { "csv/low_spread_value_convergence/MONTE_CARLOM1x10000015P10trueC0505ADDITIVEsynth_5/initial_calibration", "Caplet Value Monte-Carlo Multi-Curve", "LIBOR" });

            createPlot(0.0, 0.35,
                    "Time [years]",
                    "Value [\\\\%]",
                    "Convergence of caplet values when the LIBOR curve converges to the OIS curve.",
                    filesToProducts, fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            fileName = "low_spread_value_convergence_swaptions";

            createParamsTable("csv/low_spread_value_convergence/MONTE_CARLOM1x10000015P10trueC0505ADDITIVEsynth_1", fileName);

            filesToProducts = new ArrayList<>();

            filesToProducts.add(new String[] { "csv/low_spread_value_convergence/MONTE_CARLOM1x10000015P10trueC0505ADDITIVEsynth_1/initial_calibration", "Swaption Value Monte-Carlo Single-Curve", "OIS" });
            filesToProducts.add(new String[] { "csv/low_spread_value_convergence/MONTE_CARLOM1x10000015P10trueC0505ADDITIVEsynth_2/initial_calibration", "Swaption Value Monte-Carlo Multi-Curve", "LIBOR" });
            filesToProducts.add(new String[] { "csv/low_spread_value_convergence/MONTE_CARLOM1x10000015P10trueC0505ADDITIVEsynth_3/initial_calibration", "Swaption Value Monte-Carlo Multi-Curve", "LIBOR" });
            filesToProducts.add(new String[] { "csv/low_spread_value_convergence/MONTE_CARLOM1x10000015P10trueC0505ADDITIVEsynth_4/initial_calibration", "Swaption Value Monte-Carlo Multi-Curve", "LIBOR" });
            filesToProducts.add(new String[] { "csv/low_spread_value_convergence/MONTE_CARLOM1x10000015P10trueC0505ADDITIVEsynth_5/initial_calibration", "Swaption Value Monte-Carlo Multi-Curve", "LIBOR" });

            createPlot(0.0, 1.8,
                    "Time [years]",
                    "Value [\\\\%]",
                    "Convergence of swaption values when the LIBOR curve converges to the OIS curve.",
                    filesToProducts, fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            fileName = "low_spread_vola_convergence_forwards";

            createParamsTable("csv/low_spread_vola_convergence/MONTE_CARLOM1x10000015P10trueC0505ADDITIVEsynth_1", fileName);

            filesToProducts = new ArrayList<>();

            filesToProducts.add(new String[] { "csv/low_spread_vola_convergence/MONTE_CARLOM1x10000015P10trueC0505ADDITIVEsynth_1/initial_calibration", "OIS Forward Rate Multi-Curve", "OIS" });
            filesToProducts.add(new String[] { "csv/low_spread_vola_convergence/MONTE_CARLOM1x10000015P10trueC0505ADDITIVEsynth_2/initial_calibration", "LIBOR Spot Rate Multi-Curve", "LIBOR" });
            filesToProducts.add(new String[] { "csv/low_spread_vola_convergence/MONTE_CARLOM1x10000015P10trueC0505ADDITIVEsynth_3/initial_calibration", "LIBOR Spot Rate Multi-Curve", "LIBOR" });
            filesToProducts.add(new String[] { "csv/low_spread_vola_convergence/MONTE_CARLOM1x10000015P10trueC0505ADDITIVEsynth_4/initial_calibration", "LIBOR Spot Rate Multi-Curve", "LIBOR" });
            filesToProducts.add(new String[] { "csv/low_spread_vola_convergence/MONTE_CARLOM1x10000015P10trueC0505ADDITIVEsynth_5/initial_calibration", "LIBOR Spot Rate Multi-Curve", "LIBOR" });

            createPlot(1.0, 3.0,
                    "Time [years]",
                    "Rate [\\\\%]",
                    "LIBOR and OIS curves for spread volatility converging to 0.",
                    filesToProducts, fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            fileName = "low_spread_vola_convergence_caplets";

            createParamsTable("csv/low_spread_vola_convergence/MONTE_CARLOM1x10000015P10trueC0505ADDITIVEsynth_1", fileName);

            filesToProducts = new ArrayList<>();

            filesToProducts.add(new String[] { "csv/low_spread_vola_convergence/MONTE_CARLOM1x10000015P10trueC0505ADDITIVEsynth_1/initial_calibration", "Caplet Value Monte-Carlo Single-Curve", "OIS" });
            filesToProducts.add(new String[] { "csv/low_spread_vola_convergence/MONTE_CARLOM1x10000015P10trueC0505ADDITIVEsynth_2/initial_calibration", "Caplet Value Monte-Carlo Multi-Curve", "LIBOR" });
            filesToProducts.add(new String[] { "csv/low_spread_vola_convergence/MONTE_CARLOM1x10000015P10trueC0505ADDITIVEsynth_3/initial_calibration", "Caplet Value Monte-Carlo Multi-Curve", "LIBOR" });
            filesToProducts.add(new String[] { "csv/low_spread_vola_convergence/MONTE_CARLOM1x10000015P10trueC0505ADDITIVEsynth_4/initial_calibration", "Caplet Value Monte-Carlo Multi-Curve", "LIBOR" });
            filesToProducts.add(new String[] { "csv/low_spread_vola_convergence/MONTE_CARLOM1x10000015P10trueC0505ADDITIVEsynth_5/initial_calibration", "Caplet Value Monte-Carlo Multi-Curve", "LIBOR" });

            createPlot(0.0, 0.35,
                    "Time [years]",
                    "Value [\\\\%]",
                    "Caplet values for spread volatility converging to 0.",
                    filesToProducts, fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            fileName = "low_spread_vola_convergence_swaptions";

            createParamsTable("csv/low_spread_vola_convergence/MONTE_CARLOM1x10000015P10trueC0505ADDITIVEsynth_1", fileName);

            filesToProducts = new ArrayList<>();

            filesToProducts.add(new String[] { "csv/low_spread_vola_convergence/MONTE_CARLOM1x10000015P10trueC0505ADDITIVEsynth_1/initial_calibration", "Swaption Value Monte-Carlo Single-Curve", "OIS" });
            filesToProducts.add(new String[] { "csv/low_spread_vola_convergence/MONTE_CARLOM1x10000015P10trueC0505ADDITIVEsynth_2/initial_calibration", "Swaption Value Monte-Carlo Multi-Curve", "LIBOR" });
            filesToProducts.add(new String[] { "csv/low_spread_vola_convergence/MONTE_CARLOM1x10000015P10trueC0505ADDITIVEsynth_3/initial_calibration", "Swaption Value Monte-Carlo Multi-Curve", "LIBOR" });
            filesToProducts.add(new String[] { "csv/low_spread_vola_convergence/MONTE_CARLOM1x10000015P10trueC0505ADDITIVEsynth_4/initial_calibration", "Swaption Value Monte-Carlo Multi-Curve", "LIBOR" });
            filesToProducts.add(new String[] { "csv/low_spread_vola_convergence/MONTE_CARLOM1x10000015P10trueC0505ADDITIVEsynth_5/initial_calibration", "Swaption Value Monte-Carlo Multi-Curve", "LIBOR" });

            createPlot(0.0, 2.8,
                    "Time [years]",
                    "Value [\\\\%]",
                    "Swaption values for spread volatility converging to 0.",
                    filesToProducts, fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

