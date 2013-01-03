class ImaginaryTestedSystem {
    /*
        this class should be in 'testdata' instead of 'src', but if saved in 'testdata' it
        won't behave as proper singleton across invocation of .groovy action scripts

        This can be observed by having different instances printed by
            println "Accessing ${INSTANCE}"

     */


    public static  ImaginaryTestedSystem INSTANCE = new ImaginaryTestedSystem()

    def messages = [:]

    def sendMessage(String message) {
        println "Accessing ${INSTANCE}"
        def parsed = new XmlParser().parseText(message)
        def key =  parsed.key.text()
        messages[key] = message
        println("Stroring ${key}, repository contains ${messages.size()} items")
        return key
    }

    def readResponse(String key) {
        println "Accessing ${INSTANCE}"
        println("Waiting for ${key}, repository contains ${messages.size()} items")
        //sleep(1)
        messages[key]
    }

    def sleep(int time) {
        Thread.sleep(time * 1000)
    }
}


