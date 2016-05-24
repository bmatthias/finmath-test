import com.bmatthias.finmath.test.Plot

plot('calib_caplet_integrated_expectation_2015_additive') {
    params = [
            xLabel: 'Caplet Maturity [years]',
            yLabel: 'Caplet Vola [\\\\%]',
            caption: 'Valuation of ATM caplets in the single-curve model compared to the valuation in a multi-curve model with uncorrelated spread.',
            yMin: 20,
            yMax: 70,
    ]

    execute = {
        Map<String, Map> fileToProducts = [ 'results/pricing_2013_calib/additive_integrated_expectation_1/initial_calibration': Plot.DEFAULT_PLOTS.Caplets_Vola_Multi_Curve ]
        plotFromCSVWithErrors(fileToProducts)
    }
}

plot('calib_caplet_integrated_expectation_2013_additive') {
    params = [
            xLabel: 'Caplet Maturity [years]',
            yLabel: 'Caplet Vola [\\\\%]',
            caption: 'Valuation of ATM caplets in the single-curve model compared to the valuation in a multi-curve model with uncorrelated spread.',
            yMin: 20,
            yMax: 70,
    ]

    execute = {
        Map<String, Map> fileToProducts = [ 'results/pricing_2013_calib/additive_integrated_expectation_1/initial_calibration': Plot.DEFAULT_PLOTS.Caplets_Vola_Multi_Curve ]
        plotFromCSVWithErrors(fileToProducts)
    }
}

plot('calib_caplet_integrated_expectation_2010_additive') {
    params = [
            xLabel: 'Caplet Maturity [years]',
            yLabel: 'Caplet Vola [\\\\%]',
            caption: 'Valuation of ATM caplets in the single-curve model compared to the valuation in a multi-curve model with uncorrelated spread.',
            yMin: 20,
            yMax: 70,
    ]

    execute = {
        Map<String, Map> fileToProducts = [ 'results/pricing_2010_calib/additive_integrated_expectation_1/initial_calibration': Plot.DEFAULT_PLOTS.Caplets_Vola_Multi_Curve ]
        plotFromCSVWithErrors(fileToProducts)
    }
}

plot('calib_caplet_integrated_expectation_2015_multiplicative') {
    params = [
            xLabel: 'Caplet Maturity [years]',
            yLabel: 'Caplet Vola [\\\\%]',
            caption: 'Valuation of ATM caplets in the single-curve model compared to the valuation in a multi-curve model with uncorrelated spread.',
            yMin: 20,
            yMax: 70,
    ]

    execute = {
        Map<String, Map> fileToProducts = [ 'results/pricing_2015_calib/multiplicative_integrated_expectation_1/initial_calibration': Plot.DEFAULT_PLOTS.Caplets_Vola_Multi_Curve ]
        plotFromCSVWithErrors(fileToProducts)
    }
}

plot('calib_caplet_integrated_expectation_2013_multiplicative') {
    params = [
            xLabel: 'Caplet Maturity [years]',
            yLabel: 'Caplet Vola [\\\\%]',
            caption: 'Valuation of ATM caplets in the single-curve model compared to the valuation in a multi-curve model with uncorrelated spread.',
            yMin: 20,
            yMax: 70,
    ]

    execute = {
        Map<String, Map> fileToProducts = [ 'results/pricing_2013_calib/multiplicative_integrated_expectation_1/initial_calibration': Plot.DEFAULT_PLOTS.Caplets_Vola_Multi_Curve ]
        plotFromCSVWithErrors(fileToProducts)
    }
}

plot('calib_caplet_integrated_expectation_2010_multiplicative') {
    params = [
            xLabel: 'Caplet Maturity [years]',
            yLabel: 'Caplet Vola [\\\\%]',
            caption: 'Valuation of ATM caplets in the single-curve model compared to the valuation in a multi-curve model with uncorrelated spread.',
            yMin: 20,
            yMax: 70,
    ]

    execute = {
        Map<String, Map> fileToProducts = [ 'results/pricing_2010_calib/multiplicative_integrated_expectation_1/initial_calibration': Plot.DEFAULT_PLOTS.Caplets_Vola_Multi_Curve ]
        plotFromCSVWithErrors(fileToProducts)
    }
}