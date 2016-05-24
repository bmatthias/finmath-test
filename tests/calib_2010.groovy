package tests

import com.bmatthias.finmath.test.tools.tools.calibrationdata.SyntheticProductsWithCapletCalibration
import net.finmath.marketdata.model.curves.ForwardCurve
import net.finmath.montecarlo.interestrate.MultiCurveLIBORMarketModel

[ SyntheticProductsWithCapletCalibration.CalibrationMethod.INTEGRATED_EXPECTATION,
  SyntheticProductsWithCapletCalibration.CalibrationMethod.JU,
  SyntheticProductsWithCapletCalibration.CalibrationMethod.SWAPTION ].each { calibrationMethod ->

    test("pricing_2010_calib/additive_${calibrationMethod.toString().toLowerCase()}") {
        params = [
                numberOfParams: 15,
                useSeperateCorrelationModels: true,
                numberOfFactors: 8,
                numberOfPaths: 100000,
                numberOfSeeds: 1,
                multiCurveModel: MultiCurveLIBORMarketModel.MultiCurveModel.ADDITIVE,
                numberOfRandomRuns: 0,
                setParams: false,
                calibrateSC: true,
                calibrateMC: true
        ]

        calibrationData = SyntheticProductsWithCapletCalibration.newInstance(
                forwardCurve('2010_EURIBOR_6M_Bianchetti'),
                forwardCurve('2010_EONIA_OIS_Bianchetti'),
                0.3,0.29, calibrationMethod
        )
    }

    test("pricing_2010_calib/multiplicative_${calibrationMethod.toString().toLowerCase()}") {
        params = [
                numberOfParams: 15,
                useSeperateCorrelationModels: true,
                numberOfFactors: 8,
                numberOfPaths: 100000,
                numberOfSeeds: 1,
                multiCurveModel: MultiCurveLIBORMarketModel.MultiCurveModel.MULTIPLICATIVE,
                numberOfRandomRuns: 0,
                setParams: false,
                calibrateSC: true,
                calibrateMC: true
        ]

        calibrationData = SyntheticProductsWithCapletCalibration.newInstance(
                forwardCurve('2010_EURIBOR_6M_Bianchetti'),
                forwardCurve('2010_EONIA_OIS_Bianchetti'),
                0.3,0.29, calibrationMethod
        )
    }

    test("pricing_2010_calib/martingale_${calibrationMethod.toString().toLowerCase()}") {
        params = [
                numberOfParams: 15,
                useSeperateCorrelationModels: true,
                numberOfFactors: 8,
                numberOfPaths: 100000,
                numberOfSeeds: 1,
                multiCurveModel: MultiCurveLIBORMarketModel.MultiCurveModel.MMARTINGALE,
                numberOfRandomRuns: 0,
                setParams: false,
                calibrateSC: true,
                calibrateMC: true
        ]

        calibrationData = SyntheticProductsWithCapletCalibration.newInstance(
                forwardCurve('2010_EURIBOR_6M_Bianchetti'),
                forwardCurve('2010_EONIA_OIS_Bianchetti'),
                0.3,0.29, calibrationMethod
        )
    }
}

