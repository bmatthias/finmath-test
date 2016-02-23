package com.bmatthias.finmath.test

abstract class PlotScriptBaseClass extends Script {
    def plot(String fileName, Closure configuration) {
        def plot = new Plot(fileName: outputDirectory + File.separator + fileName)
        configuration.delegate = plot
        configuration.resolveStrategy = Closure.DELEGATE_FIRST
        configuration()
        plots.add(plot)
    }
}
