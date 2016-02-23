package tests

import com.bmatthias.finmath.test.tools.tools.calibrationdata.SyntheticProductsWithCapletCalibration
import net.finmath.montecarlo.interestrate.MultiCurveLIBORMarketModel

/*
Creates ten tests with decreasing spread value correlation and otherwise identical parameters
 */
(0..10).each { val ->
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
                syntheticCurve('synth_EURIBOR', [ 0.0: 0.03 - 0.02 * val, 5.0: 0.03 - 0.01 * val, 10.0: 0.03 - 0.01 * val, 15.0: 0.03 - 0.01 * val, 20.0: 0.03 - 0.02 * val ]),
                syntheticCurve('synth_OIS', [ 0.0: 0.01, 5.0: 0.02, 10.0: 0.02, 15.0: 0.02, 20.0: 0.01 ]),
                0.9, 0.85, SyntheticProductsWithCapletCalibration.CalibrationMethod.NONE
        )
    }
}

/*
Creates ten tests with decreasing spread value correlation and otherwise identical parameters
 */
(0..10).each { val ->
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
                syntheticCurve('synth_EURIBOR', [ 0.0: 0.03 - 0.02 * val, 5.0: 0.03 - 0.01 * val, 10.0: 0.03 - 0.01 * val, 15.0: 0.03 - 0.01 * val, 20.0: 0.03 - 0.02 * val ]),
                syntheticCurve('synth_OIS', [ 0.0: 0.01, 5.0: 0.02, 10.0: 0.02, 15.0: 0.02, 20.0: 0.01 ]),
                0.9, 0.85, SyntheticProductsWithCapletCalibration.CalibrationMethod.NONE
        )
    }
}

/*
Creates ten tests with decreasing spread value correlation and otherwise identical parameters
 */
(0..10).each { val ->
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
                syntheticCurve('synth_EURIBOR', [ 0.0: 0.03 - 0.02 * val, 5.0: 0.03 - 0.01 * val, 10.0: 0.03 - 0.01 * val, 15.0: 0.03 - 0.01 * val, 20.0: 0.03 - 0.02 * val ]),
                syntheticCurve('synth_OIS', [ 0.0: 0.01, 5.0: 0.02, 10.0: 0.02, 15.0: 0.02, 20.0: 0.01 ]),
                0.9, 0.85, SyntheticProductsWithCapletCalibration.CalibrationMethod.NONE
        )
    }
}

/*
Creates ten tests with decreasing spread volatility and otherwise identical parameters
 */
(0..10).each { vol ->
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
                syntheticCurve('synth_EURIBOR', [ 0.0: 0.01, 5.0: 0.025, 10.0: 0.03, 15.0: 0.03, 20.0: 0.03 ]),
                syntheticCurve('synth_OIS', [ 0.0: 0.01, 5.0: 0.02, 10.0: 0.02, 15.0: 0.02, 20.0: 0.01 ]),
                0.9, 0.85, SyntheticProductsWithCapletCalibration.CalibrationMethod.NONE
        )
    }
}

/*
Creates ten tests with decreasing spread volatility and otherwise identical parameters
 */
(0..10).each { vol ->
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
                syntheticCurve('synth_EURIBOR', [ 0.0: 0.01, 5.0: 0.025, 10.0: 0.03, 15.0: 0.03, 20.0: 0.03 ]),
                syntheticCurve('synth_OIS', [ 0.0: 0.01, 5.0: 0.02, 10.0: 0.02, 15.0: 0.02, 20.0: 0.01 ]),
                0.9, 0.85, SyntheticProductsWithCapletCalibration.CalibrationMethod.NONE
        )
    }
}

/*
Creates ten tests with decreasing spread volatility and otherwise identical parameters
 */
(0..10).each { vol ->
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
                syntheticCurve('synth_EURIBOR', [ 0.0: 0.01, 5.0: 0.025, 10.0: 0.03, 15.0: 0.03, 20.0: 0.03 ]),
                syntheticCurve('synth_OIS', [ 0.0: 0.01, 5.0: 0.02, 10.0: 0.02, 15.0: 0.02, 20.0: 0.01 ]),
                0.9, 0.85, SyntheticProductsWithCapletCalibration.CalibrationMethod.NONE
        )
    }
}