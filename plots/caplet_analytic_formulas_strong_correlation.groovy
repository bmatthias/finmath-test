import com.bmatthias.finmath.test.Helper
import com.bmatthias.finmath.test.Plot

plot('caplet_analytic_formulas_strong_correlation_2013') {
    params = [
            xLabel: 'Caplet Maturity [years]',
            yLabel: 'Caplet Vola [\\\\%]',
            caption: 'Valuation of ATM caplets in the single-curve model compared to the valuation in a multi-curve model with perfectly correlated spread.',
            yMin: 20,
            yMax: 70,
    ]

    execute = {
        Map<String, Map> fileToProducts = [ 'results/pricing_2013_increasing_correlation/10_1': [
                'Monte-Carlo Single-Curve'  : 'Caplet Vola Monte-Carlo Single-Curve',
                'Monte-Carlo Multi-Curve'   : 'Caplet Vola Monte-Carlo Multi-Curve',
                'Analytic Single-Curve'     : 'Caplet Vola Analytic Single-Curve',
                'Integrated-Expectation'    : 'Caplet Vola Analytic Multi-Curve Integrated-Expectation' ,
                'Levy'                      : 'Caplet Vola Analytic Multi-Curve Fenton-Wilkinson' ,
                'Ju'                        : 'Caplet Vola Analytic Multi-Curve Ju' ,
                'Ho'                        : 'Caplet Vola Analytic Multi-Curve Ho'
        ]]
        plotFromCSV(fileToProducts)
    }
}

plot('caplet_analytic_formulas_strong_correlation_2010') {
    params = [
            xLabel: 'Caplet Maturity [years]',
            yLabel: 'Caplet Vola [\\\\%]',
            caption: 'Valuation of ATM caplets in the single-curve model compared to the valuation in a multi-curve model with perfectly correlated spread.',
            yMin: 20,
            yMax: 70,
    ]

    execute = {
        Map<String, Map> fileToProducts = [ 'results/pricing_2010_increasing_correlation/10_1': [
                'Monte-Carlo Single-Curve'  : 'Caplet Vola Monte-Carlo Single-Curve',
                'Monte-Carlo Multi-Curve'   : 'Caplet Vola Monte-Carlo Multi-Curve',
                'Analytic Single-Curve'     : 'Caplet Vola Analytic Single-Curve',
                'Integrated-Expectation'    : 'Caplet Vola Analytic Multi-Curve Integrated-Expectation' ,
                'Levy'                      : 'Caplet Vola Analytic Multi-Curve Fenton-Wilkinson' ,
                'Ju'                        : 'Caplet Vola Analytic Multi-Curve Ju' ,
                'Ho'                        : 'Caplet Vola Analytic Multi-Curve Ho'
        ]]
        plotFromCSV(fileToProducts)
    }
}

plot('caplet_analytic_formulas_strong_correlation_synth') {
    params = [
            xLabel: 'Caplet Maturity [years]',
            yLabel: 'Caplet Vola [\\\\%]',
            caption: 'Valuation of ATM caplets in the single-curve model compared to the valuation in a multi-curve model with perfectly correlated spread.',
            yMin: 20,
            yMax: 70,
    ]

    execute = {
        Map<String, Map> fileToProducts = [ 'results/pricing_synth_increasing_correlation/10_1': [
                'Monte-Carlo Single-Curve'  : 'Caplet Vola Monte-Carlo Single-Curve',
                'Monte-Carlo Multi-Curve'   : 'Caplet Vola Monte-Carlo Multi-Curve',
                'Analytic Single-Curve'     : 'Caplet Vola Analytic Single-Curve',
                'Integrated-Expectation'    : 'Caplet Vola Analytic Multi-Curve Integrated-Expectation' ,
                'Levy'                      : 'Caplet Vola Analytic Multi-Curve Fenton-Wilkinson' ,
                'Ju'                        : 'Caplet Vola Analytic Multi-Curve Ju' ,
                'Ho'                        : 'Caplet Vola Analytic Multi-Curve Ho'
        ]]
        plotFromCSV(fileToProducts)
    }
}