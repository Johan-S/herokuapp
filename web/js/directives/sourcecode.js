

app.directive('sourcecode', function() {
  function lineNumber (n) {
    var res = n + "";
    while (res.length < 3)
      res = '0' + res;
    return res + " ";
  }
  return {
    restrict: 'E',
    scope: {text: '='},
    template: "<code class='linenumber'></code><code style='min-width: 60em'></code>",
    link: function(scope, el) {
      var lineNumberDiv = el.children()[0];
      var codeDiv = el.children()[1];
      
      function addRow(num, row) {
        var el = document.createElement("div");
        el.appendChild(document.createTextNode(row));
        el.appendChild(document.createElement('br'));
        codeDiv.appendChild(el);
        el = document.createElement("div");
        el.appendChild(document.createTextNode(lineNumber(num)));
        lineNumberDiv.appendChild(el);
      }
      
      function setText(rows) {
        codeDiv.innerHTML = "";
        lineNumberDiv.innerHTML = "";
        for (var i = 0; i < rows.length; ++i) {
          addRow(i + 1, rows[i]);
        }
      }
      
      scope.$watch('text', function() {
        var rows = [];
        var data = scope.text, last = 0, cur = 0;
        for (var i = 0; i < data.length; ++i) {
          if (data[i] === '\n') {
            rows.push(data.substring(last, cur));
            last = i + 1;
            cur = last;
          } else if (data[i] !== '\r') {
            cur = i + 1;
          }
        }
        if (cur !== last)
            rows.push(data.substring(last, cur));
        setText(rows);
      });
      
    }
  };
});