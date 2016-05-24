import com.bmatthias.finmath.test.Helper
import com.bmatthias.finmath.test.Plot

plot('swaption_analytic_formulas_mid_correlation_2015_additive') {
    params = [
            xLabel: 'Swaption Maturity x Number of Periods',
            yLabel: 'Swaption Vola [\\\\%]',
            caption: 'Valuation of ATM swaptions in the a multi-curve model.',
            yMin: 20,
            yMax: 70,
    ]

    execute = {
        Map<String, Map> fileToProducts = [ 'results/pricing_2015_increasing_correlation/5_additive_1/initial_calibration': Plot.DEFAULT_PLOTS.Swaptions_Vola_Multi_Curve ]
        plotFromCSVWithErrors(fileToProducts)
    }
}

plot('swaption_analytic_formulas_mid_correlation_2013_additive') {
    params = [
            xLabel: 'Swaption Maturity x Number of Periods',
            yLabel: 'Swaption Vola [\\\\%]',
            caption: 'Valuation of ATM swaptions in the a multi-curve model.',
            yMin: 20,
            yMax: 70,
    ]

    execute = {
        Map<String, Map> fileToProducts = [ 'results/pricing_2013_increasing_correlation/5_additive_1/initial_calibration': Plot.DEFAULT_PLOTS.Swaptions_Vola_Multi_Curve ]
        plotFromCSVWithErrors(fileToProducts)
    }
}

plot('swaption_analytic_formulas_mid_correlation_2010_additive') {
    params = [
            xLabel: 'Swaption Maturity x Number of Periods',
            yLabel: 'Swaption Vola [\\\\%]',
            caption: 'Valuation of ATM swaptions in the a multi-curve model.',
            yMin: 20,
            yMax: 70,
    ]

    execute = {
        Map<String, Map> fileToProducts = [ 'results/pricing_2010_increasing_correlation/5_additive_1/initial_calibration': Plot.DEFAULT_PLOTS.Swaptions_Vola_Multi_Curve ]
        plotFromCSVWithErrors(fileToProducts)
    }
}

plot('swaption_analytic_formulas_mid_correlation_2015_multiplicative') {
    params = [
            xLabel: 'Swaption Maturity x Number of Periods',
            yLabel: 'Swaption Vola [\\\\%]',
            caption: 'Valuation of ATM swaptions in the a multi-curve model.',
            yMin: 20,
            yMax: 70,
    ]

    execute = {
        Map<String, Map> fileToProducts = [ 'results/pricing_2015_increasing_correlation/5_multiplicative_1/initial_calibration': Plot.DEFAULT_PLOTS.Swaptions_Vola_Multi_Curve ]
        plotFromCSVWithErrors(fileToProducts)
    }
}

plot('swaption_analytic_formulas_mid_correlation_2013_multiplicative') {
    params = [
            xLabel: 'Swaption Maturity x Number of Periods',
            yLabel: 'Swaption Vola [\\\\%]',
            caption: 'Valuation of ATM swaptions in the a multi-curve model.',
            yMin: 20,
            yMax: 70,
    ]

    execute = {
        Map<String, Map> fileToProducts = [ 'results/pricing_2013_increasing_correlation/5_multiplicative_1/initial_calibration': Plot.DEFAULT_PLOTS.Swaptions_Vola_Multi_Curve ]
        plotFromCSVWithErrors(fileToProducts)
    }
}

plot('swaption_analytic_formulas_mid_correlation_2010_multiplicative') {
    params = [
            xLabel: 'Swaption Maturity x Number of Periods',
            yLabel: 'Swaption Vola [\\\\%]',
            caption: 'Valuation of ATM swaptions in the a multi-curve model.',
            yMin: 20,
            yMax: 70,
    ]

    execute = {
        Map<String, Map> fileToProducts = [ 'results/pricing_2010_increasing_correlation/5_multiplicative_1/initial_calibration': Plot.DEFAULT_PLOTS.Swaptions_Vola_Multi_Curve ]
        plotFromCSVWithErrors(fileToProducts)
    }
}

plot('swaption_analytic_formulas_mid_correlation_2015_additive') {
    params = [
            yLabel: 'Error',
            caption: 'Error of swaption approximation compared to Monte-Carlo simulation.',
            yMin: 0.0,
            yMax: 0.03,
    ]

    execute = {
        Map<String, Map> fileToComparisons = [ 'results/pricing_2015_increasing_correlation/5_additive_1/initial_calibration': Plot.DEFAULT_COMPARISONS.Swaptions ]
        plotErrors(fileToComparisons)
    }
}

plot('swaption_analytic_formulas_mid_correlation_2013_additive') {
    params = [
            yLabel: 'Error',
            caption: 'Error of swaption approximation compared to Monte-Carlo simulation.',
            yMin: 0.0,
            yMax: 0.03,
    ]

    execute = {
        Map<String, Map> fileToComparisons = [ 'results/pricing_2013_increasing_correlation/5_additive_1/initial_calibration': Plot.DEFAULT_COMPARISONS.Swaptions ]
        plotErrors(fileToComparisons)
    }
}

plot('swaption_analytic_formulas_mid_correlation_2010_additive') {
    params = [
            yLabel: 'Error',
            caption: 'Error of swaption approximation compared to Monte-Carlo simulation.',
            yMin: 0.0,
            yMax: 0.03,
    ]

    execute = {
        Map<String, Map> fileToComparisons = [ 'results/pricing_2010_increasing_correlation/5_additive_1/initial_calibration': Plot.DEFAULT_COMPARISONS.Swaptions ]
        plotErrors(fileToComparisons)
    }
}

plot('swaption_analytic_formulas_mid_correlation_2015_multiplicative') {
    params = [
            yLabel: 'Error',
            caption: 'Error of swaption approximation compared to Monte-Carlo simulation.',
            yMin: 0.0,
            yMax: 0.03,
    ]

    execute = {
        Map<String, Map> fileToComparisons = [ 'results/pricing_2015_increasing_correlation/5_multiplicative_1/initial_calibration': Plot.DEFAULT_COMPARISONS.Swaptions ]
        plotErrors(fileToComparisons)
    }
}

plot('swaption_analytic_formulas_mid_correlation_2013_multiplicative') {
    params = [
            yLabel: 'Error',
            caption: 'Error of swaption approximation compared to Monte-Carlo simulation.',
            yMin: 0.0,
            yMax: 0.03,
    ]

    execute = {
        Map<String, Map> fileToComparisons = [ 'results/pricing_2013_increasing_correlation/5_multiplicative_1/initial_calibration': Plot.DEFAULT_COMPARISONS.Swaptions ]
        plotErrors(fileToComparisons)
    }
}

plot('swaption_analytic_formulas_mid_correlation_2010_multiplicative') {
    params = [
            yLabel: 'Error',
            caption: 'Error of swaption approximation compared to Monte-Carlo simulation.',
            yMin: 0.0,
            yMax: 0.03,
    ]

    execute = {
        Map<String, Map> fileToComparisons = [ 'results/pricing_2010_increasing_correlation/5_multiplicative_1/initial_calibration': Plot.DEFAULT_COMPARISONS.Swaptions ]
        plotErrors(fileToComparisons)
    }
}
