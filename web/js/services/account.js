/* 
 * Copyright 2015 Johan Strååt.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



app.factory("account", function($cookies, $http) {
  var data = {id: 0, name: ""};
  $http.get('/me').then(function(response) {
    if (response.data.id)
      data = response.data;
  });
  return {
    login: function(name, pass) {
      $http.post('/login', {name: name, password: pass}).then(function(response) {
        data.id = response.data.id;
        data.name = response.data.name;
      });
    },
    register: function(name, pass) {
      $http.post('/register', {name: name, password: pass}).then(function(response) {
        data.id = response.data.id;
        data.name = response.data.name;
      });
    },
    logout: function() {
      data.id = 0;
      data.name = "";
      $cookies.remove("session");
    },
    isLogged: function() {
      return data.id !== 0;
    },
    getId: function() {
      return data.id;
    },
    getName: function() {
      return data.name;
    }
  };
});