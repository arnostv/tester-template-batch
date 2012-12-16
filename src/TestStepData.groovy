
class TestStepData {
    TestSTS testSTS
    def evaluatedTemplate

    TestStepData(TestSTS testSTS, String evaluatedTemplate) {
        this.testSTS = testSTS
        this.evaluatedTemplate = evaluatedTemplate

    }
}
