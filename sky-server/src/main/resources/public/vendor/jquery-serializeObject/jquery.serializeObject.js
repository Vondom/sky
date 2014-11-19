/**
  (The MIT License)

  Copyright (c) 2012 Gilles Ruppert <gilles@madeofbytes.com>

  Permission is hereby granted, free of charge, to any person obtaining
  a copy of this software and associated documentation files (the
  'Software'), to deal in the Software without restriction, including
  without limitation the rights to use, copy, modify, merge, publish,
  distribute, sublicense, and/or sell copies of the Software, and to
  permit persons to whom the Software is furnished to do so, subject to
  the following conditions:

  The above copyright notice and this permission notice shall be
  included in all copies or substantial portions of the Software.

  THE SOFTWARE IS PROVIDED 'AS IS', WITHOUT WARRANTY OF ANY KIND,
  EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
  MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
  IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
  CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
  TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
  SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.


  serialises a form to an object. The use is the same than $.fn.serialize
  and $.fn.serializeArray.
  The keys are the form element names and the value is the the form element
  value. If multiple form elements have the same name with different values,
  the value is an array with all the values. `undefined` and `null` are
  converted to empty strings.
 */

jQuery.fn.serializeObject = function() {
  var o = {};
  var a = this.serializeArray();
  for (var i = 0, l = a.length; i < l; i++) {
    var item = a[i];
    var name = item.name;
    // if the value is null or undefined, we set to empty string, else we
    // use the value passed
    var value = item.value != null ? item.value : '';

    // if the key already exists we convert it to an array
    if (o[name] !== undefined) {
      if (!o[name].push) {
        // convert to array
        o[name] = [o[name]];
      }
      o[name].push(value);
    }
    else {
      o[name] = value ;
    }
  }

  return o;
};
