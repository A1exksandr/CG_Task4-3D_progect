package com.vsucg.objreader;

public class ObjReaderException extends RuntimeException {  // Наследуемся от RuntimeException
    private final int lineNumber;

    public ObjReaderException(String message, int lineInd) {
        super("Error parsing OBJ file on line: " + lineInd + ". " + message);
        this.lineNumber = lineInd;
    }

    public ObjReaderException(String message, int lineInd, Throwable cause) {
        super("Error parsing OBJ file on line: " + lineInd + ". " + message, cause);
        this.lineNumber = lineInd;
    }

    public int getLineNumber() {
        return lineNumber;
    }
}
