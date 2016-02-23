package com.bmatthias.finmath.test

import org.codehaus.groovy.control.CompilerConfiguration
import org.codehaus.groovy.control.customizers.builder.CompilerCustomizationBuilder
import org.codehaus.groovy.control.messages.WarningMessage

import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


class Runner {
    static void main(String[] args) {
        def cli = new CliBuilder(usage: 'java -jar finmath-test.jar -[bt]')
        cli.with {
            h longOpt: 'help', 'Show usage information'
            b longOpt: 'baseDir', args: 1, argName: 'basedir', 'Base directory to read tests from and write results to.'
            t longOpt: 'threads', args: 1, argName: 'threads', 'Number of tests to run in parallel.'
            f longOpt: 'filter', args: 1, argName: 'filter', 'A filename filter for the tests.'
            p longOpt: 'plotOnly', 'Only create plots without running tests first.'
        }

        def options = cli.parse(args)

        // Show usage text when -h or --help option is used.
        if (options.h) {
            cli.usage()
            return
        }

        String baseDir = options.b ?: '.'
        int numberOfThreads = Integer.valueOf(options.t ?: 1)

        if(!options.p) {
            def tests = getTests(baseDir, options.f ?: null)

            def threadPool = Executors.newFixedThreadPool(numberOfThreads)

            println "Running tests with ${numberOfThreads} threads."

            tests.each { threadPool.submit it }

            threadPool.shutdown()
            threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS)
        }

        def plots = getPlots(baseDir)
        plots*.call()
    }

    private static getTests(String baseDir, String filter = null) {
        def tests = []
        // Initialization/Compilation
        def shell = getConfiguredGroovyShell(TestScriptBaseClass)
        def allTests = scriptsIn(baseDir + File.separator + 'tests', filter)

        List<Script> testScripts = parseScripts(allTests, shell)

        testScripts.findAll().each { script ->
            script.binding = new Binding(tests: tests, baseDirectory: baseDir, outputDirectory: 'results')
            try {
                script.run()
            } catch (Throwable t) {
                println "Unexpected exception in script ${script} with message: ${t.message}"
            }
        }
        tests
    }


    private static getPlots(String baseDir) {
        // Initialization/Compilation
        def plots = []
        def shell = getConfiguredGroovyShell(PlotScriptBaseClass)
        def allPlots = scriptsIn(baseDir + File.separator + 'plots')

        List<Script> plotScripts = parseScripts(allPlots, shell)

        plotScripts.findAll().each { script ->
            script.binding = new Binding(plots: plots, baseDirectory: baseDir, outputDirectory: 'tex')
            try {
                script.run()
            } catch (Throwable t) {
                println "Unexpected exception in script ${script} with message: ${t.message}"
            }
        }
        plots
    }

    static GroovyShell getConfiguredGroovyShell(Class scriptBaseClass) {
        // Create compiler configurations
        def compilerConfiguration = new CompilerConfiguration([
                debug          : true,
                scriptBaseClass: scriptBaseClass.canonicalName,
                targetBytecode : CompilerConfiguration.JDK8,
                warningLevel   : WarningMessage.POSSIBLE_ERRORS
        ])

        CompilerCustomizationBuilder.withConfig(compilerConfiguration){
            imports{
                star scriptBaseClass.package.name
            }
        }
        return new GroovyShell(compilerConfiguration)
    }

    static List<File> scriptsIn(String directory, String filter = null) {
        def result = []
        try {
            new File(directory).eachFileRecurse{ file ->
                if (file.path.endsWith('.groovy') && (!filter || file.path.contains(filter))) {
                    result.add(file)
                }
            }
        } catch (e) {
            e.printStackTrace()
        }
        return result
    }

    private static List<Script> parseScripts(List<File> allTests, shell) {
        List<Script> testScripts = allTests.collect { testFile ->
            try {
                println "Parsing script ${testFile}"
                return shell.parse(testFile)
            } catch (Throwable t) {
                println "Could not compile ${testFile}: ${t.message}"
            }
            return null
        }
        testScripts
    }
}

