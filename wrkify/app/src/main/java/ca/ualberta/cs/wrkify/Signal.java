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


import java.io.IOException;

/**
 * Signal is a remoteObject that holds
 * notification info.
 */
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
    private final String targetId;
    private final String targetName;

    /**
     * create a signal with the relavent info
     * @param user the user who the signal is to.
     * @param type the type of Signal.
     * @param targetId the id of the subject object
     * @param targetName the name of the subject object
     */
    public Signal(User user, SignalType type, String targetId, String targetName) {
        this.user = user.reference();
        this.type = type;
        this.targetId = targetId;
        this.targetName = targetName;
    }

    /**
     * return the type of the signal
     * @return the type
     */
    public SignalType getType() {
        return type;
    }

    /**
     * return the TargetId
     * @return the TargetId
     */
    public String getTargetId() {
        return targetId;
    }

    /**
     * return the TargetName
     * @return the TargetName
     */
    public String getTargetName() {
        return targetName;
    }

    /**
     * get the user that the signal is for
     * @param client the client we get the user from
     * @return the user
     * @throws IOException when the client fails to connect
     */
    public User getRemoteUser(RemoteClient client) throws IOException {
        return user.getRemote(client, User.class);
    }
}
