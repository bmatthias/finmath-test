package tests

import com.bmatthias.finmath.test.tools.tools.calibrationdata.SyntheticProductsWithCapletCalibration
import net.finmath.marketdata.model.curves.ForwardCurve
import net.finmath.montecarlo.interestrate.MultiCurveLIBORMarketModel

/*
Creates ten tests with increasing correlation and otherwise identical parameters
 */
(0..10).each { corr ->
    test("pricing_2010_increasing_correlation/${corr}_additive_shifted") {
        params = [
                numberOfParams: 15,
                useSeperateCorrelationModels: true,
                numberOfFactors: 8,
                numberOfPaths: 100000,
                numberOfSeeds: 1,
                multiCurveModel: MultiCurveLIBORMarketModel.MultiCurveModel.ADDITIVE,
                oisParams: [ 0.2,0.3,0.4,0.0,0.1,0.1,0.1,0.0 ],
                spreadParams: [ 0.4,0.3,0.4,0.1,0.1,0.1,0.1,0.1 * corr,0.03 ],
                oisShift: ForwardCurve.createForwardCurveFromForwards("oisShift", [ 0.0 ] as double[], [ 0.03 ] as double[], 0.0),
                spreadShift: ForwardCurve.createForwardCurveFromForwards("spreadShift", [ 0.0 ] as double[], [ 0.03 ] as double[], 0.0),
                numberOfRandomRuns: 0
        ]

        calibrationData = SyntheticProductsWithCapletCalibration.newInstance(
                forwardCurve('2010_EURIBOR_6M_Bianchetti'),
                forwardCurve('2010_EONIA_OIS_Bianchetti'),
                0.9, 0.85, SyntheticProductsWithCapletCalibration.CalibrationMethod.NONE
        )
    }
}

/*
Creates ten tests with increasing volatility and otherwise identical parameters
 */
(0..10).each { vol ->
    test("pricing_2010_increasing_vola/${vol}_additive_shifted") {
        params = [
                numberOfParams: 15,
                useSeperateCorrelationModels: true,
                numberOfFactors: 8,
                numberOfPaths: 100000,
                numberOfSeeds: 1,
                multiCurveModel: MultiCurveLIBORMarketModel.MultiCurveModel.ADDITIVE,
                oisParams: [ 0.2,0.2,0.4,0.05 * vol,0.1,0.1,0.1,0.0 ],
                spreadParams: [ 0.2,0.2,0.4,0.05 * vol,0.1,0.1,0.1,0.5,0.03 ],
                oisShift: ForwardCurve.createForwardCurveFromForwards("oisShift", [ 0.0 ] as double[], [ 0.03 ] as double[], 0.0),
                spreadShift: ForwardCurve.createForwardCurveFromForwards("spreadShift", [ 0.0 ] as double[], [ 0.03 ] as double[], 0.0),
                numberOfRandomRuns: 0
        ]

        calibrationData = SyntheticProductsWithCapletCalibration.newInstance(
                forwardCurve('2010_EURIBOR_6M_Bianchetti'),
                forwardCurve('2010_EONIA_OIS_Bianchetti'),
                0.9, 0.85, SyntheticProductsWithCapletCalibration.CalibrationMethod.NONE
        )
    }
}

/*
Creates ten tests with increasing correlation and otherwise identical parameters
 */
(0..10).each { corr ->
    test("pricing_2010_increasing_correlation/${corr}_multiplicative_shifted") {
        params = [
                numberOfParams: 15,
                useSeperateCorrelationModels: true,
                numberOfFactors: 8,
                numberOfPaths: 100000,
                numberOfSeeds: 1,
                multiCurveModel: MultiCurveLIBORMarketModel.MultiCurveModel.MULTIPLICATIVE,
                oisParams: [ 0.2,0.3,0.4,0.0,0.1,0.1,0.1,0.0 ],
                spreadParams: [ 0.4,0.3,0.4,0.1,0.1,0.1,0.1,0.1 * corr,0.03 ],
                oisShift: ForwardCurve.createForwardCurveFromForwards("oisShift", [ 0.0 ] as double[], [ 0.03 ] as double[], 0.0),
                spreadShift: ForwardCurve.createForwardCurveFromForwards("spreadShift", [ 0.0 ] as double[], [ 0.03 ] as double[], 0.0),
                numberOfRandomRuns: 0
        ]

        calibrationData = SyntheticProductsWithCapletCalibration.newInstance(
                forwardCurve('2010_EURIBOR_6M_Bianchetti'),
                forwardCurve('2010_EONIA_OIS_Bianchetti'),
                0.9, 0.85, SyntheticProductsWithCapletCalibration.CalibrationMethod.NONE
        )
    }
}

/*
Creates ten tests with increasing volatility and otherwise identical parameters
 */
(0..10).each { vol ->
    test("pricing_2010_increasing_vola/${vol}_multiplicative_shifted") {
        params = [
                numberOfParams: 15,
                useSeperateCorrelationModels: true,
                numberOfFactors: 8,
                numberOfPaths: 100000,
                numberOfSeeds: 1,
                multiCurveModel: MultiCurveLIBORMarketModel.MultiCurveModel.MULTIPLICATIVE,
                oisParams: [ 0.2,0.2,0.4,0.05 * vol,0.1,0.1,0.1,0.0 ],
                spreadParams: [ 0.2,0.2,0.4,0.05 * vol,0.1,0.1,0.1,0.5,0.03 ],
                oisShift: ForwardCurve.createForwardCurveFromForwards("oisShift", [ 0.0 ] as double[], [ 0.03 ] as double[], 0.0),
                spreadShift: ForwardCurve.createForwardCurveFromForwards("spreadShift", [ 0.0 ] as double[], [ 0.03 ] as double[], 0.0),
                numberOfRandomRuns: 0
        ]

        calibrationData = SyntheticProductsWithCapletCalibration.newInstance(
                forwardCurve('2010_EURIBOR_6M_Bianchetti'),
                forwardCurve('2010_EONIA_OIS_Bianchetti'),
                0.9, 0.85, SyntheticProductsWithCapletCalibration.CalibrationMethod.NONE
        )
    }
}
