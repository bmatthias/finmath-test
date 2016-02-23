package tests

import com.bmatthias.finmath.test.tools.tools.calibrationdata.SyntheticProductsWithCapletCalibration
import net.finmath.montecarlo.interestrate.MultiCurveLIBORMarketModel

/*
Creates four tests with increasing number of monte carlo paths
 */
[ [1,1000], [1,10000], [1,100000], [10,100000] ].each { paths ->
    test("pricing_2013_convergence/${paths[0]*paths[1]}_additive") {
        params = [
                numberOfParams: 15,
                useSeperateCorrelationModels: true,
                numberOfFactors: 8,
                numberOfPaths: paths[1],
                numberOfSeeds: paths[0],
                multiCurveModel: MultiCurveLIBORMarketModel.MultiCurveModel.ADDITIVE,
                numberOfRandomRuns: 0,
                setParams: false
        ]

        calibrationData = SyntheticProductsWithCapletCalibration.newInstance(
                forwardCurve('2013_EURIBOR_6M_Kienitz'),
                forwardCurve('2013_EONIA_OIS_Kienitz'),
                0.6,0.57, SyntheticProductsWithCapletCalibration.CalibrationMethod.INTEGRATED_EXPECTATION
        )
    }
}

/*
Creates four tests with increasing number of monte carlo paths
 */
[ [1,1000], [1,10000], [1,100000], [10,100000] ].each { paths ->
    test("pricing_2013_convergence/${paths[0]*paths[1]}_multiplicative") {
        params = [
                numberOfParams: 15,
                useSeperateCorrelationModels: true,
                numberOfFactors: 8,
                numberOfPaths: paths[1],
                numberOfSeeds: paths[0],
                multiCurveModel: MultiCurveLIBORMarketModel.MultiCurveModel.MULTIPLICATIVE,
                numberOfRandomRuns: 0,
                setParams: false
        ]

        calibrationData = SyntheticProductsWithCapletCalibration.newInstance(
                forwardCurve('2013_EURIBOR_6M_Kienitz'),
                forwardCurve('2013_EONIA_OIS_Kienitz'),
                0.6,0.57, SyntheticProductsWithCapletCalibration.CalibrationMethod.INTEGRATED_EXPECTATION
        )
    }
}

/*
Creates four tests with increasing number of monte carlo paths
 */
[ [1,1000], [1,10000], [1,100000], [10,100000] ].each { paths ->
    test("pricing_2013_convergence/${paths[0]*paths[1]}_martingale") {
        params = [
                numberOfParams: 15,
                useSeperateCorrelationModels: true,
                numberOfFactors: 8,
                numberOfPaths: paths[1],
                numberOfSeeds: paths[0],
                multiCurveModel: MultiCurveLIBORMarketModel.MultiCurveModel.MMARTINGALE,
                numberOfRandomRuns: 0,
                setParams: false
        ]

        calibrationData = SyntheticProductsWithCapletCalibration.newInstance(
                forwardCurve('2013_EURIBOR_6M_Kienitz'),
                forwardCurve('2013_EONIA_OIS_Kienitz'),
                0.6,0.57, SyntheticProductsWithCapletCalibration.CalibrationMethod.INTEGRATED_EXPECTATION
        )
    }
}