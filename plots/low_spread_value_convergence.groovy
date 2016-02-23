plot('convergence_low_spread_value_additive') {
    params = [
            xLabel: 'Time [years]',
            yLabel: 'Forward Rate [\\\\%]',
            caption: 'Convergence of the LIBOR curve to the OIS curve for spreads converging to 0.',
            yMin: 1,
            yMax: 3,
    ]

    execute = {
        Map fileToProduct = [
                'results/low_spread_value_convergence/0_additive_1/initial_calibration' : [ 'OIS': 'OIS Forward Rate Multi-Curve',
                                                                                           'LIBOR': 'LIBOR Spot Rate Multi-Curve'],
                'results/low_spread_value_convergence/1_additive_1/initial_calibration' : [ 'LIBOR': 'LIBOR Spot Rate Multi-Curve' ],
                'results/low_spread_value_convergence/2_additive_1/initial_calibration' : [ 'LIBOR': 'LIBOR Spot Rate Multi-Curve' ],
                'results/low_spread_value_convergence/3_additive_1/initial_calibration' : [ 'LIBOR': 'LIBOR Spot Rate Multi-Curve' ],
                'results/low_spread_value_convergence/4_additive_1/initial_calibration' : [ 'LIBOR': 'LIBOR Spot Rate Multi-Curve' ],
                'results/low_spread_value_convergence/5_additive_1/initial_calibration' : [ 'LIBOR': 'LIBOR Spot Rate Multi-Curve' ],
                'results/low_spread_value_convergence/6_additive_1/initial_calibration' : [ 'LIBOR': 'LIBOR Spot Rate Multi-Curve' ],
                'results/low_spread_value_convergence/7_additive_1/initial_calibration' : [ 'LIBOR': 'LIBOR Spot Rate Multi-Curve' ],
                'results/low_spread_value_convergence/8_additive_1/initial_calibration' : [ 'LIBOR': 'LIBOR Spot Rate Multi-Curve' ],
                'results/low_spread_value_convergence/9_additive_1/initial_calibration' : [ 'LIBOR': 'LIBOR Spot Rate Multi-Curve' ],
                'results/low_spread_value_convergence/10_additive_1/initial_calibration' : [ 'LIBOR': 'LIBOR Spot Rate Multi-Curve' ]

        ]
        plotFromCSV(fileToProduct)
    }
}

plot('convergence_low_spread_value_multiplicative') {
    params = [
            xLabel: 'Time [years]',
            yLabel: 'Forward Rate [\\\\%]',
            caption: 'Convergence of the LIBOR curve to the OIS curve for spreads converging to 0.',
            yMin: 1,
            yMax: 3,
    ]

    execute = {
        Map fileToProduct = [
                'results/low_spread_value_convergence/0_multiplicative_1/initial_calibration' : [ 'OIS': 'OIS Forward Rate Multi-Curve',
                                                                                   'LIBOR': 'LIBOR Spot Rate Multi-Curve'],
                'results/low_spread_value_convergence/1_multiplicative_1/initial_calibration' : [ 'LIBOR': 'LIBOR Spot Rate Multi-Curve' ],
                'results/low_spread_value_convergence/2_multiplicative_1/initial_calibration' : [ 'LIBOR': 'LIBOR Spot Rate Multi-Curve' ],
                'results/low_spread_value_convergence/3_multiplicative_1/initial_calibration' : [ 'LIBOR': 'LIBOR Spot Rate Multi-Curve' ],
                'results/low_spread_value_convergence/4_multiplicative_1/initial_calibration' : [ 'LIBOR': 'LIBOR Spot Rate Multi-Curve' ],
                'results/low_spread_value_convergence/5_multiplicative_1/initial_calibration' : [ 'LIBOR': 'LIBOR Spot Rate Multi-Curve' ],
                'results/low_spread_value_convergence/6_multiplicative_1/initial_calibration' : [ 'LIBOR': 'LIBOR Spot Rate Multi-Curve' ],
                'results/low_spread_value_convergence/7_multiplicative_1/initial_calibration' : [ 'LIBOR': 'LIBOR Spot Rate Multi-Curve' ],
                'results/low_spread_value_convergence/8_multiplicative_1/initial_calibration' : [ 'LIBOR': 'LIBOR Spot Rate Multi-Curve' ],
                'results/low_spread_value_convergence/9_multiplicative_1/initial_calibration' : [ 'LIBOR': 'LIBOR Spot Rate Multi-Curve' ],
                'results/low_spread_value_convergence/10_multiplicative_1/initial_calibration' : [ 'LIBOR': 'LIBOR Spot Rate Multi-Curve' ]

        ]
        plotFromCSV(fileToProduct)
    }
}

plot('convergence_low_spread_value_martingale') {
    params = [
            xLabel: 'Time [years]',
            yLabel: 'Forward Rate [\\\\%]',
            caption: 'Convergence of the LIBOR curve to the OIS curve for spreads converging to 0.',
            yMin: 1,
            yMax: 3,
    ]

    execute = {
        Map fileToProduct = [
                'results/low_spread_value_convergence/0_martingale_1/initial_calibration' : [ 'OIS': 'OIS Forward Rate Multi-Curve',
                                                                                                  'LIBOR': 'LIBOR Spot Rate Multi-Curve'],
                'results/low_spread_value_convergence/1_martingale_1/initial_calibration' : [ 'LIBOR': 'LIBOR Spot Rate Multi-Curve' ],
                'results/low_spread_value_convergence/2_martingale_1/initial_calibration' : [ 'LIBOR': 'LIBOR Spot Rate Multi-Curve' ],
                'results/low_spread_value_convergence/3_martingale_1/initial_calibration' : [ 'LIBOR': 'LIBOR Spot Rate Multi-Curve' ],
                'results/low_spread_value_convergence/4_martingale_1/initial_calibration' : [ 'LIBOR': 'LIBOR Spot Rate Multi-Curve' ],
                'results/low_spread_value_convergence/5_martingale_1/initial_calibration' : [ 'LIBOR': 'LIBOR Spot Rate Multi-Curve' ],
                'results/low_spread_value_convergence/6_martingale_1/initial_calibration' : [ 'LIBOR': 'LIBOR Spot Rate Multi-Curve' ],
                'results/low_spread_value_convergence/7_martingale_1/initial_calibration' : [ 'LIBOR': 'LIBOR Spot Rate Multi-Curve' ],
                'results/low_spread_value_convergence/8_martingale_1/initial_calibration' : [ 'LIBOR': 'LIBOR Spot Rate Multi-Curve' ],
                'results/low_spread_value_convergence/9_martingale_1/initial_calibration' : [ 'LIBOR': 'LIBOR Spot Rate Multi-Curve' ],
                'results/low_spread_value_convergence/10_martingale_1/initial_calibration' : [ 'LIBOR': 'LIBOR Spot Rate Multi-Curve' ]

        ]
        plotFromCSV(fileToProduct)
    }
}