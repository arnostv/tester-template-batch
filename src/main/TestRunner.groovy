import groovy.text.GStringTemplateEngine

class TestRunner {

    def testRunnerHandler = new TestRunnerHandler()

    def templateEngine = new GStringTemplateEngine()//XmlTemplateEngine()

    def runTestSuite(TestSuite testSuite) {
        TestSTS sts = TestSTS.createWithTestSuite(testSuite)
        testSuite.testCases.each {
            runTestCase(sts.withTestCase(it))
        }
    }

    def runTestCase(TestSTS testSTS) {
        def testCase = testSTS.testCase

        println "Running ${testCase}"

        def defaultMappings = evalTestCaseParams(testCase)

        def steps = testCase.steps

        steps.each{runTestStep(defaultMappings, testSTS.withTestStep(it))}

    }

    def runTestStep(def testCaseParams, TestSTS testSTS) {
        def testStep = testSTS.step
        println "Running step ${testStep}"

        def testStepParams = evalTestStepParams(testStep, testCaseParams)

        def params = new HashMap(testStepParams) // template engine seems to fail with immutable map

        def evaluatedTemplate = null
        if (testStep.templateFilePath) {
            def template = templateEngine.createTemplate(new File(testStep.templateFilePath)).make(params)
            evaluatedTemplate =  template.toString()
        }
        testRunnerHandler.runTestStep(new TestStepData(testSTS.withParams(params), evaluatedTemplate))
    }

    def evalTestCaseParams(TestCase testCase) {
        if (testCase.paramsScriptPath) {
            println "Evaluating parameters from ${testCase.paramsScriptPath}"
            Map mappings = new GroovyShell().evaluate(new File(testCase.paramsScriptPath))
            mappings.asImmutable()
        } else {
            Map mappingsUndefined = [_parametersForTestCaseNotSet : true]
            mappingsUndefined.asImmutable()
        }
    }

    def evalTestStepParams(TestStep testStep, Map alreadyDefinedParams) {
        def testStepParams = new File(testStep.fileGroup.directory(), testStep.fileGroup.prefix + ".params.groovy")
        Map paramsForStep
        if (testStepParams.isFile()) {
            println "Evaluating parameters from ${testStepParams}"
            paramsForStep = new GroovyShell().evaluate(testStepParams)
        } else {
            paramsForStep = [_parametersForTestStepNotSet : true]
        }

        Map union = (alreadyDefinedParams + paramsForStep)
        union.asImmutable()
    }


}