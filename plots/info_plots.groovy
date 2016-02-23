plot('caplet_volatility_curves') {
    params = [
            xLabel: 'Caplet Maturity [years]',
            yLabel: 'Caplet Volatility [\\\\%]',
            caption: 'Target caplet volatilities in different test scenarios.',
            yMin: 10,
            yMax: 110,
    ]

    execute = {
        Map fileToProduct = [
                'results/pricing_2010_convergence/1000_additive_1/initial_calibration' : [ 'Low Volatility': 'Target (LIBOR)' ],
                'results/pricing_2013_convergence/1000_additive_1/initial_calibration' : [ 'Medium Volatility': 'Target (LIBOR)' ],
                'results/pricing_2015_convergence/1000_additive_1/initial_calibration' : [ 'High Volatility': 'Target (LIBOR)' ]
        ]
        plotFromCSV(fileToProduct)
    }
}

plot('forward_curves') {
    params = [
            xLabel: 'Time [years]',
            yLabel: 'Forward Rate [\\\\%]',
            caption: 'Different forward rates test scenarios.',
            yMin: -0.5,
            yMax: 5,
    ]

    execute = {
        Map fileToProduct = [
                'results/pricing_2010_convergence/1000_additive_1/initial_calibration' : [ 'LIBOR High': 'LIBOR Forward Rate Multi-Curve',
                                                                   'OIS High': 'OIS Forward Rate Multi-Curve' ],
                'results/pricing_2013_convergence/1000_additive_1/initial_calibration' : [ 'LIBOR Low': 'LIBOR Forward Rate Multi-Curve',
                                                                   'OIS Low': 'OIS Forward Rate Multi-Curve' ],
                'results/pricing_2015_convergence/1000_additive_1/initial_calibration' : [ 'LIBOR Negative': 'LIBOR Forward Rate Multi-Curve',
                                                                   'OIS Negative': 'OIS Forward Rate Multi-Curve' ],
        ]
        plotFromCSV(fileToProduct)
    }
}