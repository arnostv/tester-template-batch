println "Default test suite action for test - uppercase it"

def utils = new TestingUtils()
utils.hello()

println payload.toString().toUpperCase()

