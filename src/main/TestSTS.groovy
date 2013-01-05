@groovy.transform.Immutable class TestSTS {
    TestSuite suite
    TestCase testCase
    TestStep step
    Map params

    static TestSTS createWithTestSuite(TestSuite testSuite) {
        new TestSTS(suite: testSuite)
    }

    TestSTS withTestCase(TestCase testCase1) {
        new TestSTS(suite: suite, testCase: testCase1)
    }

    TestSTS withTestStep(TestStep testStep) {
        new TestSTS(suite: suite, testCase: testCase, step: testStep)
    }

    TestSTS withParams(Map params) {
        new TestSTS(suite: suite, testCase: testCase, step: step, params: params)
    }

    String findActionScriptPath () {
        String actionScriptPath = null
        if (step?.actionScriptPath) {
            actionScriptPath = step?.actionScriptPath
            println "Using test Step script ${actionScriptPath}"
        } else if (testCase?.actionScriptPath) {
            actionScriptPath = testCase?.actionScriptPath
            println "Using test Case script ${actionScriptPath}"
        } else if (suite?.actionScriptPath) {
            actionScriptPath = suite?.actionScriptPath
            println "Using test Suite script ${actionScriptPath}"
        }
        actionScriptPath
    }
}
