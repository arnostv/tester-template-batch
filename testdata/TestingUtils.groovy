import java.text.SimpleDateFormat

class TestingUtils {
    def hello() {
        println "Hello from testing utils"
    }

    def timeStampString() {
        new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date())
    }
}
