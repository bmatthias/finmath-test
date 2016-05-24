plot('convergence_single_curve_forwards_2015') {
    params = [
            xLabel: 'Time [years]',
            yLabel: 'Forward Rate [\\\\%]',
            caption: 'Valuation of single-curve forward rates with different amounts of Monte- Carlo paths. Here, MxN means the average was taken over M runs with N paths, respectively.',
            yMin: -0.2,
            yMax: 0.03,
    ]

    execute = {
        Map fileToProduct = [
                'results/pricing_2015_convergence/1000_additive_1/initial_calibration' : [ 'Reference': 'OIS Forward Rate Multi-Curve',
                                                                                           '1x1000': 'OIS Spot Rate Single-Curve'],
                'results/pricing_2015_convergence/10000_additive_1/initial_calibration' : [ '1x10000': 'OIS Spot Rate Single-Curve' ],
                'results/pricing_2015_convergence/100000_additive_1/initial_calibration' : [ '1x100000': 'OIS Spot Rate Single-Curve' ],
                'results/pricing_2015_convergence/1000000_additive_1/initial_calibration' : [ '10x100000': 'OIS Spot Rate Single-Curve' ]
        ]
        plotFromCSV(fileToProduct)
    }
}

plot('convergence_single_curve_forwards_2015') {
    params = [
            xLabel: 'Time [years]',
            yLabel: 'error',
            caption: 'Valuation of single-curve forward rates with different amounts of Monte- Carlo paths. Here, MxN means the average was taken over M runs with N paths, respectively.',
            yMin: 0.0,
            yMax: 0.03,
    ]

    execute = {
        Map fileToComparisons = [
                'results/pricing_2015_convergence/1000_additive_1/initial_calibration' : [ '1x1000': [ 'OIS Forward Rate Multi-Curve', 'OIS Spot Rate Single-Curve' ] ],
                'results/pricing_2015_convergence/10000_additive_1/initial_calibration' : [ '1x10000': [ 'OIS Forward Rate Multi-Curve', 'OIS Spot Rate Single-Curve' ] ],
                'results/pricing_2015_convergence/100000_additive_1/initial_calibration' : [ '1x100000': [ 'OIS Forward Rate Multi-Curve', 'OIS Spot Rate Single-Curve' ] ],
                'results/pricing_2015_convergence/1000000_additive_1/initial_calibration' : [ '10x100000': [ 'OIS Forward Rate Multi-Curve', 'OIS Spot Rate Single-Curve' ] ]
        ]
        plotErrors(fileToComparisons)
    }
}

plot('convergence_single_curve_caplets_2015') {
    params = [
            xLabel: 'Caplet Maturity [years]',
            yLabel: 'Caplet Volatility [\\\\%]',
            caption: 'Valuation of ATM caplets with underlying (single-curve) forward rates. Monte-Carlo valuation with different amounts of paths is compared to the analytical valuation through the Black formula.',
            yMin: 30,
            yMax: 120,
    ]

    execute = {
        Map fileToProduct = [
                'results/pricing_2015_convergence/1000_additive_1/initial_calibration' : [ 'Analytic': 'Caplet Vola Analytic Single-Curve',
                                                                                                 '1x1000': 'Caplet Vola Monte-Carlo Single-Curve'],
                'results/pricing_2015_convergence/10000_additive_1/initial_calibration' : [ '1x10000': 'Caplet Vola Monte-Carlo Single-Curve' ],
                'results/pricing_2015_convergence/100000_additive_1/initial_calibration' : [ '1x100000': 'Caplet Vola Monte-Carlo Single-Curve' ],
                'results/pricing_2015_convergence/1000000_additive_1/initial_calibration' : [ '10x100000': 'Caplet Vola Monte-Carlo Single-Curve' ]
        ]
        plotFromCSV(fileToProduct)
    }
}

plot('convergence_single_curve_caplets_2015') {
    params = [
            xLabel: 'Caplet Maturity [years]',
            yLabel: 'error',
            caption: 'Errors of Monte-Carlo valuation of caplets vs. their Black valuation in a single-curve model.',
            //yMax: 120,
    ]

    execute = {
        Map fileToComparisons = [
                'results/pricing_2015_convergence/1000_additive_1/initial_calibration' : [ '1x1000': [ 'Caplet Vola Analytic Single-Curve', 'Caplet Vola Monte-Carlo Single-Curve' ] ],
                'results/pricing_2015_convergence/10000_additive_1/initial_calibration' : [ '1x10000': [ 'Caplet Vola Analytic Single-Curve', 'Caplet Vola Monte-Carlo Single-Curve' ] ],
                'results/pricing_2015_convergence/100000_additive_1/initial_calibration' : [ '1x100000': [ 'Caplet Vola Analytic Single-Curve', 'Caplet Vola Monte-Carlo Single-Curve' ] ],
                'results/pricing_2015_convergence/1000000_additive_1/initial_calibration' : [ '10x100000': [ 'Caplet Vola Analytic Single-Curve', 'Caplet Vola Monte-Carlo Single-Curve' ] ]
        ]
        plotErrors(fileToComparisons)
    }
}