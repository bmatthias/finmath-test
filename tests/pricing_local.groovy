import com.bmatthias.finmath.test.tools.tools.calibrationdata.SyntheticProductsWithCapletCalibration
import net.finmath.montecarlo.interestrate.MultiCurveLIBORMarketModel

/*
Creates tests with increasing correlation and otherwise identical parameters
 */
[10].each { corr ->
    test("pricing_local_increasing_correlation/${corr}") {
        params = [
                numberOfParams: 15,
                useSeperateCorrelationModels: true,
                numberOfFactors: 8,
                numberOfPaths: 10000,
                numberOfSeeds: 1,
                multiCurveModel: MultiCurveLIBORMarketModel.MultiCurveModel.MULTIPLICATIVE,
                oisParams: [ 0.2,0.2,0.2,0.2,0.1,0.1,0.1 ],
                spreadParams: [ 0.2,0.2,0.2,0.2,0.1,0.1,0.1,0.1 * corr ],
                numberOfRandomRuns: 0
        ]

        calibrationData = SyntheticProductsWithCapletCalibration.newInstance(
                syntheticCurve('synth_EURIBOR', [ 0.0: 0.03, 20.0: 0.03 ]),
                syntheticCurve('synth_OIS', [ 0.0: 0.015, 20.0: 0.015 ]),
                0.9, 0.85, SyntheticProductsWithCapletCalibration.CalibrationMethod.NONE
        )
    }
}

[ [1,10000] ].each { paths ->
    test("pricing_local_2015_convergence/${paths[0]*paths[1]}_additive") {
        params = [
                numberOfParams: 15,
                useSeperateCorrelationModels: true,
                numberOfFactors: 8,
                numberOfPaths: paths[1],
                numberOfSeeds: paths[0],
                multiCurveModel: MultiCurveLIBORMarketModel.MultiCurveModel.MULTIPLICATIVE,
                numberOfRandomRuns: 0,
                setParams: false,
                calibrateSC: true,
                calibrateMC: true
        ]

        calibrationData = SyntheticProductsWithCapletCalibration.newInstance(
                forwardCurve('2015_EURIBOR_6M'),
                forwardCurve('2015_EONIA_OIS'),
                0.9, 0.85, SyntheticProductsWithCapletCalibration.CalibrationMethod.INTEGRATED_EXPECTATION
        )
    }
}