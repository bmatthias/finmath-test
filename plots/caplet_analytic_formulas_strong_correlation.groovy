import com.bmatthias.finmath.test.Plot

plot('caplet_analytic_formulas_strong_correlation_2015_additive') {
    params = [
            xLabel: 'Caplet Maturity [years]',
            yLabel: 'Caplet Vola [\\\\%]',
            caption: 'Valuation of ATM caplets in the single-curve model compared to the valuation in a multi-curve model with perfectly correlated spread.',
            yMin: 20,
            yMax: 70,
    ]

    execute = {
        Map<String, Map> fileToProducts = [ 'results/pricing_2015_increasing_correlation/10_additive_1/initial_calibration': Plot.DEFAULT_PLOTS.Caplets_Vola_Multi_Curve ]
        plotFromCSVWithErrors(fileToProducts)
    }
}

plot('caplet_analytic_formulas_strong_correlation_2013_additive') {
    params = [
            xLabel: 'Caplet Maturity [years]',
            yLabel: 'Caplet Vola [\\\\%]',
            caption: 'Valuation of ATM caplets in the single-curve model compared to the valuation in a multi-curve model with perfectly correlated spread.',
            yMin: 20,
            yMax: 70,
    ]

    execute = {
        Map<String, Map> fileToProducts = [ 'results/pricing_2013_increasing_correlation/10_additive_1/initial_calibration': Plot.DEFAULT_PLOTS.Caplets_Vola_Multi_Curve ]
        plotFromCSVWithErrors(fileToProducts)
    }
}

plot('caplet_analytic_formulas_strong_correlation_2010_additive') {
    params = [
            xLabel: 'Caplet Maturity [years]',
            yLabel: 'Caplet Vola [\\\\%]',
            caption: 'Valuation of ATM caplets in the single-curve model compared to the valuation in a multi-curve model with perfectly correlated spread.',
            yMin: 20,
            yMax: 70,
    ]

    execute = {
        Map<String, Map> fileToProducts = [ 'results/pricing_2010_increasing_correlation/10_additive_1/initial_calibration': Plot.DEFAULT_PLOTS.Caplets_Vola_Multi_Curve ]
        plotFromCSVWithErrors(fileToProducts)
    }
}

plot('caplet_analytic_formulas_strong_correlation_synth_additive') {
    params = [
            xLabel: 'Caplet Maturity [years]',
            yLabel: 'Caplet Vola [\\\\%]',
            caption: 'Valuation of ATM caplets in the single-curve model compared to the valuation in a multi-curve model with perfectly correlated spread.',
            yMin: 20,
            yMax: 70,
    ]

    execute = {
        Map<String, Map> fileToProducts = [ 'results/pricing_synth_increasing_correlation/10_additive_1/initial_calibration': Plot.DEFAULT_PLOTS.Caplets_Vola_Single_And_Multi_Curve ]
        plotFromCSV(fileToProducts)
    }
}

plot('caplet_analytic_formulas_strong_correlation_2015_multiplicative') {
    params = [
            xLabel: 'Caplet Maturity [years]',
            yLabel: 'Caplet Vola [\\\\%]',
            caption: 'Valuation of ATM caplets in the single-curve model compared to the valuation in a multi-curve model with perfectly correlated spread.',
            yMin: 20,
            yMax: 70,
    ]

    execute = {
        Map<String, Map> fileToProducts = [ 'results/pricing_2015_increasing_correlation/10_multiplicative_1/initial_calibration': Plot.DEFAULT_PLOTS.Caplets_Vola_Multi_Curve ]
        plotFromCSVWithErrors(fileToProducts)
    }
}

plot('caplet_analytic_formulas_strong_correlation_2013_multiplicative') {
    params = [
            xLabel: 'Caplet Maturity [years]',
            yLabel: 'Caplet Vola [\\\\%]',
            caption: 'Valuation of ATM caplets in the single-curve model compared to the valuation in a multi-curve model with perfectly correlated spread.',
            yMin: 20,
            yMax: 70,
    ]

    execute = {
        Map<String, Map> fileToProducts = [ 'results/pricing_2013_increasing_correlation/10_multiplicative_1/initial_calibration': Plot.DEFAULT_PLOTS.Caplets_Vola_Multi_Curve ]
        plotFromCSVWithErrors(fileToProducts)
    }
}

