<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<tiles:insertTemplate template="/WEB-INF/include/layout.jsp">
  <tiles:putAttribute name="body">
    <script id="headerTemplate" type="text/template">
      <div class="row" style="border-bottom: 2px inset #2d6ca2;">
        <div class="col-md-12">
          <h1>{{account}}</h1>
        </div>
      </div>
    </script>
    <script id="contentsTemplate" type="text/template">
      <div class="row">
        <dl class="dl-horizontal col-md-10">
          <dt>{{name}}</dt>
          <dd>{{description}}</dd>
        </dl>
        <div class="col-md-2 pull-right">
          <input type="checkbox" class="switch" data-size="small" />
        </div>
      </div>
    </script>
    <section class="container">

    </section>
    <script type="text/javascript">
      $(function () {
        var $projects = $("#projects"),
            $section = $("section.container"),
            github = new Github({
              token: sky.ACCESS_TOKEN,
              auth: "oauth"
            }),
            user = github.getUser(),
            _$ = {
              repos: [],
              build: function (repos) {
                var acc = {},
                    _headerTemplate = _.template($("#headerTemplate").html()),
                    _contentsTemplate = _.template($("#contentsTemplate").html()),
                    $section = $("section.container");

                _.forEach(repos, function (repo) {
                  var username = repo.owner.login;
                  if (!acc[username]) {
                    acc[username] = [];
                  }
                  acc[username].push(repo);
                });

                _.forEach(acc, function (repos, username) {
                  $(_headerTemplate({
                    account: username
                  })).appendTo($section);

                  _.forEach(repos, function (repo) {
                    $(_contentsTemplate({
                      name: repo.name,
                      description: repo.description,
                      data: {

                      }
                    })).appendTo($section).find('.switch').bootstrapSwitch({
                      onSwitchChange: function (event, state) {
                        var $this = $(this);
                        if (state) {

                        } else {

                        }
                      }
                    });
                  });
                });
              }
            };

        user.repos(function (err, repos) {
          if (err) throw err;

          _$.build(repos);

          user.orgs(function (err, orgs) {
            if (err) throw err;
            _.forEach(orgs, function (org) {
              user.orgRepos(org.login, function (err, repos) {
                if (err) throw err;

                _$.build(repos);
              });
            });

          });
        });
      });
    </script>
  </tiles:putAttribute>
</tiles:insertTemplate>

