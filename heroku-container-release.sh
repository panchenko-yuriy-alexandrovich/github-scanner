#!/bin/bash
imageId=$(docker inspect registry.heroku.com/github-scanner/web --format={{.Id}})
payload='{"updates":[{"type":"web","docker_image":"'"$imageId"'"}]}'
curl -n -X PATCH https://api.heroku.com/apps/github-scanner/formation \
-d "$payload" \
-H "Content-Type: application/json" \
-H "Accept: application/vnd.heroku+json; version=3.docker-releases" \
-H "Authorization: Bearer $HEROKU_AUTH_TOKEN"