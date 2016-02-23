package tests

import com.bmatthias.finmath.test.tools.tools.calibrationdata.SyntheticProductsWithCapletCalibration
import net.finmath.montecarlo.interestrate.MultiCurveLIBORMarketModel

/*
Creates tests with increasing correlation and otherwise identical parameters
 */
[0,1].each { corr ->
    test("pricing_local_increasing_correlation/${corr}") {
        params = [
                numberOfParams: 15,
                useSeperateCorrelationModels: true,
                numberOfFactors: 8,
                numberOfPaths: 10000,
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