package tests

import com.bmatthias.finmath.test.tools.tools.calibrationdata.SyntheticProductsWithCapletCalibration
import net.finmath.montecarlo.interestrate.MultiCurveLIBORMarketModel

/*
Creates four tests with increasing number of monte carlo paths
 */
[ [1,1000], [1,10000], [1,100000], [10,100000] ].each { paths ->
    test("pricing_synth_convergence/${paths[0]*paths[1]}_additive") {
        params = [
                numberOfParams: 15,
                useSeperateCorrelationModels: true,
                numberOfFactors: 40,
                numberOfPaths: paths[1],
                numberOfSeeds: paths[0],
                multiCurveModel: MultiCurveLIBORMarketModel.MultiCurveModel.ADDITIVE,
                numberOfRandomRuns: 0,
                oisParams: [ 0.5,0.5,0.4,0.0,0.1,0.1,0.1,0.0 ],
                spreadParams: [ 0.6,0.6,0.4,0.1,0.1,0.1,0.1,0.1,0.0 ],
                calibrateSC: true,
                calibrateMC: true
        ]

        calibrationData = SyntheticProductsWithCapletCalibration.newInstance(
                syntheticCurve('synth_EURIBOR', [ 0.0: 0.01, 20.0: 0.01 ]),
                syntheticCurve('synth_OIS', [ 0.0: 0.008, 20.0: 0.008 ]),
                0.3,0.29, SyntheticProductsWithCapletCalibration.CalibrationMethod.INTEGRATED_EXPECTATION
        )
    }
}

/*
Creates four tests with increasing number of monte carlo paths
 */
[ [1,1000], [1,10000], [1,100000], [10,100000] ].each { paths ->
    test("pricing_synth_convergence/${paths[0]*paths[1]}_multiplicative") {
        params = [
                numberOfParams: 15,
                useSeperateCorrelationModels: true,
                numberOfFactors: 8,
                numberOfPaths: paths[1],
                numberOfSeeds: paths[0],
                multiCurveModel: MultiCurveLIBORMarketModel.MultiCurveModel.MULTIPLICATIVE,
                numberOfRandomRuns: 0,
                oisParams: [ 0.5,0.5,0.4,0.0,0.1,0.1,0.1,0.0 ],
                spreadParams: [ 0.6,0.6,0.4,0.1,0.1,0.1,0.1,0.1,0.0 ],
                calibrateSC: true,
                calibrateMC: true
        ]

        calibrationData = SyntheticProductsWithCapletCalibration.newInstance(
                syntheticCurve('synth_EURIBOR', [ 0.0: 0.01, 20.0: 0.01 ]),
                syntheticCurve('synth_OIS', [ 0.0: 0.008, 20.0: 0.008 ]),
                0.3,0.29, SyntheticProductsWithCapletCalibration.CalibrationMethod.INTEGRATED_EXPECTATION
        )
    }
}

/*
Creates four tests with increasing number of monte carlo paths
 */
[ [1,1000], [1,10000], [1,100000], [10,100000] ].each { paths ->
    test("pricing_synth_convergence/${paths[0]*paths[1]}_martingale") {
        params = [
                numberOfParams: 15,
                useSeperateCorrelationModels: true,
                numberOfFactors: 8,
                numberOfPaths: paths[1],
                numberOfSeeds: paths[0],
                multiCurveModel: MultiCurveLIBORMarketModel.MultiCurveModel.MMARTINGALE,
                numberOfRandomRuns: 0,
                oisParams: [ 0.5,0.5,0.4,0.0,0.1,0.1,0.1,0.0 ],
                spreadParams: [ 0.6,0.6,0.4,0.1,0.1,0.1,0.1,0.1,0.0 ],
                calibrateSC: true,
                calibrateMC: true
        ]

        calibrationData = SyntheticProductsWithCapletCalibration.newInstance(
                syntheticCurve('synth_EURIBOR', [ 0.0: 0.01, 20.0: 0.01 ]),
                syntheticCurve('synth_OIS', [ 0.0: 0.008, 20.0: 0.008 ]),
                0.3,0.29, SyntheticProductsWithCapletCalibration.CalibrationMethod.INTEGRATED_EXPECTATION
        )
    }
}