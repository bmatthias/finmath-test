plot('convergence_low_spread_vola_forwards_additive') {
    params = [
            xLabel: 'Time [years]',
            yLabel: 'Forward Rate [\\\\%]',
            caption: 'Convergence of the LIBOR curve to the OIS curve for spreads converging to 0.',
            yMin: 1,
            yMax: 3,
    ]

    execute = {
        Map fileToProduct = [
                'results/low_spread_vola_convergence/0_additive_1/initial_calibration' : [ 'OIS': 'OIS Forward Rate Multi-Curve',
                                                                                           'LIBOR': 'LIBOR Spot Rate Multi-Curve'],
                'results/low_spread_vola_convergence/1_additive_1/initial_calibration' : [ 'LIBOR': 'LIBOR Spot Rate Multi-Curve' ],
                'results/low_spread_vola_convergence/3_additive_1/initial_calibration' : [ 'LIBOR': 'LIBOR Spot Rate Multi-Curve' ],
                'results/low_spread_vola_convergence/6_additive_1/initial_calibration' : [ 'LIBOR': 'LIBOR Spot Rate Multi-Curve' ],
                'results/low_spread_vola_convergence/10_additive_1/initial_calibration' : [ 'LIBOR': 'LIBOR Spot Rate Multi-Curve' ]

        ]
        plotFromCSV(fileToProduct)
    }
}

plot('convergence_low_spread_vola_forwards_multiplicative') {
    params = [
            xLabel: 'Time [years]',
            yLabel: 'Forward Rate [\\\\%]',
            caption: 'Convergence of the LIBOR curve to the OIS curve for spreads converging to 0.',
            yMin: 1,
            yMax: 3,
    ]

    execute = {
        Map fileToProduct = [
                'results/low_spread_vola_convergence/0_multiplicative_1/initial_calibration' : [ 'OIS': 'OIS Forward Rate Multi-Curve',
                                                                                   'LIBOR': 'LIBOR Spot Rate Multi-Curve'],
                'results/low_spread_vola_convergence/1_multiplicative_1/initial_calibration' : [ 'LIBOR': 'LIBOR Spot Rate Multi-Curve' ],
                'results/low_spread_vola_convergence/3_multiplicative_1/initial_calibration' : [ 'LIBOR': 'LIBOR Spot Rate Multi-Curve' ],
                'results/low_spread_vola_convergence/6_multiplicative_1/initial_calibration' : [ 'LIBOR': 'LIBOR Spot Rate Multi-Curve' ],
                'results/low_spread_vola_convergence/10_multiplicative_1/initial_calibration' : [ 'LIBOR': 'LIBOR Spot Rate Multi-Curve' ]

        ]
        plotFromCSV(fileToProduct)
    }
}

plot('convergence_low_spread_vola_forwards_martingale') {
    params = [
            xLabel: 'Time [years]',
            yLabel: 'Forward Rate [\\\\%]',
            caption: 'Convergence of the LIBOR curve to the OIS curve for spreads converging to 0.',
            yMin: 1,
            yMax: 3,
    ]

    execute = {
        Map fileToProduct = [
                'results/low_spread_vola_convergence/0_martingale_1/initial_calibration' : [ 'OIS': 'OIS Forward Rate Multi-Curve',
                                                                                                  'LIBOR': 'LIBOR Spot Rate Multi-Curve'],
                'results/low_spread_vola_convergence/1_martingale_1/initial_calibration' : [ 'LIBOR': 'LIBOR Spot Rate Multi-Curve' ],
                'results/low_spread_vola_convergence/3_martingale_1/initial_calibration' : [ 'LIBOR': 'LIBOR Spot Rate Multi-Curve' ],
                'results/low_spread_vola_convergence/6_martingale_1/initial_calibration' : [ 'LIBOR': 'LIBOR Spot Rate Multi-Curve' ],
                'results/low_spread_vola_convergence/10_martingale_1/initial_calibration' : [ 'LIBOR': 'LIBOR Spot Rate Multi-Curve' ]

        ]
        plotFromCSV(fileToProduct)
    }
}