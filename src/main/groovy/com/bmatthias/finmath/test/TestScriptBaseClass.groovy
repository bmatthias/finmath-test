package com.bmatthias.finmath.test

import com.bmatthias.finmath.test.tools.tools.CSVHelper
import net.finmath.interpolation.RationalFunctionInterpolation
import net.finmath.marketdata.model.curves.ForwardCurve
import net.finmath.marketdata.model.curves.ForwardCurveInterface

import java.util.stream.IntStream

abstract class TestScriptBaseClass extends Script {
    ForwardCurveInterface forwardCurve(String fileName, double periodLength = 0.5, double timeHorizon = 20.0) {
        return CSVHelper.forwardCurveFromForwards(fileName, periodLength, timeHorizon)
    }

    ForwardCurveInterface syntheticCurve(String name, Map rates, double periodLength = 0.5, double timeHorizon = 20.0) {
        RationalFunctionInterpolation interpolation = new RationalFunctionInterpolation(
                rates.keySet() as double[], rates.values() as double[],
                RationalFunctionInterpolation.InterpolationMethod.AKIMA,
                RationalFunctionInterpolation.ExtrapolationMethod.DEFAULT
        );
        double[] dates = (0..(timeHorizon / periodLength)).collect { periodLength * it } as double[]
        return ForwardCurve.createForwardCurveFromForwards(
                name, "DiscountCurve", dates, dates.collect{ date -> interpolation.getValue(date) } as double[], periodLength
        );
    }

    def test(String name, Closure configuration) {
        def test = new Test(name: name, outputDirectory: binding.outputDirectory)
        configuration.delegate = test
        configuration.resolveStrategy = Closure.DELEGATE_FIRST
        configuration()
        tests.add(test)
    }
}
