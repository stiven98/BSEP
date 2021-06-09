package rs.ac.uns.ftn.bsep.service.impl;


import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

@Service
public class LoggerService {

    public Logger logger;

    public LoggerService() throws IOException {
        this.logger=Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
        FileHandler fh= new FileHandler("UserLog.log");
        SimpleFormatter formatter=new SimpleFormatter();
        fh.setFormatter(formatter);
        logger.addHandler(fh);
    }
}
