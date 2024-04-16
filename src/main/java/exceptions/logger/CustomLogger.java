package exceptions.logger;

import exceptions.APIException;
import io.javalin.http.Context;
import io.javalin.http.Handler;

import java.io.IOException;
import java.util.logging.*;

public class CustomLogger<T> {
    private static final Logger logger = Logger.getLogger(CustomLogger.class.getName());
    private static final String logFilePath = "C:\\Users\\baban\\OneDrive\\Skrivebord\\exam\\src\\main\\java\\exceptions\\logger\\errors.txt";

    static {
        try {
            FileHandler fileHandler = new FileHandler(logFilePath);
            fileHandler.setFormatter(new SimpleFormatter() {
                @Override
                public String format(LogRecord record) {
                    return record.getMessage() + "\n";
                }
            });
            logger.addHandler(fileHandler);
            logger.setUseParentHandlers(false);
        } catch (IOException e) {
            throw new RuntimeException("Error creating log file: " + logFilePath, e);
        }
    }

    public Handler handleExceptions(Handler handler) {
        return ctx -> {
            try {
                handler.handle(ctx);
            } catch (APIException e) {
                logException(ctx, e);
                ctx.json("{\"status\": \"" + e.getStatusCode() + "\", \"message\": \"" + e.getMessage() + "\", \"timestamp\": \"" + e.getTimeStamp() + "\"}");
            }
        };
    }

    private void logException(Context ctx, APIException e) {
        String logMessage = "Method: " + ctx.method() + ", ";
        logMessage += "Status: " + e.getStatusCode() + ", ";
        logMessage += "Message: " + e.getMessage() + ", ";
        logMessage += "Timestamp: " + e.getTimeStamp() + ", ";
        logMessage += "IP Address: " + ctx.ip();
        logger.log(Level.SEVERE, logMessage);
    }
}