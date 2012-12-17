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
}
