package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class Utils {

    public static class ProcessOutput {
        private String error;
        private String output;
        private int    exitCode;

        public String getOutput() {
            return output;
        }

        public String getError() {
            return error;
        }

        public void addOutput(String output) {
            if (this.output == null) {
                this.output = output;
            } else {
                this.output += output;
            }
        }

        public void addError(String error) {
            if (this.error == null) {
                this.error = error;
            } else {
                this.error += error;
            }
        }

        public void setExitCode(int exitCode) {
            this.exitCode = exitCode;
        }

        public int getExitCode() {
            return exitCode;
        }

        @Override
        public String toString() {
            return "ProcessOutput [error=" + error + ", output=" + output + ", exitCode=" + exitCode + "]";
        }

    }


      public static ProcessOutput systemExecute(String... command) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(command);
        Process process = pb.start();
        ProcessOutput output = new ProcessOutput();
        process.waitFor();

        InputStream errorStream = process.getErrorStream();
        InputStream inputStream = process.getInputStream();
        OutputStream outputStream = process.getOutputStream();
        try {
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(errorStream));
            BufferedReader stdoutReader = new BufferedReader(new InputStreamReader(inputStream));
            while (true) {
                String line = errorReader.readLine();
                if (line == null) {
                    break;
                }
                output.addError(line + System.getProperty("line.separator"));
            }
            while (true) {
                String line = stdoutReader.readLine();
                if (line == null) {
                    break;
                }
                output.addOutput(line + System.getProperty("line.separator"));
            }
            output.setExitCode(process.exitValue());
            return output;
 			} finally {
            if (errorStream != null) {
                errorStream.close();
            }
            if (errorStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
        }
    }

}

