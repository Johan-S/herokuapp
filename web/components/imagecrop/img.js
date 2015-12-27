app.directive("imagecrop", function() {

  function getRel(el) {
    var x = 0, y = 0;
    for (;el;el = el.offsetParent){
      x += el.offsetLeft - el.scrollLeft;
      y += el.offsetTop - el.scrollTop;
    }
    return {x: x, y: y};
  }
  function clearSelection() {
    if (window.getSelection) {
      if (window.getSelection().empty) {  // Chrome
        window.getSelection().empty();
      } else if (window.getSelection().removeAllRanges) {  // Firefox
        window.getSelection().removeAllRanges();
      }
    } else if (document.selection) {  // IE?
      document.selection.empty();
    }
  }

  function parseComponent(el) {
    var resizeMargin = 4;
    var height = 60, width = 60;
    var oHeight = 0, oWidth = 0;
    var borderWidth = 0;
    var input = el.querySelector('input[type="file"]');
    var selected = el.querySelector(".selected img");
    var wrapper = el.querySelector(".selected");
    var selectionbox = el.querySelector(".selectionbox");
    var preview = el.querySelector(".preview");
    var cover = el.querySelector(".cover");
    var coverDefault = cover.className;
    var grabBox = el.querySelector(".grabbox");
    var resizeN = el.querySelector(".resize.n");
    var resizeNE = el.querySelector(".resize.ne");
    var resizeE = el.querySelector(".resize.e");
    var resizeSE = el.querySelector(".resize.se");
    var resizeS = el.querySelector(".resize.s");
    var resizeSW = el.querySelector(".resize.sw");
    var resizeW = el.querySelector(".resize.w");
    var resizeNW = el.querySelector(".resize.nw");
    borderWidth = getComputedStyle(selectionbox).borderWidth;
    borderWidth = borderWidth.substring(0, borderWidth.length - 2) - 0;

    preview.style.height = height + "px";
    preview.style.width = width + "px";

    var left, top, w, h;

    wrapper.appendChild(grabBox);
    function boxes(x, y, w, h) {

      selectionbox.style.left = x - borderWidth + "px";
      selectionbox.style.top = y - borderWidth + "px";
      selectionbox.style.width = w + "px";
      selectionbox.style.height = h + "px";

      grabBox.style.left = x + "px";
      grabBox.style.top = y + "px";
      grabBox.style.width = w + "px";
      grabBox.style.height = h + "px";

      resizeN.style.left = x + resizeMargin + "px";
      resizeN.style.top = y - resizeMargin + "px";
      resizeN.style.width = w - resizeMargin * 2 + "px";
      resizeN.style.height = resizeMargin * 2 + "px";

      resizeS.style.left = x + resizeMargin + "px";
      resizeS.style.top = y + h - resizeMargin + "px";
      resizeS.style.width = w - resizeMargin * 2 + "px";
      resizeS.style.height = resizeMargin * 2 + "px";

      resizeE.style.left = x + w - resizeMargin + "px";
      resizeE.style.top = y + resizeMargin + "px";
      resizeE.style.width = resizeMargin * 2 + "px";
      resizeE.style.height = h - resizeMargin * 2 + "px";

      resizeW.style.left = x - resizeMargin + "px";
      resizeW.style.top = y + resizeMargin + "px";
      resizeW.style.width = resizeMargin * 2 + "px";
      resizeW.style.height = h - resizeMargin * 2 + "px";

      resizeNE.style.left = x + w - resizeMargin + "px";
      resizeNE.style.top = y - resizeMargin + "px";
      resizeNE.style.width = resizeMargin * 2 + "px";
      resizeNE.style.height = resizeMargin * 2 + "px";

      resizeNW.style.left = x - resizeMargin + "px";
      resizeNW.style.top = y - resizeMargin + "px";
      resizeNW.style.width = resizeMargin * 2 + "px";
      resizeNW.style.height = resizeMargin * 2 + "px";

      resizeSW.style.left = x - resizeMargin + "px";
      resizeSW.style.top = y + h - resizeMargin + "px";
      resizeSW.style.width = resizeMargin * 2 + "px";
      resizeSW.style.height = resizeMargin * 2 + "px";

      resizeSE.style.left = x + w - resizeMargin + "px";
      resizeSE.style.top = y + h - resizeMargin + "px";
      resizeSE.style.width = resizeMargin * 2 + "px";
      resizeSE.style.height = resizeMargin * 2 + "px";
    }

    function setBox() {
      var kw = width / w;
      var kh = height / h;
      preview.style.backgroundSize = (kw * oWidth) + "px " + (kh * oHeight) + "px";
      preview.style.backgroundPosition = (- kw * left) + "px " + (- kh * top) + "px";
    }

    selected.onload = function() {
      oHeight = selected.height;
      oWidth = selected.width;
    }

    function setURL(url) {
      selected.src = url;
      preview.style.background = "url(" + url + ")";
    }
    setURL("/mountains.jpg");

    input.onchange = function() {
      var file = input.files[0];
      var url = URL.createObjectURL(file);
      setURL(url);
    }

    var x, y, ox, oy;
    function clearResize() {
      for (var i = 0; i < resizers.length; ++i) {
        el.removeEventListener("mousemove", resizers[i]);
      }
      cover.className = coverDefault;
      taken = false;
    }

    var taken = false;
    function resizer (f) {
      function res(e) {
        clearSelection();
        if (e.which !== 1) {
          clearResize();
          return false;
        }
        var rel = getRel(selected);
        var cx = e.clientX - rel.x, cy = e.clientY - rel.y;
        f(cx, cy);
        boxes(left, top, w, h);
        setBox();
        return false;
      }
      return res;
    }

    var create = resizer(function (cx, cy) {
      left = Math.min(x, cx);
      w = Math.abs(x - cx);
      top = Math.min(y, cy);
      h = Math.abs(y - cy);

      if (left + w > oWidth)
        if (x === left)
          w = oWidth - left;
        else
          left = oWidth - w;
      else if (left < 0)
        w += left, left = 0;

      if (top + h > oHeight)
        if (y === top)
          h = oHeight - top;
        else
          top = oHeight - h;
      else if (top < 0)
        h += top, top = 0;
    });

    var move = resizer(function (cx, cy) {
      var dx = cx - x, dy = cy - y;
      left = dx + ox;
      top = dy + oy;
      if (left < 0)left = 0;
      if (top < 0)top = 0;
      if (left + w > oWidth)
        left = oWidth - w;
      if (top + h > oHeight)
        top = oHeight - h;
    });

    function resN(cx, cy) {
      var bot = top + h;
      if (cy > bot - 10)
        cy = bot - 10;
      else if (cy < 0)
        cy = 0;
      top = cy;
      h = bot - top;
    }
    function resS(cx, cy) {
      if (cy < top + 10)
        cy = top + 10;
      if (cy > oHeight)
        cy = oHeight;
      h = cy - top;
    }
    function resE(cx, cy) {
      if (cx < left + 10)
        cx = left + 10;
      if (cx > oWidth)
        cx = oWidth;
      w = cx - left;
    }
    function resW(cx, cy) {
      var right = left + w;
      if (cx > right - 10)
        cx = right - 10;
      else if (cx < 0)
        cx = 0;
      left = cx;
      w = right - left;
    }
    function resNW(cx, cy) {
      resN(cx, cy);
      resW(cx, cy);
    }
    function resNE(cx, cy) {
      resN(cx, cy);
      resE(cx, cy);
    }
    function resSW(cx, cy) {
      resS(cx, cy);
      resW(cx, cy);
    }
    function resSE(cx, cy) {
      resS(cx, cy);
      resE(cx, cy);
    }
    var resizerN = resizer(resN);
    var resizerS = resizer(resS);
    var resizerE = resizer(resE);
    var resizerW = resizer(resW);
    var resizerNE = resizer(resNE);
    var resizerNW = resizer(resNW);
    var resizerSE = resizer(resSE);
    var resizerSW = resizer(resSW);

    var resizers = [];
    function initResize(f, cursor, elem) {
      resizers.push(f);
      elem.addEventListener("mousedown", function(e) {
        if (e.which !== 1) {
          return false;
        }
        clearSelection();
        if (taken) {
          return false;
        }
        taken = true;
        ox = left;
        oy = top;
        var rel = getRel(selected);
        var cx = e.clientX - rel.x, cy = e.clientY - rel.y;
        x = cx, y = cy;
        cover.className += " display " + cursor
          el.addEventListener("mousemove", f);
        return false;
      });
    }

    initResize(create, "createcursor", wrapper);
    initResize(move, "movecursor", grabBox);
    initResize(resizerN, "resize-n-cursor", resizeN);
    initResize(resizerNE, "resize-ne-cursor", resizeNE);
    initResize(resizerE, "resize-e-cursor", resizeE);
    initResize(resizerSE, "resize-se-cursor", resizeSE);
    initResize(resizerS, "resize-s-cursor", resizeS);
    initResize(resizerSW, "resize-sw-cursor", resizeSW);
    initResize(resizerW, "resize-w-cursor", resizeW);
    initResize(resizerNW, "resize-nw-cursor", resizeNW);

    el.addEventListener("mouseup", function(e) {
      clearResize();
      return false;
    });
  }
  return {
    templateUrl: "components/imagecrop/img.html",
    link: function(scope, el) {
      el = el[0];
      parseComponent(el);
    }
  }
});
