/*
 * Copyright 2018 CMPUT301W18T18
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package ca.ualberta.cs.wrkify;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class JSONConstructor {
    /**
     * Builds a JSON object.
     * @param elements
     * @return
     */
    public static String makeJSONObject(JSONConstructorElement... elements) {
        return jo(elements).toString();
    }

    public static JSONConstructorElement j(String key, String value) {
        return new JSONConstructorElement(key, new JSONConstructorString(value));
    }

    public static JSONConstructorElement j(String key, Number number) {
        return new JSONConstructorElement(key, new JSONConstructorNumber(number));
    }

    public static JSONConstructorElement j(String key, JSONConstructorValue value) {
        return new JSONConstructorElement(key, value);
    }

    public static JSONConstructorElement j(String key, JSONConstructorElement... elements) {
        return new JSONConstructorElement(key, new JSONConstructorObject(elements));
    }

    public static JSONConstructorObject jo(JSONConstructorElement... elements) {
        return new JSONConstructorObject(elements);
    }

    public static JSONConstructorArray ja(String... valueStrings) {
        ArrayList<JSONConstructorValue> values = new ArrayList<>();
        for (String valueString: valueStrings) {
            values.add(new JSONConstructorString(valueString));
        }
        return ja(values.toArray(new JSONConstructorValue[]{}));
    }

    public static JSONConstructorArray ja(JSONConstructorValue... values) {
        return new JSONConstructorArray(values);
    }

    private static String commaJoin(Object... objects) {
        StringBuilder stringBuilder = new StringBuilder("");
        for(Object elem: objects) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append(",");
            }
            stringBuilder.append(elem);
        }
        return stringBuilder.toString();
    }

    private static String escape(String string) {
        return string.replaceAll("[\"\\\\]", "\\\\$0");
    }

    static class JSONConstructorElement {
        private String key;
        private JSONConstructorValue value;

        public JSONConstructorElement(String key, JSONConstructorValue value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return "\"" + key + "\":" + value;
        }
    }

    static class JSONConstructorValue { }

    static class JSONConstructorObject extends JSONConstructorValue {
        private JSONConstructorElement[] elements;

        public JSONConstructorObject(JSONConstructorElement... elements) {
            this.elements = elements;
        }

        @Override
        public String toString() {
            return "{" + commaJoin(elements) + "}";
        }
    }

    static class JSONConstructorArray extends JSONConstructorValue {
        private JSONConstructorValue[] values;

        public JSONConstructorArray(JSONConstructorValue... values) {
            this.values = values;
        }

        @Override
        public String toString() {
            return "[" + commaJoin(values) + "]";
        }
    }

    static class JSONConstructorString extends JSONConstructorValue {
        private String value;

        public JSONConstructorString(String value) {
            this.value = escape(value);
        }

        @Override
        public String toString() {
            return "\"" + value + "\"";
        }
    }

    static class JSONConstructorNumber extends JSONConstructorValue {
        private Number number;

        public JSONConstructorNumber(Number number) {
            this.number = number;
        }

        @Override
        public String toString() {
            return number.toString();
        }
    }
}
