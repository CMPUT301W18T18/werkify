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


/**
 * Entity representing an abstract notification.
 * A notification has three text fields: a target, and text to display
 * before and after the target. A NotificationInfo object can be
 * rendered in multiple ways; most likely in a NotificationView, but
 * it could also be converted to a system notification or any other
 * kind of visual form.
 *
 * @see NotificationView
 */
public class NotificationInfo {
    private String preText;
    private String targetText;
    private String postText;

    public NotificationInfo(String preText, String targetText, String postText) {
        this.preText = preText;
        this.targetText = targetText;
        this.postText = postText;
    }

    public String getPreText() {
        return preText;
    }

    public String getTargetText() {
        return targetText;
    }

    public String getPostText() {
        return postText;
    }
}
