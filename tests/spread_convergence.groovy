package tests

import com.bmatthias.finmath.test.tools.tools.calibrationdata.SyntheticProductsWithCapletCalibration
import net.finmath.montecarlo.interestrate.MultiCurveLIBORMarketModel

def forwardCurve2010 = [ 0.0: 0.01, 2.0: 0.025, 3.0: 0.029, 6.0: 0.041, 9.0: 0.045, 12.0: 0.046, 15.0: 0.045, 18.0: 0.042, 21.0: 0.038 ]

/*
Creates ten tests with decreasing spread value correlation and otherwise identical parameters
 */
[ 10, 6, 3, 1, 0 ].each { val ->

    test("low_spread_value_convergence/${val}_additive") {
        params = [
                numberOfParams: 15,
                useSeperateCorrelationModels: true,
                numberOfFactors: 8,
                numberOfPaths: 100000,
                numberOfSeeds: 1,
                multiCurveModel: MultiCurveLIBORMarketModel.MultiCurveModel.ADDITIVE,
                oisParams: [ 1.2,0.2,0.2,-0.7,1.0,1.0,1.0,0.0 ],
                spreadParams: [ 1.6,0.2,0.2,-1.0,1.0,1.0,1.0,0.5,0.0 ],
                numberOfRandomRuns: 0
        ]

        calibrationData = SyntheticProductsWithCapletCalibration.newInstance(
                syntheticCurve('synth_EURIBOR', forwardCurve2010.collectEntries { k,v -> [ (k): v + 0.001 * val ] }),
                syntheticCurve('synth_OIS', forwardCurve2010),
                0.9, 0.85, SyntheticProductsWithCapletCalibration.CalibrationMethod.NONE
        )
    }
}

/*
Creates ten tests with decreasing spread value correlation and otherwise identical parameters
 */
[ 10, 6, 3, 1, 0 ].each { val ->
    test("low_spread_value_convergence/${val}_multiplicative") {
        params = [
                numberOfParams: 15,
                useSeperateCorrelationModels: true,
                numberOfFactors: 8,
                numberOfPaths: 100000,
                numberOfSeeds: 1,
                multiCurveModel: MultiCurveLIBORMarketModel.MultiCurveModel.MULTIPLICATIVE,
                oisParams: [ 1.2,0.2,0.2,-0.7,1.0,1.0,1.0,0.0 ],
                spreadParams: [ 1.6,0.2,0.2,-1.0,1.0,1.0,1.0,0.5,0.0 ],
                numberOfRandomRuns: 0
        ]

        calibrationData = SyntheticProductsWithCapletCalibration.newInstance(
                syntheticCurve('synth_EURIBOR', forwardCurve2010.collectEntries { k,v -> [ (k): v + 0.001 * val ] }),
                syntheticCurve('synth_OIS', forwardCurve2010),
                0.9, 0.85, SyntheticProductsWithCapletCalibration.CalibrationMethod.NONE
        )
    }
}

/*
Creates ten tests with decreasing spread value correlation and otherwise identical parameters
 */
[ 10, 6, 3, 1, 0 ].each { val ->
    test("low_spread_value_convergence/${val}_martingale") {
        params = [
                numberOfParams: 15,
                useSeperateCorrelationModels: true,
                numberOfFactors: 8,
                numberOfPaths: 100000,
                numberOfSeeds: 1,
                multiCurveModel: MultiCurveLIBORMarketModel.MultiCurveModel.MMARTINGALE,
                oisParams: [ 1.2,0.2,0.2,-0.7,1.0,1.0,1.0,0.0 ],
                spreadParams: [ 1.6,0.2,0.2,-1.0,1.0,1.0,1.0,0.5,0.0 ],
                numberOfRandomRuns: 0
        ]

        calibrationData = SyntheticProductsWithCapletCalibration.newInstance(
                syntheticCurve('synth_EURIBOR', forwardCurve2010.collectEntries { k,v -> [ (k): v + 0.001 * val ] }),
                syntheticCurve('synth_OIS', forwardCurve2010),
                0.9, 0.85, SyntheticProductsWithCapletCalibration.CalibrationMethod.NONE
        )
    }
}

/*
Creates ten tests with decreasing spread volatility and otherwise identical parameters
 */
[ 10, 6, 3, 1, 0 ].each { vol ->
    test("low_spread_vola_convergence/${vol}_additive") {
        params = [
                numberOfParams: 15,
                useSeperateCorrelationModels: true,
                numberOfFactors: 8,
                numberOfPaths: 100000,
                numberOfSeeds: 1,
                multiCurveModel: MultiCurveLIBORMarketModel.MultiCurveModel.ADDITIVE,
                oisParams: [ 1.2,0.2,0.2,-0.7,1.0,1.0,1.0,0.0 ],
                spreadParams: [ 0.6 - 0.06 * vol,0.0,0.1,0.0,1.0,1.0,1.0,0.5,0.0 ],
                numberOfRandomRuns: 0
        ]

        calibrationData = SyntheticProductsWithCapletCalibration.newInstance(
                forwardCurve('2013_EURIBOR_6M_Kienitz'),
                forwardCurve('2013_EONIA_OIS_Kienitz'),
                0.9, 0.85, SyntheticProductsWithCapletCalibration.CalibrationMethod.NONE
        )
    }
}

/*
Creates ten tests with decreasing spread volatility and otherwise identical parameters
 */
[ 10, 6, 3, 1, 0 ].each { vol ->
    test("low_spread_vola_convergence/${vol}_multiplicative") {
        params = [
                numberOfParams: 15,
                useSeperateCorrelationModels: true,
                numberOfFactors: 8,
                numberOfPaths: 100000,
                numberOfSeeds: 1,
                multiCurveModel: MultiCurveLIBORMarketModel.MultiCurveModel.MULTIPLICATIVE,
                oisParams: [ 1.2,0.2,0.2,-0.7,1.0,1.0,1.0,0.0 ],
                spreadParams: [ 0.6 - 0.06 * vol,0.0,0.1,0.0,1.0,1.0,1.0,0.5,0.0 ],
                numberOfRandomRuns: 0
        ]

        calibrationData = SyntheticProductsWithCapletCalibration.newInstance(
                forwardCurve('2013_EURIBOR_6M_Kienitz'),
                forwardCurve('2013_EONIA_OIS_Kienitz'),
                0.9, 0.85, SyntheticProductsWithCapletCalibration.CalibrationMethod.NONE
        )
    }
}

/*
Creates ten tests with decreasing spread volatility and otherwise identical parameters
 */
[ 10, 6, 3, 1, 0 ].each { vol ->
    test("low_spread_vola_convergence/${vol}_martingale") {
        params = [
                numberOfParams: 15,
                useSeperateCorrelationModels: true,
                numberOfFactors: 8,
                numberOfPaths: 100000,
                numberOfSeeds: 1,
                multiCurveModel: MultiCurveLIBORMarketModel.MultiCurveModel.MMARTINGALE,
                oisParams: [ 1.2,0.2,0.2,-0.7,1.0,1.0,1.0,0.0 ],
                spreadParams: [ 0.6 - 0.06 * vol,0.0,0.1,0.0,1.0,1.0,1.0,0.5,0.0 ],
                numberOfRandomRuns: 0
        ]

        calibrationData = SyntheticProductsWithCapletCalibration.newInstance(
                forwardCurve('2013_EURIBOR_6M_Kienitz'),
                forwardCurve('2013_EONIA_OIS_Kienitz'),
                0.9, 0.85, SyntheticProductsWithCapletCalibration.CalibrationMethod.NONE
        )
    }
}