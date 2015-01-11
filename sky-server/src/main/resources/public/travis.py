__author__ = 'user'

import httplib
import os
import string
# conn = http.client.HTTPConnection("localhost", 8080)
# conn.request("GET", "/api/worker")

branchName = os.environ.get('TRAVIS_BRANCH')
if branchName is None:
    branchName = 'branchName'
repoSlug = os.environ.get('TRAVIS_REPO_SLUG')
if repoSlug is None:
    repoSlug = 'account/projectName'

account = repoSlug.split('/')[0];
projectName = repoSlug.split('/')[1];

print('branchName : ' + branchName)
print('repoSlug : ' + repoSlug)

apiUrl = '/account/' + account + '/project/' + projectName + '/branch/' + branchName
print('apiUrl : ' + apiUrl)

# print('branchName : ' + branchName)
# print('repoSlug : ' + repoSlug)

header = {
   'User-Agent': 'MyClient/1.0.0',
   'Accept': 'application/vnd.travis-ci.2+json',
   'Host': 'api.travis-ci.org'
}
conn = httplib.HTTPSConnection('api.travis-ci.org')
conn.request("GET", "/", headers=header)



response = conn.getresponse()
print('response : ', response.status, response.reason)
data = response.read()
print('data : ', data)