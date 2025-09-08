package effectivejava;

interface FileFormatter {

    void format(Document document);
}

public class FancyEditor {

    private final FileFormatter fileFormatter;

    public FancyEditor(FileFormatter fileFormatter) {
        this.fileFormatter = fileFormatter;
    }

    public void update(Document document) {
        // Updating...
        fileFormatter.format(document);
    }
}

class WordDocFormatter implements FileFormatter {

    @Override
    public void format(Document document) {

    }
}

class Document {

}