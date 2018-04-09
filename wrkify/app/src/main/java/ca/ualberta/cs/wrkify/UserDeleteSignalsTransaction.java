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
import java.util.List;

public class UserDeleteSignalsTransaction extends Transaction<User> {
    private String targetId;

    public UserDeleteSignalsTransaction(User user, String targetId) {
        super(user, User.class);
        this.targetId = targetId;
    }

    @Override
    public boolean applyInClient(CachingClient client) throws IOException {
        List<Signal> signals = client.getSearcher().findSignalsByUserAndTargetIds(getId(), targetId);
        for (Signal signal : signals) {
            client.delete(signal);
        }

        return true;
    }
}
