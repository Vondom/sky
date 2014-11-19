jQuery.serializeObject
======================

The missing jQuery form serialisation function.

jQuery comes with the built-in `serialize` and `serializeArray`, which though
useful, are not always what you need, especially when dealing with a RESTful
backend that you talk to with JSON messages.

This tiny plugin creates a simple object where the key is the form element name
and the value is the value or an array of values (if multiple form elements have
the same name).