plot('caplet_analytic_formulas_strong_correlation_2010_multiplicative') {
    params = [
            xLabel: 'Caplet Maturity [years]',
            yLabel: 'Caplet Vola [\\\\%]',
            caption: 'Valuation of ATM caplets in the single-curve model compared to the valuation in a multi-curve model with perfectly correlated spread.',
            yMin: 20,
            yMax: 70,
    ]

    execute = {
        Map<String, Map> fileToProducts = [ 'results/pricing_2010_increasing_correlation/10_multiplicative_1/initial_calibration': Plot.DEFAULT_PLOTS.Caplets_Vola_Multi_Curve ]
        plotFromCSVWithErrors(fileToProducts)
    }
}

plot('caplet_analytic_formulas_strong_correlation_synth_multiplicative') {
    params = [
            xLabel: 'Caplet Maturity [years]',
            yLabel: 'Caplet Vola [\\\\%]',
            caption: 'Valuation of ATM caplets in the single-curve model compared to the valuation in a multi-curve model with perfectly correlated spread.',
            yMin: 20,
            yMax: 70,
    ]

    execute = {
        Map<String, Map> fileToProducts = [ 'results/pricing_synth_increasing_correlation/10_multiplicative_1/initial_calibration': Plot.DEFAULT_PLOTS.Caplets_Vola_Single_And_Multi_Curve ]
        plotFromCSV(fileToProducts)
    }
}

plot('caplet_analytic_formulas_mid_correlation_2015_additive') {
    params = [
            xLabel: 'Time [years]',
            yLabel: 'error',
            caption: 'Valuation of ATM caplets in the single-curve model compared to the valuation in a multi-curve model',
            yMin: 0.0,
            yMax: 0.03,
    ]

    execute = {
        Map<String, Map> fileToComparisons = [ 'results/pricing_2015_increasing_correlation/10_additive_1/initial_calibration': Plot.DEFAULT_COMPARISONS.Caplets ]
        plotErrors(fileToComparisons)
    }
}

plot('caplet_analytic_formulas_mid_correlation_2013_additive') {
    params = [
            xLabel: 'Time [years]',
            yLabel: 'error',
            caption: 'Valuation of ATM caplets in the single-curve model compared to the valuation in a multi-curve model',
            yMin: 0.0,
            yMax: 0.03,
    ]

    execute = {
        Map<String, Map> fileToComparisons = [ 'results/pricing_2013_increasing_correlation/10_additive_1/initial_calibration': Plot.DEFAULT_COMPARISONS.Caplets ]
        plotErrors(fileToComparisons)
    }
}

plot('caplet_analytic_formulas_mid_correlation_2010_additive') {
    params = [
            xLabel: 'Time [years]',
            yLabel: 'error',
            caption: 'Valuation of ATM caplets in the single-curve model compared to the valuation in a multi-curve model',
            yMin: 0.0,
            yMax: 0.03,
    ]

    execute = {
        Map<String, Map> fileToComparisons = [ 'results/pricing_2010_increasing_correlation/10_additive_1/initial_calibration': Plot.DEFAULT_COMPARISONS.Caplets ]
        plotErrors(fileToComparisons)
    }
}

plot('caplet_analytic_formulas_mid_correlation_2015_multiplicative') {
    params = [
            xLabel: 'Time [years]',
            yLabel: 'error',
            caption: 'Valuation of ATM caplets in the single-curve model compared to the valuation in a multi-curve model',
            yMin: 0.0,
            yMax: 0.03,
    ]

    execute = {
        Map<String, Map> fileToComparisons = [ 'results/pricing_2015_increasing_correlation/10_multiplicative_1/initial_calibration': Plot.DEFAULT_COMPARISONS.Caplets ]
        plotErrors(fileToComparisons)
    }
}

plot('caplet_analytic_formulas_mid_correlation_2013_multiplicative') {
    params = [
            xLabel: 'Time [years]',
            yLabel: 'error',
            caption: 'Valuation of ATM caplets in the single-curve model compared to the valuation in a multi-curve model',
            yMin: 0.0,
            yMax: 0.03,
    ]

    execute = {
        Map<String, Map> fileToComparisons = [ 'results/pricing_2013_increasing_correlation/10_multiplicative_1/initial_calibration': Plot.DEFAULT_COMPARISONS.Caplets ]
        plotErrors(fileToComparisons)
    }
}

plot('caplet_analytic_formulas_mid_correlation_2010_multiplicative') {
    params = [
            xLabel: 'Time [years]',
            yLabel: 'error',
            caption: 'Valuation of ATM caplets in the single-curve model compared to the valuation in a multi-curve model',
            yMin: 0.0,
            yMax: 0.03,
    ]

    execute = {
        Map<String, Map> fileToComparisons = [ 'results/pricing_2010_increasing_correlation/10_multiplicative_1/initial_calibration': Plot.DEFAULT_COMPARISONS.Caplets ]
        plotErrors(fileToComparisons)
    }
}