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


public class Signal extends RemoteObject {
    public enum SignalType {
        SIGNAL_NEW_BID,
        SIGNAL_OUTBID,
        SIGNAL_REJECTED,
        SIGNAL_ASSIGNED,
        SIGNAL_DEASSIGNED,
        SIGNAL_CLOSED
    }

    private final RemoteReference<User> user;

    private final SignalType type;
    private final String targetID;
    private final String targetName;

    public Signal(User user, SignalType type, String targetID, String targetName) {
        this.user = user.reference();
        this.type = type;
        this.targetID = targetID;
        this.targetName = targetName;
    }

    public SignalType getType() {
        return type;
    }

    public String getTargetID() {
        return targetID;
    }

    public String getTargetName() {
        return targetName;
    }
}
