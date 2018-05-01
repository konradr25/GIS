package com.github.konradr25.gis;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileValidatorService {

    void validateArgs(String[] args) {
        boolean isFile = args != null && args.length == 1 && isFileName(args[0]);
        if (!isFile && !isGraphFromStandardInput()) {
            throw new InvalidInputException();
        }
    }

    private boolean isGraphFromStandardInput() {
        //TODO
        return false;
    }

    private boolean isFileName(String arg) {
        //TODO
        return true;
    }
}
