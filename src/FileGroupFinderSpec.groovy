import spock.lang.Specification

class FileGroupFinderSpec extends Specification {
    static file1 = new File("file1")
    static file2 = new File("file2")
    static file1txt = new File("file1.txt")
    static file1xml = new File("file1.xml")
    static file2txt = new File("file2.txt")
    static excluded1 = new File("excluded1.xml")

    def "empty directory contains no groups"() {
        setup:
        File location = Mock();
        location.listFiles() >> []

        when:
        def groups = new FileGroupFinder(location).findGroups()

        then:
        groups.isEmpty()

    }

    def "directory with one file yields one group"() {
        setup:
        File location = Mock();
        location.listFiles() >> [file1txt]

        when:
        def groups = new FileGroupFinder(location).findGroups()

        then:
        groups == [new FileGroup(file1txt)]
    }

    def "directory with two files with same prefix yields one group"() {
        setup:
        File location = Mock();
        location.listFiles() >> [file1txt, file1xml]

        when:
        def groups = new FileGroupFinder(location).findGroups()

        then:

        groups == [new FileGroup(file1txt, file1xml)]
    }

    def "directory with two files but one excluded yields one group with one file"() {
        setup:
        File location = Mock();
        location.listFiles() >> [file1txt, excluded1]

        when:
        def groups = new FileGroupFinder(location, [excluded1.name]).findGroups()

        then:

        groups == [new FileGroup(file1txt)]
    }

    def "directory with three files in two groups"() {
        setup:
        File location = Mock();
        location.listFiles() >> [file1txt, file2txt, file1xml]

        when:
        def groups = new FileGroupFinder(location).findGroups()

        then:
        groups == [new FileGroup(file1txt, file1xml), new FileGroup(file2txt)]
    }

    def "directory with two files with no suffix yields two groups"() {
        setup:
        File location = Mock();
        location.listFiles() >> [file1, file2]

        when:
        def groups = new FileGroupFinder(location).findGroups()

        then:
        groups == [new FileGroup(file1), new FileGroup(file2)]

    }

}
