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

import android.util.Log;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.List;

import static ca.ualberta.cs.wrkify.JSONConstructor.j;
import static ca.ualberta.cs.wrkify.JSONConstructor.ja;
import static ca.ualberta.cs.wrkify.JSONConstructor.jo;
import static ca.ualberta.cs.wrkify.JSONConstructor.makeJSONObject;

/**
 * Collection of static methods to perform pre-defined searches.
 */
public class Searcher {

    /**
     * Find all tasks where the given User is the task requester and
     * the status of the task is one of the provided statuses.
     * @param client RemoteClient to search in
     * @param requester User to search for
     * @param statuses the statuses that are valid in your search
     * @return List of tasks matching the search (may be empty)
     * @throws IOException if RemoteClient is disconnected
     */
    static List<Task> findTasksByRequester(RemoteClient client, User requester, TaskStatus... statuses) throws IOException {
        String query = "{\"query\":{\"bool\":{\"must\":[{\"nested\":{\"path\":\"requester\",\"query\":"
                + "{\"match\":{\"requester.refId\":\"" + requester.getId() + "\"}}}},{"
                + getTaskStatusQuery(statuses)+ "}]}}}";
        return client.search(query, Task.class);
    }

    /**
     * Find all tasks where the given User is the task requester.
     * @param client RemoteClient to search in
     * @param requester User to search for
     * @return List of tasks matching the search (may be empty)
     * @throws IOException if RemoteClient is disconnected
     */
    static List<Task> findTasksByRequester(RemoteClient client, User requester) throws IOException {
        return findTasksByRequester(client, requester,
                TaskStatus.BIDDED, TaskStatus.REQUESTED, TaskStatus.ASSIGNED, TaskStatus.DONE);
    }

    /**
     * Find all tasks where the given User is the assigned task provider and
     * the status of the task is one of the provided statuses.
     * @param client RemoteClient to search in
     * @param provider User to search for
     * @param statuses the statuses you want in your search
     * @return List of tasks matching the search (may be empty)
     * @throws IOException if RemoteClient is disconnected
     */
    static List<Task> findTasksByProvider(RemoteClient client, User provider, TaskStatus... statuses) throws IOException {
        String query = "{\"query\":{\"bool\":{\"must\":[{\"nested\":{\"path\":\"provider\",\"query\":"
                + "{\"match\":{\"provider.refId\":\"" + provider.getId() + "\"}}}},{"
                + getTaskStatusQuery(statuses)+ "}]}}}";
        Log.e("query", query);
        return client.search(query, Task.class);
    }

    /**
     * Find all tasks where the given User is the assigned task provider.
     * @param client RemoteClient to search in
     * @param provider User to search for
     * @return List of tasks matching the search (may be empty)
     * @throws IOException if RemoteClient is disconnected
     */
    static List<Task> findTasksByProvider(RemoteClient client, User provider) throws IOException {
        return findTasksByProvider(client, provider,
                TaskStatus.BIDDED, TaskStatus.REQUESTED, TaskStatus.ASSIGNED, TaskStatus.DONE);
    }

    /**
     * Find all tasks where the given User has placed a bid and
     * the status of the task is one of the provided statuses.
     * @param client RemoteClient to search in
     * @param bidder User to search for
     * @param statuses the statuses you want in your search
     * @return List of tasks matching the search (may be empty)
     * @throws IOException if RemoteClient is disconnected
     */
    static List<Task> findTasksByBidder(RemoteClient client, User bidder, TaskStatus... statuses) throws IOException {
        String query = "{\"query\":{\"bool\":{\"must\":[{\"nested\":{\"path\":\"bidList.bidder\",\"query\":"
                + "{\"match\":{\"bidList.bidder.refId\":\"" + bidder.getId() + "\"}}}},{"
                + getTaskStatusQuery(statuses)+ "}]}}}";
        return client.search(query, Task.class);
    }

    /**
     * Find all tasks where the given User has placed a bid.
     * @param client RemoteClient to search in
     * @param bidder User to search for
     * @return List of tasks matching the search (may be empty)
     * @throws IOException if RemoteClient is disconnected
     */
    static List<Task> findTasksByBidder(RemoteClient client, User bidder) throws IOException {
        return findTasksByBidder(client, bidder,
                TaskStatus.BIDDED, TaskStatus.REQUESTED, TaskStatus.ASSIGNED, TaskStatus.DONE);
    }

    /**
     * Find all tasks matching a search string.
     * @param client RemoteClient to search in
     * @param keywords Keywords to search for
     * @return List of tasks matching the search (may be empty)
     * @throws IOException if RemoteClient is disconnected
     */
    static List<Task> findTasksByKeywords(RemoteClient client, String keywords) throws IOException {
        String query = makeJSONObject(
            j("query",
                j("bool",
                    j("should", ja(
                        jo(
                            j("match",
                                j("title", keywords)
                            )
                        ),
                        jo(
                            j("match",
                                j("description", keywords)
                            )
                        )
                    )),
                    j("minimum_should_match", 1)
                )
            )
        );
        Log.d("-->", query);
        return client.search(query, Task.class);
    }

    static List<Task> findTasksByKeywordsNear(RemoteClient client, String keywords, TaskLocation location) throws IOException {
        String query = makeJSONObject(
            j("query",
                j("bool",
                    j("should", ja(
                        jo(
                            j("match",
                                j("title", keywords)
                            )
                        ),
                        jo(
                            j("match",
                                j("description", keywords)
                            )
                        )
                    )),
                    j("minimum_should_match", 1)
                )
            ),
            j("filter",
                j("geo_distance",
                    j("distance","1km"),
                    j("location",
                        j("lat", location.getLatitude()),
                        j("lon", location.getLongitude())
                    )
                )
            )
        );
        Log.d("-->", query);
        return client.search(query, Task.class);
    }

    /**
     * gets a user by its username
     * @param client RemoteClient to search in
     * @param username the username of the user
     * @return the User associated with username
     * @throws IOException if RemoteClient is disconnected
     */
    static User getUser(RemoteClient client, String username) throws IOException {
        String query = "{\"query\":{\"match\":{\"username\": \"" + username + "\"}}}";
        List<User> results = client.search(query, User.class);
        if (results.size() == 0) {
            return null;
        }
        return results.get(0);
    }

    // TODO findTasksByLocation?

    /**
     * generates an elasticsearch bool query to match
     * tasks that have one of the provided statuses.
     * @param statuses the valid statuses for this query.
     * @return the json bool query as a string
     */
    private static String getTaskStatusQuery(TaskStatus... statuses) {
        Gson gson = new Gson();

        String arr = "";
        for (TaskStatus status: statuses) {
            arr += String.format("{\"match\": {\"status\": %s}},", gson.toJson(status));
        }

        arr = arr.substring(0, arr.length()-1);

        return String.format("\"bool\":{\"should\":[%s],\"minimum_number_should_match\":1}", arr);
    }
}
