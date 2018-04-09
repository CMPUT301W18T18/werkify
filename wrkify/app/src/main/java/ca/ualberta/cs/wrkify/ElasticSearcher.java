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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static ca.ualberta.cs.wrkify.JSONConstructor.j;
import static ca.ualberta.cs.wrkify.JSONConstructor.ja;
import static ca.ualberta.cs.wrkify.JSONConstructor.jo;
import static ca.ualberta.cs.wrkify.JSONConstructor.makeJSONObject;

/**
 * object to preform pre-defined searches on a client
 *
 * @see Searcher
 */
public class ElasticSearcher extends Searcher<ElasticClient> {
    /**
     * set the client to search on
     * @param client the client to search on
     */
    public ElasticSearcher(ElasticClient client) {
        super(client);
    }

    /**
     * find all the tasks where the requester is requester and the status
     * is one of statuses.
     * @param requester User to search for.
     * @param statuses the statuses that are valid in your search.
     * @return the List<Task> of results
     * @throws IOException when elasticsearch fails
     */
    @Override
    public List<Task> findTasksByRequester(User requester, TaskStatus... statuses) throws IOException {
        String query = makeJSONObject(
            j("query",
                j("bool",
                    j("must", ja(
                        jo(
                            j("nested",
                                j("path", "requester"),
                                j("query",
                                    j("term",
                                        j("requester.refId", requester.getId())
                                    )
                                )
                            )
                        ),
                        getTaskStatusQuery(statuses)
                    ))
                )
            )
        );
        Log.d("query", query);
        return client.executeQuery(query, Task.class);
    }

    /**
     * find the tasks by a requester.
     * @param requester User to search for
     * @return the List <Task> of result
     * @throws IOException when elastic search fails
     */
    @Override
    public List<Task> findTasksByRequester(User requester) throws IOException {
        return findTasksByRequester(requester,
                TaskStatus.BIDDED, TaskStatus.REQUESTED, TaskStatus.ASSIGNED, TaskStatus.DONE);
    }

    /**
     * finds all of the tasks of a given provider with the status being one
     * of the statuses.
     * @param provider User to search for
     * @param statuses the statuses you want in your search.
     * @return the List<Task> of results
     * @throws IOException when elasticsearch fails
     */
    @Override
    public List<Task> findTasksByProvider(User provider, TaskStatus... statuses) throws IOException {
        String query = makeJSONObject(
            j("query",
                j("bool",
                    j("must", ja(
                        jo(
                            j("nested",
                                j("path", "provider"),
                                j("query",
                                    j("term",
                                        j("provider.refId", provider.getId())
                                    )
                                )
                            )
                        ),
                        getTaskStatusQuery(statuses)
                    ))
                )
            )
        );
        Log.e("query", query);
        return client.executeQuery(query, Task.class);
    }

    /**
     * finds all of the tasks of a given provider.
     * @param provider User to search for
     * @return the List<Task> of results
     * @throws IOException when elasticsearch fails
     */
    @Override
    public List<Task> findTasksByProvider(User provider) throws IOException {
        return findTasksByProvider(provider,
                TaskStatus.BIDDED, TaskStatus.REQUESTED, TaskStatus.ASSIGNED, TaskStatus.DONE);
    }

    /**
     * finds all of the tasks of a given bidder with the status being one
     * of the statuses.
     * @param bidder User to search for
     * @param statuses the statuses you want in your search.
     * @return the List<Task> of results
     * @throws IOException when elasticsearch fails
     */
    @Override
    public List<Task> findTasksByBidder(User bidder, TaskStatus... statuses) throws IOException {
        String query = makeJSONObject(
            j("query",
                j("bool",
                    j("must", ja(
                        jo(
                            j("nested",
                                j("path", "bidList.bidder"),
                                j("query",
                                    j("term",
                                        j("bidList.bidder.refId", bidder.getId())
                                    )
                                )
                            )
                        ),
                        getTaskStatusQuery(statuses)
                    ))
                )
            )
        );
        Log.d("query", "query");
        return client.executeQuery(query, Task.class);
    }

