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
 * Collection of static methods to perform pre-defined searches.
 */
public class ElasticSearcher extends Searcher<ElasticClient> {
    public ElasticSearcher(ElasticClient client) {
        super(client);
    }

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
                                    j("match",
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

    @Override
    public List<Task> findTasksByRequester(User requester) throws IOException {
        return findTasksByRequester(requester,
                TaskStatus.BIDDED, TaskStatus.REQUESTED, TaskStatus.ASSIGNED, TaskStatus.DONE);
    }

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
                                    j("match",
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

    @Override
    public List<Task> findTasksByProvider(User provider) throws IOException {
        return findTasksByProvider(provider,
                TaskStatus.BIDDED, TaskStatus.REQUESTED, TaskStatus.ASSIGNED, TaskStatus.DONE);
    }

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
                                    j("match",
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

    @Override
    public List<Task> findTasksByBidder(User bidder) throws IOException {
        return findTasksByBidder(bidder,
                TaskStatus.BIDDED, TaskStatus.REQUESTED, TaskStatus.ASSIGNED, TaskStatus.DONE);
    }

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

    @Override
    public List<Task> findTasksByKeywordsNear(String keywords, TaskLocation location) throws IOException {
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
        return client.executeQuery(query, Task.class);
    }

    @Override
    public User getUser(String username) throws IOException {
        String query = makeJSONObject(
            j("query",
                j("match",
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

    // TODO findTasksByLocation?

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
                j("match",
                    j("status", status.toString())
                )
            ));
            //arr += String.format("{\"match\": {\"status\": %s}},", gson.toJson(status));
        }

        //arr = arr.substring(0, arr.length()-1);

        return jo(
            j("bool",
                j("should", ja(values.toArray(new JSONConstructor.JSONConstructorValue[]{}))),
                j("minimum_number_should_match", 1)
            ));

        //return String.format("\"bool\":{\"should\":[%s],\"minimum_number_should_match\":1}", arr);
    }
}
