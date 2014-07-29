<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<tiles:insertTemplate template="/WEB-INF/include/layout.jsp">
  <tiles:putAttribute name="body">
    <div id="project-create" class="container-fluid" data-user-id="${userId}">
      <section class="row">
        <div class="col-md-offset-2 col-md-8">
          <form role="form">
            <div class="form-group">
              <label for="inputName">Name</label>
              <input name="name" type="text" class="form-control" id="inputName" placeholder="Name">
            </div>
            <div class="form-group">
              <label for="inputJarFile">Jar File</label>
              <input name="jarFile" type="file" id="inputJarFile">
            </div>
            <div class="form-group">
              <label for="inputArguments">Arguments</label>
              <textarea name="arguments" id="inputArguments" class="form-control"></textarea>
            </div>
            <button type="submit" class="btn btn-success" data-loading-text="Creating...">Create</button>
          </form>
        </div>
      </section>
    </div>
    <script>
      $(function () {
        $("#project-create form[role='form']").each(function () {
          $(this).submit(function (e) {
            e.preventDefault();

            var $submit = $(this).find('button[type="submit"]');

            $submit.button('loading');
            try {
              $.ajax({
                url: sky.API_PROJECT_URL,
                method: 'POST',
                data: JSON.stringify(_.defaults($(this).serializeObject(), {
                  jarFile: new sky.Reader().readAsArrayBuffer($(this).find('input[type="file"]')[0].files[0])
                })),
                success: function () {
//                  location.href="/project";
                },

                error: function (jqAjax) {
                  alert(jqAjax.responseText);
                }
              }).always(function () {
                $submit.button('reset');
              });
            } catch (err) {
              throw err;
            }
          });
        });
      });
    </script>
  </tiles:putAttribute>
</tiles:insertTemplate>