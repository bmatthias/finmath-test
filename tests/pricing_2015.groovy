package tests

import com.bmatthias.finmath.test.tools.tools.calibrationdata.SyntheticProductsWithCapletCalibration
import net.finmath.montecarlo.interestrate.MultiCurveLIBORMarketModel

/*
Creates ten tests with increasing correlation and otherwise identical parameters
 */
(0..10).each { corr ->
    test("pricing_2015_increasing_correlation/${corr}_additive") {
        params = [
                numberOfParams: 15,
                useSeperateCorrelationModels: true,
                numberOfFactors: 8,
                numberOfPaths: 100000,
                numberOfSeeds: 1,
                multiCurveModel: MultiCurveLIBORMarketModel.MultiCurveModel.ADDITIVE,
                oisParams: [ 0.7,0.7,0.5,0.1,0.1,0.1,0.1,0.004 ],
                spreadParams: [ 0.8,0.8,0.8,0.0,0.1,0.1,0.1,0.1 * corr,0.004 ],
                numberOfRandomRuns: 0
        ]

        calibrationData = SyntheticProductsWithCapletCalibration.newInstance(
                forwardCurve('2015_EURIBOR_6M'),
                forwardCurve('2015_EONIA_OIS'),
                0.9, 0.85, SyntheticProductsWithCapletCalibration.CalibrationMethod.NONE
        )
    }
}

/*
Creates ten tests with increasing volatility and otherwise identical parameters
 */
(0..10).each { vol ->
    test("pricing_2015_increasing_vola/${vol}_additive") {
        params = [
                numberOfParams: 15,
                useSeperateCorrelationModels: true,
                numberOfFactors: 8,
                numberOfPaths: 100000,
                numberOfSeeds: 1,
                multiCurveModel: MultiCurveLIBORMarketModel.MultiCurveModel.ADDITIVE,
                oisParams: [ 0.4,0.3,0.4,0.05 * vol,0.1,0.1,0.1,0.004 ],
                spreadParams: [ 0.4,0.3,0.4,0.05 * vol,0.1,0.1,0.1,0.5,0.004 ],
                numberOfRandomRuns: 0
        ]

        calibrationData = SyntheticProductsWithCapletCalibration.newInstance(
                forwardCurve('2015_EURIBOR_6M'),
                forwardCurve('2015_EONIA_OIS'),
                0.9, 0.85, SyntheticProductsWithCapletCalibration.CalibrationMethod.NONE
        )
    }
}

/*
Creates ten tests with increasing correlation and otherwise identical parameters
 */
(0..10).each { corr ->
    test("pricing_2015_increasing_correlation/${corr}_multiplicative") {
        params = [
                numberOfParams: 15,
                useSeperateCorrelationModels: true,
                numberOfFactors: 8,
                numberOfPaths: 100000,
                numberOfSeeds: 1,
                multiCurveModel: MultiCurveLIBORMarketModel.MultiCurveModel.MULTIPLICATIVE,
                oisParams: [ 0.7,0.7,0.5,0.1,0.1,0.1,0.1,0.004 ],
                spreadParams: [ 0.8,0.8,0.8,0.0,0.1,0.1,0.1,0.1 * corr,0.004 ],
                numberOfRandomRuns: 0
        ]

        calibrationData = SyntheticProductsWithCapletCalibration.newInstance(
                forwardCurve('2015_EURIBOR_6M'),
                forwardCurve('2015_EONIA_OIS'),
                0.9, 0.85, SyntheticProductsWithCapletCalibration.CalibrationMethod.NONE
        )
    }
}

/*
Creates ten tests with increasing volatility and otherwise identical parameters
 */
(0..10).each { vol ->
    test("pricing_2015_increasing_vola/${vol}_multiplicative") {
        params = [
                numberOfParams: 15,
                useSeperateCorrelationModels: true,
                numberOfFactors: 8,
                numberOfPaths: 100000,
                numberOfSeeds: 1,
                multiCurveModel: MultiCurveLIBORMarketModel.MultiCurveModel.MULTIPLICATIVE,
                oisParams: [ 0.4,0.3,0.4,0.05 * vol,0.1,0.1,0.1,0.004 ],
                spreadParams: [ 0.4,0.3,0.4,0.05 * vol,0.1,0.1,0.1,0.5,0.004 ],
                numberOfRandomRuns: 0
        ]

        calibrationData = SyntheticProductsWithCapletCalibration.newInstance(
                forwardCurve('2015_EURIBOR_6M'),
                forwardCurve('2015_EONIA_OIS'),
                0.9, 0.85, SyntheticProductsWithCapletCalibration.CalibrationMethod.NONE
        )
    }
}

