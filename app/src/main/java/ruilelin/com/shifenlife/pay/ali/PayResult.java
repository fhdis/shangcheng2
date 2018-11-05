package ruilelin.com.shifenlife.pay.ali;

import java.util.Map;

import android.text.TextUtils;
import android.util.Log;

public class PayResult {
    private String resultStatus;
    private String result;
    private String memo;

    public PayResult(Map<String, String> rawResult) {
        if (rawResult == null) {
            return;
        }

        for (String key : rawResult.keySet()) {
            if (TextUtils.equals(key, "resultStatus")) {
                Log.d("test","test2");
                resultStatus = rawResult.get(key);
            } else if (TextUtils.equals(key, "result")) {
                Log.d("test","test3");
                result = rawResult.get(key);
            } else if (TextUtils.equals(key, "memo")) {
                Log.d("test","test4");
                memo = rawResult.get(key);
            }
        }
    }

    @Override
    public String toString() {
        return "resultStatus={" + resultStatus + "};memo={" + memo
                + "};result={" + result + "}";
    }

    /**
     * @return the resultStatus
     */
    public String getResultStatus() {
        return resultStatus;
    }

    /**
     * @return the memo
     */
    public String getMemo() {
        return memo;
    }

    /**
     * @return the result
     */
    public String getResult() {
        return result;
    }
}

