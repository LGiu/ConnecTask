package com.connectask.activity.classes;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
/**
 * Created by Leonardo Giuliani on 29/03/2018.
 */

public class Cpf {
    private static final String CPFMask = "###.###.###-##";
    private static final String CNPJMask = "##.###.###/####-##";



    public static String unmask(String s) {
        return s.replaceAll("[^0-9]*", "");
    }

    private static String getDefaultMask(String str) {
        String defaultMask = CPFMask;
        if (str.length() == 14){
            defaultMask = CNPJMask;
        }
        return CPFMask;
    }

    public static TextWatcher insert(final EditText editText, final int maskType) {
        return new TextWatcher() {

            boolean isUpdating;
            String oldValue = "";

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String value = Cpf.unmask(s.toString());
                String mask;
                switch (maskType) {
                    case 1:
                        mask = CPFMask;
                        break;
                    case 2:
                        mask = CNPJMask;
                        break;
                    default:
                        mask = getDefaultMask(value);
                        break;
                }

                String maskAux = "";
                if (isUpdating) {
                    oldValue = value;
                    isUpdating = false;
                    return;
                }
                int i = 0;
                for (char m : mask.toCharArray()) {
                    if ((m != '#' && value.length() > oldValue.length()) || (m != '#' && value.length() < oldValue.length() && value.length() != i)) {
                        maskAux += m;
                        continue;
                    }

                    try {
                        maskAux += value.charAt(i);
                    } catch (Exception e) {
                        break;
                    }
                    i++;
                }
                isUpdating = true;
                editText.setText(maskAux);
                editText.setSelection(maskAux.length());
            }

            public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
            public void afterTextChanged(Editable s) {}
        };
    }
}
