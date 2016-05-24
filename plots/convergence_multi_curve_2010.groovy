plot('convergence_multi_curve_ibor_forward_additive_2010') {
    params = [
            xLabel: 'Time [years]',
            yLabel: 'Forward Rate [\\\\%]',
            caption: 'Valuation of IBOR forward rates in the additive model with different amounts of Monte-Carlo paths.',
            yMin: -0.2,
            yMax: 0.03,
    ]

    execute = {
        Map fileToProduct = [
                'results/pricing_2010_convergence/1000_additive_1/initial_calibration' : [ 'Reference': 'LIBOR Forward Rate Multi-Curve',
                                                                                           '1x1000': 'LIBOR Spot Rate Multi-Curve'],
                'results/pricing_2010_convergence/10000_additive_1/initial_calibration' : [ '1x10000': 'LIBOR Spot Rate Multi-Curve' ],
                'results/pricing_2010_convergence/100000_additive_1/initial_calibration' : [ '1x100000': 'LIBOR Spot Rate Multi-Curve' ],
                'results/pricing_2010_convergence/1000000_additive_1/initial_calibration' : [ '10x100000': 'LIBOR Spot Rate Multi-Curve' ]
        ]
        plotFromCSV(fileToProduct)
    }
}

plot('convergence_multi_curve_ois_forward_additive_2010') {
    params = [
            xLabel: 'Time [years]',
            yLabel: 'Forward Rate [\\\\%]',
            caption: 'Valuation of OIS forward rates in the additive model with different amounts of Monte-Carlo paths.',
            yMin: -0.2,
            yMax: 0.03,
    ]

    execute = {
        Map fileToProduct = [
                'results/pricing_2010_convergence/1000_additive_1/initial_calibration' : [ 'Reference': 'OIS Forward Rate Multi-Curve',
                                                                                           '1x1000': 'OIS Spot Rate Single-Curve'],
                'results/pricing_2010_convergence/10000_additive_1/initial_calibration' : [ '1x10000': 'OIS Spot Rate Single-Curve' ],
                'results/pricing_2010_convergence/100000_additive_1/initial_calibration' : [ '1x100000': 'OIS Spot Rate Single-Curve' ],
                'results/pricing_2010_convergence/1000000_additive_1/initial_calibration' : [ '10x100000': 'OIS Spot Rate Single-Curve' ]
        ]
        plotFromCSV(fileToProduct)
    }
}

plot('convergence_multi_curve_ibor_forward_additive_2010') {
    params = [
            xLabel: 'Time [years]',
            yLabel: 'error',
            caption: 'Errors of Monte-Carlo valuation of (IBOR) forward rates vs. their expected values in the additive model.',
            yMin: 0.0,
            yMax: 0.03,
    ]

    execute = {
        Map fileToComparisons = [
                'results/pricing_2010_convergence/1000_additive_1/initial_calibration' : [ '1x1000': [ 'LIBOR Forward Rate Multi-Curve', 'LIBOR Spot Rate Multi-Curve' ] ],
                'results/pricing_2010_convergence/10000_additive_1/initial_calibration' : [ '1x10000': [ 'LIBOR Forward Rate Multi-Curve', 'LIBOR Spot Rate Multi-Curve' ] ],
                'results/pricing_2010_convergence/100000_additive_1/initial_calibration' : [ '1x100000': [ 'LIBOR Forward Rate Multi-Curve', 'LIBOR Spot Rate Multi-Curve' ] ],
                'results/pricing_2010_convergence/1000000_additive_1/initial_calibration' : [ '10x100000': [ 'LIBOR Forward Rate Multi-Curve', 'LIBOR Spot Rate Multi-Curve' ] ]
        ]
        plotErrors(fileToComparisons)
    }
}

plot('convergence_multi_curve_ois_forward_additive_2010') {
    params = [
            xLabel: 'Time [years]',
            yLabel: 'error',
            caption: 'Errors of Monte-Carlo valuation of (OIS) forward rates vs. their expected values in the additive model.',
            yMin: 0.0,
            yMax: 0.03,
    ]

    execute = {
        Map fileToComparisons = [
                'results/pricing_2010_convergence/1000_additive_1/initial_calibration' : [ '1x1000': [ 'OIS Forward Rate Multi-Curve', 'OIS Spot Rate Single-Curve' ] ],
                'results/pricing_2010_convergence/10000_additive_1/initial_calibration' : [ '1x10000': [ 'OIS Forward Rate Multi-Curve', 'OIS Spot Rate Single-Curve' ] ],
                'results/pricing_2010_convergence/100000_additive_1/initial_calibration' : [ '1x100000': [ 'OIS Forward Rate Multi-Curve', 'OIS Spot Rate Single-Curve' ] ],
                'results/pricing_2010_convergence/1000000_additive_1/initial_calibration' : [ '10x100000': [ 'OIS Forward Rate Multi-Curve', 'OIS Spot Rate Single-Curve' ] ]
        ]
        plotErrors(fileToComparisons)
    }
}

