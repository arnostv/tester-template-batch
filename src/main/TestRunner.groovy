import groovy.text.GStringTemplateEngine

class TestRunner {

    def testResultHandler = new TestResultHandler();
    def testRunnerHandler = new TestRunnerHandler(testResultHandler)

    def templateEngine = new GStringTemplateEngine()//XmlTemplateEngine()

    def runTestSuite(TestSuite testSuite) {
        TestSTS testSTS = TestSTS.createWithTestSuite(testSuite)
        testResultHandler.initializeTestOutputDirectory(testSTS)

        def defaultMappings = evalTestSuiteParams(testSTS,testSuite)
        testSuite.testCases.each {
            runTestCase(defaultMappings, testSTS.withTestCase(it))
        }

        testResultHandler.summarizeTestSuite(testSTS)
    }

    def runTestCase(def testSuiteMappings,TestSTS testSTS) {
        def testCase = testSTS.testCase

        println "Running ${testCase}"

        def testCaseMappings = evalTestCaseParams(testSTS,testCase)
        def defaultMappings = new HashMap(testSuiteMappings)
        defaultMappings.putAll(testCaseMappings)

        def steps = testCase.steps

        steps.each{runTestStep(defaultMappings, testSTS.withTestStep(it))}

        testResultHandler.summarizeTestCase(testSTS)
    }

    def runTestStep(def testCaseParams, TestSTS testSTS) {
        def testStep = testSTS.step
        println "Running step ${testStep}"

        def testStepParams = evalTestStepParams(testSTS,testStep, testCaseParams)

        def params = new HashMap(testStepParams) // template engine seems to fail with immutable map

        def evaluatedTemplate = null
        if (testStep.templateFilePath) {
            def template = templateEngine.createTemplate(new File(testStep.templateFilePath)).make(params)
            evaluatedTemplate =  template.toString()
        }
        testRunnerHandler.runTestStep(new TestStepData(testSTS.withParams(params), evaluatedTemplate))
    }

    def evalTestSuiteParams(TestSTS testSTS,TestSuite testSuite) {
        if (testSuite.paramsScriptPath) {
            println "Evaluating parameters from ${testSuite.paramsScriptPath}"
            Map mappings = new GroovyShell(new TestSuiteClassloader(testSTS)).evaluate(new File(testSuite.paramsScriptPath))
            mappings.asImmutable()
        } else {
            Map mappingsUndefined = [_parametersForTestSuiteNotSet : true]
            mappingsUndefined.asImmutable()
        }
    }

    def evalTestCaseParams(TestSTS testSTS,TestCase testCase) {
        if (testCase.paramsScriptPath) {
            println "Evaluating parameters from ${testCase.paramsScriptPath}"
            Map mappings = new GroovyShell(new TestSuiteClassloader(testSTS)).evaluate(new File(testCase.paramsScriptPath))
            mappings.asImmutable()
        } else {
            Map mappingsUndefined = [_parametersForTestCaseNotSet : true]
            mappingsUndefined.asImmutable()
        }
    }

    def evalTestStepParams(TestSTS testSTS,TestStep testStep, Map alreadyDefinedParams) {
        def testStepParams = new File(testStep.fileGroup.directory(), testStep.fileGroup.prefix + ".params.groovy")
        Map paramsForStep
        if (testStepParams.isFile()) {
            println "Evaluating parameters from ${testStepParams}"
            paramsForStep = new GroovyShell(new TestSuiteClassloader(testSTS)).evaluate(testStepParams)
        } else {
            paramsForStep = [_parametersForTestStepNotSet : true]
        }

        Map union = (alreadyDefinedParams + paramsForStep)
        union.asImmutable()
    }


}
