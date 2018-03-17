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
 * ElasticUser provides a User that is an ElasticObject
 *
 * @see ElasticObject
 * @see User
 * @see ConcreteUser
 */
public class ElasticUser extends ElasticObject<ConcreteUser> implements User {

    /**
     * create an ElasticUser from id and the
     * ElasticClient Singleton
     * @param id the elasticsearch id
     */
    public ElasticUser(String id) {
        super(id, ConcreteUser.class);
    }

    /**
     * create an ElasticUser from id and a
     * provided ElasticClient
     * @param id the elasticsearch id
     * @param client an ElasticClient
     */
    public ElasticUser(String id, ElasticClient client) {
        super(id, ConcreteUser.class, client);
    }

    /**
     * create an ElasticUser around an existing ConcreteUser
     * and the ElasticClient singleton
     * @param user a ConcreteUser
     */
    public ElasticUser(ConcreteUser user) {
        super(user, ConcreteUser.class);
    }

    /**
     * create an ElasticUser around an existing ConcreteUser
     * and a provided ElasticClient
     * @param user a ConcreteUser
     * @param client an ElasticClient
     */
    public ElasticUser(ConcreteUser user, ElasticClient client) {
        super(user, ConcreteUser.class, client);
    }

    /**
     * gets the username
     * required by the User interface
     * @return the username, or "" if there is an IOException
     */
    public String getUsername() {
        try {
            return getObj().getUsername();
        } catch (IOException e) {
            //TODO handle better
            return "";
        }
    }

    /**
     * gets the email
     * required by the User interface
     * @return the email, or "" if there is an IOException
     */
    public String getEmail() {
        try {
            return getObj().getEmail();
        } catch (IOException e) {
            //TODO handle better
            return "";
        }
    }

    /**
     * gets the phonenumber
     * required by the User interface
     * @return the phonenumber, or "" if there is an IOException
     */
    public String getPhoneNumber() {
        try {
            return getObj().getPhoneNumber();
        } catch (IOException e) {
            //TODO handle better
            return "";
        }
    }

    /**
     * sets the username then pushes to elasticsearch
     * @param username the username
     */
    public void setUsername(String username) {
        try {
            getObj().setUsername(username);
            update();
        } catch (IOException e) {
            //TODO figure out behavior when no object exists yet
        }
    }

    /**
     * sets the email then pushes to elasticsearch
     * @param email the email
     */
    public void setEmail(String email) {
        try {
            getObj().setEmail(email);
            update();
        } catch (IOException e) {
            //TODO figure out behavior when no object exists yet
        }
    }

    /**
     * sets the phone number then pushes to elasticsearch
     * @param phoneNumber the phone number
     */
    public void setPhoneNumber(String phoneNumber) {
        try {
            getObj().setPhoneNumber(phoneNumber);
            update();
        } catch (IOException e) {
            //TODO figure out behavior when no object exists yet
        }
    }
}
