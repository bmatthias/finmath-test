package com.bmatthias.finmath.test

import au.com.bytecode.opencsv.CSVReader

class Helper {
    /*
    Takes a File or a Folder representing a test, and aggregates the pre-computed errors.
     */
    static Map<String, double[][]> getErrors(File fileOrFolder, Map<String, List> comparisons) {
        return comparisons.collectEntries { comparison ->
            double[][] errors = new double[3][3]
            if (fileOrFolder.isDirectory()) {
                FilenameFilter filter = { dir, name -> name.endsWith("_results.csv") }
                fileOrFolder.listFiles(filter).each { aggregateErrors(it.absolutePath.replace("_results.csv", ''), comparison.value, errors) }
                (0..2).each { errors[it][0] /= fileOrFolder.listFiles(filter).length; }
            } else {
                aggregateErrors(fileOrFolder.absolutePath, comparison.value, errors)
            }
            return [(comparison.key): errors]
        }
    }

    static aggregateErrors(String fileName, List products, double[][] errors) throws IOException {
        [ "_max_errs.csv", "_mean_errs.csv", "_square_errs.csv" ].eachWithIndex { suffix, errorType ->
            String fileToRead = fileName + suffix;
            new File(fileToRead).withReader { reader ->
                CSVReader csvReader = new CSVReader(reader, '\t' as char);

                String product1 = products[0];
                String product2 = products[1];
                try {
                    String[] line = csvReader.readNext();
                    for(int productIndex = 0; productIndex < line.length; productIndex++) {
                        if (product1.equals(line[productIndex])) {
                            while ((line = csvReader.readNext()) != null) {
                                if(product2.equals(line[0])) break;
                            }

                            double error = {
                                try {
                                    return Double.parseDouble(line[productIndex]);
                                } catch (e) { return Double.MAX_VALUE }
                            }.call() as double
                            errors[errorType][0] += error;
                            errors[errorType][1] = Math.max(errors[errorType][1], error);
                            errors[errorType][2] = errors[errorType][2] > 0 ? Math.min(errors[errorType][2], error) : error;
                            break;
                        }
                    }
                } catch (e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
