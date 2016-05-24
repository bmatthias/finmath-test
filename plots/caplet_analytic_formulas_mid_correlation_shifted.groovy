import com.bmatthias.finmath.test.Plot

plot('caplet_analytic_formulas_mid_correlation_2013_additive_shifted') {
    params = [
            xLabel: 'Caplet Maturity [years]',
            yLabel: 'Caplet Vola [\\\\%]',
            caption: 'Valuation of ATM caplets in the single-curve model compared to the valuation in a multi-curve model with uncorrelated spread.',
            yMin: 20,
            yMax: 70,
    ]

    execute = {
        Map<String, Map> fileToProducts = [ 'results/pricing_2013_increasing_correlation/5_additive_shifted_1/initial_calibration': Plot.DEFAULT_PLOTS.Caplets_Vola_Multi_Curve ]
        plotFromCSVWithErrors(fileToProducts)
    }
}

plot('caplet_analytic_formulas_mid_correlation_2010_additive_shifted') {
    params = [
            xLabel: 'Caplet Maturity [years]',
            yLabel: 'Caplet Vola [\\\\%]',
            caption: 'Valuation of ATM caplets in the single-curve model compared to the valuation in a multi-curve model with uncorrelated spread.',
            yMin: 20,
            yMax: 70,
    ]

    execute = {
        Map<String, Map> fileToProducts = [ 'results/pricing_2010_increasing_correlation/5_additive_shifted_1/initial_calibration': Plot.DEFAULT_PLOTS.Caplets_Vola_Multi_Curve ]
        plotFromCSVWithErrors(fileToProducts)
    }
}

plot('caplet_analytic_formulas_mid_correlation_synth_additive_shifted') {
    params = [
            xLabel: 'Caplet Maturity [years]',
            yLabel: 'Caplet Vola [\\\\%]',
            caption: 'Valuation of ATM caplets in the single-curve model compared to the valuation in a multi-curve model with uncorrelated spread.',
            yMin: 20,
            yMax: 70,
    ]

    execute = {
        Map<String, Map> fileToProducts = [ 'results/pricing_synth_increasing_correlation/5_additive_shifted_1/initial_calibration': Plot.DEFAULT_PLOTS.Caplets_Vola_Multi_Curve ]
        plotFromCSVWithErrors(fileToProducts)
    }
}

plot('caplet_analytic_formulas_mid_correlation_2013_multiplicative_shifted') {
    params = [
            xLabel: 'Caplet Maturity [years]',
            yLabel: 'Caplet Vola [\\\\%]',
            caption: 'Valuation of ATM caplets in the single-curve model compared to the valuation in a multi-curve model with uncorrelated spread.',
            yMin: 20,
            yMax: 70,
    ]

    execute = {
        Map<String, Map> fileToProducts = [ 'results/pricing_2013_increasing_correlation/5_multiplicative_shifted_1/initial_calibration': Plot.DEFAULT_PLOTS.Caplets_Vola_Multi_Curve ]
        plotFromCSVWithErrors(fileToProducts)
    }
}

plot('caplet_analytic_formulas_mid_correlation_2010_multiplicative_shifted') {
    params = [
            xLabel: 'Caplet Maturity [years]',
            yLabel: 'Caplet Vola [\\\\%]',
            caption: 'Valuation of ATM caplets in the single-curve model compared to the valuation in a multi-curve model with uncorrelated spread.',
            yMin: 20,
            yMax: 70,
    ]

    execute = {
        Map<String, Map> fileToProducts = [ 'results/pricing_2010_increasing_correlation/5_multiplicative_shifted_1/initial_calibration': Plot.DEFAULT_PLOTS.Caplets_Vola_Multi_Curve ]
        plotFromCSVWithErrors(fileToProducts)
    }
}

plot('caplet_analytic_formulas_mid_correlation_synth_multiplicative_shifted') {
    params = [
            xLabel: 'Caplet Maturity [years]',
            yLabel: 'Caplet Vola [\\\\%]',
            caption: 'Valuation of ATM caplets in the single-curve model compared to the valuation in a multi-curve model with uncorrelated spread.',
            yMin: 20,
            yMax: 70,
    ]

    execute = {
        Map<String, Map> fileToProducts = [ 'results/pricing_synth_increasing_correlation/5_multiplicative_shifted_1/initial_calibration': Plot.DEFAULT_PLOTS.Caplets_Vola_Multi_Curve ]
        plotFromCSVWithErrors(fileToProducts)
    }
}