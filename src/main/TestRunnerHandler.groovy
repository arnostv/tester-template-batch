
class TestRunnerHandler {

    def runTestStep(TestStepData testStepData) {
        def result = runScript(testStepData)

        new TestResultHandler().handleTestResult(testStepData, result)
    }


    private  runScript(TestStepData testStepData) {
        def result = null
        def actionScriptPath = testStepData.testSTS.findActionScriptPath()

        if (actionScriptPath) {
            def bindings = new HashMap(testStepData.testSTS.params)

            bindings['payload'] = testStepData.evaluatedTemplate

            bindings['out'] = System.out

            def actionFile = new File(actionScriptPath)
            def classLoader = new GroovyClassLoader()
            classLoader.addClasspath(testStepData.testSTS.suite.locationPath)
            result = new GroovyShell(classLoader, new Binding(bindings)).evaluate(actionFile)
        } else {
            println "UNKNOWN ACTION for ${testStepData}"
        }
        result
    }


}
