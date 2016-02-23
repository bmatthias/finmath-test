package com.bmatthias.finmath.test

import com.bmatthias.finmath.test.tools.tools.CalibrationData

import java.util.concurrent.Callable

class Test implements Callable {
    String name
    String outputDirectory
    Map params = [:]
    CalibrationData calibrationData

    @Override
    def call() {
        try {
            new MultiCurveLIBORMarketModelCalibration(params).start(calibrationData, outputDirectory + File.separator + name)
        } catch (e) {
            e.printStackTrace()
        }
    }
}
