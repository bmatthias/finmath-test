import com.bmatthias.finmath.test.tools.tools.calibrationdata.SyntheticProductsWithCapletCalibration
import net.finmath.montecarlo.interestrate.MultiCurveLIBORMarketModel

/*
Creates ten tests with increasing correlation and otherwise identical parameters
 */
(0..10).each { corr ->
    test("pricing_synth_increasing_correlation/${corr}_additive") {
        params = [
                numberOfParams: 15,
                useSeperateCorrelationModels: true,
                numberOfFactors: 8,
                numberOfPaths: 100000,
                numberOfSeeds: 1,
                multiCurveModel: MultiCurveLIBORMarketModel.MultiCurveModel.ADDITIVE,
                oisParams: [ 0.2,0.2,0.2,0.2,0.1,0.1,0.1,0.0 ],
                spreadParams: [ 0.2,0.2,0.2,0.2,0.1,0.1,0.1,0.1 * corr,0.0 ],
                numberOfRandomRuns: 0
        ]

        calibrationData = SyntheticProductsWithCapletCalibration.newInstance(
                syntheticCurve('synth_EURIBOR', [ 0.0: 0.03, 20.0: 0.03 ]),
                syntheticCurve('synth_OIS', [ 0.0: 0.015, 20.0: 0.015 ]),
                0.9, 0.85, SyntheticProductsWithCapletCalibration.CalibrationMethod.NONE
        )
    }
}

/*
Creates ten tests with increasing volatility and otherwise identical parameters
 */
(0..10).each { vol ->
    test("pricing_synth_increasing_vola/${vol}_additive") {
        params = [
                numberOfParams: 15,
                useSeperateCorrelationModels: true,
                numberOfFactors: 8,
                numberOfPaths: 100000,
                numberOfSeeds: 1,
                multiCurveModel: MultiCurveLIBORMarketModel.MultiCurveModel.ADDITIVE,
                oisParams: [ 0.2,0.2,0.2,0.05 * vol,0.1,0.1,0.1,0.0 ],
                spreadParams: [ 0.2,0.2,0.2,0.05 * vol,0.1,0.1,0.1,0.0,0.0 ],
                numberOfRandomRuns: 0
        ]

        calibrationData = SyntheticProductsWithCapletCalibration.newInstance(
                syntheticCurve('synth_EURIBOR', [ 0.0: 0.03, 20.0: 0.03 ]),
                syntheticCurve('synth_OIS', [ 0.0: 0.015, 20.0: 0.015 ]),
                0.9, 0.85, SyntheticProductsWithCapletCalibration.CalibrationMethod.NONE
        )
    }
}

/*
Creates ten tests with increasing correlation and otherwise identical parameters
 */
(0..10).each { corr ->
    test("pricing_synth_increasing_correlation/${corr}_multiplicative") {
        params = [
                numberOfParams: 15,
                useSeperateCorrelationModels: true,
                numberOfFactors: 8,
                numberOfPaths: 100000,
                numberOfSeeds: 1,
                multiCurveModel: MultiCurveLIBORMarketModel.MultiCurveModel.MULTIPLICATIVE,
                oisParams: [ 0.2,0.2,0.2,0.2,0.1,0.1,0.1,0.0 ],
                spreadParams: [ 0.2,0.2,0.2,0.2,0.1,0.1,0.1,0.1 * corr,0.0 ],
                numberOfRandomRuns: 0
        ]

        calibrationData = SyntheticProductsWithCapletCalibration.newInstance(
                syntheticCurve('synth_EURIBOR', [ 0.0: 0.03, 20.0: 0.03 ]),
                syntheticCurve('synth_OIS', [ 0.0: 0.015, 20.0: 0.015 ]),
                0.9, 0.85, SyntheticProductsWithCapletCalibration.CalibrationMethod.NONE
        )
    }
}

/*
Creates ten tests with increasing volatility and otherwise identical parameters
 */
(0..10).each { vol ->
    test("pricing_synth_increasing_vola/${vol}_multiplicative") {
        params = [
                numberOfParams: 15,
                useSeperateCorrelationModels: true,
                numberOfFactors: 8,
                numberOfPaths: 100000,
                numberOfSeeds: 1,
                multiCurveModel: MultiCurveLIBORMarketModel.MultiCurveModel.MULTIPLICATIVE,
                oisParams: [ 0.2,0.2,0.2,0.05 * vol,0.1,0.1,0.1,0.0 ],
                spreadParams: [ 0.2,0.2,0.2,0.05 * vol,0.1,0.1,0.1,0.0,0.0 ],
                numberOfRandomRuns: 0
        ]

        calibrationData = SyntheticProductsWithCapletCalibration.newInstance(
                syntheticCurve('synth_EURIBOR', [ 0.0: 0.03, 20.0: 0.03 ]),
                syntheticCurve('synth_OIS', [ 0.0: 0.015, 20.0: 0.015 ]),
                0.9, 0.85, SyntheticProductsWithCapletCalibration.CalibrationMethod.NONE
        )
    }
}

