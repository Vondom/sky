$.ajaxSetup({
  dataType: 'json',
  contentType: 'application/json',
  processData: false,
});

var sky = (function () {
  var contextPath = $("html").data('context-path');

  return {
    REQUEST_CONTEXT_PATH: contextPath,
    API_PROJECT_URL: contextPath + "/api/project",
    API_USER_URL: contextPath + "/api/user"
  }
}());