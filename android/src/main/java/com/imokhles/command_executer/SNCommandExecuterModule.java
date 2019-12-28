package com.imokhles.command_executer;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.WritableMap;

import java.io.DataOutputStream;
import java.io.IOException;

import javax.annotation.Nonnull;

public class SNCommandExecuterModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;

    public SNCommandExecuterModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return "SNCommandExecuter";
    }

    @ReactMethod
        public void rootExec(String cmd, Promise promise) {
            try {
                ExecResult r = execRootCmd(cmd);
                WritableMap result = Arguments.createMap();
                result.putInt("exitValue", r.exitValue);
                result.putString("stdout", r.stdout.toString());
                result.putString("stderr", r.stderr.toString());
                promise.resolve(result);
            } catch (Exception ex) {
                ex.printStackTrace();
                promise.reject(ex.getClass().getSimpleName(), ex.getMessage());
            }
        }
        
    @ReactMethod
        public void nonRootExec(String cmd, Promise promise) {
            try {
                ExecResult r = execNonRootCmd(cmd);
                WritableMap result = Arguments.createMap();
                result.putInt("exitValue", r.exitValue);
                result.putString("stdout", r.stdout.toString());
                result.putString("stderr", r.stderr.toString());
                promise.resolve(result);
            } catch (Exception ex) {
                ex.printStackTrace();
                promise.reject(ex.getClass().getSimpleName(), ex.getMessage());

            }
        }

    static public class ExecResult {
            public ExecResult() {
            }
            public int exitValue = -1;
            public StringBuffer stdout = new StringBuffer();
            public StringBuffer stderr = new StringBuffer();
        }

        public static ExecResult execRootCmd(String cmd) throws IOException, InterruptedException {
            ExecResult result = new ExecResult();

            Process p = Runtime.getRuntime().exec("su");
            DataOutputStream stdin = new DataOutputStream(p.getOutputStream());
            stdin.writeChars(cmd);
            stdin.writeChars("\n");
            stdin.flush();
            stdin.writeChars("exit\n");
            p.waitFor();
            result.exitValue = p.exitValue();
            byte[] buffer = new byte[4096];
            int readBytes;
            while ((readBytes = p.getInputStream().read(buffer)) != -1) {
                result.stdout.append(new String(buffer, 0, readBytes));
            }
            while ((readBytes = p.getErrorStream().read(buffer)) != -1) {
                result.stderr.append(new String(buffer, 0, readBytes));
            }
            return result;
        }

        public static ExecResult execNonRootCmd(String cmd) throws IOException, InterruptedException {
            ExecResult result = new ExecResult();

            Process p = Runtime.getRuntime().exec(cmd);
            DataOutputStream stdin = new DataOutputStream(p.getOutputStream());
            stdin.writeChars("\n");
            stdin.flush();
            stdin.writeChars("exit\n");
            p.waitFor();
            result.exitValue = p.exitValue();
            byte[] buffer = new byte[4096];
            int readBytes;
            while ((readBytes = p.getInputStream().read(buffer)) != -1) {
                result.stdout.append(new String(buffer, 0, readBytes));
            }
            while ((readBytes = p.getErrorStream().read(buffer)) != -1) {
                result.stderr.append(new String(buffer, 0, readBytes));
            }
            return result;
        }

}
