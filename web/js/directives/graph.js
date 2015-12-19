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

app.directive('graph', function(){
  var colors = ["green", "blue", "red", "black", "orange", "yellow"];
  function getColor(i) {
    
  }
  return {
    restrict: 'E',
    scope: {data: '='},
    link: function(scope, el) {
      var canvas = document.createElement('canvas');
      canvas.width = 400;
      canvas.height = 400;
      var ctx = canvas.getContext("2d");
      var ma = 0, mi = 0, range = 1;
      el.append(canvas);
      function refreshRange() {
        ma = 0, mi = 0;
        for (var x = 0; x < scope.data.length; ++x) {
          var list = scope.data[x];
          for (var i = 0; i < list.length; ++i)
            ma = Math.max(list[i], ma), mi = Math.min(list[i], mi);
        }
        range = ma - mi + 1;
      }
      
      function clear() {
        ctx.fillStyle = "white";
        ctx.fillRect(0, 0, canvas.width, canvas.height);
      }
      
      function draw(list) {
        ctx.beginPath();
        ctx.moveTo(0, canvas.height - (list[0] - mi) * canvas.height / range);
        for (var i = 1; i < list.length; ++i) {
          var x = i * (canvas.width - 1) / (list.length - 1);
          var y = canvas.height - (list[i] - mi) * canvas.height / range;
          ctx.lineTo(x,y);
        }
        ctx.stroke();
      }
      scope.$watch('data', function() {
        clear();
        refreshRange();
        for (var i = 0; i < scope.data.length; ++i) {
          ctx.strokeStyle = colors[i];
          draw(scope.data[i]);
        }
      }, true);
    }
  };
});