plot('convergence_multi_curve_ois_forward_multiplicative_2010') {
    params = [
            xLabel: 'Time [years]',
            yLabel: 'Forward Rate [\\\\%]',
            caption: 'Valuation of IBOR forward rates in the multiplicative model with different amounts of Monte-Carlo paths.',
            yMin: -0.2,
            yMax: 0.03,
    ]

    execute = {
        Map fileToProduct = [
                'results/pricing_2010_convergence/1000_multiplicative_1/initial_calibration' : [ 'Reference': 'OIS Forward Rate Multi-Curve',
                                                                                           '1x1000': 'OIS Spot Rate Single-Curve'],
                'results/pricing_2010_convergence/10000_multiplicative_1/initial_calibration' : [ '1x10000': 'OIS Spot Rate Single-Curve' ],
                'results/pricing_2010_convergence/100000_multiplicative_1/initial_calibration' : [ '1x100000': 'OIS Spot Rate Single-Curve' ],
                'results/pricing_2010_convergence/1000000_multiplicative_1/initial_calibration' : [ '10x100000': 'OIS Spot Rate Single-Curve' ]
        ]
        plotFromCSV(fileToProduct)
    }
}

plot('convergence_multi_curve_ibor_forward_multiplicative_2010') {
    params = [
            xLabel: 'Time [years]',
            yLabel: 'Forward Rate [\\\\%]',
            caption: 'Valuation of OIS forward rates in the multiplicative model with different amounts of Monte-Carlo paths.',
            yMin: -0.2,
            yMax: 0.03,
    ]

    execute = {
        Map fileToProduct = [
                'results/pricing_2010_convergence/1000_multiplicative_1/initial_calibration' : [ 'Reference': 'LIBOR Forward Rate Multi-Curve',
                                                                                                 '1x1000': 'LIBOR Spot Rate Multi-Curve'],
                'results/pricing_2010_convergence/10000_multiplicative_1/initial_calibration' : [ '1x10000': 'LIBOR Spot Rate Multi-Curve' ],
                'results/pricing_2010_convergence/100000_multiplicative_1/initial_calibration' : [ '1x100000': 'LIBOR Spot Rate Multi-Curve' ],
                'results/pricing_2010_convergence/1000000_multiplicative_1/initial_calibration' : [ '10x100000': 'LIBOR Spot Rate Multi-Curve' ]
        ]
        plotFromCSV(fileToProduct)
    }
}

plot('convergence_multi_curve_ibor_forward_multiplicative_2010') {
    params = [
            xLabel: 'Time [years]',
            yLabel: 'error',
            caption: 'Errors of Monte-Carlo valuation of (IBOR) forward rates vs. their expected values in the multiplicative model.',
            yMin: 0.0,
            yMax: 0.03,
    ]

    execute = {
        Map fileToComparisons = [
                'results/pricing_2010_convergence/1000_multiplicative_1/initial_calibration' : [ '1x1000': [ 'LIBOR Forward Rate Multi-Curve', 'LIBOR Spot Rate Multi-Curve' ] ],
                'results/pricing_2010_convergence/10000_multiplicative_1/initial_calibration' : [ '1x10000': [ 'LIBOR Forward Rate Multi-Curve', 'LIBOR Spot Rate Multi-Curve' ] ],
                'results/pricing_2010_convergence/100000_multiplicative_1/initial_calibration' : [ '1x100000': [ 'LIBOR Forward Rate Multi-Curve', 'LIBOR Spot Rate Multi-Curve' ] ],
                'results/pricing_2010_convergence/1000000_multiplicative_1/initial_calibration' : [ '10x100000': [ 'LIBOR Forward Rate Multi-Curve', 'LIBOR Spot Rate Multi-Curve' ] ]
        ]
        plotErrors(fileToComparisons)
    }
}

plot('convergence_multi_curve_ois_forward_multiplicative_2010') {
    params = [
            xLabel : 'Time [years]',
            yLabel : 'error',
            caption: 'Errors of Monte-Carlo valuation of (OIS) forward rates vs. their expected values in the multiplicative model.',
            yMin   : 0.0,
            yMax   : 0.03,
    ]

    execute = {
        Map fileToComparisons = [
                'results/pricing_2010_convergence/1000_multiplicative_1/initial_calibration' : [ '1x1000': [ 'OIS Forward Rate Multi-Curve', 'OIS Spot Rate Single-Curve' ] ],
                'results/pricing_2010_convergence/10000_multiplicative_1/initial_calibration' : [ '1x10000': [ 'OIS Forward Rate Multi-Curve', 'OIS Spot Rate Single-Curve' ] ],
                'results/pricing_2010_convergence/100000_multiplicative_1/initial_calibration' : [ '1x100000': [ 'OIS Forward Rate Multi-Curve', 'OIS Spot Rate Single-Curve' ] ],
                'results/pricing_2010_convergence/1000000_multiplicative_1/initial_calibration' : [ '10x100000': [ 'OIS Forward Rate Multi-Curve', 'OIS Spot Rate Single-Curve' ] ]
        ]
        plotErrors(fileToComparisons)
    }
}