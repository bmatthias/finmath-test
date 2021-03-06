plot('convergence_low_spread_vola_caplets_additive') {
    params = [
            xLabel: 'Time [years]',
            yLabel: 'Forward Rate [\\\\%]',
            caption: 'Convergence of the LIBOR curve to the OIS curve for spreads converging to 0.',
            yMin: 0,
            yMax: 0.4,
    ]

    execute = {
        Map fileToProduct = [
                'results/low_spread_vola_convergence/0_additive_1/initial_calibration' : [ 'OIS': 'Caplet Value Monte-Carlo Single-Curve',
                                                                                           'LIBOR': 'Caplet Value Monte-Carlo Multi-Curve'],
                'results/low_spread_vola_convergence/1_additive_1/initial_calibration' : [ 'LIBOR': 'Caplet Value Monte-Carlo Multi-Curve' ],
                'results/low_spread_vola_convergence/3_additive_1/initial_calibration' : [ 'LIBOR': 'Caplet Value Monte-Carlo Multi-Curve' ],
                'results/low_spread_vola_convergence/6_additive_1/initial_calibration' : [ 'LIBOR': 'Caplet Value Monte-Carlo Multi-Curve' ],
                'results/low_spread_vola_convergence/10_additive_1/initial_calibration' : [ 'LIBOR': 'Caplet Value Monte-Carlo Multi-Curve' ]

        ]
        plotFromCSV(fileToProduct)
    }
}

plot('convergence_low_spread_vola_caplets_multiplicative') {
    params = [
            xLabel: 'Time [years]',
            yLabel: 'Forward Rate [\\\\%]',
            caption: 'Convergence of the LIBOR curve to the OIS curve for spreads converging to 0.',
            yMin: 0,
            yMax: 0.4,
    ]

    execute = {
        Map fileToProduct = [
                'results/low_spread_vola_convergence/0_multiplicative_1/initial_calibration' : [ 'OIS': 'Caplet Value Monte-Carlo Single-Curve',
                                                                                   'LIBOR': 'Caplet Value Monte-Carlo Multi-Curve'],
                'results/low_spread_vola_convergence/1_multiplicative_1/initial_calibration' : [ 'LIBOR': 'Caplet Value Monte-Carlo Multi-Curve' ],
                'results/low_spread_vola_convergence/3_multiplicative_1/initial_calibration' : [ 'LIBOR': 'Caplet Value Monte-Carlo Multi-Curve' ],
                'results/low_spread_vola_convergence/6_multiplicative_1/initial_calibration' : [ 'LIBOR': 'Caplet Value Monte-Carlo Multi-Curve' ],
                'results/low_spread_vola_convergence/10_multiplicative_1/initial_calibration' : [ 'LIBOR': 'Caplet Value Monte-Carlo Multi-Curve' ]

        ]
        plotFromCSV(fileToProduct)
    }
}

plot('convergence_low_spread_vola_caplets_martingale') {
    params = [
            xLabel: 'Time [years]',
            yLabel: 'Forward Rate [\\\\%]',
            caption: 'Convergence of the LIBOR curve to the OIS curve for spreads converging to 0.',
            yMin: 0,
            yMax: 0.4,
    ]

    execute = {
        Map fileToProduct = [
                'results/low_spread_vola_convergence/0_martingale_1/initial_calibration' : [ 'OIS': 'Caplet Value Monte-Carlo Single-Curve',
                                                                                                  'LIBOR': 'Caplet Value Monte-Carlo Multi-Curve'],
                'results/low_spread_vola_convergence/1_martingale_1/initial_calibration' : [ 'LIBOR': 'Caplet Value Monte-Carlo Multi-Curve' ],
                'results/low_spread_vola_convergence/3_martingale_1/initial_calibration' : [ 'LIBOR': 'Caplet Value Monte-Carlo Multi-Curve' ],
                'results/low_spread_vola_convergence/6_martingale_1/initial_calibration' : [ 'LIBOR': 'Caplet Value Monte-Carlo Multi-Curve' ],
                'results/low_spread_vola_convergence/10_martingale_1/initial_calibration' : [ 'LIBOR': 'Caplet Value Monte-Carlo Multi-Curve' ]

        ]
        plotFromCSV(fileToProduct)
    }
}