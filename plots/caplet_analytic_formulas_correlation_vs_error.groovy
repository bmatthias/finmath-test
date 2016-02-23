import com.bmatthias.finmath.test.Helper
import com.bmatthias.finmath.test.Plot

/*plot('caplet_analytic_formulas_correlation_vs_error_2015_additive') {
    params = [
            xLabel: 'Correlation',
            yLabel: 'Error',
            caption: 'Caption',
            yMin: 0,
            yMax: 1,
    ]

    execute = {
        Map<String, Map> productsToCoordinates = [:]

        (0..10).each {
            Map<String, double[][]> errorsMap = Helper.getErrors(new File("results/pricing_2015_increasing_correlation/${it}_additive_1/"), Plot.DEFAULT_COMPARISONS.Caplets)
            errorsMap.each { product, errors ->
                if(!productsToCoordinates.get(product)) productsToCoordinates.put(product, [:])
                productsToCoordinates.get(product).put(it, errors[0][0])
            }
        }

        plotFromValues(productsToCoordinates)
    }
}*/

plot('caplet_analytic_formulas_correlation_vs_error_2013_additive') {
    params = [
            xLabel: 'Correlation',
            yLabel: 'Error',
            caption: 'Caption',
            yMin: 0,
            yMax: 0.04,
    ]

    execute = {
        Map<String, Map> productsToCoordinates = [:]

        (0..10).each {
            Map<String, double[][]> errorsMap = Helper.getErrors(new File("results/pricing_2013_increasing_correlation/${it}_additive_1/"), Plot.DEFAULT_COMPARISONS.Caplets)
            errorsMap.each { product, errors ->
                if(!productsToCoordinates.get(product)) productsToCoordinates.put(product, [:])
                productsToCoordinates.get(product).put(it, errors[2][0])
            }
        }

        plotFromValues(productsToCoordinates)
    }
}

plot('caplet_analytic_formulas_correlation_vs_error_2010_additive') {
    params = [
            xLabel: 'Correlation',
            yLabel: 'Error',
            caption: 'Caption',
            yMin: 0,
            yMax: 0.04,
    ]

    execute = {
        Map<String, Map> productsToCoordinates = [:]

        (0..10).each {
            Map<String, double[][]> errorsMap = Helper.getErrors(new File("results/pricing_2010_increasing_correlation/${it}_additive_1/"), Plot.DEFAULT_COMPARISONS.Caplets)
            errorsMap.each { product, errors ->
                if(!productsToCoordinates.get(product)) productsToCoordinates.put(product, [:])
                productsToCoordinates.get(product).put(it, errors[2][0])
            }
        }

        plotFromValues(productsToCoordinates)
    }
}

plot('caplet_analytic_formulas_correlation_vs_error_2010_additive') {
    params = [
            xLabel: 'Correlation',
            yLabel: 'Error',
            caption: 'Caption',
            yMin: 0,
            yMax: 0.04,
    ]

    execute = {
        Map<String, Map> productsToCoordinates = [:]

        (0..10).each {
            Map<String, double[][]> errorsMap = Helper.getErrors(new File("results/pricing_2010_increasing_correlation/${it}_additive_1/"), Plot.DEFAULT_COMPARISONS.Caplets)
            errorsMap.each { product, errors ->
                if(!productsToCoordinates.get(product)) productsToCoordinates.put(product, [:])
                productsToCoordinates.get(product).put(it, errors[2][0])
            }
        }

        plotFromValues(productsToCoordinates)
    }
}

/*plot('caplet_analytic_formulas_correlation_vs_error_2015_multiplicative') {
    params = [
            xLabel: 'Correlation',
            yLabel: 'Error',
            caption: 'Caption',
            yMin: 0,
            yMax: 1,
    ]

    execute = {
        Map<String, Map> productsToCoordinates = [:]

        (0..10).each {
            Map<String, double[][]> errorsMap = Helper.getErrors(new File("results/pricing_2015_increasing_correlation/${it}_multiplicative_1/"), Plot.DEFAULT_COMPARISONS.Caplets)
            errorsMap.each { product, errors ->
                if(!productsToCoordinates.get(product)) productsToCoordinates.put(product, [:])
                productsToCoordinates.get(product).put(it, errors[0][0])
            }
        }

        plotFromValues(productsToCoordinates)
    }
}*/

plot('caplet_analytic_formulas_correlation_vs_error_2013_multiplicative') {
    params = [
            xLabel: 'Correlation',
            yLabel: 'Error',
            caption: 'Caption',
            yMin: 0,
            yMax: 0.04,
    ]

    execute = {
        Map<String, Map> productsToCoordinates = [:]

        (0..10).each {
            Map<String, double[][]> errorsMap = Helper.getErrors(new File("results/pricing_2013_increasing_correlation/${it}_multiplicative_1/"), Plot.DEFAULT_COMPARISONS.Caplets)
            errorsMap.each { product, errors ->
                if(!productsToCoordinates.get(product)) productsToCoordinates.put(product, [:])
                productsToCoordinates.get(product).put(it, errors[2][0])
            }
        }

        plotFromValues(productsToCoordinates)
    }
}

plot('caplet_analytic_formulas_correlation_vs_error_2010_multiplicative') {
    params = [
            xLabel: 'Correlation',
            yLabel: 'Error',
            caption: 'Caption',
            yMin: 0,
            yMax: 0.04,
    ]

    execute = {
        Map<String, Map> productsToCoordinates = [:]

        (0..10).each {
            Map<String, double[][]> errorsMap = Helper.getErrors(new File("results/pricing_2010_increasing_correlation/${it}_multiplicative_1/"), Plot.DEFAULT_COMPARISONS.Caplets)
            errorsMap.each { product, errors ->
                if(!productsToCoordinates.get(product)) productsToCoordinates.put(product, [:])
                productsToCoordinates.get(product).put(it, errors[2][0])
            }
        }

        plotFromValues(productsToCoordinates)
    }
}

plot('caplet_analytic_formulas_correlation_vs_error_2010_multiplicative') {
    params = [
            xLabel: 'Correlation',
            yLabel: 'Error',
            caption: 'Caption',
            yMin: 0,
            yMax: 0.04,
    ]

    execute = {
        Map<String, Map> productsToCoordinates = [:]

        (0..10).each {
            Map<String, double[][]> errorsMap = Helper.getErrors(new File("results/pricing_2010_increasing_correlation/${it}_multiplicative_1/"), Plot.DEFAULT_COMPARISONS.Caplets)
            errorsMap.each { product, errors ->
                if(!productsToCoordinates.get(product)) productsToCoordinates.put(product, [:])
                productsToCoordinates.get(product).put(it, errors[2][0])
            }
        }

        plotFromValues(productsToCoordinates)
    }
}