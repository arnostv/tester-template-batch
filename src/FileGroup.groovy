
class FileGroup {

    String prefix
    Collection<File> filesInGroup

    FileGroup(String prefix, File... filesInGroup) {
        assert prefix != null
        assert filesInGroup != null

        this.prefix = prefix
        this.filesInGroup = filesInGroup.toList()
    }

    def fileByType(String type) {
        def fileName = prefix + type
        filesInGroup.find {it.name == fileName}
    }


    @Override
    public String toString() {
        return "FileGroup ${prefix} ${filesInGroup.size}:${filesInGroup}"
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false

        FileGroup fileGroup = (FileGroup) o

        if (filesInGroup != fileGroup.filesInGroup) return false
        if (prefix != fileGroup.prefix) return false

        return true
    }

    int hashCode() {
        int result
        result = prefix.hashCode()
        result = 31 * result + filesInGroup.hashCode()
        return result
    }

}
