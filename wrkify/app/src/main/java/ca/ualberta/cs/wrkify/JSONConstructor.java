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

import java.util.ArrayList;

/**
 * JSONConstructor provides static methods and classes
 * for writing json in simply in java
 */
public class JSONConstructor {
    /**
     * Builds a JSON object.
     * @param elements
     * @return a String of the Json object
     */
    public static String makeJSONObject(JSONConstructorElement... elements) {
        return jo(elements).toString();
    }

    /**
     * creates a JsonConstructorElement from a base string value
     * @param key the key to the element
     * @param value the value of the element
     * @return the element
     */
    public static JSONConstructorElement j(String key, String value) {
        return new JSONConstructorElement(key, new JSONConstructorString(value));
    }

    /**
     * creates a JsonConstructorElement from a base number value
     * @param key the key to the element
     * @param number the value of the element
     * @return the element
     */
    public static JSONConstructorElement j(String key, Number number) {
        return new JSONConstructorElement(key, new JSONConstructorNumber(number));
    }

    /**
     * creates a JsonConstructorElement from a JsonConstuctorValue
     * @param key the key to the element
     * @param value the JsonConstructorValue
     * @return the element
     */
    public static JSONConstructorElement j(String key, JSONConstructorValue value) {
        return new JSONConstructorElement(key, value);
    }

    /**
     * creates a JsonConstructorElement from a JsonConstructorElement
     * @param key the key to the element
     * @param elements the JsonConstructorElements
     * @return the element
     */
    public static JSONConstructorElement j(String key, JSONConstructorElement... elements) {
        return new JSONConstructorElement(key, new JSONConstructorObject(elements));
    }

    /**
     * creates a JsonConstructorElement from many JsonConstructorElements
     * @param elements the JsonConstructorElements
     * @return the object of the elements
     */
    public static JSONConstructorObject jo(JSONConstructorElement... elements) {
        return new JSONConstructorObject(elements);
    }

    /**
     * creates a JsonConstructorArray from many String values.
     * @param valueStrings the strings in the array.
     * @return the array of the elements
     */
    public static JSONConstructorArray ja(String... valueStrings) {
        ArrayList<JSONConstructorValue> values = new ArrayList<>();
        for (String valueString: valueStrings) {
            values.add(new JSONConstructorString(valueString));
        }
        return ja(values.toArray(new JSONConstructorValue[]{}));
    }

    /**
     * creates a JsonConstructorArray from many String values.
     * @param values the JSONConstructorValues in to make an array of
     * @return the array of the elements
     */
    public static JSONConstructorArray ja(JSONConstructorValue... values) {
        return new JSONConstructorArray(values);
    }

    /**
     * given many objects create a string of their
     * string representations joied by commas
     * @param objects the objects you want to pring
     * @return the formatted string
     */
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

    /**
     * given a string, replace all of the quotes an backslashes
     * in the string with their escaped counterparts.
     * @param string the string you want to escape.
     * @return the escaped string.
     */
    private static String escape(String string) {
        return string.replaceAll("[\"\\\\]", "\\\\$0");
    }

    /**
     * JSONConstructorElement represents
     * a json key-value pair
     */
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

    /**
     * JSONConstructorValue is inherited by
     * classes whose toString function
     * returns a valid json value.
     */
    static class JSONConstructorValue { }

    /**
     * JSONConstructorObject contains Json key-value pairs
     * and is a value
     *
     * @see JSONConstructorElement
     */
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

    /**
     * JSONConstructorArray represents a json array
     * of json values
     */
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

    /**
     * JSONConstructorString is a string literal json
     * value
     */
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

    /**
     * JSONConstructorNumber is a number literal json
     * value
     */
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