    /**
     * finds all of the tasks of a given bidder.
     * @param bidder User to search for
     * @return the List<Task> of results
     * @throws IOException when elasticsearch fails
     */
    @Override
    public List<Task> findTasksByBidder(User bidder) throws IOException {
        return findTasksByBidder(bidder,
                TaskStatus.BIDDED, TaskStatus.REQUESTED, TaskStatus.ASSIGNED, TaskStatus.DONE);
    }

    /**
     * search for tasks by keywords.
     * @param keywords Keywords to search for
     * @return the List<Task> of results.
     * @throws IOException when elasticsearch fails
     */
    @Override
    public List<Task> findTasksByKeywords(String keywords) throws IOException {
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
        return client.executeQuery(query, Task.class);
    }

    /**
     * find tasks that are near a Location
     * @param location the Location we are searching around
     * @return the List<Task> of results
     * @throws IOException when elasticsearch fails
     */
    @Override
    public List<Task> findTasksNear(TaskLocation location) throws IOException {
        String query = makeJSONObject(
            j("query",
                j("match_all", jo())
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
        return client.executeQuery(query, Task.class);
    }

    /**
     * find all the signals (notifications) for a specific user.
     * @param user the user to find signals near
     * @return the List<Signal> of results
     * @throws IOException
     */
    @Override
    public List<Signal> findSignalsByUser(User user) throws IOException {
        String query = makeJSONObject(
            j("query",
                j("nested",
                    j("path", "user"),
                    j("query",
                        j("term",
                            j("user.refId", user.getId())
                        )
                    )
                )
            )
        );
        return client.executeQuery(query, Signal.class);
    }

    /**
     * find signals that are related to a user and a target object.
     * @param userId the user associated with the signals.
     * @param targetId the target associated with the signals.
     * @return the List<Signal> of the results.
     * @throws IOException when elasticsearch fails.
     */
    @Override
    public List<Signal> findSignalsByUserAndTargetIds(String userId, String targetId) throws IOException {
        String query = makeJSONObject(
            j("query",
                j("bool",
                    j("must", ja(
                        jo(
                            j("term",
                                j("targetId", targetId)
                            )
                        ),
                        jo(
                            j("nested",
                                j("path", "user"),
                                j("query",
                                    j("term",
                                        j("user.refId", userId)
                                    )
                                )
                            )
                        )
                    ))
                )
            )
        );
        return client.executeQuery(query, Signal.class);
    }

    /**
     * gets a User by the username.
     * @param username the username of the user
     * @return the User
     * @throws IOException when elasticsearch fails.
     */
    @Override
    public User getUser(String username) throws IOException {
        String query = makeJSONObject(
            j("query",
                j("term",
                    j("username", username)
                )
            )
        );
        Log.d("query", query);
        List<User> results = client.executeQuery(query, User.class);
        if (results.size() == 0) {
            return null;
        }
        return results.get(0);
    }

    /**
     * generates an elasticsearch bool query to match
     * tasks that have one of the provided statuses.
     * @param statuses the valid statuses for this query.
     * @return the json bool query as a string
     */
    private JSONConstructor.JSONConstructorValue getTaskStatusQuery(TaskStatus... statuses) {
        //Gson gson = new Gson();

        //String arr = "";
        ArrayList<JSONConstructor.JSONConstructorValue> values = new ArrayList<>();
        for (TaskStatus status: statuses) {
            values.add(jo(
                j("term",
                    j("status", status.toString())
                )
            ));
        }

        return jo(
            j("bool",
                j("should", ja(values.toArray(new JSONConstructor.JSONConstructorValue[]{}))),
                j("minimum_number_should_match", 1)
            ));
    }
}
