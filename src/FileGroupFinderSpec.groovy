import spock.lang.Specification

class FileGroupFinderSpec extends Specification {

    def "empty directory contains no groups"() {
        setup:
        File location = Mock();
        location.list() >> []

        when:
        def groups = new FileGroupFinder(location).findGroups()

        then:
        groups.isEmpty()

    }

    def "directory with one file yields one group"() {
        setup:
        File location = Mock();
        location.list() >> ["file1.txt"]

        when:
        def groups = new FileGroupFinder(location).findGroups()

        then:
        groups == [new FileGroup("file1.txt")]
    }

    def "directory with two files with same name yields one group"() {
        setup:
        File location = Mock();
        location.list() >> ["file1.txt", "file1.xml"]

        when:
        def groups = new FileGroupFinder(location).findGroups()

        then:

        groups == [new FileGroup("file1.txt","file1.xml")]
    }

    def "directory with three files in two groups"() {
        setup:
        File location = Mock();
        location.list() >> ["file1.txt", "file2.txt", "file1.xml"]

        when:
        def groups = new FileGroupFinder(location).findGroups()

        then:
        groups == [new FileGroup("file1.txt","file1.xml"), new FileGroup("file2.txt")]
    }

    def "directory with two files with no suffix yields two groups"() {
        setup:
        File location = Mock();
        location.list() >> ["file1", "file2"]

        when:
        def groups = new FileGroupFinder(location).findGroups()

        then:
        groups == [new FileGroup("file1"), new FileGroup("file2")]

    }

}
