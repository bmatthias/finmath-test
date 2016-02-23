package com.bmatthias.finmath.test

import com.bmatthias.finmath.test.tools.tools.TexCreator

import java.util.concurrent.Callable

class Plot implements Callable {
    static final DEFAULT_COMPARISONS = [ Caplets: [
                    'Integrated-Expectation': [ 'Caplet Vola Monte-Carlo Multi-Curve', 'Caplet Vola Analytic Multi-Curve Integrated-Expectation' ],
                    'Levy'                  : [ 'Caplet Vola Monte-Carlo Multi-Curve', 'Caplet Vola Analytic Multi-Curve Fenton-Wilkinson' ],
                    'Ju'                    : [ 'Caplet Vola Monte-Carlo Multi-Curve', 'Caplet Vola Analytic Multi-Curve Ju' ],
                    'Ho'                    : [ 'Caplet Vola Monte-Carlo Multi-Curve', 'Caplet Vola Analytic Multi-Curve Ho' ]
            ], Swaptions: [

            ]
    ]

    static final  DEFAULT_PLOTS = [ Caplets: [
                    'Monte-Carlo'           : 'Caplet Vola Monte-Carlo Multi-Curve',
                    'Integrated-Expectation': 'Caplet Vola Analytic Multi-Curve Integrated-Expectation' ,
                    'Levy'                  : 'Caplet Vola Analytic Multi-Curve Fenton-Wilkinson' ,
                    'Ju'                    : 'Caplet Vola Analytic Multi-Curve Ju' ,
                    'Ho'                    : 'Caplet Vola Analytic Multi-Curve Ho'
            ]
    ]

    String fileName
    Map params
    Closure execute

    def plotFromValues(Map<String, Map> productsToCoordinatesMap) {
        String plotsArea = ''
        String products = ''
        def plots = productsToCoordinatesMap.collectEntries { product, values ->
            String coordinates = ''
            values.each { x, y -> coordinates += "(${x},${y})" }
            return [ (product): coordinates ]
        }
        plots.eachWithIndex { product, coords, index ->
            plotsArea += "\\\\addplot[color=s${index}] coordinates { ${coords} };\n"
            products += product + ','
        }
        TexCreator.writePlot(params.yMin, params.yMax, params.xLabel, params.yLabel, params.caption, fileName, plotsArea, products.substring(0, products.length() - 1));
    }

    def plotFromCSV(Map<String, Map> filesToProductsMap) {
        new File(fileName).parentFile.mkdirs()
        def res = []
        filesToProductsMap.each { file, product ->
            res.addAll(product.collect {
                [ file, it.value, it.key ] as String[]
            })
        }
        TexCreator.createPlot(params.yMin, params.yMax, params.xLabel, params.yLabel, params.caption, res, fileName)
    }

    def plotErrors(Map fileToProduct) {
        Map<String, double[][]> errors = [:]
        fileToProduct.each { key, value ->
            errors.putAll Helper.getErrors(new File(key as String), value as Map)
        }
        TexCreator.createErrorPlot(params.caption as String, errors, fileName)
    }

    @Override
    def call() {
        try {
            execute()
        } catch (e) {
            e.printStackTrace()
        }
    }
}
