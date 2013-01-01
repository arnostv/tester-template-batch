
class FileGroup {

    Collection<File> filesInGroup

    FileGroup(File... filesInGroup) {
        this.filesInGroup = filesInGroup.toList()
    }

    @Override
    public String toString() {
        return "FileGroup" + filesInGroup + '';
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false

        FileGroup fileGroup = (FileGroup) o

        if (filesInGroup != fileGroup.filesInGroup) return false

        return true
    }

    int hashCode() {
        return filesInGroup.hashCode()
    }

}
