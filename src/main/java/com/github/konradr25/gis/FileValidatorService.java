package com.github.konradr25.gis;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Slf4j
public class FileValidatorService {

    void validateArgs(String[] args) {
        boolean isFile = args != null && args.length == 1;
        if (!isFile) {
            throw new InvalidInputException("Invalid filename as argument");
        }
    }

    public boolean isGraphFromStandardInput(String lineToValidate) {
        List<String> splitedLineStr = new LinkedList<String>(Arrays.asList(lineToValidate.split("\\s+")));
        splitedLineStr.remove("");

        if (splitedLineStr.size() > 2) {
            log.error("Too many parameters in line");
            throw new InvalidInputException("Too many parameters in line");
        } else if (splitedLineStr.size() == 0) {
            log.error("Invalid empty line");
            throw new InvalidInputException("Invalid empty line");
        }

        for (String s : splitedLineStr) {
            if(!StringUtils.isNumeric(s)) {
                log.error("Non-numeric input");
                throw new InvalidInputException("Non-numeric input");
            }
        }

        return true;
    }
}
