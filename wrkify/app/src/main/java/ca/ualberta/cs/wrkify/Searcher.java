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

abstract class Searcher<TClient extends RemoteClient> {
    protected TClient client;

    public Searcher(TClient client) {
        this.client = client;
    }

    /**
     * Find all tasks where the given User is the task requester and
     * the status of the task is one of the provided statuses.
     * @param requester User to search for
     * @param statuses the statuses that are valid in your search
     * @return List of tasks matching the search (may be empty)
     * @throws IOException if RemoteClient is disconnected
     */
    public abstract List<Task> findTasksByRequester(User requester, TaskStatus... statuses) throws IOException;

    /**
     * Find all tasks where the given User is the task requester.
     * @param requester User to search for
     * @return List of tasks matching the search (may be empty)
     * @throws IOException if RemoteClient is disconnected
     */
    public abstract List<Task> findTasksByRequester(User requester) throws IOException;

    /**
     * Find all tasks where the given User is the assigned task provider and
     * the status of the task is one of the provided statuses.
     * @param provider User to search for
     * @param statuses the statuses you want in your search
     * @return List of tasks matching the search (may be empty)
     * @throws IOException if RemoteClient is disconnected
     */
    public abstract List<Task> findTasksByProvider(User provider, TaskStatus... statuses) throws IOException;

    /**
     * Find all tasks where the given User is the assigned task provider.
     * @param provider User to search for
     * @return List of tasks matching the search (may be empty)
     * @throws IOException if RemoteClient is disconnected
     */
    public abstract List<Task> findTasksByProvider(User provider) throws IOException;

    /**
     * Find all tasks where the given User has placed a bid and
     * the status of the task is one of the provided statuses.
     * @param bidder User to search for
     * @param statuses the statuses you want in your search
     * @return List of tasks matching the search (may be empty)
     * @throws IOException if RemoteClient is disconnected
     */
    public abstract List<Task> findTasksByBidder(User bidder, TaskStatus... statuses) throws IOException;

    /**
     * Find all tasks where the given User has placed a bid.
     * @param bidder User to search for
     * @return List of tasks matching the search (may be empty)
     * @throws IOException if RemoteClient is disconnected
     */
    public abstract List<Task> findTasksByBidder(User bidder) throws IOException;

    /**
     * Find all tasks matching a search string.
     * @param keywords Keywords to search for
     * @return List of tasks matching the search (may be empty)
     * @throws IOException if RemoteClient is disconnected
     */
    public abstract List<Task> findTasksByKeywords(String keywords) throws IOException;

    public abstract List<Task> findTasksNear(TaskLocation location) throws IOException;

    public abstract List<Signal> findSignalsByUser(User user) throws IOException;

    public abstract List<Signal> findSignalsByUserAndTargetIds(String userId, String targetId) throws IOException;

    /**
     * gets a user by its username
     * @param username the username of the user
     * @return the User associated with username
     * @throws IOException if RemoteClient is disconnected
     */
    public abstract User getUser(String username) throws IOException;
}
