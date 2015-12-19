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


app.directive('randomWalk', function($interval) {
  return {
    scope: {},
    template: '<graph data="list"></graph>',
    link: function(scope) {
      scope.list = [[0],[0], [0], [0]];
      var inter = $interval(function() {
        for (var i = 0; i < scope.list.length; ++i) {
          var li = scope.list[i];
          li.push(li[li.length - 1] + (Math.random() - 0.5) * 2);
          if (li.length > 100)
            li.splice(0, 1);
        }
      }, 100);

      scope.$on("$destroy", function () {
        $interval.cancel(inter);
      });
    }
  };
});